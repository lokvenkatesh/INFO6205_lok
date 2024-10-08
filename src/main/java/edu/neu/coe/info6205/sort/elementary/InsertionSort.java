package edu.neu.coe.info6205.sort.elementary;

import edu.neu.coe.info6205.sort.Helper;
import edu.neu.coe.info6205.sort.NonInstrumentingComparableHelper;
import edu.neu.coe.info6205.sort.SortWithComparableHelper;
import edu.neu.coe.info6205.util.Config;

/**
 * Class InsertionSort.
 * This class is the version of insertion sort appropriate for running instrumented benchmarks.
 * It extends SortWithComparableHelper and therefore has access to the Helper functions.
 * <p>
 * For a simpler and clearer version, see InsertionSortBasic.
 *
 * @param <X> the underlying Comparable type.
 */
public class InsertionSort<X extends Comparable<X>> extends SortWithComparableHelper<X> {

    /**
     * Constructor for any subclasses to use.
     *
     * @param description the description.
     * @param N           the number of elements expected.
     * @param nRuns       the expected number of runs.
     * @param config      the configuration.
     */
    public InsertionSort(String description, int N, int nRuns, Config config) {
        super(description, N, nRuns, config);
    }

    /**
     * Constructor for InsertionSort
     *
     * @param N      the number elements we expect to sort.
     * @param config the configuration.
     */
    public InsertionSort(int N, Config config) {
        this(DESCRIPTION, N, 1, config);
    }

    public InsertionSort(Config config) {
        this(NonInstrumentingComparableHelper.create(DESCRIPTION, config));
    }

    /**
     * Constructor for InsertionSort
     *
     * @param helper an explicit instance of Helper to be used.
     */
    public InsertionSort(Helper<X> helper) {
        super(helper);
    }

    public InsertionSort() {
        this(NonInstrumentingComparableHelper.getHelper(InsertionSort.class));
    }

    /**
     * Sort the sub-array xs:from:to using insertion sort.
     *
     * @param xs   sort the array xs from "from" to "to".
     * @param from the index of the first element to sort
     * @param to   the index of the first element not to sort
     */
    public void sort(X[] xs, int from, int to) {
        final Helper<X> helper = getHelper();  // Get the helper for assisting with the sorting.
        X a = helper.get(xs, from);  // Get the first element in the subarray.

        // Outer loop that iterates through the array from "from" to "to".
        for (int i = from + 1; i < to; i++) {
            X b = helper.get(xs, i);  // Get the current element in the array.
            X aNext = b;  // Set the next element.
            int j = i;  // Start at the current index.

            // Inner loop that shifts elements until the correct position is found.
            while (true) {
                boolean swapped = helper.swapConditional(xs, a, j - 1, j, b);  // Swap elements if needed.
                if (!swapped) break;  // If no swap is needed, exit the loop.

                if (aNext == b) aNext = a;  // Update the next element if necessary.
                j--;  // Move to the previous element.
                if (j == from) break;  // Stop if we reach the start of the array.

                a = helper.get(xs, j - 1);  // Get the previous element for comparison.
            }
            a = aNext;  // Move the next element for comparison.
        }
    }

    public static final String DESCRIPTION = "Insertion sort";

    /**
     * Static method to sort an array using Insertion Sort.
     *
     * @param <T> the type of elements in the array.
     * @param ts the array to be sorted.
     */
    public static <T extends Comparable<T>> void sort(T[] ts) {
        try (InsertionSort<T> sort = new InsertionSort<>()) {
            sort.mutatingSort(ts);  // Sort the array in place (mutating the original array).
        }
    }
}
