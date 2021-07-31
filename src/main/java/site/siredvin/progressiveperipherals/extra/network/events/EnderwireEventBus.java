package site.siredvin.progressiveperipherals.extra.network.events;

import com.google.common.collect.EvictingQueue;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

public class EnderwireEventBus<T> {
    private static final int NETWORK_BUS_QUEUE_SIZE = 150;

    private long counter;
    private EvictingQueue<EnderwireEventBusMessage<T>> queue;

    public EnderwireEventBus() {
        this.counter = 0;
        this.queue = EvictingQueue.create(NETWORK_BUS_QUEUE_SIZE);
    }

    public long getCounter() {
        return counter;
    }

    public void handleEvent(@NotNull T event) {
        queue.add(new EnderwireEventBusMessage<>(counter, event));
        counter++;
    }

    public long traverse(long lastConsumedMessage, Consumer<T> consumer) {
        for (EnderwireEventBusMessage<T> message : queue) {
            if (message.getNumber() <= lastConsumedMessage)
                continue;
            consumer.accept(message.getEvent());
            lastConsumedMessage = message.getNumber();
        }
        return lastConsumedMessage;
    }
}
