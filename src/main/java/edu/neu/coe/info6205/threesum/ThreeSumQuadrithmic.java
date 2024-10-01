package edu.neu.coe.info6205.threesum;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Implementation of ThreeSum which follows the simple optimization of
 * requiring a sorted array, then using binary search to find an element x where
 * -x is the sum of a pair of elements.
 * <p>
 * The array provided in the constructor MUST be ordered.
 * <p>
 * This algorithm runs in O(N^2 log N) time.
 */
class ThreeSumQuadrithmic implements ThreeSum {
    /**
     * Construct a ThreeSumQuadrithmic on a.
     *
     * @param a a sorted array.
     */
    public ThreeSumQuadrithmic(int[] a) {
        this.a = a;
        length = a.length;
    }

    public Triple[] getTriples() {
        Set<Triple> triples = new HashSet<>();
        for (int i = 0; i < length; i++) {
            for (int j = i + 1; j < length; j++) {
                Triple triple = getTriple(i, j);
                if (triple != null) triples.add(triple);  // Add directly to the set to avoid duplicates
            }
        }
        return triples.toArray(new Triple[0]);
    }

    public Triple getTriple(int i, int j) {
        int target = -a[i] - a[j];
        int index = Arrays.binarySearch(a, target);
        if (index >= 0 && index > j) {  // Ensure index is valid and follows j to avoid repeats
            return new Triple(a[i], a[j], a[index]);
        }
        return null;
    }

    private final int[] a;
    private final int length;
}
