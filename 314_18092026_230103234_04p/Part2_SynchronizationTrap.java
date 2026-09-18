import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicLong;

public class Part2_SynchronizationTrap {
    static AtomicLong totalHitsAtomic = new AtomicLong(0);

    public static void main(String[] args) throws InterruptedException {
        System.out.println("=== PART 2: Synchronization Trap ===");
        
        long totalPoints = 50_000_000L;
        
        // Single Thread Baseline
        long hitsSingle = 0;
        long startSingle = System.currentTimeMillis();
        for (long j = 0; j < totalPoints; j++) {
            double x = ThreadLocalRandom.current().nextDouble();
            double y = ThreadLocalRandom.current().nextDouble();
            if (x * x + y * y <= 1.0) {
                hitsSingle++;
            }
        }
        long timeSingle = System.currentTimeMillis() - startSingle;
        double piSingle = 4.0 * hitsSingle / totalPoints;
        System.out.printf("Part 2 (Single Thread) - Time: %d ms | Pi: %.5f\n", timeSingle, piSingle);

        // Atomic 4 Threads (Locked / Synchronized)
        long pointsPerThread = totalPoints / 4;
        long startAtomic = System.currentTimeMillis();
        Thread[] threads = new Thread[4];
        for (int i = 0; i < 4; i++) {
            threads[i] = new Thread(() -> {
                for (long j = 0; j < pointsPerThread; j++) {
                    double x = ThreadLocalRandom.current().nextDouble();
                    double y = ThreadLocalRandom.current().nextDouble();
                    if (x * x + y * y <= 1.0) {
                        totalHitsAtomic.incrementAndGet(); // Bus lock contention
                    }
                }
            });
        }

        for (Thread t : threads) t.start();
        for (Thread t : threads) t.join();
        long timeAtomic = System.currentTimeMillis() - startAtomic;
        double piAtomic = 4.0 * totalHitsAtomic.get() / totalPoints;
        System.out.printf("Part 2 (Atomic 4 threads) - Time: %d ms | Pi: %.5f\n", timeAtomic, piAtomic);
    }
}