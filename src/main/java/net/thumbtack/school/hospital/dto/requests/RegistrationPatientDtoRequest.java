package net.thumbtack.school.hospital.dto.requests;

import javax.validation.constraints.NotNull;

public class RegistrationPatientDtoRequest {

    @NotNull
    private String firstName;

    @NotNull
    private String lastName;

    private String patronymic;

    @NotNull
    private String email;

    @NotNull
    private String address;

    @NotNull
    private String phone; // validator

    @NotNull
    private String login;

    @NotNull
    private String password;

    public RegistrationPatientDtoRequest() {
    }

    public RegistrationPatientDtoRequest(String firstName, String lastName, String patronymic, String email, String address, String phone, String login, String password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.patronymic = patronymic;
        this.email = email;
        this.address = address;
        this.phone = phone;
        this.login = login;
        this.password = password;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
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

}
