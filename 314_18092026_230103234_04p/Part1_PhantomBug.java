import java.util.concurrent.ThreadLocalRandom;

public class Part1_PhantomBug {
    static long totalHits = 0;

    public static void main(String[] args) throws InterruptedException {
        System.out.println("=== PART 1: 5 Runs ===");
        for (int run = 1; run <= 5; run++) {
            totalHits = 0;
            long totalPoints = 50_000_000L;
            long pointsPerThread = totalPoints / 4;

            Thread[] threads = new Thread[4];
            for (int i = 0; i < 4; i++) {
                threads[i] = new Thread(() -> {
                    for (long j = 0; j < pointsPerThread; j++) {
                        double x = ThreadLocalRandom.current().nextDouble();
                        double y = ThreadLocalRandom.current().nextDouble();
                        if (x * x + y * y <= 1.0) {
                            totalHits++; // Data Race Condition
                        }
                    }
                });
            }

            for (Thread t : threads) t.start();
            for (Thread t : threads) t.join();

            double pi = 4.0 * totalHits / totalPoints;
            System.out.printf("Run %d: Part 1 Pi Approx: %.5f (Hits: %d)\n", run, pi, totalHits);
        }
    }
}