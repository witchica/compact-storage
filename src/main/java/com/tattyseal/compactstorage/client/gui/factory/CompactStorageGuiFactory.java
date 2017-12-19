package com.tattyseal.compactstorage.client.gui.factory;

import com.tattyseal.compactstorage.client.gui.config.GuiCompactStorageConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.fml.client.IModGuiFactory;

import javax.annotation.Nullable;
import java.util.Set;

/**
 * Created by tobystrong on 03/05/2017.
 */
public class CompactStorageGuiFactory implements IModGuiFactory
{

    @Override
    public void initialize(Minecraft minecraftInstance) {

    }

    @Override
    public boolean hasConfigGui() {
        return true;
    }

    @Override
    public GuiScreen createConfigGui(GuiScreen parentScreen) {
        return new GuiCompactStorageConfig(parentScreen, "compactstorage", "CompactStorage");
    }

//    @Override
//    public Class<? extends GuiScreen> mainConfigGuiClass() {
//        return GuiCompactStorageConfig.class;
//    }

    @Override
    public Set<RuntimeOptionCategoryElement> runtimeGuiCategories() {
        return null;
    }


}
