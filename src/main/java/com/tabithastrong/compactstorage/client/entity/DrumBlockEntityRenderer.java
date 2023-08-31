package com.tabithastrong.compactstorage.client.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.tabithastrong.compactstorage.block.entity.DrumBlockEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemFrameRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.DirectionalBlock;
import org.checkerframework.checker.index.qual.NonNegative;

import javax.annotation.Nonnull;

public class DrumBlockEntityRenderer implements BlockEntityRenderer<DrumBlockEntity> {

    private BlockEntityRendererProvider.Context context;
    public DrumBlockEntityRenderer(BlockEntityRendererProvider.Context ctx) {
        super();
        this.context = ctx;
    }

    @Override
    public void render(@Nonnull DrumBlockEntity drumBlock, float delta, PoseStack matrixStack, MultiBufferSource vertexConsumerProvider, int light, int overlay) {
        if(drumBlock.getDrumItemStackHandler() == null) {
            return;
        }

        ItemStack stack = drumBlock.clientDisplayStack.orElse(ItemStack.EMPTY);
        String text = drumBlock.getTextToDisplay();

        Direction direction = drumBlock.getBlockState().getValue(DirectionalBlock.FACING);

        if(!stack.isEmpty()) {
            matrixStack.pushPose();
            matrixStack.translate(0.5f, 0.5f, 0.5f);
            matrixStack.translate(direction.getStepX() * 0.5f, direction.getStepY() * 0.5f, direction.getStepZ() * 0.5f);
            matrixStack.scale(0.35f, 0.35f, 0.35f);
            matrixStack.mulPose(Axis.YP.rotationDegrees(direction.toYRot()));

            if(direction == Direction.UP || direction == Direction.DOWN) {
                matrixStack.mulPose(Axis.XP.rotationDegrees(direction == Direction.UP ? 90f : 270f));
            }

            matrixStack.translate(0f, -0.15f, 0f);
            context.getItemRenderer().renderStatic(stack, ItemDisplayContext.FIXED, 15728880, OverlayTexture.NO_OVERLAY, matrixStack, vertexConsumerProvider, drumBlock.getLevel(), drumBlock.hashCode());
            matrixStack.popPose();
        }

        matrixStack.pushPose();
        float width = this.context.getFont().width(text);
        float textScale = 0.007f;


        matrixStack.translate(0.5f, 0.5f, 0.5f);
        matrixStack.translate(direction.getStepX() * 0.51f, direction.getStepY() * 0.51f, direction.getStepZ() * 0.51f);
        matrixStack.mulPose(Axis.YP.rotationDegrees(-direction.toYRot()));

        if(direction == Direction.UP || direction == Direction.DOWN) {
            matrixStack.mulPose(Axis.XP.rotationDegrees(direction == Direction.UP ? -90f : 90f));
        }

        matrixStack.translate(-(width * textScale)/2, 0f, 0f);
        matrixStack.mulPose(Axis.XP.rotationDegrees(180f));
        matrixStack.translate(0f, -0.2f, 0f);
        matrixStack.scale(textScale, textScale, textScale);
        //matrixStack.translate(direction.getStepX() * -0.1f, direction.getStepY() * -0.1f, direction.getStepZ() * -0.1f);

        context.getFont().drawInBatch(text, 0f, 0f, 0, false, matrixStack.last().pose(), vertexConsumerProvider, Font.DisplayMode.NORMAL, 0xFFFFFF, 0);

        matrixStack.popPose();
    }
}
