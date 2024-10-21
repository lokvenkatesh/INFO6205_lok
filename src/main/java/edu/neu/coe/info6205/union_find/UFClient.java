import edu.neu.coe.info6205.union_find.UF_HWQUPC;
import java.util.Random;

public class UFClient {

    /**
     * Performs the union-find experiment for n sites.
     * Generates random pairs and performs union operations until all sites are connected.
     *
     * @param n the number of sites
     * @return the number of connections needed to connect all sites
     */
    public static int count(int n) {
        UF_HWQUPC uf = new UF_HWQUPC(n);
        Random random = new Random();
        int connections = 0;

        // Keep connecting sites until all components are reduced to 1
        while (uf.components() > 1) {
            int p = random.nextInt(n); // Generate a random site between 0 and n-1
            int q = random.nextInt(n); // Generate another random site between 0 and n-1

            // Check if they are already connected
            if (!uf.connected(p, q)) {
                uf.union(p, q); // Union them if they are not connected
                connections++;   // Increment the number of connections made
            }
        }

        return connections;
    }

    /**
     * Main method to run the experiment.
     *
     * @param args Command-line arguments
     */
    public static void main(String[] args) {
        // Get n from command-line argument or run with default values
        if (args.length > 0) {
            int n = Integer.parseInt(args[0]); // Take n as input from the command line
            int connections = count(n); // Call count to run the experiment
            System.out.println("Number of connections made for " + n + " sites: " + connections);
        } else {
            // Run for a fixed set of n values if no command-line arguments are given
            int[] ns = {10, 100, 1000, 10000};
            for (int n : ns) {
                int connections = count(n);
                System.out.println("Number of connections made for " + n + " sites: " + connections);
            }
        }
    }
}
