package net.thumbtack.school.hospital.dto.requests;

import java.util.List;
import java.util.Objects;

public class WeekSchedule {

    private String timeStart;
    private String timeEnd;
    private List<String> weekDays;

    public WeekSchedule() {
    }

    public WeekSchedule(String timeStart, String timeEnd, List<String> weekDays) {
        this.timeStart = timeStart;
        this.timeEnd = timeEnd;
        this.weekDays = weekDays;
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

    public List<String> getWeekDays() {
        return weekDays;
    }

    public void setWeekDays(List<String> weekDays) {
        this.weekDays = weekDays;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof WeekSchedule)) return false;
        WeekSchedule that = (WeekSchedule) o;
        return Objects.equals(getTimeStart(), that.getTimeStart()) &&
                Objects.equals(getTimeEnd(), that.getTimeEnd()) &&
                Objects.equals(getWeekDays(), that.getWeekDays());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getTimeStart(), getTimeEnd(), getWeekDays());
    }


}
