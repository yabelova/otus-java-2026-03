# JVM Garbage Collection Performance Study

## 1. Environment & Setup

- **OS**: Windows 11
- **JVM**: OpenJDK 25.0.2
- **CPU**: AMD Ryzen 7 5800H (8 physical cores, 16 logical threads, 3.20-4.45 GHz)
- **Tools**: JMH (Java Microbenchmark Harness) with GC Profiler

## 2. Methodology

The study was automated using a Bash script that iterates through various JVM configurations:

- **Heap Sizes**: 128 MB, 256 MB, 512 MB, 1024 MB, 2048 MB, 4096 MB.
- **GC Algorithms**: G1, Parallel, Serial, ZGC, Shenandoah.

Each benchmark consists of **2 warmup iterations** and **5 measurement iterations** to ensure JIT compiler stabilization
and statistical accuracy.

## 3. Benchmark Results (Original vs. Optimized)

![img01.png](main/resources/img01.png)

![img02.png](main/resources/img02.png)

### Detailed Benchmark Results

<details>
  <summary><b>Click to expand full table (5 GC types × 6 Heap sizes)</b></summary>

| GC Type    | Heap, MB | | Original (ms/op) | GC Time (ms) | GC Count | | Optimized (ms/op) | GC Time (ms) | GC Count |
|:-----------|:---------|:-|:-----------------|:-------------|:---------|:-|:------------------|:-------------|:---------|
| G1         | 128      | | 17588,495        | 7650         | 5170     | | 3398,121          |              | ≈ 0      |
| G1         | 256      | | 16760,010        | 2754         | 2526     | | 3454,421          |              | ≈ 0      |
| G1         | 512      | | 16530,213        | 1250         | 1280     | | 3395,060          |              | ≈ 0      |
| G1         | 1024     | | 16099,911        | 625          | 999      | | 3437,922          |              | ≈ 0      |
| G1         | 2048     | | 15608,018        | 312          | 508      | | 3393,100          |              | ≈ 0      |
| G1         | 4096     | | 15742,220        | 284          | 156      | | 3554,539          |              | ≈ 0      |
| Parallel   | 128      | | 15879,686        | 17100        | 11108    | | 3087,946          |              | ≈ 0      |
| Parallel   | 256      | | 13709,983        | 4983         | 6320     | | 3009,094          |              | ≈ 0      |
| Parallel   | 512      | | 13958,475        | 2353         | 3929     | | 2999,403          |              | ≈ 0      |
| Parallel   | 1024     | | 13222,887        | 1148         | 2056     | | 3196,590          |              | ≈ 0      |
| Parallel   | 2048     | | 12705,587        | 565          | 1007     | | 3223,036          |              | ≈ 0      |
| Parallel   | 4096     | | 12732,667        | 467          | 281      | | 3064,239          |              | ≈ 0      |
| Serial     | 128      | | 16622,834        | 21158        | 11188    | | 3111,818          |              | ≈ 0      |
| Serial     | 256      | | 14769,987        | 5587         | 10952    | | 3163,170          |              | ≈ 0      |
| Serial     | 512      | | 13621,507        | 2795         | 5694     | | 3051,522          |              | ≈ 0      |
| Serial     | 1024     | | 16070,412        | 1397         | 2792     | | 2961,325          |              | ≈ 0      |
| Serial     | 2048     | | 12485,949        | 699          | 1378     | | 3057,446          |              | ≈ 0      |
| Serial     | 4096     | | 12720,290        | 704          | 350      | | 3089,619          |              | ≈ 0      |
| ZGC        | 128      | | 21033,742        | 137639       | 32132    | | 3226,054          | 55           | 18       |
| ZGC        | 256      | | 18863,364        | 23571        | 64098    | | 3167,337          | 24           | 6        |
| ZGC        | 512      | | 14314,452        | 21078        | 7900     | | 3147,458          | 10           | 6        |
| ZGC        | 1024     | | 13922,657        | 7709         | 2622     | | 3373,181          |              | ≈ 0      |
| ZGC        | 2048     | | 14893,766        | 4267         | 1194     | | 3338,403          |              | ≈ 0      |
| ZGC        | 4096     | | 14160,343        | 1932         | 602      | | 3281,005          |              | ≈ 0      |
| Shenandoah | 128      | | 27060,221        | 12774        | 12962    | | 3542,262          |              | ≈ 0      |
| Shenandoah | 256      | | 16348,121        | 6808         | 5917     | | 3734,179          |              | ≈ 0      |
| Shenandoah | 512      | | 14182,166        | 3608         | 2829     | | 3269,724          |              | ≈ 0      |
| Shenandoah | 1024     | | 13216,266        | 1802         | 1386     | | 3348,316          |              | ≈ 0      |
| Shenandoah | 2048     | | 13277,474        | 687          | 913      | | 3450,281          |              | ≈ 0      |
| Shenandoah | 4096     | | 14293,821        | 488          | 345      | | 3602,186          |              | ≈ 0      |

#### Sample Benchmark Output

