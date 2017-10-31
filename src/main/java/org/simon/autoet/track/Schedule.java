package org.simon.autoet.track;

/**
 * 测试项的集合
 * @author simon
 * @since 2017/10/28 12:45
 * @version V1.0
 */
public class Schedule {
    private int clients;
    private int iterations;
    private String operation;

    public Schedule(int clients, int iterations, String operation) {
        this.clients = clients;
        this.iterations = iterations;
        this.operation = operation;
    }

    public int getClients() {
        return clients;
    }

    public int getIterations() {
        return iterations;
    }

    public String getOperation() {
        return operation;
    }
}
