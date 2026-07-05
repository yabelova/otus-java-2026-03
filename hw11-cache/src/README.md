# JDBC Service with WeakHashMap-based Cache

## JMH Benchmark Results

Each benchmark consists of **1 warmup iteration** and **3 measurement iterations** with **1 fork** to ensure JIT compiler stabilization and statistical accuracy.

```text
Benchmark                    Mode  Cnt     Score      Error  Units
CacheBenchmark.withCache     avgt    3     0,005 ±    0,013  us/op
CacheBenchmark.withoutCache  avgt    3  1418,678 ± 1267,049  us/op
```

The cache hit (`withCache`) is about **280,000x faster** than querying the database (`withoutCache`).

## Running

```bash
./gradlew :hw11-cache:run          # demo (save + raw + miss + hit with timing)
./gradlew :hw11-cache:jmh          # JMH benchmark (requires Docker)
./gradlew :hw11-cache:test         # unit tests
```
