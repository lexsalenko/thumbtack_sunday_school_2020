package net.thumbtack.school.hospital.dto.responses;

import java.util.List;
import java.util.Objects;

public class RegistrationDoctorDtoResponse extends CommonResponse {

    private int id;
    private String firstName;
    private String lastName;
    private String patronymic;
    private String speciality;
    private String room;
    private List<Schedule> schedule;

    public RegistrationDoctorDtoResponse() {
    }

    public RegistrationDoctorDtoResponse(int id, String firstName, String lastName, String patronymic, String speciality, String room, List<Schedule> schedule) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.patronymic = patronymic;
        this.speciality = speciality;
        this.room = room;
        this.schedule = schedule;
    }

    public RegistrationDoctorDtoResponse(String firstName, String lastName, String patronymic, String speciality, String room, List<Schedule> schedule) {
        this.id = 0;
        this.firstName = firstName;
        this.lastName = lastName;
        this.patronymic = patronymic;
        this.speciality = speciality;
        this.room = room;
        this.schedule = schedule;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPatronymic() {
        return patronymic;
    }

    public void setPatronymic(String patronymic) {
        this.patronymic = patronymic;
    }

    public String getSpeciality() {
        return speciality;
    }

    public void setSpeciality(String speciality) {
        this.speciality = speciality;
    }

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public List<Schedule> getSchedule() {
        return schedule;
    }

    public void setSchedule(List<Schedule> schedule) {
        this.schedule = schedule;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RegistrationDoctorDtoResponse)) return false;
        RegistrationDoctorDtoResponse response = (RegistrationDoctorDtoResponse) o;
        return getId() == response.getId() &&
                Objects.equals(getFirstName(), response.getFirstName()) &&
                Objects.equals(getLastName(), response.getLastName()) &&
                Objects.equals(getPatronymic(), response.getPatronymic()) &&
                Objects.equals(getSpeciality(), response.getSpeciality()) &&
                Objects.equals(getRoom(), response.getRoom()) &&
                Objects.equals(getSchedule(), response.getSchedule());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getFirstName(), getLastName(), getPatronymic(), getSpeciality(), getRoom(), getSchedule());
    }

    @Override
    public String toString() {
        return "RegistrationDoctorDtoResponse{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", patronymic='" + patronymic + '\'' +
                ", speciality='" + speciality + '\'' +
                ", room='" + room + '\'' +
                ", schedule=" + schedule +
                '}';
    }
}
