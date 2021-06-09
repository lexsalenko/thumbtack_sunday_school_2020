package net.thumbtack.school.hospital.model;

import java.util.Objects;

public class Patient {

    private int id;
    private User user;
    private String email;
    private String address;
    private String phone;

    public Patient() {
    }

    public Patient(int id) {
        this.id = id;
    }

    public Patient(int id, User user, String email, String address, String phone) {
        this.id = id;
        this.user = user;
        this.email = email;
        this.address = address;
        this.phone = phone;
    }

    public Patient(User user, String email, String address, String phone) {
        this.id = 0;
        this.user = user;
        this.email = email;
        this.address = address;
        this.phone = phone;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Patient)) return false;
        Patient patient = (Patient) o;
        return getId() == patient.getId() &&
                Objects.equals(getUser(), patient.getUser()) &&
                Objects.equals(getEmail(), patient.getEmail()) &&
                Objects.equals(getAddress(), patient.getAddress()) &&
                Objects.equals(getPhone(), patient.getPhone());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getUser(), getEmail(), getAddress(), getPhone());
    }
}
