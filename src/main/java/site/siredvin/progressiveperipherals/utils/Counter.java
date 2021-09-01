package site.siredvin.progressiveperipherals.utils;

public class Counter {
    private int counter;
    public Counter(int start) {
        counter = start;
    }

    public void increase() {
        counter++;
    }

    public void decrease() {
        counter--;
    }

    public int get() {
        return counter;
    }

    public int getAndIncrease() {
        int oldCounter = counter;
        increase();
        return oldCounter;
    }
}
