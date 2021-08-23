package site.siredvin.progressiveperipherals.common.features;

import com.mojang.serialization.Codec;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Direction;
import net.minecraft.util.Mirror;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraft.world.gen.feature.template.PlacementSettings;
import net.minecraft.world.gen.feature.template.Template;
import org.jetbrains.annotations.NotNull;
import site.siredvin.progressiveperipherals.ProgressivePeripherals;
import site.siredvin.progressiveperipherals.common.configuration.ProgressivePeripheralsConfig;

import java.util.Random;
import java.util.stream.Stream;

public class RealityBreakthroughPointFeature extends Feature<NoFeatureConfig> {
    private static final ResourceLocation templateRL = new ResourceLocation(ProgressivePeripherals.MOD_ID, "breakthrough_point");
    private static final int startYSearchLevel = 56;

    @SuppressWarnings("ConstantConditions")
    protected final PlacementSettings placementsettings = (new PlacementSettings()).setMirror(Mirror.NONE).setRotation(Rotation.NONE).setIgnoreEntities(false).setChunkPos(null);

    public RealityBreakthroughPointFeature(Codec<NoFeatureConfig> config) {
        super(config);
    }

    @Override
    public boolean place(ISeedReader world, @NotNull ChunkGenerator chunkGenerator, @NotNull Random random, @NotNull BlockPos pos, @NotNull NoFeatureConfig config) {
        BlockPos.Mutable mutable = new BlockPos.Mutable().set(pos);
        mutable.setY(startYSearchLevel);
        while (!world.isEmptyBlock(mutable)) {
            mutable.move(Direction.UP);
        }
        mutable.move(Direction.DOWN);

        boolean terrainCheck = Stream.of(
                world.getBlockState(mutable).getBlock(),
                world.getBlockState(mutable.north()).getBlock(),
                world.getBlockState(mutable.south()).getBlock(),
                world.getBlockState(mutable.west()).getBlock(),
                world.getBlockState(mutable.north()).getBlock()
        ).allMatch(block -> isDirt(block) || BlockTags.SAND.contains(block) || BlockTags.BASE_STONE_OVERWORLD.contains(block));

        if (!terrainCheck)
            return false;
        Template template = world.getLevel().getStructureManager().get(templateRL);

        if (template == null) {
            ProgressivePeripherals.LOGGER.warn(templateRL + " NTB does not exist!");
            return false;
        }
        if (random.nextInt(ProgressivePeripheralsConfig.BREAKTHROUGH_SPAWN_CHANCE_LIMIT) > ProgressivePeripheralsConfig.breakthroughPointSpawnChance)
            return false;
        BlockPos offset = new BlockPos(-template.getSize().getX() / 2, 1, -template.getSize().getZ() / 2);
        template.placeInWorldChunk(world, mutable.offset(offset), placementsettings, random);
        ProgressivePeripherals.LOGGER.info("Structure places {}", mutable.offset(offset));
        return true;
    }
}
