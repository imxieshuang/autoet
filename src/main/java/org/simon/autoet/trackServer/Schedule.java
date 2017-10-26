package org.simon.autoet.trackServer;

/**
 * Created by Administrator on 2017/10/26.
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
