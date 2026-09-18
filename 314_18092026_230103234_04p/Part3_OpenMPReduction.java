import java.util.concurrent.ThreadLocalRandom;

public class Part3_OpenMPReduction {

    public static void runBenchmark(int numThreads) throws InterruptedException {
        long totalPoints = 100_000_000L;
        long pointsPerThread = totalPoints / numThreads;
        long[] localHits = new long[numThreads];

        long start = System.currentTimeMillis();
        Thread[] threads = new Thread[numThreads];
        for (int i = 0; i < numThreads; i++) {
            final int threadId = i;
            threads[i] = new Thread(() -> {
                long hits = 0;
                for (long j = 0; j < pointsPerThread; j++) {
                    double x = ThreadLocalRandom.current().nextDouble();
                    double y = ThreadLocalRandom.current().nextDouble();
                    if (x * x + y * y <= 1.0) {
                        hits++;
                    }
                }
                localHits[threadId] = hits; // Private local accumulation
            });
        }

        for (Thread t : threads) t.start();
        for (Thread t : threads) t.join();

        long grandTotalHits = 0;
        for (long h : localHits) grandTotalHits += h;
        long elapsed = System.currentTimeMillis() - start;

        double pi = 4.0 * grandTotalHits / totalPoints;
        System.out.printf("Part 3 (T=%2d) - Time: %4d ms | Pi: %.5f\n", numThreads, elapsed, pi);
    }

    public static void main(String[] args) throws Exception {
        System.out.println("=== PART 3: Reduction Benchmark ===");
        int[] threadCounts = {1, 2, 4, 8, 16, 32};
        for (int t : threadCounts) {
            runBenchmark(t);
        }
    }
}