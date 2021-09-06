package site.siredvin.progressiveperipherals.utils;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.function.Function;

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

    public @NotNull T getNotNull() {
        return Objects.requireNonNull(value);
    }

    public boolean isEmpty() {
        return value == null;
    }

    public boolean isPresent() {
        return !isEmpty();
    }

    public void setValue(T value) {
        this.value = value;
    }

    public void mutate(@NotNull Function<T, T> mutateFunc) {
        value = mutateFunc.apply(value);
    }

    public static <T> ValueContainer<T> of(T t) {
        return new ValueContainer<>(t);
    }
}
