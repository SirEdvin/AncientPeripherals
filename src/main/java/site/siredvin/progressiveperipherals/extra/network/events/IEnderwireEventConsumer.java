package site.siredvin.progressiveperipherals.extra.network.events;

public interface IEnderwireEventConsumer<T extends IEnderwireBusEvent> {
    void consume(T event);
    @SuppressWarnings("unused")
    void terminate();
}
