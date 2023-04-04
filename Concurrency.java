import java.util.Random;

public class Concurrency {
    private static final int NUM_THREADS = 4;
    private static final int ARRAY_SIZE = 200000000;

    private static int[] array = new int[ARRAY_SIZE];

    public static void main(String[] args) {
        Random random = new Random();
        long startTime, endTime;


        for (int i = 0; i < ARRAY_SIZE; i++) {
            array[i] = random.nextInt(10) + 1;
        }


        int sum = 0;
        startTime = System.currentTimeMillis();
        SumThread[] threads = new SumThread[NUM_THREADS];
        for (int i = 0; i < NUM_THREADS; i++) {
            threads[i] = new SumThread(i * (ARRAY_SIZE / NUM_THREADS), (i + 1) * (ARRAY_SIZE / NUM_THREADS));
            threads[i].start();
        }
        try {
            for (int i = 0; i < NUM_THREADS; i++) {
                threads[i].join();
                sum += threads[i].getSum();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        endTime = System.currentTimeMillis();
        System.out.println("Parallel sum: " + sum + ", time taken: " + (endTime - startTime) + "ms");

        // Compute sum with only one thread
        sum = 0;
        startTime = System.currentTimeMillis();
        for (int i = 0; i < ARRAY_SIZE; i++) {
            sum += array[i];
        }
        endTime = System.currentTimeMillis();
        System.out.println("Single-threaded sum: " + sum + ", time taken: " + (endTime - startTime) + "ms");
    }

    private static class SumThread extends Thread {
        private int start, end, sum;

        public SumThread(int start, int end) {
            this.start = start;
            this.end = end;
        }

        public void run() {
            for (int i = start; i < end; i++) {
                sum += array[i];
            }
        }

        public int getSum() {
            return sum;
        }
    }
}