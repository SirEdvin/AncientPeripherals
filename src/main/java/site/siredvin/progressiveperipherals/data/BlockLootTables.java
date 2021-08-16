package site.siredvin.progressiveperipherals.data;

import net.minecraft.block.Block;
import net.minecraftforge.fml.RegistryObject;
import org.jetbrains.annotations.NotNull;
import site.siredvin.progressiveperipherals.common.setup.Blocks;
import site.siredvin.progressiveperipherals.common.setup.Registration;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Predicate;

public class BlockLootTables extends net.minecraft.data.loot.BlockLootTables {

    private static final Set<Block> BLOCK_BLACKLIST = new HashSet<Block>() {{
        add(Blocks.REALITY_BREAKTHROUGH_POINT.get()); // because it don't have any loot, actually
        add(Blocks.FLEXIBLE_STATUE.get()); // Processed inside
        add(Blocks.FLEXIBLE_REALITY_ANCHOR.get()); // Processed inside
    }};

    private static final Predicate<Block> NOT_FROM_BLACKLIST = (state) -> !BLOCK_BLACKLIST.contains(state.getBlock());

    @Override
    protected void addTables() {
        Registration.BLOCKS.getEntries().stream()
                .map(RegistryObject::get)
                .filter(NOT_FROM_BLACKLIST)
                .forEach(this::dropSelf);
    }

    @Override
    protected @NotNull Iterable<Block> getKnownBlocks() {
        return Registration.BLOCKS.getEntries().stream().map(RegistryObject::get).filter(NOT_FROM_BLACKLIST)::iterator;
    }
}
