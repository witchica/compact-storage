package me.tobystrong.compactstorage.client.gui;

import com.mojang.blaze3d.systems.RenderSystem;

import me.tobystrong.compactstorage.util.CompactStorageInventoryImpl;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.SliderWidget;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;

public class UpgradeInventoryScreen extends Screen {
    public static enum UpgradeType {
        CHEST, BACKPACK;
    };

    public static final Identifier CHEST_BACKGROUND_TEXTURE = new Identifier("compact-storage",
            "textures/gui/chest.png");
    public static final Identifier TABS_TEXTURE = new Identifier("textures/gui/container/creative_inventory/tabs.png");

    public int x;
    public int y;
    public int guiWidth;
    public int guiHeight;

    public UpgradeType upgrade_type;

    public SliderWidget width_slider;
    public SliderWidget height_slider;
    public SliderWidget color_slider;

    public BlockPos chest_pos;
    public ItemStack item_stack;

    public CompactStorageInventoryImpl upgrade_target_inventory;

    protected UpgradeInventoryScreen(UpgradeType upgrade_type, BlockPos pos, ItemStack item_stack) {
        super(new TranslatableText("gui.compact-storage:upgrade-inventory"));
        this.upgrade_type = upgrade_type;
        this.chest_pos = pos;
        this.item_stack = item_stack;

        guiWidth = 200;
        guiHeight = 200;
    }

    @Override
    protected void init() {
        super.init();

        x = (width / 2) - (guiWidth / 2);
        y = (height / 2) - (guiHeight / 2);

        if(upgrade_type == UpgradeType.BACKPACK) {
            //backpack logic
        } else {
            if(minecraft.world.getBlockEntity(chest_pos) instanceof CompactStorageInventoryImpl) {
                upgrade_target_inventory = (CompactStorageInventoryImpl) minecraft.world.getBlockEntity(chest_pos);
            }
        }

        color_slider = new SliderWidget(x + 7, y + 18, guiWidth - 15, 20, 0){
            @Override
            protected void updateMessage() {
                this.setMessage("Hue: " + (Math.floor(this.value * 360)));
                
            }
        
            @Override
            protected void applyValue() {
                // TODO Auto-generated method stub
                
            }
        };
    }

    public ItemStack calculateUpgradeCost() {
        return new ItemStack(Items.IRON_INGOT, upgrade_target_inventory.getInventoryWidth());
    }

    @Override
    public void render(int mouseX, int mouseY, float delta) {
        super.render(mouseX, mouseY, delta);

        this.renderBackground();
        RenderSystem.disableLighting();

        drawTab(0, false, new ItemStack(Items.CHEST, 1));  

        minecraft.getTextureManager().bindTexture(CHEST_BACKGROUND_TEXTURE);

        //blit(x, y, width, height, u, v, uWidth, vHeight, texWidth, texHeight);
        //corners

        blit(x, y, 0, 0, 7, 7, 15, 15);
        blit(x + guiWidth - 7, y, 8, 0, 7, 7, 15, 15);

        blit(x, y + guiHeight - 7, 0, 8, 8, 7, 15, 15);
        blit(x + guiWidth - 7, y + guiHeight - 7, 8, 8, 7, 7, 15, 15);

        //middle bit
        blit(x + 7, y + 7, guiWidth - 14, guiHeight - 14, 7, 7, 1, 1, 15, 15);

        //left side
        blit(x, y + 7, 7, guiHeight - 14, 0, 7, 7, 1, 15, 15);

        //right side
        blit(x + guiWidth - 7, y + 7, 7, guiHeight - 14, 8, 7, 7, 1, 15, 15);

        //top
        blit(x + 7, y, guiWidth - 14, 7, 7, 0, 1, 7, 15, 15);

        //bottom
        blit(x + 7, y + guiHeight - 7, guiWidth - 14, 7, 7, 8, 1, 7, 15, 15);

        drawTab(1, true, new ItemStack(Items.IRON_INGOT, 1));

        minecraft.textRenderer.draw("upgradeInventory", x + 7, y + 7, 0x333333);
        minecraft.textRenderer.draw("The chest is currently " + upgrade_target_inventory.getInventoryWidth() + " x " + upgrade_target_inventory.getInventoryHeight(), x + 7, y + 24, 0x333333);

        if(isMouseOverTab(1, mouseX, mouseY)) {
            renderTooltip("Settings", mouseX, mouseY);
        }

        if(isMouseOverTab(0, mouseX, mouseY)) {
            renderTooltip("Inventory", mouseX, mouseY);
        }
        
        color_slider.render(mouseX, mouseY, delta);
    }

    

    public void drawTab(int index, boolean is_active, ItemStack item) {
        this.minecraft.getTextureManager().bindTexture(TABS_TEXTURE);

        int uoffset = is_active ? 32 : 0;
        int y_offset = is_active ? 28 : 30;

        int voffset = index == 1 ? 27 : 0;

        blit(x + (index * 29), y - 28, 29, 32, voffset, uoffset, 29, 32, 256, 256);
        minecraft.getItemRenderer().renderGuiItem(item, x + (index * 29) + 6, y - 20);
    }

    public boolean isMouseOverTab(int index, double mouseX, double mouseY) {
        if(mouseX > x + (index * 29) && mouseX < x + (index * 29) + 29) {
            if(mouseY > y - 32 && mouseY < y) {
                return true;
            }
        }

        return false;
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if(color_slider.isMouseOver(mouseX, mouseY)) {
            color_slider.mouseClicked(mouseX, mouseY, button);
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
        color_slider.mouseDragged(mouseX, mouseY, button, deltaX, deltaY);
        return super.mouseDragged(mouseX, mouseY, button, deltaX, deltaY);
    }


    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        color_slider.mouseReleased(mouseX, mouseY, button);
        return super.mouseReleased(mouseX, mouseY, button);
    }

    @Override
    public void mouseMoved(double mouseX, double mouseY) {
        color_slider.mouseMoved(mouseX, mouseY);
        super.mouseMoved(mouseX, mouseY);
    }
}