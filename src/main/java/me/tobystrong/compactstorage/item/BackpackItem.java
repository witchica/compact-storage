package me.tobystrong.compactstorage.item;

import me.tobystrong.compactstorage.CompactStorage;
import me.tobystrong.compactstorage.container.BackpackContainer;
import me.tobystrong.compactstorage.util.CompactStorageUtilMethods;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.fml.network.NetworkHooks;
import net.minecraftforge.items.ItemStackHandler;

import java.util.List;
import java.util.UUID;

public class BackpackItem extends Item {
    public BackpackItem() {
        super(new Properties().group(CompactStorage.compactStorageItemGroup).maxStackSize(1));
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn) {
        //make sure we're server size
        if(!worldIn.isRemote) {
            //if it doesn't have a tag - make one to stop crashes
            if(!playerIn.getHeldItem(handIn).hasTag()) {
                CompoundNBT tag = new CompoundNBT();
                tag.putInt("width", 9);
                tag.putInt("height", 3);

                playerIn.getHeldItem(handIn).setTag(tag);
            }

            //if the backpack is in the main hand, we can check for upgrades in the off hand
            if(handIn == Hand.MAIN_HAND) {
                ItemStack mainHand = playerIn.getHeldItemMainhand();
                ItemStack offHand = playerIn.getHeldItem(Hand.OFF_HAND);

                //get a central position for particles and sounds
                BlockPos pos = new BlockPos(playerIn.getPosX(), playerIn.getPosY(), playerIn.getPosZ());
                pos = pos.add(0f, 1f, 0f);

                ServerWorld serverWorld = (ServerWorld) worldIn;

                //if the upgrade is a row upgrade
                if(offHand.getItem() == CompactStorage.upgrade_row) {
                    //get the wide
                    int width = mainHand.getTag().getInt("width");

                    //check if is within boundaries
                    if(width < 24) {
                        //change the size
                        mainHand.getTag().putInt("width", width + 1);

                        //play particles and sound
                        serverWorld.spawnParticle(ParticleTypes.HAPPY_VILLAGER, pos.getX(), pos.getY(), pos.getZ(), 5,0.25,0, 0.25, 0);
                        worldIn.playSound(null, pos, SoundEvents.ENTITY_PLAYER_LEVELUP, SoundCategory.PLAYERS, 1f, 1f);

                        //decrement the upgrade size and return the stack
                        offHand.setCount(offHand.getCount() - 1);
                        return ActionResult.resultPass(mainHand);
                    } else {
                        //if it doesn't conform, play angry sounds and send message
                        serverWorld.spawnParticle(ParticleTypes.ANGRY_VILLAGER, pos.getX(), pos.getY(), pos.getZ(), 5,0.25,0, 0.25, 0);
                        worldIn.playSound(null, pos, SoundEvents.UI_TOAST_OUT, SoundCategory.PLAYERS, 1f, 1f);

                        playerIn.sendMessage(new TranslationTextComponent("message.compactstorage.upgrade.max_width_backpack"), UUID.randomUUID());
                        return ActionResult.resultFail(mainHand);
                    }
                } else if(offHand.getItem() == CompactStorage.upgrade_column) {
                    //get the height
                    int height = mainHand.getTag().getInt("height");

                    //check if it conforms
                    if(height < 12) {
                        //if it does, change the size and play sounds / particles
                        mainHand.getTag().putInt("height", height + 1);

                        serverWorld.spawnParticle(ParticleTypes.HAPPY_VILLAGER, pos.getX(), pos.getY(), pos.getZ(), 5,0.25,0, 0.25, 0);
                        worldIn.playSound(null, pos, SoundEvents.ENTITY_PLAYER_LEVELUP, SoundCategory.PLAYERS, 1f, 1f);

                        //decrement the upgrade and return

                        if(!playerIn.isCreative()) {
                            offHand.setCount(offHand.getCount() - 1);
                        }

                        return ActionResult.resultPass(mainHand);
                    } else {
                        //if not play the angry particles and send a message
                        serverWorld.spawnParticle(ParticleTypes.ANGRY_VILLAGER, pos.getX(), pos.getY(), pos.getZ(), 5,0.25,0, 0.25, 0);
                        worldIn.playSound(null, pos, SoundEvents.UI_TOAST_OUT, SoundCategory.PLAYERS, 1f, 1f);

                        playerIn.sendMessage(new TranslationTextComponent("message.compactstorage.upgrade.max_height_backpack"), UUID.randomUUID());
                        return ActionResult.resultFail(mainHand);
                    }
                }
            }

            //if not ugprade then just open the GUI
            ServerPlayerEntity serverPlayerEntity = (ServerPlayerEntity) playerIn;
            NetworkHooks.openGui(serverPlayerEntity, new BackpackItemContainerProvider(handIn, playerIn.getHeldItem(handIn)), (buf) -> {
                buf.writeInt(handIn == Hand.MAIN_HAND ? 0 : 1);
            });
        }

        return ActionResult.resultPass(playerIn.getHeldItem(handIn));
    }

