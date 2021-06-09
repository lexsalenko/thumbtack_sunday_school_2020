package net.thumbtack.school.hospital.model;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

public class Doctor {

    private int id;
    private User user;
    private Speciality speciality;
    private Room room;
    private LocalDate dateStart;
    private LocalDate dateEnd;
    private List<DaySchedule> daySchedule;
    private int duration;

    public Doctor() {

    }

    public Doctor(int id, User user, Speciality speciality, Room room, LocalDate dateStart, LocalDate dateEnd, List<DaySchedule> daySchedule, int duration) {
        this.id = id;
        this.user = user;
        this.speciality = speciality;
        this.room = room;
        this.dateStart = dateStart;
        this.dateEnd = dateEnd;
        this.daySchedule = daySchedule;
        this.duration = duration;
    }

    public Doctor(User user, Speciality speciality, Room room, LocalDate dateStart, LocalDate dateEnd, List<DaySchedule> daySchedule, int duration) {
        this.id = 0;
        this.user = user;
        this.speciality = speciality;
        this.room = room;
        this.dateStart = dateStart;
        this.dateEnd = dateEnd;
        this.daySchedule = daySchedule;
        this.duration = duration;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Speciality getSpeciality() {
        return speciality;
    }

    public void setSpeciality(Speciality speciality) {
        this.speciality = speciality;
    }

    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
    }

    public LocalDate getDateStart() {
        return dateStart;
    }

    public void setDateStart(LocalDate dateStart) {
        this.dateStart = dateStart;
    }

    public LocalDate getDateEnd() {
        return dateEnd;
    }

    public void setDateEnd(LocalDate dateEnd) {
        this.dateEnd = dateEnd;
    }

    public List<DaySchedule> getDaySchedule() {
        return daySchedule;
    }

    public void setDaySchedule(List<DaySchedule> daySchedule) {
        this.daySchedule = daySchedule;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Doctor)) return false;
        Doctor doctor = (Doctor) o;
        return getId() == doctor.getId() &&
                getDuration() == doctor.getDuration() &&
                Objects.equals(getUser(), doctor.getUser()) &&
                Objects.equals(getSpeciality(), doctor.getSpeciality()) &&
                Objects.equals(getRoom(), doctor.getRoom()) &&
                Objects.equals(getDateStart(), doctor.getDateStart()) &&
                Objects.equals(getDateEnd(), doctor.getDateEnd()) &&
                Objects.equals(getDaySchedule(), doctor.getDaySchedule());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getUser(), getSpeciality(), getRoom(), getDateStart(), getDateEnd(), getDaySchedule(), getDuration());
    }

    @Override
    public String toString() {
        return "Doctor{" +
                "id=" + id +
                ", user=" + user +
                ", speciality=" + speciality +
                ", room=" + room +
                ", dateStart=" + dateStart +
                ", dateEnd=" + dateEnd +
                ", daySchedule=" + daySchedule +
                ", duration=" + duration +
                '}';
    }
}
