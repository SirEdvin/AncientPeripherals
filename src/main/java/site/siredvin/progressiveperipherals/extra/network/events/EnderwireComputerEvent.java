package site.siredvin.progressiveperipherals.extra.network.events;

import org.jetbrains.annotations.NotNull;

public class EnderwireComputerEvent {
    private final @NotNull String name;
    private final @NotNull Object[] data;

    public EnderwireComputerEvent(@NotNull String name, @NotNull Object[] data) {
        this.name = name;
        this.data = data;
    }

    public EnderwireComputerEvent(@NotNull String name) {
        this(name, new Object[0]);
    }

    public @NotNull String getName() {
        return name;
    }

    public @NotNull Object[] getData() {
        return data;
    }

    public static EnderwireComputerEvent of(@NotNull String name, Object... data) {
        return new EnderwireComputerEvent(name, data);
    }
}
