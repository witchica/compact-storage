package com.witchica.compactstorage.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.witchica.compactstorage.block.base.BaseItemDrumBlock;
import com.witchica.compactstorage.block.entity.base.BaseItemDrumBlockEntity;
import it.unimi.dsi.fastutil.HashCommon;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.item.ItemModelResolver;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Style;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.Nullable;

import java.util.Optional;

public class DrumBlockEntityRenderer implements BlockEntityRenderer<BaseItemDrumBlockEntity, DrumBlockEntityRenderer.DrumBlockEntityRenderState> {
    private final ItemModelResolver itemModelResolver;
    private final Font font;

    public static class DrumBlockEntityRenderState extends BlockEntityRenderState {
        public Optional<Item> item;
        public int itemCount;
        public Direction facing;
        public ItemStackRenderState itemStackRenderState;
    }

    public DrumBlockEntityRenderer(BlockEntityRendererProvider.Context context) {
        this.itemModelResolver = context.itemModelResolver();
        this.font = context.font();
    }

    @Override
    public DrumBlockEntityRenderer.DrumBlockEntityRenderState createRenderState() {
        return new DrumBlockEntityRenderState();
    }

    @Override
    public void extractRenderState(BaseItemDrumBlockEntity blockEntity, DrumBlockEntityRenderer.DrumBlockEntityRenderState renderState, float partialTick, Vec3 cameraPosition, ModelFeatureRenderer.@Nullable CrumblingOverlay breakProgress) {
        BlockEntityRenderer.super.extractRenderState(blockEntity, renderState, partialTick, cameraPosition, breakProgress);

        if(blockEntity.clientItem.isPresent()) {
            renderState.item = blockEntity.clientItem.map(ItemStack::getItem);
        } else {
            renderState.item = Optional.empty();
        }
        renderState.itemCount = blockEntity.clientStoredItems;
        renderState.facing = blockEntity.getBlockState().getValue(BaseItemDrumBlock.FACING);

        if(renderState.item.isPresent()) {
            renderState.itemStackRenderState = new ItemStackRenderState();
            int i = HashCommon.long2int(blockEntity.getBlockPos().asLong());
            this.itemModelResolver.updateForTopItem(renderState.itemStackRenderState, new ItemStack(renderState.item.get(), 1), ItemDisplayContext.FIXED, blockEntity.getLevel(), null, i);
        }
    }

    @Override
    public void submit(DrumBlockEntityRenderState drumBlockEntityRenderState, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, net.minecraft.client.renderer.state.level.CameraRenderState cameraRenderState) {
        boolean crouched = false;

        if(Minecraft.getInstance().player != null) {
            crouched = Minecraft.getInstance().player.isCrouching();
        }

        String text = getTextToDisplay(crouched, drumBlockEntityRenderState);

        Direction direction = drumBlockEntityRenderState.facing;

        if(drumBlockEntityRenderState.item.isPresent()) {
            poseStack.pushPose();
            poseStack.translate(0.5f, 0.5f, 0.5f);
            poseStack.translate(direction.getStepX() * 0.5f, direction.getStepY() * 0.5f, direction.getStepZ() * 0.5f);
            poseStack.scale(0.35f, 0.35f, 0.35f);
            poseStack.mulPose(Axis.YP.rotationDegrees(direction.toYRot()));

            if(direction == Direction.UP || direction == Direction.DOWN) {
                poseStack.mulPose(Axis.YP.rotationDegrees(direction == Direction.UP ? -90f : 90f));
                poseStack.mulPose(Axis.XP.rotationDegrees(direction == Direction.UP ? 90f : 270f));
            }
            poseStack.mulPose(Axis.YP.rotationDegrees(-direction.toYRot() * 2));
            poseStack.mulPose(Axis.YP.rotationDegrees(180));

            drumBlockEntityRenderState.itemStackRenderState.submit(poseStack, submitNodeCollector, drumBlockEntityRenderState.lightCoords, OverlayTexture.NO_OVERLAY, 0);
            poseStack.popPose();
        }

        poseStack.pushPose();
        float width = this.font.width(text);
        float textScale = 0.007f;


        poseStack.translate(0.5f, 0.5f, 0.5f);
        poseStack.translate(direction.getStepX() * 0.51f, direction.getStepY() * 0.51f, direction.getStepZ() * 0.51f);
        poseStack.mulPose(Axis.YP.rotationDegrees(-direction.toYRot()));

        if(direction == Direction.UP || direction == Direction.DOWN) {
            poseStack.mulPose(Axis.YP.rotationDegrees(direction == Direction.UP ? -90f : 90f));
            poseStack.mulPose(Axis.XP.rotationDegrees(direction == Direction.UP ? -90f : 90f));
        }

        poseStack.translate(-(width * textScale)/2, 0f, 0f);
        poseStack.mulPose(Axis.XP.rotationDegrees(180f));
        poseStack.translate(0f, -0.2f, 0f);
        poseStack.scale(textScale, textScale, textScale);
        //matrixStack.translate(direction.getStepX() * -0.1f, direction.getStepY() * -0.1f, direction.getStepZ() * -0.1f);


        submitNodeCollector.submitText(poseStack, 0f, 0f, FormattedCharSequence.forward(text, Style.EMPTY), false, Font.DisplayMode.NORMAL, drumBlockEntityRenderState.lightCoords, 0xFF000000, 0, 0);
        poseStack.popPose();
    }

    public String getTextToDisplay(boolean crouched, DrumBlockEntityRenderState drumBlockEntityRenderState) {
        if(drumBlockEntityRenderState.item.isPresent()) {
            Item storedItem = drumBlockEntityRenderState.item.get();
            int clientStackSize = storedItem.getDefaultMaxStackSize();
            int clientStoredItems = drumBlockEntityRenderState.itemCount;

            if(crouched) {
                int maxStored = (clientStackSize == 0 ? 64 : clientStackSize) * 64;
                return "%d / %d".formatted(clientStoredItems, maxStored);
            }

            if(clientStoredItems == 0) {
                return "Empty";
            }

            int numStacks = clientStoredItems / clientStackSize;
            int leftover = clientStoredItems % clientStackSize;

            if(numStacks == 0 & leftover == 0) {
                return "Empty";
            } else if(numStacks == 0) {
                return "" + leftover;
            } else if(leftover == 0) {
                return clientStackSize + " x " + numStacks;
            } else {
                return (clientStackSize + " x " + numStacks + " + " + leftover);
            }
        } else {
            return "Empty";
        }
    }
}
