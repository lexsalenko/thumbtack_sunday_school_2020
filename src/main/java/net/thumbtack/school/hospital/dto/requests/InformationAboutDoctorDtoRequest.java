package net.thumbtack.school.hospital.dto.requests;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;

public class InformationAboutDoctorDtoRequest {

    @NotNull
    private int doctorId;

    @NotNull
    private String cookie;

    private boolean schedule;

    private LocalDate startDate;

    private LocalDate endDate;

    public InformationAboutDoctorDtoRequest(int doctorId, String cookie, boolean schedule, LocalDate startDate, LocalDate endDate) {
        this.doctorId = doctorId;
        this.cookie = cookie;
        this.schedule = schedule;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public int getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(int doctorId) {
        this.doctorId = doctorId;
    }

    public String getCookie() {
        return cookie;
    }

    public void setCookie(String cookie) {
        this.cookie = cookie;
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
