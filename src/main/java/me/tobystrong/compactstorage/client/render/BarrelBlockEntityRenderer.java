package me.tobystrong.compactstorage.client.render;

import com.mojang.blaze3d.systems.RenderSystem;
import me.tobystrong.compactstorage.block.BarrelBlock;
import me.tobystrong.compactstorage.block.entity.BarrelBlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.FontManager;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderDispatcher;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.util.math.Vector3f;
import net.minecraft.entity.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import org.lwjgl.opengl.GL11;
import org.w3c.dom.Text;

public class BarrelBlockEntityRenderer extends BlockEntityRenderer<BarrelBlockEntity> {

    public BarrelBlockEntityRenderer(BlockEntityRenderDispatcher dispatcher) {
        super(dispatcher);
    }

    @Override
    public void render(BarrelBlockEntity blockEntity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
        ///super.render(blockEntity, tickDelta, matrices, vertexConsumers, light, overlay);
        TextRenderer textRenderer = MinecraftClient.getInstance().textRenderer;
        ItemStack barrel_stack = blockEntity.barrel_item;
        String text = blockEntity.getText();
        String text2 = blockEntity.getSubText();

        Direction blockDirection = blockEntity.getWorld().getBlockState(blockEntity.getPos()).get(BarrelBlock.FACING); 
        int rotation = blockDirection == Direction.NORTH ? 0 : (blockDirection == Direction.EAST ? 3 : (blockDirection == Direction.SOUTH ? 2 : 1));

        matrices.push();
        
        matrices.translate(0.5, 0, 0.5);
        matrices.multiply(Vector3f.POSITIVE_Y.getDegreesQuaternion(90f * rotation));
        matrices.translate(-0, 0.45, -0.5);
        
        matrices.multiply(Vector3f.POSITIVE_Y.getDegreesQuaternion(180f));
        matrices.scale(0.5f, 0.5f, 0.5f);
        MinecraftClient.getInstance().getItemRenderer().renderItem(barrel_stack, ModelTransformation.Mode.GROUND, light, overlay, matrices, vertexConsumers);
        matrices.pop();

        matrices.push();
        matrices.translate(0.5, 0, 0.5);

        matrices.multiply(Vector3f.POSITIVE_Y.getDegreesQuaternion(rotation * 90f));
        matrices.multiply(Vector3f.POSITIVE_X.getDegreesQuaternion(180f));
        matrices.multiply(Vector3f.POSITIVE_Y.getDegreesQuaternion(180f));
        matrices.translate(-0, -0.85, -0.45);
        matrices.scale(0.01f, 0.01f, 0.01f);

        textRenderer.draw(text, -textRenderer.getStringWidth(text) / 2, 0, 0x000000, false, matrices.peek().getModel(), vertexConsumers, false, 0x000000, light);


        matrices.translate(0, 10f, 0);

        textRenderer.draw(text2, -textRenderer.getStringWidth(text2) / 2, 0, 0x000000, false, matrices.peek().getModel(), vertexConsumers, false, 0x000000, light);
        matrices.pop();
        }

    public void renderText(BarrelBlockEntity tileEntity, float scale)
    {
        Direction facing = (Direction) tileEntity.getWorld().getBlockState(tileEntity.getPos()).get(BarrelBlock.FACING);

        GL11.glPushMatrix();
        GL11.glTranslatef((float) tileEntity.getPos().getX() + 0.5f, (float) tileEntity.getPos().getY() + 0.5f, (float) tileEntity.getPos().getZ() + 0.5f);

        rotateElement(facing);

        GL11.glTranslatef(0f, -0.225f, -0.44f);

        GL11.glScalef(scale, scale, scale);

        TextRenderer fontrenderer = MinecraftClient.getInstance().textRenderer;
        byte b0 = 0;

        String s = tileEntity.getText();

        fontrenderer.draw(s, -fontrenderer.getStringWidth(s) / 2, 0, b0);

        GL11.glPopMatrix();
    }

    public void rotateElement(Direction facing)
    {
        switch(facing)
        {
            case WEST:
            {
                GL11.glRotatef(180f, 1F, 0.0F, 0f);
                GL11.glRotatef(270f, 0F, 1F, 0f);
                break;
            }
            case EAST:
            {
                GL11.glRotatef(180f, 1F, 0.0F, 0f);
                GL11.glRotatef(90f, 0F, 1F, 0f);
                break;
            }
            case SOUTH:
            {
                GL11.glRotatef(180f, 1F, 0.0F, 0f);
                GL11.glRotatef(180f, 0F, 1F, 0f);
                break;
            }
            case NORTH:
            {
                GL11.glRotatef(180f, 1F, 0.0F, 0f);
                GL11.glRotatef(0f, 0F, 1F, 0f);
                break;
            }
            default:
            {
                GL11.glRotatef(180F, -1F, 0.0F, 3F);
                break;
            }
        }
    }

    public void renderItem(BarrelBlockEntity tileEntity, float scale, float size)
    {
        Direction facing = (Direction) tileEntity.getWorld().getBlockState(tileEntity.getPos()).get(BarrelBlock.FACING);
        BlockPos pos = tileEntity.getPos();
        ItemStack stack = tileEntity.barrel_item.copy();

        if(stack != null && !stack.isEmpty())
        {
            if (stack.isEmpty())
            {
                return;
            }

            stack.setCount(1);

            GL11.glPushMatrix();
            ItemEntity ent = new ItemEntity(tileEntity.getWorld(), tileEntity.getPos().getX(), tileEntity.getPos().getY(), tileEntity.getPos().getZ(), stack);

            ent.horizontalSpeed = 0;

            RenderSystem.disableLighting();

            GL11.glTranslatef((float) tileEntity.getPos().getX() + 0.5f, (float) tileEntity.getPos().getY() + 0.5f, (float) tileEntity.getPos().getZ() + 0.5f);
            rotateElement(facing);
            //GL11.glRotatef(180f, 0, 0, 0);
            GL11.glTranslatef(-(size / 3), -0.1f, -0.55f);
            GL11.glScalef(size / 24, size / 24, 0.001f);

            //Minecraft.getMinecraft().getRenderManager().renderEntity(ent, 0, 0, 0,0,0, false);
            MinecraftClient.getInstance().getItemRenderer().renderGuiItem(stack, 0, 0);
            GL11.glPopMatrix();
        }
    }
}
