package site.siredvin.progressiveperipherals.extra.network.events;

public interface IEnderwireBusEvent {
    long getEpoch();
    boolean isValid();
}
