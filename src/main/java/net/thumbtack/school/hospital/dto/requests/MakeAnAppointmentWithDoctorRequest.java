package net.thumbtack.school.hospital.dto.requests;

import javax.validation.constraints.NotNull;

public class MakeAnAppointmentWithDoctorRequest {


    private int doctorId;

    private String speciality;

    @NotNull
    private String date;

    @NotNull
    private String time;

    public MakeAnAppointmentWithDoctorRequest() {
    }

    public MakeAnAppointmentWithDoctorRequest(int doctorId, String speciality, String date, String time) {
        this.doctorId = doctorId;
        this.speciality = speciality;
        this.date = date;
        this.time = time;
    }

    public int getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(int doctorId) {
        this.doctorId = doctorId;
    }

    public String getSpeciality() {
        return speciality;
    }

    public void setSpeciality(String speciality) {
        this.speciality = speciality;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
