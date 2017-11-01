package org.simon.autoet.track;

import java.util.List;

/**
 * 定义挑战
 *
 * @author simon
 * @version V1.0
 * @since 2017/10/28 12:43
 */
public class Challenge {
    private String name;
    private List<Schedule> schedules;
    private Boolean autoManaged;

    public Challenge(String name, List<Schedule> schedules, Boolean autoManaged) {
        this.name = name;
        this.schedules = schedules;
        this.autoManaged=autoManaged;
    }

    public Boolean getAutoManaged() {
        return autoManaged;
    }

    public String getName() {
        return name;
    }

    public List<Schedule> getSchedules() {
        return schedules;
    }
}
