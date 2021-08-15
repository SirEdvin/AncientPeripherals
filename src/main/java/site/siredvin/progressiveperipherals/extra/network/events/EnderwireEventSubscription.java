package site.siredvin.progressiveperipherals.extra.network.events;

import org.jetbrains.annotations.NotNull;

import java.lang.ref.WeakReference;

public class EnderwireEventSubscription<T extends IEnderwireBusEvent> implements IEnderwireEventConsumer<T> {
    private final @NotNull WeakReference<IEnderwireEventConsumer<T>> consumerRef;

    public EnderwireEventSubscription(@NotNull IEnderwireEventConsumer<T> consumer) {
        this.consumerRef = new WeakReference<>(consumer);
    }

    public boolean isValid() {
        return consumerRef.get() != null;
    }

    @Override
    public void consume(T event) {
        IEnderwireEventConsumer<T> consumer = consumerRef.get();
        if (consumer != null)
            consumer.consume(event);
    }
}
