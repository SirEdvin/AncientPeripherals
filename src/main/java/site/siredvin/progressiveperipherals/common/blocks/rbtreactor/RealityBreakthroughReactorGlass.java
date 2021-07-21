package site.siredvin.progressiveperipherals.common.blocks.rbtreactor;

import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class RealityBreakthroughReactorGlass extends RealityBreakthroughReactorBlock {

    public RealityBreakthroughReactorGlass(Properties properties) {
        super(properties);
    }

    @Override
    public boolean useShapeForLightOcclusion(BlockState state) {
        return true;
    }

    public VoxelShape getVisualShape(BlockState state, IBlockReader world, BlockPos pos, ISelectionContext context) {
        return VoxelShapes.empty();
    }

    @OnlyIn(Dist.CLIENT)
    public float getShadeBrightness(BlockState state, IBlockReader world, BlockPos pos) {
        return 1.0F;
    }

    public boolean propagatesSkylightDown(BlockState state, IBlockReader world, BlockPos pos) {
        return true;
    }
}
