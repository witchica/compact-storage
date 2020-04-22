//package me.tobystrong.compactstorage.client.render;
//
//import com.mojang.blaze3d.matrix.MatrixStack;
//import me.tobystrong.compactstorage.block.BarrelBlock;
//import me.tobystrong.compactstorage.block.entity.BarrelBlockEntity;
//import net.minecraft.client.Minecraft;
//import net.minecraft.client.gui.FontRenderer;
//import net.minecraft.client.renderer.IRenderTypeBuffer;
//import net.minecraft.client.renderer.Vector3f;
//import net.minecraft.client.renderer.model.ItemCameraTransforms;
//import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
//import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
//import net.minecraft.item.ItemStack;
//import net.minecraft.util.Direction;
//
//public class BarrelBlockEntityRenderer extends TileEntityRenderer<BarrelBlockEntity> {
//
//    public BarrelBlockEntityRenderer(TileEntityRendererDispatcher dispatcher) {
//        super(dispatcher);
//    }
//
//    @Override
//    public void render(BarrelBlockEntity blockEntity, float tickDelta, MatrixStack matrices, IRenderTypeBuffer vertexConsumers, int light, int overlay) {
//        ///super.render(blockEntity, tickDelta, matrices, vertexConsumers, light, overlay);
//        FontRenderer textRenderer = Minecraft.getInstance().fontRenderer;
//        ItemStack barrel_stack = blockEntity.getBarrelItem();
//        String text = blockEntity.getText();
//        String text2 = blockEntity.getSubText();
//
//        Direction blockDirection = blockEntity.getWorld().getBlockState(blockEntity.getPos()).get(BarrelBlock.FACING);
//        int rotation = blockDirection == Direction.NORTH ? 0 : (blockDirection == Direction.EAST ? 3 : (blockDirection == Direction.SOUTH ? 2 : 1));
//
//        matrices.push();
//
//        matrices.translate(0.5, 0, 0.5);
//        matrices.rotate(Vector3f.YP.rotationDegrees(90f * rotation));
//        matrices.translate(-0, 0.45, -0.5);
//
//        matrices.rotate(Vector3f.YP.rotationDegrees(180f));
//        matrices.scale(0.5f, 0.5f, 0.5f);
//        Minecraft.getInstance().getItemRenderer().renderItem(barrel_stack, ItemCameraTransforms.TransformType.GROUND, light, overlay, matrices, vertexConsumers);
//        matrices.pop();
//
//        matrices.push();
//        matrices.translate(0.5, 0, 0.5);
//
//        matrices.rotate(Vector3f.YP.rotationDegrees(rotation * 90f));
//        matrices.rotate(Vector3f.XP.rotationDegrees(180f));
//        matrices.rotate(Vector3f.YP.rotationDegrees(180f));
//        matrices.translate(-0, -0.85, -0.45);
//        matrices.scale(0.01f, 0.01f, 0.01f);
//
//        textRenderer.renderString(text, -textRenderer.getStringWidth(text) / 2, 0, 0x000000, false, matrices.getLast().getMatrix(), vertexConsumers, false, 0x000000, light);
//
//
//        matrices.translate(0, 10f, 0);
//
//        textRenderer.renderString(text2, -textRenderer.getStringWidth(text2) / 2, 0, 0x000000, false, matrices.getLast().getMatrix(), vertexConsumers, false, 0x000000, light);
//        matrices.pop();
//    }
//}
