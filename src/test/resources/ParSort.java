package edu.neu.coe.info6205.sort.par;

import java.util.Arrays;
import java.util.concurrent.CompletableFuture;

class ParSort {

    public static int cutoff = 1000;

    public static void sort(int[] array, int from, int to) {
        if (to - from < cutoff) {
            // Sort directly if within cutoff
            Arrays.sort(array, from, to);
        } else {
            // Sort two halves in parallel
            CompletableFuture<int[]> parsort1 = parsort(array, from, from + (to - from) / 2);
            CompletableFuture<int[]> parsort2 = parsort(array, from + (to - from) / 2, to);
            
            // Merge the two sorted halves
            CompletableFuture<int[]> parsort = parsort1.thenCombine(parsort2, (xs1, xs2) -> {
                int[] result = new int[xs1.length + xs2.length];
                
                // Merging two sorted arrays xs1 and xs2 into result
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

            // Copy the merged result back to the original array
            parsort.whenComplete((result, throwable) -> System.arraycopy(result, 0, array, from, result.length));
            parsort.join(); // Ensure tasks are complete
        }
    }

    private static CompletableFuture<int[]> parsort(int[] array, int from, int to) {
        return CompletableFuture.supplyAsync(() -> {
            int[] result = new int[to - from];
            // Copy the segment of array to result
            System.arraycopy(array, from, result, 0, result.length);
            // Sort the copied segment
            sort(result, 0, result.length);
            return result;
        });
    }
}
