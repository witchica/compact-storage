package me.tobystrong.compactstorage.client.render;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import me.tobystrong.compactstorage.block.CompactChestBlock;
import me.tobystrong.compactstorage.block.entity.CompactChestBlockEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.ChestBlock;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.Vector3f;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;


@OnlyIn(Dist.CLIENT)
public class CompactChestBlockEntityRenderer extends TileEntityRenderer<CompactChestBlockEntity> {
    private final ModelRenderer model_part;
    private final ModelRenderer model_part_2;
    private final ModelRenderer model_part_3;

    public static final ResourceLocation CHEST_TEXTURE = new ResourceLocation("compact-storage", "textures/entities/chest.png");

    public CompactChestBlockEntityRenderer(TileEntityRendererDispatcher blockEntityRenderDispatcher) {
      super(blockEntityRenderDispatcher);

      this.model_part_3 = new ModelRenderer(64, 64, 0, 19);
      this.model_part_3.addBox(1.0F, 0.0F, 1.0F, 14.0F, 10.0F, 14.0F, 0.0F);

      this.model_part = new ModelRenderer(64, 64, 0, 0);
      this.model_part.addBox(1.0F, 0.0F, 0.0F, 14.0F, 5.0F, 14.0F, 0.0F);
      this.model_part.rotationPointY = 9.0F;
      this.model_part.rotationPointZ = 1.0F;
      
      this.model_part_2 = new ModelRenderer(64, 64, 0, 0);
      this.model_part_2.addBox(7.0F, -1.0F, 15.0F, 2.0F, 4.0F, 1.0F, 0.0F);
      this.model_part_2.rotationPointY = 8.0F;
   }

   public void render(CompactChestBlockEntity blockEntity, float tickDelta, MatrixStack matrices, IRenderTypeBuffer vertexConsumers, int light, int overlay) {
        World world = blockEntity.getWorld();
        boolean bl = world != null;

        BlockState blockState = bl ? blockEntity.getBlockState() : (BlockState)Blocks.CHEST.getDefaultState().with(ChestBlock.FACING, Direction.SOUTH);
        Block block = blockState.getBlock();

        matrices.push();

        float y_rotation = ((Direction) blockState.get(CompactChestBlock.FACING)).getHorizontalAngle();

        matrices.translate(0.5D, 0.5D, 0.5D);
        matrices.rotate(Vector3f.YP.rotationDegrees(-y_rotation));
        matrices.translate(-0.5D, -0.5D, -0.5D);

        float lid_openness = blockEntity.getLidAngle(tickDelta);

        //MinecraftClient.getInstance().getTextureManager().bindTexture(CHEST_TEXTURE);

        //SpriteIdentifier spriteIdentifier = TexturedRenderLayers.getChestTexture(blockEntity, ChestType.SINGLE, false);
        //VertexConsumer vertexConsumer = spriteIdentifier.getVertexConsumer(vertexConsumers, RenderLayer::getEntityCutout);

        IVertexBuilder vertexConsumer = vertexConsumers.getBuffer(RenderType.getEntitySolid(CHEST_TEXTURE));

        model_part.rotateAngleX = -(lid_openness * 1.5707964F);
        model_part_2.rotateAngleX = model_part.rotateAngleX;

        model_part.render(matrices, vertexConsumer, light, overlay);
        model_part_2.render(matrices, vertexConsumer, light, overlay);
        model_part_3.render(matrices, vertexConsumer, light, overlay);

        matrices.pop();
   }
}
