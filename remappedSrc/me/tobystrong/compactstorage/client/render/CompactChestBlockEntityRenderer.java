package me.tobystrong.compactstorage.client.render;

import com.mojang.blaze3d.platform.GlStateManager;

import me.tobystrong.compactstorage.block.CompactChestBlock;
import me.tobystrong.compactstorage.block.entity.CompactChestBlockEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.ChestBlock;
import net.minecraft.block.enums.ChestType;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.TexturedRenderLayers;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderDispatcher;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.util.SpriteIdentifier;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.util.math.Vector3f;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;



@Environment(EnvType.CLIENT)
public class CompactChestBlockEntityRenderer extends BlockEntityRenderer<CompactChestBlockEntity> {
    private final ModelPart model_part;
    private final ModelPart model_part_2;
    private final ModelPart model_part_3;

    public static final Identifier CHEST_TEXTURE = new Identifier("compact-storage", "textures/entities/chest.png");

    public CompactChestBlockEntityRenderer(BlockEntityRenderDispatcher blockEntityRenderDispatcher) {
      super(blockEntityRenderDispatcher);

      this.model_part_3 = new ModelPart(64, 64, 0, 19);
      this.model_part_3.addCuboid(1.0F, 0.0F, 1.0F, 14.0F, 10.0F, 14.0F, 0.0F);

      this.model_part = new ModelPart(64, 64, 0, 0);
      this.model_part.addCuboid(1.0F, 0.0F, 0.0F, 14.0F, 5.0F, 14.0F, 0.0F);
      this.model_part.pivotY = 9.0F;
      this.model_part.pivotZ = 1.0F;
      
      this.model_part_2 = new ModelPart(64, 64, 0, 0);
      this.model_part_2.addCuboid(7.0F, -1.0F, 15.0F, 2.0F, 4.0F, 1.0F, 0.0F);
      this.model_part_2.pivotY = 8.0F;
   }

   public void render(CompactChestBlockEntity blockEntity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
        World world = blockEntity.getWorld();
        boolean bl = world != null;

        BlockState blockState = bl ? blockEntity.getCachedState() : (BlockState)Blocks.CHEST.getDefaultState().with(ChestBlock.FACING, Direction.SOUTH);
        Block block = blockState.getBlock();

        matrices.push();

        float y_rotation = ((Direction) blockState.get(CompactChestBlock.FACING)).asRotation();

        matrices.translate(0.5D, 0.5D, 0.5D);
        matrices.multiply(Vector3f.POSITIVE_Y.getDegreesQuaternion(-y_rotation));
        matrices.translate(-0.5D, -0.5D, -0.5D);

        float lid_openness = blockEntity.getAnimationProgress(tickDelta); 

        //MinecraftClient.getInstance().getTextureManager().bindTexture(CHEST_TEXTURE);

        //SpriteIdentifier spriteIdentifier = TexturedRenderLayers.getChestTexture(blockEntity, ChestType.SINGLE, false);
        //VertexConsumer vertexConsumer = spriteIdentifier.getVertexConsumer(vertexConsumers, RenderLayer::getEntityCutout);

        VertexConsumer vertexConsumer = vertexConsumers.getBuffer(RenderLayer.getEntitySolid(CHEST_TEXTURE));

        model_part.pitch = -(lid_openness * 1.5707964F);
        model_part_2.pitch = model_part.pitch;

        model_part.render(matrices, vertexConsumer, light, overlay);
        model_part_2.render(matrices, vertexConsumer, light, overlay);
        model_part_3.render(matrices, vertexConsumer, light, overlay);

        matrices.pop();
   }
}
