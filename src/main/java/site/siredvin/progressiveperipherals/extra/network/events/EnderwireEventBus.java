package site.siredvin.progressiveperipherals.extra.network.events;

import com.google.common.collect.EvictingQueue;
import org.jetbrains.annotations.NotNull;
import site.siredvin.progressiveperipherals.common.configuration.ProgressivePeripheralsConfig;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class EnderwireEventBus<T extends IEpochEvent> {
    private static final long ALLOWED_DELAY_IN_SECONDS = 60 * 15;

    private long counter;
    private final EvictingQueue<EnderwireEventBusMessage<T>> queue;

    public EnderwireEventBus() {
        this.counter = 0;
        this.queue = EvictingQueue.create(ProgressivePeripheralsConfig.enderwireNetworkComputerBusSize);
    }

    public long getCounter() {
        return counter;
    }

    public synchronized void handleEvent(@NotNull T event) {
        queue.add(new EnderwireEventBusMessage<>(counter, event));
        counter++;
    }

    public boolean isEmpty() {
        return queue.isEmpty();
    }

    public synchronized void cleanup () {
        long currentEpoch = LocalDateTime.now().toEpochSecond(ZoneOffset.UTC);
        List<EnderwireEventBusMessage<T>> removeList = new ArrayList<>();
        for (EnderwireEventBusMessage<T> message: queue) {
            if (message.getEvent().getEpoch() - currentEpoch > ALLOWED_DELAY_IN_SECONDS)
                removeList.add(message);
        }
        removeList.forEach(queue::remove);
    }

    public synchronized long traverse(long lastConsumedMessage, Consumer<T> consumer) {
        for (EnderwireEventBusMessage<T> message : queue) {
            if (message.getNumber() <= lastConsumedMessage)
                continue;
            consumer.accept(message.getEvent());
            lastConsumedMessage = message.getNumber();
        }
        return lastConsumedMessage;
    }
}
