package site.siredvin.progressiveperipherals.integrations.computercraft.peripherals;

import dan200.computercraft.api.lua.IArguments;
import dan200.computercraft.api.lua.LuaException;
import dan200.computercraft.api.lua.LuaFunction;
import dan200.computercraft.api.lua.MethodResult;
import de.srendi.advancedperipherals.common.addons.computercraft.base.BasePeripheral;
import de.srendi.advancedperipherals.common.util.Pair;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.EnumProperty;
import net.minecraft.state.IntegerProperty;
import net.minecraft.state.Property;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import site.siredvin.progressiveperipherals.common.blocks.FlexibleRealityAnchor;
import site.siredvin.progressiveperipherals.common.configuration.ProgressivePeripheralsConfig;
import site.siredvin.progressiveperipherals.common.setup.Blocks;
import site.siredvin.progressiveperipherals.common.tileentities.FlexibleRealityAnchorTileEntity;
import site.siredvin.progressiveperipherals.common.tileentities.RealityForgerTileEntity;
import site.siredvin.progressiveperipherals.utils.CheckUtils;
import site.siredvin.progressiveperipherals.utils.LuaUtils;
import site.siredvin.progressiveperipherals.utils.RepresentationUtils;
import site.siredvin.progressiveperipherals.utils.ScanUtils;

import java.util.*;

public class RealityForgerPeripheral extends BasePeripheral {
    public static final String TYPE = "realityForger";

    private static final Map<String, BooleanProperty> FLAG_MAPPING = new HashMap<String, BooleanProperty>() {{
        put("playerPassable", FlexibleRealityAnchor.PLAYER_PASSABLE);
        put("lightPassable", FlexibleRealityAnchor.LIGHT_PASSABLE);
        put("skyLightPassable", FlexibleRealityAnchor.SKY_LIGHT_PASSABLE);
        put("invisible", FlexibleRealityAnchor.INVISIBLE);
    }};

    public RealityForgerPeripheral(RealityForgerTileEntity tileEntity) {
        super(TYPE, tileEntity);
    }

    @SuppressWarnings("SameParameterValue")
    protected RealityForgerPeripheral(String type, RealityForgerTileEntity tileEntity) {
        super(type, tileEntity);
    }

    @Override
    public boolean isEnabled() {
        return ProgressivePeripheralsConfig.enableRealityForger;
    }

    public int _getInteractionRadius() {
        return ProgressivePeripheralsConfig.realityForgerRadius;
    }

    // Please, don't blame me for this untyped garbage code
    // If you can handle it better, you're welcome
    @SuppressWarnings({"unchecked", "rawtypes"})
    private BlockState applyBlockAttrs(BlockState state, Map<?, ?> blockAttrs) throws LuaException {
        for (Map.Entry<?, ?> entry: blockAttrs.entrySet()) {
            Optional<Property<?>> optProperty = state.getValues().keySet().stream().filter(property -> property.getName().equals(entry.getKey())).findAny();
            if (!optProperty.isPresent())
                throw new LuaException(String.format("Unknown property name %s", entry.getKey()));
            Property property = optProperty.get();
            if (property instanceof EnumProperty) {
                EnumProperty enumProperty = (EnumProperty<?>) property;
                String value = entry.getValue().toString().toLowerCase();
                // Well, how even here could happen NPE?
                Optional targetedEnum = enumProperty.getPossibleValues().stream().filter(candidate -> candidate.toString().toLowerCase().equals(value)).findAny();
                if (!targetedEnum.isPresent())
                    throw new LuaException(String.format("Incorrect value %s, only %s is allowed", entry.getKey(), RepresentationUtils.mergeValues(enumProperty.getPossibleValues())));
                state = state.setValue(enumProperty, (Enum)targetedEnum.get());
            } else if (property instanceof BooleanProperty) {
                if (!(entry.getValue() instanceof Boolean))
                    throw new LuaException(String.format("Incorrect value %s, should be boolean", entry.getKey()));
                state = state.setValue(property, (Boolean) entry.getValue());
            } else if (property instanceof IntegerProperty) {
                if (!(entry.getValue() instanceof Number))
                    throw new LuaException(String.format("Incorrect value %s, should be boolean", entry.getKey()));
                state = state.setValue(property, ((Number) entry.getValue()).intValue());
            }
        }
        return state;
    }

