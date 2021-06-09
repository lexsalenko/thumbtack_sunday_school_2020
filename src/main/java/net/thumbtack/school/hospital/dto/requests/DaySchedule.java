package net.thumbtack.school.hospital.dto.requests;

import javax.validation.constraints.NotNull;
import java.util.Objects;

public class DaySchedule {

    private String weekDay;
    private String timeStart;
    private String timeEnd;

    public DaySchedule(String weekDay, String timeStart, String timeEnd) {
        this.weekDay = weekDay;
        this.timeStart = timeStart;
        this.timeEnd = timeEnd;
    }

    public String getWeekDay() {
        return weekDay;
    }

    public void setWeekDay(String weekDay) {
        this.weekDay = weekDay;
    }

    public String getTimeStart() {
        return timeStart;
    }

    public void setTimeStart(String timeStart) {
        this.timeStart = timeStart;
    }

    public String getTimeEnd() {
        return timeEnd;
    }

    public void setTimeEnd(String timeEnd) {
        this.timeEnd = timeEnd;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DaySchedule)) return false;
        DaySchedule that = (DaySchedule) o;
        return Objects.equals(getWeekDay(), that.getWeekDay()) &&
                Objects.equals(getTimeStart(), that.getTimeStart()) &&
                Objects.equals(getTimeEnd(), that.getTimeEnd());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getWeekDay(), getTimeStart(), getTimeEnd());
    }


}
