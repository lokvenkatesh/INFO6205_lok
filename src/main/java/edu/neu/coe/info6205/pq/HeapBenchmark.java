package edu.neu.coe.info6205.pq;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import javax.swing.*;
import java.awt.*;
import java.util.Comparator;
import java.util.Optional;
import java.util.Random;
import java.util.function.Supplier;

public class HeapBenchmark extends JFrame {

    private static final int M = 4095; // Max heap capacity
    private static final int INSERTIONS = 16000; // Number of elements to insert
    private static final int REMOVALS = 4000; // Number of elements to remove
    private static final Random RANDOM = new Random();

    private static final XYSeriesCollection dataset = new XYSeriesCollection();

    public HeapBenchmark(String title) {
        super(title);
        setLayout(new BorderLayout());
        JFreeChart chart = createChart();
        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new Dimension(800, 600));
        setContentPane(chartPanel);
    }

    public static void main(String[] args) {

        Comparator<Integer> comparator = Comparator.naturalOrder(); // Min heap

        // Benchmark binary heap without Floyd's trick
        System.out.println("Benchmarking Binary Heap (No Floyd):");
        benchmarkHeap("Binary Heap (No Floyd)", () -> new PriorityQueue<>(M, false, comparator, false));

        // Benchmark binary heap with Floyd's trick
        System.out.println("\nBenchmarking Binary Heap (With Floyd):");
        benchmarkHeap("Binary Heap (With Floyd)", () -> new PriorityQueue<>(M, false, comparator, true));

        // Benchmark 4-ary heap without Floyd's trick
        System.out.println("\nBenchmarking 4-ary Heap (No Floyd):");
        benchmarkHeap("4-ary Heap (No Floyd)", () -> new PriorityQueue<>(M, true, comparator, false));

        // Benchmark 4-ary heap with Floyd's trick
        System.out.println("\nBenchmarking 4-ary Heap (With Floyd):");
        benchmarkHeap("4-ary Heap (With Floyd)", () -> new PriorityQueue<>(M, true, comparator, true));

        // Display the chart
        SwingUtilities.invokeLater(() -> {
            HeapBenchmark example = new HeapBenchmark("Heap Benchmarking (Log-Log Plot)");
            example.setSize(800, 600);
            example.setLocationRelativeTo(null);
            example.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
            example.setVisible(true);
        });
    }

    private static void benchmarkHeap(String heapName, Supplier<PriorityQueue<Integer>> heapSupplier) {
        PriorityQueue<Integer> heap = heapSupplier.get();
        java.util.List<Integer> spilledElements = new java.util.ArrayList<>(); // Use fully qualified name

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
        System.out.println("Heap: " + heapName);
        System.out.println("Insertion Time (ns): " + insertTime);
        System.out.println("Removal Time (ns): " + removeTime);
        highestSpilled.ifPresent(e -> System.out.println("Highest Priority Spilled Element: " + e));
        System.out.println("---------------------------------------");

        // Add data to series for graph plotting
        XYSeries series = new XYSeries(heapName);
        series.add(Math.log(INSERTIONS), Math.log(insertTime));
        series.add(Math.log(REMOVALS), Math.log(removeTime));
        dataset.addSeries(series);
    }

    private JFreeChart createChart() {
        return ChartFactory.createXYLineChart(
                "Heap Benchmarking (Log-Log Plot)",
                "Log of Operations", "Log of Time (ns)",
                dataset, PlotOrientation.VERTICAL,
                true, true, false);
    }
}
