#!/bin/bash

GCS=("UseG1GC" "UseParallelGC" "UseSerialGC" "UseZGC" "UseShenandoahGC")
HEAPS=("128m" "256m" "512m" "1024m" "2048m" "4096m")

echo "Starting GC benchmarks..."

for gc in "${GCS[@]}"
do
    for h in "${HEAPS[@]}"
    do
        echo "========================================"
        echo " Running Benchmark: GC = $gc | Heap = $h"
        echo "========================================"

        ./gradlew :hw04-gc:jmh -Pheap=$h -Pgc=$gc
    done
done

echo "Benchmarks completed. Check your reports in '/hw04-gc/build/results/jmh/' folder"