Here is a snippet from a raw JMH report (`results_UseParallelGC_1024m_05-05_02-43.txt`) showing how metrics are
captured:

```text
Benchmark                                                 Mode  Cnt            Score      Error   Units
GcPerformanceBenchmark.testOptimized                      avgt    5         3196,590 ±  137,551   ms/op
GcPerformanceBenchmark.testOptimized:·gc.alloc.rate       avgt    5            0,158 ±    0,007  MB/sec
GcPerformanceBenchmark.testOptimized:·gc.alloc.rate.norm  avgt    5       529563,200 ±  363,498    B/op
GcPerformanceBenchmark.testOptimized:·gc.count            avgt    5              ≈ 0             counts
GcPerformanceBenchmark.testOriginal                       avgt    5        13222,887 ± 1982,292   ms/op
GcPerformanceBenchmark.testOriginal:·gc.alloc.rate        avgt    5         5776,610 ±  834,025  MB/sec
GcPerformanceBenchmark.testOriginal:·gc.alloc.rate.norm   avgt    5  80001408892,800 ± 9064,899    B/op
GcPerformanceBenchmark.testOriginal:·gc.count             avgt    5         1148,000             counts
GcPerformanceBenchmark.testOriginal:·gc.time              avgt    5         2056,000                 ms
```

*Note: Full raw logs are excluded from the repository to maintain cleanliness. Results were extracted from JMH text
reports.*

</details>

## 4. Optimal GC Configuration for Original Version

Based on the measurements of the original application, the following observations were made:

- **Memory Constraints**: At small heap sizes (128 MB - 256 MB), all collectors showed poor results due to an excessive
  number of GC cycles. For example, Serial GC triggered over 11,000 collections, spending more time on cleanup than on
  actual work. Modern collectors (ZGC and Shenandoah) performed even worse; they require extra memory for their internal
  metadata, meaning background GC tasks were almost continuously stealing massive amounts of CPU cycles.
- **Performance Gain**: Increasing the heap size to 1024 MB resulted in a steady improvement in execution time.
- **Saturation Point**: When moving from 2048 MB to 4096 MB, the execution time stopped decreasing.
- **Throughput vs. Latency**: Across the entire test range, Serial and Parallel collectors showed nearly identical
  throughput. Even on a high-performance AMD Ryzen 7 (8 cores/16 threads), the multi-threaded Parallel GC did not
  significantly outperform the single-threaded Serial GC for this specific workload. This suggests that for a
  single-application task with heavy object allocation, the simplicity of Serial GC is just as effective as the
  multi-threaded approach. Meanwhile, modern collectors showed lower throughput because they prioritize shorter pause
  times over raw calculation speed.

**Verdict**: The optimal configuration for the original application is a 2048 MB heap size with Serial or Parallel GC.

## 5. Optimization

The optimized version demonstrates consistently superior performance that is independent of Garbage Collector settings
or Heap size.

- **Execution Speed**: ~3s vs ~15s (5x faster).
- **Memory Consumption**: ~0.5 MB vs 80 GB (150,000x reduction).

### Implemented Improvements

To achieve these results, the following refactoring steps were taken:

- **Eliminated Autoboxing**: `Integer` objects were replaced with primitive `int` types in the `Summator` and `Data`
  classes to remove the overhead of wrapper object creation.
- **Object Reuse**: Instead of instantiating 500 million temporary `Data` objects, a single mutable instance was
  reused.  
  ⚠️ **Note**: This is an intentional trade-off. Although this object is added to the `listValues` collection, it is
  safe because the list acts only as a counter (via `size()`) and is never used to retrieve individual elements.
- **Collection Tuning**: The `ArrayList` in the `Summator` class was initialized with an `initialCapacity` of 100,000 to
  prevent expensive internal array resizing and data copying.
- **Loop Logic Optimization**: The modulo operator (`%`) in the `CalcDemo` class was replaced with a simple counter in
  the logging loop to minimize CPU cycles spent on division.

### Correctness Verification

To ensure that optimizations did not break the application logic, a suite of **JUnit 5 tests** was implemented. These
tests verify that both the `Original` and `Optimized` versions produce identical mathematical results.

## 6. Conclusions

1. **Optimal Heap Size**: For the original application the optimal heap is **2048 MB**. Larger heap sizes do not provide
   any further performance benefits.
2. **Optimization Impact**: Code optimization reduced memory allocations from 80 GB to 0.5 MB and increased execution
   speed by 5x, making the application performance independent of JVM settings.
3. **Core Takeaway**: No amount of JVM tuning (Heap size or GC choice) can compensate for inefficient code.

## 7. How to Run

To reproduce these benchmarks:

1. Run the automation script:
   ```bash
   chmod +x ./hw04-gc/run_gc_test.sh
   ./hw04-gc/run_gc_test.sh
   ```
2. Wait for the benchmarks to complete (it’s a perfect time for a coffee break ☕)
3. Reports will be generated in `hw04-gc/build/results/jmh/`.
