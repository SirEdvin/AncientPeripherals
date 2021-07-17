package site.siredvin.progressiveperipherals.client.models;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import net.minecraft.resources.IResourceManager;
import net.minecraftforge.client.model.IModelLoader;

public class FlexibleStatueModelLoader implements IModelLoader<FlexibleStatueGeometry> {
    @Override
    public void onResourceManagerReload(IResourceManager resourceManager) {

    }

    @Override
    public FlexibleStatueGeometry read(JsonDeserializationContext deserializationContext, JsonObject modelContents) {
        return new FlexibleStatueGeometry();
    }
}
