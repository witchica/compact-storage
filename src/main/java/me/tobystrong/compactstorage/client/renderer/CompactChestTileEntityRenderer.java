package me.tobystrong.compactstorage.client.renderer;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import me.tobystrong.compactstorage.block.CompactChestBlock;
import me.tobystrong.compactstorage.block.tile.CompactChestTileEntity;
import net.minecraft.block.*;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.client.renderer.model.RenderMaterial;
import net.minecraft.client.renderer.tileentity.ChestTileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.item.DyeColor;
import net.minecraft.state.properties.ChestType;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraft.world.World;

import java.util.HashMap;
import java.util.Map;

public class CompactChestTileEntityRenderer extends TileEntityRenderer<CompactChestTileEntity> {
    private final ModelRenderer singleLid;
    private final ModelRenderer singleBottom;
    private final ModelRenderer singleLatch;

    public static Map<String, ResourceLocation> compactChestTextures;

    static {
        compactChestTextures = new HashMap<String, ResourceLocation>();

        //load all of the textures for each colour of chest
        for(int i = 0; i < DyeColor.values().length; i++) {
            String chest = "compact_chest_" + DyeColor.values()[i].name().toLowerCase();
            compactChestTextures.put(chest, new ResourceLocation("compactstorage", "textures/entities/" + chest + ".png"));
        }
    }

    public CompactChestTileEntityRenderer(TileEntityRendererDispatcher rendererDispatcherIn) {
        super(rendererDispatcherIn);

        this.singleBottom = new ModelRenderer(64, 64, 0, 19);
        this.singleBottom.addBox(1.0F, 0.0F, 1.0F, 14.0F, 10.0F, 14.0F, 0.0F);
        this.singleLid = new ModelRenderer(64, 64, 0, 0);
        this.singleLid.addBox(1.0F, 0.0F, 0.0F, 14.0F, 5.0F, 14.0F, 0.0F);
        this.singleLid.rotationPointY = 9.0F;
        this.singleLid.rotationPointZ = 1.0F;
        this.singleLatch = new ModelRenderer(64, 64, 0, 0);
        this.singleLatch.addBox(7.0F, -1.0F, 15.0F, 2.0F, 4.0F, 1.0F, 0.0F);
        this.singleLatch.rotationPointY = 8.0F;
    }

    @Override
    public void render(CompactChestTileEntity tileEntityIn, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int combinedLightIn, int combinedOverlayIn) {
        World world = tileEntityIn.getWorld();
        BlockState blockstate = tileEntityIn.getBlockState();
        Block block = blockstate.getBlock();

        matrixStackIn.push();

        //get the angle from the facing property and rotate properly
        float f = blockstate.get(CompactChestBlock.PROPERTY_FACING).getHorizontalAngle();
        matrixStackIn.translate(0.5D, 0.5D, 0.5D);
        matrixStackIn.rotate(Vector3f.YP.rotationDegrees(-f));
        matrixStackIn.translate(-0.5D, -0.5D, -0.5D);

        float lidAngle = tileEntityIn.getLidAngle(partialTicks); //chest lid rotation
        lidAngle = 1.0F - lidAngle;
        lidAngle = 1.0F - lidAngle * lidAngle * lidAngle;

        //get buffer from the texture of block
        IVertexBuilder ivertexbuilder = bufferIn.getBuffer(RenderType.getEntitySolid(compactChestTextures.get(block.getRegistryName().getPath())));
        singleLid.rotateAngleX = -(lidAngle * ((float)Math.PI / 2F));

        singleLatch.rotateAngleX = singleLid.rotateAngleX;

        singleLid.render(matrixStackIn, ivertexbuilder, combinedLightIn, combinedOverlayIn);
        singleLatch.render(matrixStackIn, ivertexbuilder, combinedLightIn, combinedOverlayIn);
        singleBottom.render(matrixStackIn, ivertexbuilder, combinedLightIn, combinedOverlayIn);

        matrixStackIn.pop();
    }
}
