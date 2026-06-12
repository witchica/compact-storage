package com.witchica.compactstorage.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.witchica.compactstorage.CompactStorage;
import com.witchica.compactstorage.api.StorageTypeProvider;
import com.witchica.compactstorage.data.StorageType;
import com.witchica.compactstorage.mod.CompactStorageItems;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.player.PlayerModel;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.entity.state.AvatarRenderState;
import net.minecraft.client.renderer.entity.state.HumanoidRenderState;
import net.minecraft.client.renderer.item.ItemModelResolver;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

public class BackpackFeatureRenderer extends RenderLayer<AvatarRenderState, PlayerModel> {
    private final ItemModelResolver itemModelResolver;

    public BackpackFeatureRenderer(RenderLayerParent<AvatarRenderState, PlayerModel> renderer, ItemModelResolver itemModelResolver) {
        super(renderer);
        this.itemModelResolver = itemModelResolver;
    }

    @Override
    public void submit(PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int i, AvatarRenderState avatarRenderState, float v, float v1) {
        // Hide for Elytra
        if(!avatarRenderState.chestEquipment.isEmpty()) {
            return;
        }

        // Hide for cape
        if(avatarRenderState.skin.cape() != null && avatarRenderState.showCape) {
            return;
        }

        if(Minecraft.getInstance() != null) {
            if(Minecraft.getInstance().level != null) {
                Entity entity = Minecraft.getInstance().level.getEntity(avatarRenderState.id);

                if(entity instanceof Player player) {
                    ItemStack backpack = CompactStorage.findCuriosBackpack(player);
                    StorageType type = StorageType.RED;

                    if(backpack.getItem() instanceof StorageTypeProvider provider) {
                        type = provider.getStorageType();
                    }

                    if(!backpack.isEmpty()) {
                        poseStack.pushPose();
                        poseStack.mulPose(Axis.XP.rotationDegrees(180f));

                        if(avatarRenderState.isCrouching) {
                            poseStack.mulPose(Axis.XP.rotationDegrees(30f));
                            poseStack.translate(0f,-0.175f,0.1f);
                        }

                        if(type.isWooden()) {
                            poseStack.translate(0f, -0.275f, -0.15f);
                            poseStack.scale(0.5f, 0.5f, 0.5f);
                        } else {
                            poseStack.translate(0f, -0.325f, -0.165f);
                            poseStack.scale(0.9f, 0.9f, 0.9f);
                        }

                        ItemStackRenderState renderState = new ItemStackRenderState();
                        itemModelResolver.updateForTopItem(renderState, backpack, ItemDisplayContext.FIXED, null, null, 0);
                        renderState.submit(poseStack, submitNodeCollector, avatarRenderState.lightCoords, OverlayTexture.NO_OVERLAY, avatarRenderState.outlineColor);
                        poseStack.popPose();
                    }
                }
            }
        }
    }
}
