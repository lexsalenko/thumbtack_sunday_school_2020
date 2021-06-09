package net.thumbtack.school.hospital.dto.requests;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;

public class InformationAboutDoctorsDtoRequest {

    @NotNull
    private String cookie;

    @NotNull
    private String speciality;

    private boolean schedule;

    private LocalDate startDate;

    private LocalDate endDate;

    public InformationAboutDoctorsDtoRequest(String cookie, String speciality, boolean schedule, LocalDate startDate, LocalDate endDate) {
        this.cookie = cookie;
        this.speciality = speciality;
        this.schedule = schedule;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public String getCookie() {
        return cookie;
    }

    public void setCookie(String cookie) {
        this.cookie = cookie;
    }

    public String getSpeciality() {
        return speciality;
    }

    public void setSpeciality(String speciality) {
        this.speciality = speciality;
    }

    public boolean isSchedule() {
        return schedule;
    }

    public void setSchedule(boolean schedule) {
        this.schedule = schedule;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }
}
