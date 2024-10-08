package edu.neu.coe.info6205.util;

import java.util.Random;
import java.util.Scanner;
import java.util.function.Consumer;

public class InsertionSortBenchmark {

    // Insertion sort implementation
    public static <T extends Comparable<T>> void insertionSort(T[] array) {
        for (int i = 1; i < array.length; i++) {
            T key = array[i];
            int j = i - 1;
            while (j >= 0 && array[j].compareTo(key) > 0) {
                array[j + 1] = array[j];
                j = j - 1;
            }
            array[j + 1] = key;
        }
    }

    // Helper method to generate random arrays
    private static Integer[] generateRandomArray(int size) {
        Random random = new Random();
        Integer[] array = new Integer[size];
        for (int i = 0; i < size; i++) {
            array[i] = random.nextInt(size);
        }
        return array;
    }

    // Helper method to generate ordered arrays
    private static Integer[] generateOrderedArray(int size) {
        Integer[] array = new Integer[size];
        for (int i = 0; i < size; i++) {
            array[i] = i;
        }
        return array;
    }

    // Helper method to generate reverse-ordered arrays
    private static Integer[] generateReverseOrderedArray(int size) {
        Integer[] array = new Integer[size];
        for (int i = 0; i < size; i++) {
            array[i] = size - i;
        }
        return array;
    }

    // Helper method to generate partially ordered arrays
    private static Integer[] generatePartiallyOrderedArray(int size) {
        Integer[] array = generateOrderedArray(size);
        Random random = new Random();
        // Shuffle 25% of the array
        for (int i = 0; i < size / 4; i++) {
            int randomIndex = random.nextInt(size);
            int temp = array[i];
            array[i] = array[randomIndex];
            array[randomIndex] = temp;
        }
        return array;
    }

    // Main method for benchmarking insertion sort under different conditions
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Get the number of times to run the benchmark
        System.out.print("Enter the number of times to run the benchmark: ");
        int numRuns = scanner.nextInt();

        // Get the type of array the user wants to benchmark
        System.out.println("Select the array type:");
        System.out.println("1. Random Array");
        System.out.println("2. Ordered Array");
        System.out.println("3. Reverse-Ordered Array");
        System.out.println("4. Partially Ordered Array");

        int choice = scanner.nextInt();

        // Doubling method: test at least five array sizes
        int[] sizes = {1000, 2000, 4000, 8000, 16000};

        for (int size : sizes) {
            System.out.println("Benchmarking for array size: " + size);
            Integer[] array = new Integer[size];

            // Generate the appropriate array based on user input
            switch (choice) {
                case 1:
                    array = generateRandomArray(size);
                    System.out.println("Random Array selected.");
                    break;
                case 2:
                    array = generateOrderedArray(size);
                    System.out.println("Ordered Array selected.");
                    break;
                case 3:
                    array = generateReverseOrderedArray(size);
                    System.out.println("Reverse-Ordered Array selected.");
                    break;
                case 4:
                    array = generatePartiallyOrderedArray(size);
                    System.out.println("Partially Ordered Array selected.");
                    break;
                default:
                    System.out.println("Invalid choice. Exiting.");
                    System.exit(0);
            }

            // Run the benchmark
            Consumer<Integer[]> insertionSort = InsertionSortBenchmark::insertionSort;
            Benchmark_Timer<Integer[]> benchmarkTimer = new Benchmark_Timer<>("Insertion Sort", insertionSort);

            // Print array before sorting (optional, for small array sizes)
            if (size <= 20) {  // To avoid printing huge arrays, print only if the array is small
                System.out.println("Array before sorting:");
                System.out.println(java.util.Arrays.toString(array));
            }

            double timeTaken = benchmarkTimer.run(array, numRuns);

            // Print array after sorting (optional, for small array sizes)
            if (size <= 20) {
                System.out.println("Array after sorting:");
                System.out.println(java.util.Arrays.toString(array));
            }

            // Print the time taken
            System.out.println("Time taken for insertion sort: " + timeTaken + " ms");

            System.out.println("----------------------------------------");
        }

        scanner.close();
    }
}
