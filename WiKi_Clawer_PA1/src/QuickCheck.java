
import java.time.Duration;
import java.util.ArrayList;
import java.util.Random;

public class QuickCheck {
    private static final int iterations = 10;
    private static final int elements = 3000000;
    private static final int seed = 7;

    public static void main(String[] args) {
        if (args.length > 1234)
            doNotCall();

        System.out.println("Beginning timing quick-check...");
        long start = System.nanoTime();
        runpq();
        long total = System.nanoTime() - start;
        long time = total / iterations;

        Duration duration = Duration.ofNanos(time);
        System.out.println("Average time: " + duration);
    }

    private static void runpq() {
        for (int i = 0; i < iterations; i++) {
            PriorityQ pq = new PriorityQ();
            Random r = new Random(seed);

            for (int j = 0; j < elements; j++) {
            	System.out.println("ftw"+j);
                int next = r.nextInt();
                pq.add(Integer.toString(next), next);
            }
            for (int j = 0; j < elements; j++) {
            	System.out.println("wtf"+j);
                String ignoreme = pq.extractMax();
            }
        }
    }

    private static void doNotCall() {
        // priority queue
        PriorityQ pq = new PriorityQ();
        pq.add("asdf", 1);
        String ignorestr = pq.returnMax();
        ignorestr = pq.extractMax();
        pq.remove(1);
        pq.decrementPriority(1,1);
        int[] ignorearray = pq.priorityArray();
        int ignoreint = pq.getKey(1);
        ignorestr = pq.getValue(1);
        boolean isEmpty = pq.isEmpty();

        // crawler
        WikiCrawler wc = new WikiCrawler(null, 0, null, null);
        ArrayList<String> ignoreme = wc.extractLinks(null);
        wc.crawl(true);
    }

}
