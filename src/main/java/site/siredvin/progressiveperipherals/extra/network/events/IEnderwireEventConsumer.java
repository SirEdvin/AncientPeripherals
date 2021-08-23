package site.siredvin.progressiveperipherals.extra.network.events;

@FunctionalInterface
public interface IEnderwireEventConsumer<T extends IEnderwireBusEvent> {
    void consume(T event);
    @SuppressWarnings("unused")
    default void terminate() {

    }
}
