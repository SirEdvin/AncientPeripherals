package site.siredvin.progressiveperipherals.data;

import net.minecraft.data.DataGenerator;
import net.minecraft.data.TagsProvider;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import site.siredvin.progressiveperipherals.ProgressivePeripherals;
import site.siredvin.progressiveperipherals.common.setup.Blocks;
import site.siredvin.progressiveperipherals.common.tags.ItemTags;

import java.nio.file.Path;

public class ItemTagsProvider extends TagsProvider<Item> {

    public ItemTagsProvider(DataGenerator generator, @Nullable ExistingFileHelper existingFileHelper) {
        super(generator, Registry.ITEM, ProgressivePeripherals.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags() {
        this.tag(ItemTags.BREAKTHROUGH_REACTOR_CASING).add(
                Blocks.REALITY_BREAKTHROUGH_REACTOR_CASING.get().asItem(),
                Blocks.REALITY_BREAKTHROUGH_REACTOR_GLASS.get().asItem(),
                Blocks.REALITY_BREAKTHROUGH_REACTOR_IO_PORT.get().asItem(),
                Blocks.REALITY_BREAKTHROUGH_REACTOR_CONTROLLER.get().asItem()
        );
        this.tag(ItemTags.BREAKTHROUGH_REACTOR_CORNER).add(
                Blocks.REALITY_BREAKTHROUGH_REACTOR_CONTROLLER.get().asItem(),
                Blocks.REALITY_BREAKTHROUGH_REACTOR_CASING.get().asItem()
        );
    }

    @Override
    protected @NotNull Path getPath(ResourceLocation p_200431_1_) {
        return this.generator.getOutputFolder().resolve("data/" + p_200431_1_.getNamespace() + "/tags/items/" + p_200431_1_.getPath() + ".json");
    }

    @Override
    public @NotNull String getName() {
        return "Item tags";
    }
}