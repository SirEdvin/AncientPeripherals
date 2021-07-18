package site.siredvin.progressiveperipherals.api.integrations;

import net.minecraft.block.BlockState;
import net.minecraft.util.text.ITextComponent;

import java.util.Collections;
import java.util.List;

public interface IProbeable {
    List<ITextComponent> commonProbeData(BlockState state);
    default List<ITextComponent> extraProbeData(BlockState state) {
        return Collections.emptyList();
    }
}
