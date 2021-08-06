package site.siredvin.progressiveperipherals.common.tileentities.enderwire;

import dan200.computercraft.api.lua.LuaException;
import dan200.computercraft.api.lua.MethodResult;
import de.srendi.advancedperipherals.common.addons.computercraft.base.BasePeripheral;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import site.siredvin.progressiveperipherals.client.renderer.EnderwireLightEmitterTileRenderer;
import site.siredvin.progressiveperipherals.common.blocks.enderwire.EnderwireLightEmitterBlock;
import site.siredvin.progressiveperipherals.common.setup.TileEntityTypes;
import site.siredvin.progressiveperipherals.extra.network.api.EnderwireElementType;
import site.siredvin.progressiveperipherals.utils.LuaUtils;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class EnderwireLightEmitterTileEntity extends BaseEnderwireTileEntity<EnderwireLightEmitterTileEntity, BasePeripheral> {

    private static final String COLOR_TAG = "color";

    private Color color = new Color(255, 255, 255, 125);

    public EnderwireLightEmitterTileEntity() {
        super(TileEntityTypes.ENDERWIRE_LIGHT_EMITTER.get());
    }

    public Color getColor() {
        return color;
    }

    public boolean isEnabled() {
        return getBlockState().getValue(EnderwireLightEmitterBlock.ENABLED);
    }

    @Override
    public EnderwireLightEmitterTileEntity getThis() {
        return this;
    }

    @Override
    public EnderwireElementType getElementType() {
        return EnderwireElementType.LIGHT_EMITTER;
    }

    @Override
    public Map<String, Object> getCurrentState() {
        return new HashMap<>();
    }

    @Override
    public boolean isRequiredRenderUpdate() {
        return true;
    }

    @Override
    public void loadInternalData(BlockState state, CompoundNBT data) {
        super.loadInternalData(state, data);
        color = new Color(data.getInt(COLOR_TAG));
    }

    @Override
    public CompoundNBT saveInternalData(CompoundNBT data) {
        data = super.saveInternalData(data);
        data.putInt(COLOR_TAG, color.getRGB());
        return data;
    }

    @Override
    public MethodResult configure(Map<?, ?> data) throws LuaException {
        Object enabled = data.get("enabled");
        Object colorRaw = data.get("color");
        BlockState pushState = getBlockState();
        boolean isConfigured = false;
        if (colorRaw != null) {
            color = LuaUtils.convertToColor(colorRaw,  EnderwireLightEmitterTileRenderer.REQUIRED_COLOR_ALPHA);
            isConfigured = true;
        }
        if (enabled != null) {
            if (!(enabled instanceof Boolean))
                throw new LuaException("enabled should be boolean");
            pushState = pushState.setValue(EnderwireLightEmitterBlock.ENABLED, (Boolean) enabled);
            isConfigured = true;
        }
        if (isConfigured)
            pushInternalDataChangeToClient(pushState);
        return MethodResult.of(true);
    }
}
