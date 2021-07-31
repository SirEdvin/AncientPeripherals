package site.siredvin.progressiveperipherals.api.enderwire;

import org.jetbrains.annotations.Nullable;

public interface IEnderwireElement {
    @Nullable String getAttachedNetwork();
    void setAttachedNetwork(String name);
    void clearAttachedNetwork();
}
