package dev.enderpalm.lignin.text.container;

public class Badge {

    private final int data;

    public Badge(int data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return String.valueOf(this.data);
    }
}
