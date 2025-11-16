package com.witchica.compactstorage.common.client.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.witchica.compactstorage.CompactStoragePlatform;
import com.witchica.compactstorage.common.item.BackpackItem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.ItemInHandRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.PlayerModelPart;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import java.util.Optional;

public class BackpackFeatureRenderer extends RenderLayer<AbstractClientPlayer, PlayerModel<AbstractClientPlayer>> {
    private final ItemInHandRenderer itemInHandRenderer;

    public BackpackFeatureRenderer(PlayerRenderer playerRenderer, ItemInHandRenderer itemInHandRenderer) {
        super(playerRenderer);
        this.itemInHandRenderer = itemInHandRenderer;
    }

    @Override
    public void render(PoseStack poseStack, MultiBufferSource buffer, int packedLight, AbstractClientPlayer livingEntity, float limbSwing, float limbSwingAmount, float partialTick, float ageInTicks, float netHeadYaw, float headPitch) {
        // Hide for Elytra
        if(livingEntity.getItemBySlot(EquipmentSlot.CHEST).is(Items.ELYTRA)) {
            return;
        }

        // Hide for cape
        if(livingEntity.isCapeLoaded() && livingEntity.isModelPartShown(PlayerModelPart.CAPE) && livingEntity.getCloakTextureLocation() != null) {
            return;
        }

        Optional<ItemStack> backpack = CompactStoragePlatform.getBackpackToRender(livingEntity);

        if(backpack.isPresent()) {
            ItemStack stack = backpack.get();
            BackpackItem backpackItem = (BackpackItem)stack.getItem();

            poseStack.pushPose();
            poseStack.mulPose(Axis.XP.rotationDegrees(180f));

            if(livingEntity.isCrouching()) {
                poseStack.mulPose(Axis.XP.rotationDegrees(30f));
                poseStack.translate(0f,-0.175f,0.1f);
            }

            if(backpackItem.getVisualType().isWooden()) {
                poseStack.translate(0f, -0.3f, -0.19f);
                poseStack.scale(0.45f, 0.45f, 0.45f);
            } else {
                poseStack.translate(0f, -0.3f, -0.15f);
                poseStack.scale(0.9f, 0.9f, 0.9f);
            }
            itemInHandRenderer.renderItem(livingEntity, stack, ItemDisplayContext.FIXED, false, poseStack, buffer, packedLight);
            poseStack.popPose();
        }
    }
}
