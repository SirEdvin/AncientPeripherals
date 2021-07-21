package site.siredvin.progressiveperipherals.utils;

public class ValueContainer<T> {
    private T value;

    public ValueContainer(T value) {
        this.value = value;
    }

    public ValueContainer() {
        this.value = null;
    }

    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        this.value = value;
    }
}
