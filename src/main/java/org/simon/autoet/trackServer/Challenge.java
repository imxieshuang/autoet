package org.simon.autoet.trackServer;

import java.util.ArrayList;

/**
 * 定义挑战
 * @author simon
 * @since 2017/10/28 12:43
 * @version V1.0
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