    @Override
    public void addInformation(ItemStack stack, World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        int width = 9;
        int height = 3;

        if(stack.hasTag() && stack.getTag().contains("width")) {
            width = stack.getTag().getInt("width");
            height = stack.getTag().getInt("height");
        }

        StringTextComponent widthComponent = new StringTextComponent("Width: ");
        widthComponent.append(new StringTextComponent(width + "").mergeStyle(TextFormatting.LIGHT_PURPLE));

        StringTextComponent heightComponent = new StringTextComponent("Height: ");
        heightComponent.append(new StringTextComponent(height + "").mergeStyle(TextFormatting.LIGHT_PURPLE));

        tooltip.add(widthComponent);
        tooltip.add(heightComponent);

        super.addInformation(stack, worldIn, tooltip, flagIn);
    }

    /*
            Class to handle the opening of Backpack Container
         */
    public class BackpackItemContainerProvider implements INamedContainerProvider {
        private int inventoryWidth;
        private int inventoryHeight;
        private ItemStackHandler inventory;
        private Hand hand;

        //constructor takes the hand and backpack stack
        public BackpackItemContainerProvider(Hand hand, ItemStack backpack) {
            int inventoryWidth = 9;
            int inventoryHeight = 3;

            //if no tag make one now
            if(backpack.getTag() == null) {
                backpack.setTag(new CompoundNBT());
            }

            CompoundNBT tag = backpack.getTag();

            //if no width add it now
            if(!tag.contains("width")) {
                tag.putInt("width", inventoryWidth);
                tag.putInt("height", inventoryHeight);
            } else {
                inventoryWidth = tag.getInt("width");
                inventoryHeight = tag.getInt("height");
            }

            //create our handler using the size
            ItemStackHandler inventoryHandler = new ItemStackHandler(inventoryWidth * inventoryHeight);

            if(tag.contains("Inventory")) {
                //read from nbt!
                inventoryHandler.deserializeNBT(tag.getCompound("Inventory"));
                //check the size just in case
                inventoryHandler = CompactStorageUtilMethods.validateHandlerSize(inventoryHandler, inventoryWidth, inventoryHeight);
            }

            //save all this data so our container can use it
            this.inventoryWidth = inventoryWidth;
            this.inventoryHeight = inventoryHeight;
            this.inventory = inventoryHandler;
            this.hand = hand;
        }

        @Override
        public ITextComponent getDisplayName() {
            return new TranslationTextComponent("container.compactstorage.backpack");
        }

        @Override
        public Container createMenu(int windowID, PlayerInventory playerInventory, PlayerEntity playerEntity) {
            return new BackpackContainer(windowID, playerInventory, inventoryWidth, inventoryHeight, inventory, hand);
        }
    }
}
