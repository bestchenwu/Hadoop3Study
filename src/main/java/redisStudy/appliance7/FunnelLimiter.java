package redisStudy.appliance7;

public class FunnelLimiter {

    //总容量
    private int capacity;
    //漏水速度
    private float leakingRate;
    //剩余容量
    private int leftQuota;
    //当前泄露的时间(单位毫秒数)
    private long leakingTs;

    public FunnelLimiter(int capacity, float leakingRate) {
        this.capacity = capacity;
        this.leakingRate = leakingRate;
        this.leftQuota = capacity;
        this.leakingTs = System.currentTimeMillis();
    }

    public void makeSpace() {
        long currentTs = System.currentTimeMillis();
        float deltaQuota = (currentTs - leakingTs) * leakingRate;
        //时间太长,溢出了
        if (deltaQuota < 0) {
            leftQuota = capacity;
            leakingTs = currentTs;
            return;
        }
        if (deltaQuota < 1) {
            return;
        }
        this.leftQuota += deltaQuota;
        this.leakingTs = currentTs;
        if (this.leftQuota > capacity) {
            this.leftQuota = capacity;
        }

    }

    public boolean watering(int quota) {
        makeSpace();
        if (this.leftQuota > quota) {
            this.leftQuota -= quota;
            return true;
        } else {
            return false;
        }
    }
}
