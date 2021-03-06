package net.thumbtack.school.hospital.model;

import java.util.Objects;

public class Speciality {

    private int id;
    private String speciality;

    public Speciality(int id, String speciality) {
        this.id = id;
        this.speciality = speciality;
    }

    public Speciality(String speciality) {
        this.id = 0;
        this.speciality = speciality;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSpeciality() {
        return speciality;
    }

    public void setSpeciality(String speciality) {
        this.speciality = speciality;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Speciality)) return false;
        Speciality that = (Speciality) o;
        return getId() == that.getId() &&
                Objects.equals(getSpeciality(), that.getSpeciality());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getSpeciality());
    }

}
