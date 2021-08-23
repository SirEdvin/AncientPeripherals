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
import site.siredvin.progressiveperipherals.utils.ColorUtils;
import site.siredvin.progressiveperipherals.utils.LuaUtils;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class EnderwireLightEmitterTileEntity extends BaseEnderwireTileEntity<EnderwireLightEmitterTileEntity, BasePeripheral> {

    private static final int MAX_LIGHT_LEVEL = 15;
    private static final String COLOR_TAG = "color";
    private static final String LIGHT_LEVEL_TAG = "lightLevel";

    private Color color = new Color(255, 255, 255, 125);
    private int lightLevel = 0;

    public EnderwireLightEmitterTileEntity() {
        super(TileEntityTypes.ENDERWIRE_LIGHT_EMITTER.get());
    }

    public Color getColor() {
        return color;
    }

    public int getLightLevel() {
        return lightLevel;
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
        return new HashMap<String, Object>() {{
            put("lightLevel", lightLevel);
            put("color", LuaUtils.toLua(color));
        }};
    }

    @Override
    public boolean isRequiredRenderUpdate() {
        return true;
    }

    @Override
    public void loadInternalData(BlockState state, CompoundNBT data) {
        super.loadInternalData(state, data);
        color = ColorUtils.swapAlpha(new Color(data.getInt(COLOR_TAG)), EnderwireLightEmitterTileRenderer.REQUIRED_COLOR_ALPHA);
        lightLevel = data.getInt(LIGHT_LEVEL_TAG);
    }

    @Override
    public CompoundNBT saveInternalData(CompoundNBT data) {
        data = super.saveInternalData(data);
        data.putInt(COLOR_TAG, color.getRGB());
        data.putInt(LIGHT_LEVEL_TAG, lightLevel);
        return data;
    }

    @Override
    public MethodResult configure(Map<?, ?> data) throws LuaException {
        Object lightLevel = data.get("lightLevel");
        Object colorRaw = data.get("color");
        BlockState pushState = getBlockState();
        boolean isConfigured = false;
        if (colorRaw != null) {
            Color newColor = LuaUtils.convertToColor(colorRaw,  EnderwireLightEmitterTileRenderer.REQUIRED_COLOR_ALPHA);
            if (!newColor.equals(color)) {
                color = newColor;
                isConfigured = true;
            }
        }
        if (lightLevel != null) {
            if (!(lightLevel instanceof Number))
                throw new LuaException("lightLevel should be number");
            int newLightLevel = Math.min(MAX_LIGHT_LEVEL, ((Number) lightLevel).intValue());
            if (newLightLevel < 0)
                throw new LuaException("lightLevel should be 0 or bigger");
            if (newLightLevel != this.lightLevel) {
                this.lightLevel = newLightLevel;
                pushState = pushState.setValue(EnderwireLightEmitterBlock.ENABLED, newLightLevel != 0);
                isConfigured = true;
            }
        }
        if (isConfigured)
            pushInternalDataChangeToClient(pushState);
        return MethodResult.of(true);
    }
}
