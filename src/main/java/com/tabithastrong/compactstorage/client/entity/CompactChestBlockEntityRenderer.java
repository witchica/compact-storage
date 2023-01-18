package com.tabithastrong.compactstorage.client.entity;

import com.tabithastrong.compactstorage.CompactStorage;
import com.tabithastrong.compactstorage.block.CompactChestBlock;
import com.tabithastrong.compactstorage.block.entity.CompactChestBlockEntity;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.render.block.entity.ChestBlockEntityRenderer;
import net.minecraft.client.render.entity.model.EntityModelLayers;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

@Environment(EnvType.CLIENT)
public class CompactChestBlockEntityRenderer implements BlockEntityRenderer<CompactChestBlockEntity> {
    private final ModelPart chestBase;
    private final ModelPart chestLid;
    private final ModelPart chestLock;

    public static final Map<Block, Identifier> CHEST_TEXTURES = new HashMap<Block, Identifier>();

    static {
        for(int i = 0; i < 16; i++) {
            CHEST_TEXTURES.put(CompactStorage.COMPACT_CHEST_BLOCKS[i], new Identifier("compact_storage", String.format("textures/block/compact_chest_%s.png", DyeColor.byId(i).name().toLowerCase())));
        }
    }
    
    public CompactChestBlockEntityRenderer(BlockEntityRendererFactory.Context ctx) {
        super();

        ModelPart modelPart = ctx.getLayerModelPart(EntityModelLayers.CHEST);
        this.chestBase = modelPart.getChild("bottom");
        this.chestLid = modelPart.getChild("lid");
        this.chestLock = modelPart.getChild("lock");
    }

    @Override
    public void render(CompactChestBlockEntity compactChestBlockEntity, float delta, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int light, int overlay) {
        World world = compactChestBlockEntity.getWorld();
        boolean bl = world != null;


        BlockState blockState = bl ? compactChestBlockEntity.getCachedState() : CompactStorage.COMPACT_CHEST_BLOCKS[0].getDefaultState().with(CompactChestBlock.FACING, Direction.SOUTH);
        Block block = blockState.getBlock();

        matrixStack.push();

        float y_rotation = ((Direction) blockState.get(CompactChestBlock.FACING)).asRotation();

        matrixStack.translate(0.5D, 0.5D, 0.5D);
        matrixStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(-y_rotation));
        matrixStack.translate(-0.5D, -0.5D, -0.5D);

        float lid_openness = compactChestBlockEntity.getAnimationProgress(delta);
        
        VertexConsumer vertexConsumer = vertexConsumerProvider.getBuffer(RenderLayer.getEntitySolid(CHEST_TEXTURES.get(block)));

        chestLid.pitch = -(lid_openness * 1.5707964F);
        chestLock.pitch = chestLid.pitch;

        chestBase.render(matrixStack, vertexConsumer, light, overlay);
        chestLid.render(matrixStack, vertexConsumer, light, overlay);
        chestLock.render(matrixStack, vertexConsumer, light, overlay);

        matrixStack.pop();
    }
    
}
