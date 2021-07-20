package site.siredvin.progressiveperipherals.utils;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraftforge.common.ToolType;
import org.jetbrains.annotations.Nullable;

public class BlockUtils {

    public static AbstractBlock.Properties createProperties(
            Material material, float destroyTime, float explosionResistance, @Nullable SoundType soundType, int harvestLevel, ToolType harvestTool, boolean isOcclusion, boolean hasDrop) {
        AbstractBlock.Properties properties = AbstractBlock.Properties.of(material)
                .strength(destroyTime, explosionResistance).harvestLevel(harvestLevel).harvestTool(harvestTool);
        if (soundType != null)
            properties = properties.sound(soundType);
        if (!isOcclusion)
            properties = properties.noOcclusion();
        if (!hasDrop) {
            properties = properties.noDrops();
        } else {
            properties = properties.requiresCorrectToolForDrops();
        }
        return properties;
    }

    public static AbstractBlock.Properties defaultProperties() {
        return createProperties(
                Material.METAL, 1, 5, SoundType.METAL,
                0, ToolType.PICKAXE, false, true
        );
    }

    public static AbstractBlock.Properties decoration() {
        return createProperties(
                Material.DECORATION, 1, 5, SoundType.WOOD,
                0, ToolType.PICKAXE, false, true
        );
    }

    public static AbstractBlock.Properties unbreakable() {
        return createProperties(
                Material.DECORATION, -1.0F, 3600000.0F,
                null, 0, ToolType.PICKAXE, false, false
        );
    }
}
