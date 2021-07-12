package site.siredvin.ancientperipherals.common.models;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import net.minecraft.resources.IResourceManager;
import net.minecraftforge.client.model.IModelLoader;

public class FlexibleRealityAnchorModelLoader implements IModelLoader<FlexibleRealityAnchorGeometry> {
    @Override
    public void onResourceManagerReload(IResourceManager resourceManager) {

    }

    @Override
    public FlexibleRealityAnchorGeometry read(JsonDeserializationContext deserializationContext, JsonObject modelContents) {
        return new FlexibleRealityAnchorGeometry();
    }
}
