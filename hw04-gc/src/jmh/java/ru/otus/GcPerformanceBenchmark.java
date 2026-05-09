package ru.otus;

import org.openjdk.jmh.annotations.*;

import java.util.concurrent.TimeUnit;

@State(Scope.Thread)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
public class GcPerformanceBenchmark {

    @Benchmark
    public void testOriginal() {
        ru.otus.calculator.original.CalcDemo.main(new String[0]);
    }

    @Benchmark
    public void testOptimized() {
        ru.otus.calculator.optimized.CalcDemo.main(new String[0]);
    }
}