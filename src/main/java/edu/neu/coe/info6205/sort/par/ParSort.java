package edu.neu.coe.info6205.sort.par;

import java.util.Arrays;
import java.util.concurrent.CompletableFuture;

/**
 * This code has been fleshed out by Ziyao Qiao. Thanks very much.
 * CONSIDER tidy it up a bit.
 */
class ParSort {

    public static int cutoff = 1000;

    public static void sort(int[] array, int from, int to) {
        if (to - from < cutoff) {
            // Use sequential sort for smaller partitions
            Arrays.sort(array, from, to);
        } else {
            // Determine the midpoint for dividing the array
            int mid = from + (to - from) / 2;

            // Initiate parallel sorting for each half of the array
            CompletableFuture<int[]> parsort1 = parsort(array, from, mid);
            CompletableFuture<int[]> parsort2 = parsort(array, mid, to);

            // Combine the results of both halves
            CompletableFuture<int[]> parsort = parsort1.thenCombine(parsort2, (xs1, xs2) -> {
                int[] result = new int[xs1.length + xs2.length];

                // Merge the two sorted halves into the result array
                int i = 0, j = 0;
                for (int k = 0; k < result.length; k++) {
                    if (i >= xs1.length) {
                        result[k] = xs2[j++];
                    } else if (j >= xs2.length) {
                        result[k] = xs1[i++];
                    } else if (xs2[j] < xs1[i]) {
                        result[k] = xs2[j++];
                    } else {
                        result[k] = xs1[i++];
                    }
                }
                return result;
            });

            // Copy the sorted and merged result back into the original array
            parsort.whenComplete((result, throwable) -> {
                if (throwable == null) {
                    System.arraycopy(result, 0, array, from, result.length);
                } else {
                    throwable.printStackTrace();
                }
            });

            // Wait for all parallel tasks to complete
            parsort.join();
        }
    }

    private static CompletableFuture<int[]> parsort(int[] array, int from, int to) {
        return CompletableFuture.supplyAsync(() -> {
            int[] result = new int[to - from];
            // Copy the specified range into a new array
            System.arraycopy(array, from, result, 0, result.length);
            // Recursively sort the sub-array
            sort(result, 0, result.length);
            return result;
        });
    }
}
