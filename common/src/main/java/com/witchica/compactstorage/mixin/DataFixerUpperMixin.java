package com.witchica.compactstorage.mixin;

import com.mojang.datafixers.DataFixerBuilder;
import com.mojang.datafixers.schemas.Schema;
import com.mojang.datafixers.types.templates.TypeTemplate;
import net.minecraft.util.datafix.DataFixers;
import net.minecraft.util.datafix.fixes.BlockEntityRenameFix;
import net.minecraft.util.datafix.fixes.BlockRenameFix;
import net.minecraft.util.datafix.fixes.ItemRenameFix;
import net.minecraft.util.datafix.schemas.NamespacedSchema;
import net.minecraft.util.datafix.schemas.V1466;
import net.minecraft.util.datafix.schemas.V4656;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import javax.security.auth.callback.Callback;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

@Mixin(DataFixers.class)
public class DataFixerUpperMixin {
    private static final Map<String, String> DFU_BLOCKS = new HashMap<>();
    private static final Map<String, String> DFU_ITEMS = new HashMap<>();

    static {
        for(DyeColor dye : DyeColor.values()) {
            DFU_BLOCKS.put("compact_storage:compact_chest_" + dye.getName(), "compact_storage:" + dye.getName() + "_compact_chest");
            DFU_BLOCKS.put("compact_storage:compact_barrel_" + dye.getName(), "compact_storage:" + dye.getName() + "_compact_barrel");

            DFU_ITEMS.put("compact_storage:backpack_" + dye.getName(), "compact_storage:" + dye.getName() + "_backpack");
        }
    }

    @Inject(method="addFixers", at = @At("RETURN"))
    private static void addFixers(DataFixerBuilder dataFixerBuilder, CallbackInfo ci) {
        Schema schema = dataFixerBuilder.addSchema(4671, NamespacedSchema::new);
        dataFixerBuilder.addFixer(BlockRenameFix.create(schema, "Rename old CompactStorage blocks", s -> DFU_BLOCKS.getOrDefault(NamespacedSchema.ensureNamespaced(s), s)));
        dataFixerBuilder.addFixer(ItemRenameFix.create(schema, "Rename of CompactStorage items", s -> DFU_ITEMS.getOrDefault(NamespacedSchema.ensureNamespaced(s), s)));
    }
}
