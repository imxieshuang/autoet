package org.simon.autoet.trackServer;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/10/25.
 */
public class Challenge {
    private String name;
    private ArrayList<Schedule> schedules;

    public Challenge(String name, ArrayList<Schedule> schedules) {
        this.name = name;

        this.schedules = schedules;
    }

    public String getName() {
        return name;
    }

    public ArrayList<Schedule> getSchedules() {
        return schedules;
    }
}
