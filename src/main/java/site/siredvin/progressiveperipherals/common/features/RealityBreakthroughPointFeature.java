package site.siredvin.progressiveperipherals.common.features;

import com.mojang.serialization.Codec;
import net.minecraft.block.Block;
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
import site.siredvin.progressiveperipherals.ProgressivePeripherals;
import site.siredvin.progressiveperipherals.common.configuration.ProgressivePeripheralsConfig;

import java.util.Random;

public class RealityBreakthroughPointFeature extends Feature<NoFeatureConfig> {
    private static final ResourceLocation templateRL = new ResourceLocation(ProgressivePeripherals.MOD_ID, "breakthrough_point");
    protected PlacementSettings placementsettings = (new PlacementSettings()).setMirror(Mirror.NONE).setRotation(Rotation.NONE).setIgnoreEntities(false).setChunkPos(null);
    private static final int startYSearchLevel = 56;

    public RealityBreakthroughPointFeature(Codec<NoFeatureConfig> config) {
        super(config);
    }

    @Override
    public boolean place(ISeedReader world, ChunkGenerator chunkGenerator, Random random, BlockPos pos, NoFeatureConfig config) {
        BlockPos.Mutable mutable = new BlockPos.Mutable().set(pos);
        mutable.setY(startYSearchLevel);
        while (!world.isEmptyBlock(mutable)) {
            mutable.move(Direction.UP);
        }
        mutable.move(Direction.DOWN);

        // check to make sure spot is valid and not a single block ledge
        Block block = world.getBlockState(mutable).getBlock();
        if (!world.isEmptyBlock(mutable.above()) || !world.isEmptyBlock(mutable.above(2)))
            return false;
        if (!isDirt(block) && !BlockTags.SAND.contains(block) && !BlockTags.BASE_STONE_OVERWORLD.contains(block))
            return false;
        Template template = world.getLevel().getStructureManager().get(templateRL);

        if (template == null) {
            ProgressivePeripherals.LOGGER.warn(templateRL + " NTB does not exist!");
            return false;
        }
        if (random.nextInt(ProgressivePeripheralsConfig.BREAKTHROUGH_SPAWN_LIMIT) <= ProgressivePeripheralsConfig.breakthroughPointSpawnChance)
            return false;
        // Creates the well centered on our spot
        BlockPos offset = new BlockPos(-template.getSize().getX() / 2, -1, -template.getSize().getZ() / 2);
        template.placeInWorldChunk(world, mutable.offset(offset), placementsettings, random);
        ProgressivePeripherals.LOGGER.info("Structure places {}", mutable.offset(offset));
        return true;
    }
}
