package net.thumbtack.school.hospital.dto.responses;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

public class Schedule {

    private LocalDate date;
    private List<DaySchedule> daySchedule;

    public Schedule() {
    }

    public Schedule(LocalDate date, List<DaySchedule> daySchedule) {
        this.date = date;
        this.daySchedule = daySchedule;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public List<DaySchedule> getDaySchedule() {
        return daySchedule;
    }

    public void setDaySchedule(List<DaySchedule> daySchedule) {
        this.daySchedule = daySchedule;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Schedule)) return false;
        Schedule schedule = (Schedule) o;
        return Objects.equals(getDate(), schedule.getDate()) &&
                Objects.equals(getDaySchedule(), schedule.getDaySchedule());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getDate(), getDaySchedule());
    }

}
