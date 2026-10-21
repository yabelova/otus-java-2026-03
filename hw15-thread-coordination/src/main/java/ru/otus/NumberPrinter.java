package ru.otus;

public class NumberPrinter {

    private final Object monitor = new Object();
    private int current = 1;
    private int direction = 1;
    private int turn = 1;
    private int cyclesDone = 0;
    private final int maxCycles;

    public NumberPrinter(int maxCycles) {
        this.maxCycles = maxCycles;
    }

    public void print(int threadId, String threadName) {
        synchronized (monitor) {
            while (cyclesDone < maxCycles) {
                while (turn != threadId && cyclesDone < maxCycles) {
                    try {
                        monitor.wait();
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        return;
                    }
                }

                if (cyclesDone >= maxCycles) {
                    return;
                }

                System.out.println(threadName + ": " + current);

                if (threadId == 2) {
                    if (direction == 1 && current == 10) {
                        direction = -1;
                        current = 9;
                    } else if (direction == -1 && current == 1) {
                        cyclesDone++;
                        direction = 1;
                        current = 2;
                    } else if (direction == 1) {
                        current++;
                    } else {
                        current--;
                    }
                }

                turn = (threadId == 1) ? 2 : 1;
                monitor.notifyAll();
            }
        }
    }
}
