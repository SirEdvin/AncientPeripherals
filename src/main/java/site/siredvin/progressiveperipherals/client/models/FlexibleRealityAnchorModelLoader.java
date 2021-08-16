package site.siredvin.progressiveperipherals.client.models;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import net.minecraft.resources.IResourceManager;
import net.minecraftforge.client.model.IModelLoader;
import org.jetbrains.annotations.NotNull;

public class FlexibleRealityAnchorModelLoader implements IModelLoader<FlexibleRealityAnchorGeometry> {
    @Override
    public void onResourceManagerReload(@NotNull IResourceManager resourceManager) {

    }

    @Override
    public @NotNull FlexibleRealityAnchorGeometry read(@NotNull JsonDeserializationContext deserializationContext, @NotNull JsonObject modelContents) {
        return new FlexibleRealityAnchorGeometry();
    }
}
