package site.siredvin.ancientperipherals.computercraft.peripherals;

import dan200.computercraft.api.lua.IArguments;
import dan200.computercraft.api.lua.LuaException;
import dan200.computercraft.api.lua.LuaFunction;
import dan200.computercraft.api.lua.MethodResult;
import de.srendi.advancedperipherals.common.addons.computercraft.base.BasePeripheral;
import de.srendi.advancedperipherals.common.util.Pair;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.state.BooleanProperty;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;
import site.siredvin.ancientperipherals.common.blocks.FlexibleRealityAnchor;
import site.siredvin.ancientperipherals.common.setup.Blocks;
import site.siredvin.ancientperipherals.common.tileentities.RealityForgerTileEntity;
import site.siredvin.ancientperipherals.common.tileentities.FlexibleRealityAnchorTileEntity;
import site.siredvin.ancientperipherals.utils.LuaUtils;
import site.siredvin.ancientperipherals.utils.ScanUtils;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;

public class RealityForgerPeripheral extends BasePeripheral {
    private static final int DEFAULT_RADIUS = 8;
    private static final Map<String, BooleanProperty> FLAG_MAPPING = new HashMap<String, BooleanProperty>() {{
        put("playerPassable", FlexibleRealityAnchor.PLAYER_PASSABLE);
        put("lightPassable", FlexibleRealityAnchor.LIGHT_PASSABLE);
        put("skyLightPassable", FlexibleRealityAnchor.SKY_LIGHT_PASSABLE);
        put("invisible", FlexibleRealityAnchor.INVISIBLE);
    }};

    public RealityForgerPeripheral(String type, RealityForgerTileEntity tileEntity) {
        super(type, tileEntity);
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    private Pair<MethodResult, Pair<Boolean, BlockState>> findBlock(Map<?, ?> table) {
        boolean applyBlock = false;
        BlockState targetState = null;
        if (table.containsKey("block")) {
            Optional<Block> blockOptional = Registry.BLOCK.getOptional(new ResourceLocation(table.get("block").toString()));
            if (!blockOptional.isPresent())
                return Pair.onlyLeft(MethodResult.of(null, "Cannot find block"));
            Block block = blockOptional.get();
            targetState = block.defaultBlockState();
            applyBlock = true;
        }
        return Pair.onlyRight(Pair.of(applyBlock, targetState));
    }

    private void forgeRealityTileEntity(FlexibleRealityAnchorTileEntity realityMirror, @Nullable BlockState targetState, Map<?, ?> flags, boolean applyState) {
        if (applyState)
            realityMirror.setMimic(targetState);
        flags.forEach((key, value) -> {
            BooleanProperty targetProperty = FLAG_MAPPING.get(key.toString());
            if (value instanceof Boolean) {
                if (targetProperty != null) {
                    realityMirror.setBooleanStateValue(targetProperty, (Boolean) value);
                }
            }
        });
        realityMirror.pushState();
    }

    @LuaFunction
    public final List<Map<String, Integer>> detectAnchors() {
        List<Map<String, Integer>> data = new ArrayList<>();
        ScanUtils.relativeTraverseBlocks(getWorld(), getPos(), DEFAULT_RADIUS, (blockState, pos) -> {
            if (blockState.is(Blocks.FLEXIBLE_REALITY_ANCHOR.get())) {
                data.add(new HashMap<String, Integer>(){{
                    put("x", pos.getX());
                    put("y", pos.getY());
                    put("z", pos.getZ());
                }});
            }
        });
        return data;
    }

    @LuaFunction(mainThread = true)
    public final MethodResult forgeRealityPiece(@Nonnull IArguments arguments) throws LuaException {
        BlockPos targetPosition = LuaUtils.convertToBlockPos(getPos(), arguments.getTable(0));
        Map<?, ?> table = arguments.getTable(1);
        World world = getWorld();
        TileEntity entity = world.getBlockEntity(targetPosition);
        if (!(entity instanceof FlexibleRealityAnchorTileEntity))
            return MethodResult.of(null, "Incorrect coordinates");
        Pair<MethodResult, Pair<Boolean, BlockState>> blockFindResult = findBlock(table);
        if (blockFindResult.leftPresent())
            return blockFindResult.getLeft();
        forgeRealityTileEntity((FlexibleRealityAnchorTileEntity) entity, blockFindResult.getRight().getRight(), table, blockFindResult.getRight().getLeft());
        return MethodResult.of(true);
    }

    @LuaFunction(mainThread = true)
    public final MethodResult forgeReality(@Nonnull IArguments arguments) throws LuaException {
        Map<?, ?> table = arguments.getTable(0);
        Pair<MethodResult, Pair<Boolean, BlockState>> blockFindResult = findBlock(table);
        if (blockFindResult.leftPresent())
            return blockFindResult.getLeft();
        BlockPos pos = getPos();
        World world = getWorld();
        ScanUtils.traverseBlocks(getWorld(), getPos(), DEFAULT_RADIUS, (blockState, newPos) -> {
            TileEntity blockEntity = world.getBlockEntity(newPos);
            if (blockEntity instanceof FlexibleRealityAnchorTileEntity) {
                forgeRealityTileEntity((FlexibleRealityAnchorTileEntity) blockEntity, blockFindResult.getRight().getRight(), table, blockFindResult.getRight().getLeft());
            }
        });
        return MethodResult.of(true);
    }
}
