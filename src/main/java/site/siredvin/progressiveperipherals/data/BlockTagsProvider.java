package site.siredvin.progressiveperipherals.data;

import net.minecraft.block.Block;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.TagsProvider;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import site.siredvin.progressiveperipherals.ProgressivePeripherals;
import site.siredvin.progressiveperipherals.common.setup.Blocks;
import site.siredvin.progressiveperipherals.common.tags.BlockTags;

import java.nio.file.Path;

public class BlockTagsProvider extends TagsProvider<Block> {

    public BlockTagsProvider(DataGenerator generator, @Nullable ExistingFileHelper existingFileHelper) {
        super(generator, Registry.BLOCK, ProgressivePeripherals.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags() {
        this.tag(BlockTags.BREAKTHROUGH_REACTOR_CASING).add(
                Blocks.REALITY_BREAKTHROUGH_REACTOR_CASING.get(),
                Blocks.REALITY_BREAKTHROUGH_REACTOR_GLASS.get(),
                Blocks.REALITY_BREAKTHROUGH_REACTOR_IO_PORT.get()
        );
    }

    @Override
    protected @NotNull Path getPath(ResourceLocation p_200431_1_) {
        return this.generator.getOutputFolder().resolve("data/" + p_200431_1_.getNamespace() + "/tags/blocks/" + p_200431_1_.getPath() + ".json");
    }

    @Override
    public @NotNull String getName() {
        return "Block tags";
    }
}