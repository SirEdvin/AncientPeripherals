package site.siredvin.progressiveperipherals.extra.network.events;

import org.jetbrains.annotations.NotNull;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

public class EnderwireComputerEvent implements IEpochEvent {
    private final @NotNull String name;
    private final @NotNull Map<String, Object> data;
    private final long epoch;


    public EnderwireComputerEvent(@NotNull String name, @NotNull Map<String, Object> data, long epoch) {
        this.name = name;
        this.data = data;
        this.epoch = epoch;
    }

    public EnderwireComputerEvent(@NotNull String name) {
        this(name, new HashMap<>(), LocalDateTime.now().toEpochSecond(ZoneOffset.UTC));
    }

    public @NotNull String getName() {
        return name;
    }

    public @NotNull Map<String, Object> getData() {
        return data;
    }

    public long getEpoch() {
        return epoch;
    }

    public static EnderwireComputerEvent timed(@NotNull String name, Map<String, Object> data) {
        LocalDateTime now = LocalDateTime.now();
        long epoch = now.toEpochSecond(ZoneOffset.UTC);

        data.put("datetime", now.format(DateTimeFormatter.ISO_DATE_TIME));
        data.put("epoch", epoch);
        data.put("event", name);

        return new EnderwireComputerEvent(name, data, epoch);
    }
}
