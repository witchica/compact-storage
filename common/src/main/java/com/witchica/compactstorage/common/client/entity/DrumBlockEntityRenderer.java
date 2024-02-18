package com.witchica.compactstorage.common.client.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.witchica.compactstorage.common.block.DrumBlock;
import com.witchica.compactstorage.common.block.entity.DrumBlockEntity;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

public class DrumBlockEntityRenderer implements BlockEntityRenderer<DrumBlockEntity> {
    private final BlockEntityRendererProvider.Context context;

    public DrumBlockEntityRenderer(BlockEntityRendererProvider.Context ctx) {
        super();
        this.context = ctx;
    }
    @Override
    public void render(DrumBlockEntity drumBlock, float tickDelta, PoseStack matrices, MultiBufferSource vertexConsumers, int light, int overlay) {
        if(drumBlock == null) {
            return;
        }

        ItemStack stack = drumBlock.clientItem;
        String text = drumBlock.getTextToDisplay();

        Direction direction = drumBlock.getBlockState().getValue(DrumBlock.FACING);

        if(!stack.isEmpty()) {
            matrices.pushPose();
            matrices.translate(0.5f, 0.5f, 0.5f);
            matrices.translate(direction.getStepX() * 0.5f, direction.getStepY() * 0.5f, direction.getStepZ() * 0.5f);
            matrices.scale(0.35f, 0.35f, 0.35f);
            matrices.mulPose(Axis.YP.rotationDegrees(direction.toYRot()));

            if(direction == Direction.UP || direction == Direction.DOWN) {
                matrices.mulPose(Axis.YP.rotationDegrees(direction == Direction.UP ? 90f : 270f));
            }

            matrices.translate(0f, -0.15f, 0f);

            context.getItemRenderer().renderStatic(stack, ItemDisplayContext.FIXED, 15728880, OverlayTexture.NO_OVERLAY, matrices, vertexConsumers, drumBlock.getLevel(), drumBlock.hashCode());
            matrices.popPose();
        }

        matrices.pushPose();
        float width = this.context.getFont().width(text);
        float textScale = 0.007f;


        matrices.translate(0.5f, 0.5f, 0.5f);
        matrices.translate(direction.getStepX() * 0.51f, direction.getStepY() * 0.51f, direction.getStepZ() * 0.51f);
        matrices.mulPose(Axis.YP.rotationDegrees(-direction.toYRot()));

        if(direction == Direction.UP || direction == Direction.DOWN) {
            matrices.mulPose(Axis.YP.rotationDegrees(direction == Direction.UP ? -90f : 90f));
            matrices.mulPose(Axis.XP.rotationDegrees(direction == Direction.UP ? -90f : 90f));
        }

        matrices.translate(-(width * textScale)/2, 0f, 0f);
        matrices.mulPose(Axis.XP.rotationDegrees(180f));
        matrices.translate(0f, -0.2f, 0f);
        matrices.scale(textScale, textScale, textScale);
        //matrixStack.translate(direction.getStepX() * -0.1f, direction.getStepY() * -0.1f, direction.getStepZ() * -0.1f);

        context.getFont().drawInBatch(text, 0f, 0f, 0, false, matrices.last().pose(), vertexConsumers, Font.DisplayMode.NORMAL, 0xFFFFFF, 0);

        matrices.popPose();
    }
}
