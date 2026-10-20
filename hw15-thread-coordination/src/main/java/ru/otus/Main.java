package ru.otus;

public class Main {

    private static final int DEFAULT_MAX_CYCLES = 2;

    public static void main(String[] args) throws InterruptedException {

        NumberPrinter printer = new NumberPrinter(getMaxCyclesFromParam(args));
        Thread t1 = new Thread(() -> printer.print(1, "thread 1"));
        Thread t2 = new Thread(() -> printer.print(2, "thread 2"));
        t1.start();
        t2.start();
        t1.join();
        t2.join();
    }

    private static int getMaxCyclesFromParam(String[] args) {
        if (args.length == 0) {
            return DEFAULT_MAX_CYCLES;
        }
        try {
            return Integer.parseInt(args[0]);
        } catch (NumberFormatException e) {
            System.out.printf("Invalid argument \"%s\", using default: %d%n\n", args[0], DEFAULT_MAX_CYCLES);
            return DEFAULT_MAX_CYCLES;
        }
    }
}
