package org.simon.autoet.track;

import java.util.List;

/**
 * 定义挑战
 * @author simon
 * @since 2017/10/28 12:43
 * @version V1.0
 */
public class Challenge {
    private String name;
    private List<Schedule> schedules;

    public Challenge(String name, List<Schedule> schedules) {
        this.name = name;

        this.schedules = schedules;
    }

    public String getName() {
        return name;
    }

    public List<Schedule> getSchedules() {
        return schedules;
    }
}
