package redisStudy.appliance7;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class FunnelLimiterTest {

    private FunnelLimiter funnelLimiter;

    @Before
    public void init() {
        this.funnelLimiter = new FunnelLimiter(50, 2.0f);
    }

    @Test
    public void testFunnelLimiter() {
        for (int i = 0; i < 50; i++) {
            boolean canWatering
                    = funnelLimiter.watering(20);
            if (canWatering) {
                System.out.println(i + " can water");
            } else {
                System.out.println(i + " can not water");
            }
            if (i % 3 == 0) {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    @After
    public void close() {

    }
}
