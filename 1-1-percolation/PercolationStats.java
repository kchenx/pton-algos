/******************************************************************************
 *  Compilation:  javac PercolationStats.java
 *  Execution:    java PercolationStats n T
 *  Dependencies: Percolation.java StdRandom.java StdStats.java
 *
 *  This program takes two integers `n` and `T` as arguments.
 *  Then, it performs `T` Monte Carlo simulations as such:
 *
 *    - Creates an `n`-by-`n` grid of sites (intially all blocked)
 *    - Choose a site uniformly at random among all blocked sites and open
 *      the site until the system percolates
 *    - Calculate the percolation threshold, the fraction of sites that are
 *      open when the system percolates
 *
 *  Then the program will output the mean, standard deviation, and 95% confidence
 *  interval of the percolation threshold across the `T` simulations.
 *
 ******************************************************************************/

import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {

    private static final double CONFIDENCE_95 = 1.96;

    private final int ntrials;      // number of trials
    private final double[] results; // list of percolation thresholds
    private double mean = -1;
    private double stddev = -1;

    /**
     * Performs `trials` independent trials on an n-by-n percolation system. Adds the percolation
     * threshold of each trial to `results`
     *
     * @param n      dimension of grid
     * @param trials number of trials to perform
     */
    public PercolationStats(int n, int trials) {
        if (n <= 0 || trials <= 0) {
            throw new IllegalArgumentException("n must be positive");
        }

        results = new double[trials];
        ntrials = trials;

        for (int i = 0; i < trials; i++) {
            Percolation perc = new Percolation(n);
            while (!perc.percolates()) {
                int row = StdRandom.uniform(1, n + 1);
                int col = StdRandom.uniform(1, n + 1);
                perc.open(row, col);
            }
            results[i] = ((double) perc.numberOfOpenSites()) / (n * n);
        }
    }

    /**
     * Calculates sample mean of percolation threshold
     *
     * @return sample mean of percolation threshold
     */
    public double mean() {
        if (mean == -1) {
            mean = StdStats.mean(results);
        }
        return mean;
    }

    /**
     * Calculates sample standard deviation of percolation threshold
     *
     * @return sample standard deviation of percolation threshold
     */
    public double stddev() {
        if (ntrials == 1) {
            return Double.NaN;
        }
        if (stddev == -1) {
            stddev = StdStats.stddev(results);
        }
        return stddev;
    }

    /**
     * Calculates low endpoint of 95% confidence interval of percolation threshold
     *
     * @return low endpoint of 95% confidence interval
     */
    public double confidenceLo() {
        return mean() - CONFIDENCE_95 * stddev() / Math.sqrt(ntrials);
    }

    /**
     * Calculates high endpoint of 95% confidence interval of percolation threshold
     *
     * @return high endpoint of 95% confidence interval
     */
    public double confidenceHi() {
        return mean() + CONFIDENCE_95 * stddev() / Math.sqrt(ntrials);
    }

    // test client
    public static void main(String[] args) {
        if (args.length != 2) {
            System.out.println("Usage: PercolationStats gridsize ntrials");
            return;
        }

        int arg0 = Integer.parseInt(args[0]);
        int arg1 = Integer.parseInt(args[1]);
        PercolationStats p = new PercolationStats(arg0, arg1);

        System.out.println("mean                    = " + p.mean());
        System.out.println("stddev                  = " + p.stddev());
        System.out.printf("95%% confidence interval = [%f, %f]\n", p.confidenceLo(),
                          p.confidenceHi());
    }

}
