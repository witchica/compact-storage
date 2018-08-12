package com.tattyseal.compactstorage.event;

import com.tattyseal.compactstorage.client.gui.GuiChest;
import com.tattyseal.compactstorage.block.BlockBarrel;
import com.tattyseal.compactstorage.block.BlockChest;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.client.ForgeHooksClient;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.eventhandler.Event.Result;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;

import org.lwjgl.opengl.GL11;

public class CompactStorageEventHandler
{
	@SubscribeEvent
	public void onPlayerInteract(PlayerInteractEvent.RightClickBlock event)
	{
		Block block = event.getEntity().world.getBlockState(event.getPos()).getBlock();

		if(block instanceof BlockChest || block instanceof BlockBarrel)
		{
			event.setUseBlock(Result.ALLOW);
		}
	}
	
	/* -- Code for scaling following, could be moved to a seperate class -- */
	
	private int normalSettingsScale = 0;
	
	private void overrideSettingsScale(float scaling)
	{
		Minecraft mc = Minecraft.getMinecraft();
		
		normalSettingsScale = mc.gameSettings.guiScale;
		
		// We need to get ScaledResolution's scale factor here,
		// because mc.gameSettings.guiScale can be 0 (auto scale).
		mc.gameSettings.guiScale = (int)((new ScaledResolution(mc)).getScaleFactor() * scaling);
	}
	
	private void restoreSettingsScale()
	{
		Minecraft.getMinecraft().gameSettings.guiScale = normalSettingsScale;
	}

	
	private float getDrawScreenScaling(GuiScreenEvent.DrawScreenEvent event)
	{
		if(event.getGui() instanceof GuiChest)
			
			return ((GuiChest)event.getGui()).getScaling();
			
		return 1f;
	}
	
	private float getGuiOpenScaling(GuiOpenEvent event)
	{
		if(event.getGui() instanceof GuiChest)
			
			return ((GuiChest)event.getGui()).getScaling();
			
		return 1f;
	}
	
	
	private boolean renderingHooked = false;
	
	@SubscribeEvent(priority=EventPriority.HIGHEST)
	public void onPreDrawGui(GuiScreenEvent.DrawScreenEvent.Pre event)
	{
		// Perform this check in case this event has been re-posted.
		if(!renderingHooked)
		{
			float scaling = getDrawScreenScaling(event);
			
			if(scaling != 1f)
			{					
				GL11.glPushMatrix();
				GL11.glScalef(scaling, scaling, 1f);
				
				overrideSettingsScale(scaling);
				
				renderingHooked = true;
				
				GuiChest chest = (GuiChest)event.getGui();
				int mouseX = (int)(event.getMouseX() / scaling);
				int mouseY = (int)(event.getMouseY() / scaling);
				float ticks = event.getRenderPartialTicks();
				
				// DrawScreenEvent's mouse coordinates aren't supposed to change
				// and Forge calls drawScreen without checking them.
				// Work around by re-posting the event here and calling drawScreen on our own.
				
				// Alternatively, scale the mouse coordinates in GuiChest's drawScreen,
				// and use scaleEventMousePos to scale them for other event handlers.
				
				if (!MinecraftForge.EVENT_BUS.post(new GuiScreenEvent.DrawScreenEvent.Pre(chest, mouseX, mouseY, ticks)))
					chest.drawScreen(mouseX, mouseY, ticks);
				
				renderingHooked = false;
				
				// Cancel this event so that Forge doesn't call
				// drawScreen and other event handlers a second time.
				event.setCanceled(true);
			}
		}
	}
	
	@SubscribeEvent(priority=EventPriority.HIGHEST)
	public void onPostDrawGuiFirst(GuiScreenEvent.DrawScreenEvent.Post event)
	{
		float scaling = getDrawScreenScaling(event);
		
		if(scaling != 1f) scaleEventMousePos(event, scaling);
	}
	
	@SubscribeEvent(priority=EventPriority.LOWEST)
	public void onPostDrawGuiLast(GuiScreenEvent.DrawScreenEvent.Post event)
	{
		if(getDrawScreenScaling(event) != 1f)
		{	
			// Restore state, onPreDrawGui overrode these earlier.
			restoreSettingsScale();
			GL11.glPopMatrix();
		}
	}
	
	@SubscribeEvent(priority=EventPriority.HIGHEST)
	public void onGuiOpenFirst(GuiOpenEvent event)
	{
		float scaling = getGuiOpenScaling(event);
		
		if(scaling != 1f) overrideSettingsScale(scaling);
	}
	
	@SubscribeEvent(priority=EventPriority.LOWEST)
	public void onGuiOpenLast(GuiOpenEvent event)
	{
		if(getGuiOpenScaling(event) != 1f) restoreSettingsScale();
	}
	
	// Staticly initialized to minimize reflection's overhead when rendering.
	private static java.lang.reflect.Field fMouseX = null;
	private static java.lang.reflect.Field fMouseY = null;
	
	static
	{
		try
		{
			fMouseX = GuiScreenEvent.DrawScreenEvent.class.getDeclaredField("mouseX");
			fMouseY = GuiScreenEvent.DrawScreenEvent.class.getDeclaredField("mouseY");
			
			// Change private modifier to public
			fMouseX.setAccessible(true);
			fMouseY.setAccessible(true);
			
			// Remove final modifier
			java.lang.reflect.Field modifiersField = java.lang.reflect.Field.class.getDeclaredField("modifiers");
			modifiersField.setAccessible(true);
			modifiersField.setInt(fMouseX, fMouseX.getModifiers() & ~java.lang.reflect.Modifier.FINAL);
			modifiersField.setInt(fMouseY, fMouseY.getModifiers() & ~java.lang.reflect.Modifier.FINAL);
			
			System.out.println("Reflection on GuiScreenEvent.DrawScreenEvent mouseX and mouseY succeeded!");
		}
		catch(java.lang.Exception e)
		{
			System.out.println("Reflection on GuiScreenEvent.DrawScreenEvent mouseX and mouseY failed! Reason: " + e);
		}
	}
	
	private void scaleEventMousePos(GuiScreenEvent.DrawScreenEvent event, float scaling)
	{
		if(fMouseX != null && fMouseY != null)
		{
			try
			{
				fMouseX.set(event, (int)(event.getMouseX() / scaling));
				fMouseY.set(event, (int)(event.getMouseY() / scaling));		
			}
			catch(java.lang.Exception e)
			{
				System.out.println("Call to scaleEventMousePos failed! This should never happen! Reason: " + e);
			}
		}
	}

}
