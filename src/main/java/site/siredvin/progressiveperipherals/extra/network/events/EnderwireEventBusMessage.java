package site.siredvin.progressiveperipherals.extra.network.events;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class EnderwireEventBusMessage<T extends IEpochEvent> {
    private final long number;
    private final @NotNull T event;

    public EnderwireEventBusMessage(long number, @NotNull T event) {
        this.number = number;
        this.event = event;
    }

    public long getNumber() {
        return number;
    }

    public @NotNull T getEvent() {
        return event;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof EnderwireEventBusMessage)) return false;
        EnderwireEventBusMessage<?> that = (EnderwireEventBusMessage<?>) o;
        return number == that.number && event.equals(that.event);
    }

    @Override
    public int hashCode() {
        return Objects.hash(number, event);
    }
}
