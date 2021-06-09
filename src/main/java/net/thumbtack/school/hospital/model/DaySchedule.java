package net.thumbtack.school.hospital.model;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Objects;

public class DaySchedule {

    private int id;
    private LocalDate dateDay;
    private List<Appointment> appointmentList;

    public DaySchedule() {
    }

    public DaySchedule(int id, LocalDate dateDay, List<Appointment> appointmentList) {
        this.id = id;
        this.dateDay = dateDay;
        this.appointmentList = appointmentList;
    }

    public DaySchedule(LocalDate dateDay, List<Appointment> appointmentList) {
        this.id = 0;
        this.dateDay = dateDay;
        this.appointmentList = appointmentList;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public LocalDate getDateDay() {
        return dateDay;
    }

    public void setDateDay(LocalDate dateDay) {
        this.dateDay = dateDay;
    }

    public List<Appointment> getAppointmentList() {
        return appointmentList;
    }

    public void setAppointmentList(List<Appointment> appointmentList) {
        this.appointmentList = appointmentList;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DaySchedule)) return false;
        DaySchedule that = (DaySchedule) o;
        return getId() == that.getId() &&
                Objects.equals(getDateDay(), that.getDateDay()) &&
                Objects.equals(getAppointmentList(), that.getAppointmentList());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getDateDay(), getAppointmentList());
    }

    @Override
    public String toString() {
        return "DaySchedule{" +
                "id=" + id +
                ", dateDay=" + dateDay +
                ", appointmentList=" + appointmentList +
                '}';
    }
}
