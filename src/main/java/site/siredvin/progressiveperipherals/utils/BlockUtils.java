package site.siredvin.progressiveperipherals.utils;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraftforge.common.ToolType;

public class BlockUtils {
    public static AbstractBlock.Properties defaultProperties() {
        return AbstractBlock.Properties.of(Material.METAL).strength(1, 5)
                .harvestLevel(0).sound(SoundType.METAL).noOcclusion().harvestTool(ToolType.PICKAXE).requiresCorrectToolForDrops();
    }

    public static AbstractBlock.Properties decoration() {
        return AbstractBlock.Properties.of(Material.DECORATION).strength(1, 5)
                .harvestLevel(0).sound(SoundType.WOOD).noOcclusion().harvestTool(ToolType.PICKAXE).requiresCorrectToolForDrops();
    }

    public static AbstractBlock.Properties unbreakable() {
        return AbstractBlock.Properties.of(Material.STONE).strength(-1.0F, 3600000.0F)
                .sound(SoundType.METAL).noOcclusion().noDrops();
    }
}