    private Pair<Boolean, BlockState> findBlock(Map<?, ?> table) throws LuaException {
        boolean applyBlock = false;
        BlockState targetState = null;
        if (table.containsKey("block")) {
            String blockID = table.get("block").toString();
            if (ProgressivePeripheralsConfig.realityForgerBlacklist.contains(blockID))
                throw new LuaException("You cannot use this block, is blacklisted");
            Optional<Block> blockOptional = Registry.BLOCK.getOptional(new ResourceLocation(blockID));
            if (!blockOptional.isPresent())
                throw new LuaException(String.format("Cannot find block %s", table.get("block")));
            Block block = blockOptional.get();
            targetState = block.defaultBlockState();
            if (table.containsKey("attrs")) {
                Object blockAttrs = table.get("attrs");
                if (!(blockAttrs instanceof Map))
                    throw new LuaException("attrs should be a table");
                targetState = applyBlockAttrs(targetState, (Map<?, ?>) blockAttrs);
            }
            applyBlock = true;
        }
        return Pair.of(applyBlock, targetState);
    }

    private void forgeRealityTileEntity(FlexibleRealityAnchorTileEntity realityMirror, @Nullable BlockState targetState, Map<?, ?> flags, boolean applyState) {
        if (applyState)
            realityMirror.setMimic(targetState, true);
        flags.forEach((key, value) -> {
            if (value instanceof Boolean) {
                BooleanProperty targetProperty = FLAG_MAPPING.get(key.toString());
                if (targetProperty != null) {
                    realityMirror.setBooleanStateValue(targetProperty, (Boolean) value);
                }
            } else if (key.equals("lightLevel") && value instanceof Number) {
                realityMirror.setLightLevel(((Number) value).intValue());
            }
        });
        realityMirror.pushInternalDataChangeToClient();
    }

    @LuaFunction
    public final int getInteractionRadius() {
        return _getInteractionRadius();
    }

    @LuaFunction
    public final List<Map<String, Integer>> detectAnchors() {
        List<Map<String, Integer>> data = new ArrayList<>();
        ScanUtils.relativeTraverseBlocks(getWorld(), getPos(), ProgressivePeripheralsConfig.realityForgerRadius, (blockState, pos) -> {
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
    public final MethodResult forgeRealityPiece(@NotNull IArguments arguments) throws LuaException {
        BlockPos center = getPos();
        BlockPos targetPosition = LuaUtils.convertToBlockPos(center, arguments.getTable(0));
        if (!CheckUtils.radiusCorrect(center, targetPosition, _getInteractionRadius()))
            return MethodResult.of(null, "Block are too far away");
        Map<?, ?> table = arguments.getTable(1);
        World world = getWorld();
        TileEntity entity = world.getBlockEntity(targetPosition);
        if (!(entity instanceof FlexibleRealityAnchorTileEntity))
            return MethodResult.of(false, "Incorrect coordinates");
        Pair<Boolean, BlockState> blockFindResult = findBlock(table);
        forgeRealityTileEntity((FlexibleRealityAnchorTileEntity) entity, blockFindResult.getRight(), table, blockFindResult.getLeft());
        return MethodResult.of(true);
    }


    @LuaFunction(mainThread = true)
    public final MethodResult forgeRealityPieces(@NotNull IArguments arguments) throws LuaException {
        BlockPos center = getPos();
        World world = getWorld();
        List<BlockPos> poses = new ArrayList<>();
        for (Object value: arguments.getTable(0).values()) {
            if (!(value instanceof Map))
                throw new LuaException("First argument should be list of block positions");
            poses.add(LuaUtils.convertToBlockPos(center, (Map<?, ?>) value));
        }
        List<FlexibleRealityAnchorTileEntity> entities = new ArrayList<>();
        for (BlockPos pos: poses) {
            if (!CheckUtils.radiusCorrect(center, pos, _getInteractionRadius()))
                return MethodResult.of(null, "One of blocks are too far away");
            TileEntity entity = world.getBlockEntity(pos);
            if (!(entity instanceof FlexibleRealityAnchorTileEntity))
                return MethodResult.of(false, String.format("Incorrect coordinates (%d, %d, %d)", pos.getX(), pos.getY(), pos.getZ()));
            entities.add((FlexibleRealityAnchorTileEntity) entity);
        }
        Map<?, ?> table = arguments.getTable(1);
        Pair<Boolean, BlockState> blockFindResult = findBlock(table);
        entities.forEach(entity -> forgeRealityTileEntity(entity, blockFindResult.getRight(), table, blockFindResult.getLeft()));
        return MethodResult.of(true);
    }

    @LuaFunction(mainThread = true)
    public final MethodResult forgeReality(@NotNull IArguments arguments) throws LuaException {
        Map<?, ?> table = arguments.getTable(0);
        Pair<Boolean, BlockState> blockFindResult = findBlock(table);
        World world = getWorld();
        ScanUtils.traverseBlocks(world, getPos(), _getInteractionRadius(), (blockState, newPos) -> {
            TileEntity blockEntity = world.getBlockEntity(newPos);
            if (blockEntity instanceof FlexibleRealityAnchorTileEntity) {
                forgeRealityTileEntity((FlexibleRealityAnchorTileEntity) blockEntity, blockFindResult.getRight(), table, blockFindResult.getLeft());
            }
        });
        return MethodResult.of(true);
    }
}
