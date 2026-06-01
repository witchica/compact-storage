package com.witchica.compactstorage.client.renderer.model;

import net.minecraft.client.model.Model;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.rendertype.RenderTypes;

public class ChestLockModel extends Model<Float> {
    private final ModelPart lock;

    public ChestLockModel(ModelPart root) {
        super(root, RenderTypes::entitySolid);
        this.lock = root.getChild("lock");
        root.getChild("bottom").skipDraw = true;
        root.getChild("lid").skipDraw = true;
    }

    @Override
    public void setupAnim(Float renderState) {
        super.setupAnim(renderState);
        this.lock.xRot = -(renderState * ((float)Math.PI / 2F));
    }
}
