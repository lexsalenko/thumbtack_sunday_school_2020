package net.thumbtack.school.hospital.dto.requests;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;

public class ChangingDoctorScheduleDtoRequest {

    @NotNull
    private String dateStart;

    @NotNull
    private String dateEnd;

    private WeekSchedule weekSchedule;

    private List<DaySchedule> weekDaysSchedule;

    @NotNull
    private int duration;

    public ChangingDoctorScheduleDtoRequest() {
    }

    public ChangingDoctorScheduleDtoRequest(String dateStart, String dateEnd, WeekSchedule weekSchedule, List<DaySchedule> weekDaysSchedule, int duration) {
        this.dateStart = dateStart;
        this.dateEnd = dateEnd;
        this.weekSchedule = weekSchedule;
        this.weekDaysSchedule = weekDaysSchedule;
        this.duration = duration;
    }

    public String getDateStart() {
        return dateStart;
    }

    public void setDateStart(String dateStart) {
        this.dateStart = dateStart;
    }

    public String getDateEnd() {
        return dateEnd;
    }

    public void setDateEnd(String dateEnd) {
        this.dateEnd = dateEnd;
    }

    public WeekSchedule getWeekSchedule() {
        return weekSchedule;
    }

    public void setWeekSchedule(WeekSchedule weekSchedule) {
        this.weekSchedule = weekSchedule;
    }

    public List<DaySchedule> getWeekDaysSchedule() {
        return weekDaysSchedule;
    }

    public void setWeekDaysSchedule(List<DaySchedule> weekDaysSchedule) {
        this.weekDaysSchedule = weekDaysSchedule;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }
}
