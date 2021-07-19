package site.siredvin.progressiveperipherals.data;

import net.minecraft.block.Block;
import net.minecraftforge.fml.RegistryObject;
import site.siredvin.progressiveperipherals.common.setup.Blocks;
import site.siredvin.progressiveperipherals.common.setup.Registration;

public class BlockLootTables extends net.minecraft.data.loot.BlockLootTables {

    @Override
    protected void addTables() {
        Registration.BLOCKS.getEntries().stream()
                .map(RegistryObject::get)
                .filter(block -> !block.is(Blocks.REALITY_BREAKTHROUGH_POINT.get()))
                .forEach(this::dropSelf);
    }

    @Override
    protected Iterable<Block> getKnownBlocks() {
        return Registration.BLOCKS.getEntries().stream().map(RegistryObject::get).filter(block -> !block.is(Blocks.REALITY_BREAKTHROUGH_POINT.get()))::iterator;
    }
}
