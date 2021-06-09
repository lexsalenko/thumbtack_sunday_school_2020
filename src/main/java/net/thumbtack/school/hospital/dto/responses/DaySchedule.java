package net.thumbtack.school.hospital.dto.responses;

import java.time.LocalTime;
import java.util.Objects;

public class DaySchedule {

    private LocalTime time;
    private Patient patient;

    public DaySchedule() {
    }

    public DaySchedule(LocalTime time, Patient patient) {
        this.time = time;
        this.patient = patient;
    }

    public LocalTime getTime() {
        return time;
    }

    public void setTime(LocalTime time) {
        this.time = time;
    }

    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DaySchedule)) return false;
        DaySchedule that = (DaySchedule) o;
        return Objects.equals(getTime(), that.getTime()) &&
                Objects.equals(getPatient(), that.getPatient());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getTime(), getPatient());
    }

}
