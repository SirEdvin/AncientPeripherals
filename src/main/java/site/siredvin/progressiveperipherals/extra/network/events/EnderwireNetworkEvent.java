package site.siredvin.progressiveperipherals.extra.network.events;

import site.siredvin.progressiveperipherals.extra.network.NetworkElementData;

import java.util.Arrays;

public class EnderwireNetworkEvent {
    private final NetworkElementData[] removedElements;
    private final NetworkElementData[] addedElements;

    EnderwireNetworkEvent(NetworkElementData[] removedElements, NetworkElementData[] addedElements) {
        this.removedElements = removedElements;
        this.addedElements = addedElements;
    }

    public NetworkElementData[] getAddedElements() {
        return addedElements;
    }

    public NetworkElementData[] getRemovedElements() {
        return removedElements;
    }

    public static EnderwireNetworkEvent addedElements(NetworkElementData... elements) {
        return new EnderwireNetworkEvent(new NetworkElementData[0], elements);
    }

    public static EnderwireNetworkEvent removedElements(NetworkElementData... elements) {
        return new EnderwireNetworkEvent(elements, new NetworkElementData[0]);
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
