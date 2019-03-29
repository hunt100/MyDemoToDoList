package com.example.demo.entity;

import java.util.Comparator;

public class SortDateTasks implements Comparator<Task> {
    @Override
    public int compare(Task t1, Task t2) {
        if (t1.getDate().compareTo(t2.getDate()) > 0) {
            return 1;
        } else if (t1.getDate().compareTo(t2.getDate()) < 0) {
            return -1;
        } else if (t1.getDate().compareTo(t2.getDate()) == 0) {
            return 0;
        }
        return 123;
    }
}
