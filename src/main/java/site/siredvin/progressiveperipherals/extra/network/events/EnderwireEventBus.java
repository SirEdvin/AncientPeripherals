package site.siredvin.progressiveperipherals.extra.network.events;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class EnderwireEventBus<T extends IEnderwireBusEvent> {
    private final List<EnderwireEventSubscription<T>> consumers = new ArrayList<>();

    public EnderwireEventBus() {}

    public EnderwireEventSubscription<T> subscribe(IEnderwireEventConsumer<T> consumer) {
        EnderwireEventSubscription<T> subscription = new EnderwireEventSubscription<>(consumer);
        consumers.add(subscription);
        return subscription;
    }

    public void unsubscribe(EnderwireEventSubscription<T> subscription) {
        consumers.remove(subscription);
    }

    public synchronized void handleEvent(@NotNull T event) {
        Iterator<EnderwireEventSubscription<T>> iterator = consumers.listIterator();
        while (iterator.hasNext()) {
            EnderwireEventSubscription<T> sub = iterator.next();
            if (!sub.isValid())
                iterator.remove();
            sub.consume(event);
        }
    }
}
