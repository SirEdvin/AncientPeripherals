package site.siredvin.progressiveperipherals.extra.network.events;

import site.siredvin.progressiveperipherals.extra.network.EnderwireNetworkElement;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Arrays;

public class EnderwireNetworkEvent implements IEpochEvent {
    private final EnderwireNetworkElement[] removedElements;
    private final EnderwireNetworkElement[] addedElements;
    private final long epoch;

    EnderwireNetworkEvent(EnderwireNetworkElement[] removedElements, EnderwireNetworkElement[] addedElements) {
        this.removedElements = removedElements;
        this.addedElements = addedElements;
        this.epoch = LocalDateTime.now().toEpochSecond(ZoneOffset.UTC);
    }

    public EnderwireNetworkElement[] getAddedElements() {
        return addedElements;
    }

    public EnderwireNetworkElement[] getRemovedElements() {
        return removedElements;
    }

    public long getEpoch() {
        return epoch;
    }

    public static EnderwireNetworkEvent addedElements(EnderwireNetworkElement... elements) {
        return new EnderwireNetworkEvent(new EnderwireNetworkElement[0], elements);
    }

    public static EnderwireNetworkEvent removedElements(EnderwireNetworkElement... elements) {
        return new EnderwireNetworkEvent(elements, new EnderwireNetworkElement[0]);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof EnderwireNetworkEvent)) return false;
        EnderwireNetworkEvent that = (EnderwireNetworkEvent) o;
        return Arrays.equals(removedElements, that.removedElements) && Arrays.equals(addedElements, that.addedElements);
    }

    @Override
    public int hashCode() {
        int result = Arrays.hashCode(removedElements);
        result = 31 * result + Arrays.hashCode(addedElements);
        return result;
    }
}
