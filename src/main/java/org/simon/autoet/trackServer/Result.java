package org.simon.autoet.trackServer;

/**
 * Created by Administrator on 2017/10/26.
 */
public class Result {
    private long took;
    private long success;
    private long total;
    private long error;
    private double throughout;
    private double errorRate;

    public Result(long took, long success, long total, long error, double throughout, double errorRate) {
        this.took = took;
        this.success = success;
        this.total = total;
        this.error = error;
        this.throughout = throughout;
        this.errorRate = errorRate;
    }

    public long getTook() {
        return took;
    }

    public long getSuccess() {
        return success;
    }

    public long getTotal() {
        return total;
    }

    public long getError() {
        return error;
    }

    public double getThroughout() {
        return throughout;
    }

    public double getErrorRate() {
        return errorRate;
    }
}
