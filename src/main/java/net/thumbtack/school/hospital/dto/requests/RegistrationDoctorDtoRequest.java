package net.thumbtack.school.hospital.dto.requests;


import javax.validation.constraints.NotNull;
import java.util.List;

public class RegistrationDoctorDtoRequest {

    @NotNull
    private String firstName;

    @NotNull
    private String lastName;

    private String patronymic;

    @NotNull
    private String speciality;

    @NotNull
    private String room;

    @NotNull
    private String login;

    @NotNull
    private String password;

    @NotNull
    private String dateStart;

    @NotNull
    private String dateEnd;

    private WeekSchedule weekSchedule;

    private List<DaySchedule> weekDaysSchedule;

    @NotNull
    private int duration;

    public RegistrationDoctorDtoRequest() {
    }

    public RegistrationDoctorDtoRequest(String firstName, String lastName, String patronymic, String speciality, String room, String login, String password, String dateStart, String dateEnd, WeekSchedule weekSchedule, List<DaySchedule> weekDaysSchedule, int duration) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.patronymic = patronymic;
        this.speciality = speciality;
        this.room = room;
        this.login = login;
        this.password = password;
        this.dateStart = dateStart;
        this.dateEnd = dateEnd;
        this.weekSchedule = weekSchedule;
        this.weekDaysSchedule = weekDaysSchedule;
        this.duration = duration;
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

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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
