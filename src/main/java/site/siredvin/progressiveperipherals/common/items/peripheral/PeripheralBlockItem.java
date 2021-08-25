package site.siredvin.progressiveperipherals.common.items.peripheral;

import net.minecraft.block.Block;
import org.jetbrains.annotations.NotNull;
import site.siredvin.progressiveperipherals.common.items.base.BaseBlockItem;

import java.util.function.Supplier;

public class PeripheralBlockItem extends BaseBlockItem {

    private final @NotNull Supplier<Boolean> enabledSup;

    public PeripheralBlockItem(@NotNull Block blockID, @NotNull Properties properties, @NotNull Supplier<Boolean> enabledSup) {
        super(blockID, properties);
        this.enabledSup = enabledSup;
    }

    public PeripheralBlockItem(@NotNull Block blockID, @NotNull Supplier<Boolean> enabledSup) {
        super(blockID);
        this.enabledSup = enabledSup;
    }

    @Override
    public boolean isEnabled() {
        return enabledSup.get();
    }
}
