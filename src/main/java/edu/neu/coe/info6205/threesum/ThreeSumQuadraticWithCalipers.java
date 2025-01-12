package edu.neu.coe.info6205.threesum;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;
import java.util.function.Function;
import java.util.stream.Stream;
import java.util.Arrays;

/**
 * Implementation of ThreeSum which follows the approach of dividing the solution-space into
 * N sub-spaces where each subspace corresponds to a fixed value for the middle index of the three values.
 * Each subspace is then solved by expanding the scope of the other two indices outwards from the starting point.
 * Since each subspace can be solved in O(N) time, the overall complexity is O(N^2).
 * <p>
 * The array provided in the constructor MUST be ordered.
 */
public class ThreeSumQuadraticWithCalipers implements ThreeSum {
    /**
     * Construct ints ThreeSumQuadratic on ints.
     *
     * @param ints a sorted array.
     */
    public ThreeSumQuadraticWithCalipers(int[] ints) {
        this.a = ints;
        length = ints.length;
    }

    /**
     * Get an array or Triple containing all of those triples for which sum is zero.
     *
     * @return a Triple[].
     */
    public Triple[] getTriples() {
        List<Triple> triples = new ArrayList<>();
        for (int i = 0; i < length - 2; i++) {
            triples.addAll(calipers(a, i, Triple::sum));
        }
        return triples.stream().distinct().toArray(Triple[]::new);
    }

    /**
     * Get a set of candidate Triples such that the first index is the given value i.
     * Any candidate triple is added to the result if it yields zero when passed into function.
     *
     * @param a        a sorted array of ints. This method is concerned only with the partition of a starting with index i+1.
     * @param i        the index of the first element of resulting triples.
     * @param function a function which takes a triple and returns a value which will be compared with zero.
     * @return a List of Triples.
     */
    public static List<Triple> calipers(int[] a, int i, Function<Triple, Integer> function) {
        List<Triple> triples = new ArrayList<>();
        int left = i + 1;
        int right = a.length - 1;

        while (left < right) {
            Triple triple = new Triple(a[i], a[left], a[right]);
            int sum = function.apply(triple);

            if (sum == 0) {
                triples.add(triple);
                left++;
                right--;


                while (left < right && a[left] == a[left - 1]) left++;
                while (left < right && a[right] == a[right + 1]) right--;
            } else if (sum < 0) {
                left++;
            } else {
                right--;
            }
        }

        return triples;
    }

    private final int[] a;
    private final int length;

    public static void main(String[] args) {
        // Taking user input
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter numbers separated by space:");
        String input = scanner.nextLine();

        int[] userInputArray = Stream.of(input.split("\\s+")).mapToInt(Integer::parseInt).toArray();

        Arrays.sort(userInputArray);


        ThreeSumQuadraticWithCalipers threeSum = new ThreeSumQuadraticWithCalipers(userInputArray);


        Triple[] result = threeSum.getTriples();
        if (result.length > 0) {
            System.out.println("Unique triples that sum to zero:");
            for (Triple triple : result) {
                System.out.println(triple);
            }
        } else {
            System.out.println("No triples found that sum to zero.");
        }
    }
}
