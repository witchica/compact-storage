package com.witchica.compactstorage.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.witchica.compactstorage.CompactStorage;
import com.witchica.compactstorage.block.CompactChestBlock;
import com.witchica.compactstorage.block.entity.CompactChestBlockEntity;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.model.object.chest.ChestModel;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.state.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.Material;
import net.minecraft.client.resources.model.MaterialSet;
import net.minecraft.resources.Identifier;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.Nullable;
import com.witchica.compactstorage.data.StorageType;

import java.util.HashMap;
import java.util.Map;

public class CompactChestBlockEntityRenderer implements BlockEntityRenderer<CompactChestBlockEntity, CompactChestBlockEntityRenderer.CompactChestRenderState> {
    public static class CompactChestRenderState extends BlockEntityRenderState {
        float lidAngle;
        float rotation;
        StorageType type = StorageType.OAK;
    }

    public static final Map<StorageType, Material> CHEST_MATERIALS = new HashMap<>();

    static {
        for(StorageType type : StorageType.values()) {
            CHEST_MATERIALS.put(type, Sheets.CHEST_MAPPER.apply(Identifier.fromNamespaceAndPath(CompactStorage.MOD_ID, type.getName() + "_chest")));
        }
    }

    private final ChestModel chestModel;
    private final MaterialSet materials;

    public CompactChestBlockEntityRenderer(BlockEntityRendererProvider.Context context) {
        super();
        this.materials = context.materials();
        this.chestModel = new ChestModel(context.bakeLayer(ModelLayers.CHEST));
    }


    @Override
    public CompactChestRenderState createRenderState() {
        return new CompactChestRenderState();
    }

    @Override
    public void extractRenderState(CompactChestBlockEntity blockEntity, CompactChestRenderState renderState, float partialTick, Vec3 cameraPosition, ModelFeatureRenderer.@Nullable CrumblingOverlay breakProgress) {
        BlockEntityRenderer.super.extractRenderState(blockEntity, renderState, partialTick, cameraPosition, breakProgress);

        if(blockEntity != null) {
            renderState.rotation = blockEntity.getBlockState().getValue(CompactChestBlock.FACING).toYRot();
            renderState.lidAngle = blockEntity.getOpenNess(partialTick);
            if(blockEntity.getBlockState().getBlock() instanceof CompactChestBlock compactChestBlock) {
                renderState.type = compactChestBlock.getStorageType();
            }
        }
    }

    @Override
    public void submit(CompactChestRenderState compactChestRenderState, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState cameraRenderState) {
        poseStack.pushPose();
        poseStack.translate(0.5f, 0.5f, 0.5f);
        poseStack.mulPose(Axis.YP.rotationDegrees(-compactChestRenderState.rotation));
        poseStack.translate(-0.5f, -0.5f, -0.5f);
        float f = compactChestRenderState.lidAngle;
        f = 1.0F - f;
        f = 1.0F - f * f * f;

        Material material = CHEST_MATERIALS.get(compactChestRenderState.type);
        RenderType renderType = material.renderType(RenderTypes::entitySolid);
        TextureAtlasSprite textureAtlasSprite = this.materials.get(material);

        submitNodeCollector.submitModel(chestModel, f, poseStack, renderType, compactChestRenderState.lightCoords, OverlayTexture.NO_OVERLAY, -1, textureAtlasSprite, 0, compactChestRenderState.breakProgress);
        poseStack.popPose();
    }
}
