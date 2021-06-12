package org.javaunit.autoparams.customization.test;

import java.util.ArrayList;

public class Inventory<T> {

    private int capacity;
    private ArrayList<T> items;

    public int getCapacity() {
        return capacity;
    }

    public Iterable<T> getItems() {
        return items;
    }

}
