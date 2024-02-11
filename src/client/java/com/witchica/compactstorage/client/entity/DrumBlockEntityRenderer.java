package com.witchica.compactstorage.client.entity;

import com.witchica.compactstorage.block.DrumBlock;
import com.witchica.compactstorage.block.entity.DrumBlockEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.font.Font;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.render.entity.ItemFrameEntityRenderer;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.RotationAxis;

public class DrumBlockEntityRenderer implements BlockEntityRenderer<DrumBlockEntity> {
    private final BlockEntityRendererFactory.Context context;

    public DrumBlockEntityRenderer(BlockEntityRendererFactory.Context ctx) {
        super();
        this.context = ctx;
    }
    @Override
    public void render(DrumBlockEntity drumBlock, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
        if(drumBlock == null) {
            return;
        }

        ItemStack stack = drumBlock.clientItem;
        String text = drumBlock.getTextToDisplay();

        Direction direction = drumBlock.getCachedState().get(DrumBlock.FACING);

        if(!stack.isEmpty()) {
            matrices.push();
            matrices.translate(0.5f, 0.5f, 0.5f);
            matrices.translate(direction.getOffsetX() * 0.5f, direction.getOffsetY() * 0.5f, direction.getOffsetZ() * 0.5f);
            matrices.scale(0.35f, 0.35f, 0.35f);
            matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(direction.asRotation()));

            if(direction == Direction.UP || direction == Direction.DOWN) {
                matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(direction == Direction.UP ? 90f : 270f));
            }

            matrices.translate(0f, -0.15f, 0f);

            context.getItemRenderer().renderItem(stack, ModelTransformationMode.FIXED, 15728880, OverlayTexture.DEFAULT_UV, matrices, vertexConsumers, drumBlock.getWorld(), drumBlock.hashCode());
            matrices.pop();
        }

        matrices.push();
        float width = this.context.getTextRenderer().getWidth(text);
        float textScale = 0.007f;


        matrices.translate(0.5f, 0.5f, 0.5f);
        matrices.translate(direction.getOffsetX() * 0.51f, direction.getOffsetY() * 0.51f, direction.getOffsetZ() * 0.51f);
        matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(-direction.asRotation()));

        if(direction == Direction.UP || direction == Direction.DOWN) {
            matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(direction == Direction.UP ? -90f : 90f));
            matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(direction == Direction.UP ? -90f : 90f));
        }

        matrices.translate(-(width * textScale)/2, 0f, 0f);
        matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(180f));
        matrices.translate(0f, -0.2f, 0f);
        matrices.scale(textScale, textScale, textScale);
        //matrixStack.translate(direction.getStepX() * -0.1f, direction.getStepY() * -0.1f, direction.getStepZ() * -0.1f);

        context.getTextRenderer().draw(text, 0f, 0f, 0, false, matrices.peek().getPositionMatrix(), vertexConsumers, TextRenderer.TextLayerType.NORMAL, 0xFFFFFF, 0);

        matrices.pop();
    }
}
