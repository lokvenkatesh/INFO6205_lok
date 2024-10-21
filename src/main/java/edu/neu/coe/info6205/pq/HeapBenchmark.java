package edu.neu.coe.info6205.pq;

import java.util.*;
import java.util.function.Supplier;

public class HeapBenchmark {

    private static final int M = 4095; // Max heap capacity
    private static final int INSERTIONS = 16000; // Number of elements to insert
    private static final int REMOVALS = 4000; // Number of elements to remove
    private static final Random RANDOM = new Random();

    public static void main(String[] args) {

        Comparator<Integer> comparator = Comparator.naturalOrder(); // Min heap

        // Benchmark binary heap without Floyd's trick
        System.out.println("Benchmarking Binary Heap (No Floyd):");
        benchmarkHeap(() -> new PriorityQueue<>(M, false, comparator, false));

        // Benchmark binary heap with Floyd's trick
        System.out.println("\nBenchmarking Binary Heap (With Floyd):");
        benchmarkHeap(() -> new PriorityQueue<>(M, false, comparator, true));

        // Benchmark 4-ary heap without Floyd's trick
        System.out.println("\nBenchmarking 4-ary Heap (No Floyd):");
        benchmarkHeap(() -> new PriorityQueue<>(M, true, comparator, false));

        // Benchmark 4-ary heap with Floyd's trick
        System.out.println("\nBenchmarking 4-ary Heap (With Floyd):");
        benchmarkHeap(() -> new PriorityQueue<>(M, true, comparator, true));
    }

    /**
     * Benchmark the performance of a heap implementation.
     *
     * @param heapSupplier A supplier for creating the priority queue instance.
     */
    private static void benchmarkHeap(Supplier<PriorityQueue<Integer>> heapSupplier) {
        PriorityQueue<Integer> heap = heapSupplier.get();
        List<Integer> spilledElements = new ArrayList<>();

        // Insert random elements
        long startTime = System.nanoTime();
        for (int i = 0; i < INSERTIONS; i++) {
            int element = RANDOM.nextInt(100000); // Random integer
            if (heap.size() >= M) {
                spilledElements.add(element); // Spill elements when heap is full
            } else {
                heap.give(element); // Insert into heap
            }
        }
        long insertTime = System.nanoTime() - startTime;

        // Remove elements from the heap
        startTime = System.nanoTime();
        for (int i = 0; i < REMOVALS; i++) {
            try {
                heap.take();
            } catch (PQException e) {
                e.printStackTrace();
            }
            
        }
        long removeTime = System.nanoTime() - startTime;

        // Find the highest priority spilled element
        Optional<Integer> highestSpilled = spilledElements.stream().max(Comparator.naturalOrder());

        // Output the results
        System.out.println("Insertion Time (ns): " + insertTime);
        System.out.println("Removal Time (ns): " + removeTime);
        highestSpilled.ifPresent(e -> System.out.println("Highest Priority Spilled Element: " + e));
    }
}
