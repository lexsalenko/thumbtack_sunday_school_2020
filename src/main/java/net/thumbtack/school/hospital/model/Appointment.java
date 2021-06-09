package net.thumbtack.school.hospital.model;

import java.time.LocalTime;
import java.util.Objects;

public class Appointment {

    private int id;
    private Patient patient;
    private String ticket;
    private LocalTime time;

    public Appointment() {
    }

    public Appointment(int id, Patient patient, String ticket, LocalTime time) {
        this.id = id;
        this.patient = patient;
        this.ticket = ticket;
        this.time = time;
    }

    public Appointment(Patient patient, String ticket, LocalTime time) {
        this.id = 0;
        this.patient = patient;
        this.ticket = ticket;
        this.time = time;
    }

    public Appointment(LocalTime time) {
        this.id = 0;
        this.patient = null;
        this.ticket = null;
        this.time = time;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public String getTicket() {
        return ticket;
    }

    public void setTicket(String ticket) {
        this.ticket = ticket;
    }

    public LocalTime getTime() {
        return time;
    }

    public void setTime(LocalTime time) {
        this.time = time;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Appointment)) return false;
        Appointment that = (Appointment) o;
        return getId() == that.getId() &&
                Objects.equals(getPatient(), that.getPatient()) &&
                Objects.equals(getTicket(), that.getTicket()) &&
                Objects.equals(getTime(), that.getTime());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getPatient(), getTicket(), getTime());
    }

}
