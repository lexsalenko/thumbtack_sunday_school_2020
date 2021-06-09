package net.thumbtack.school.hospital.dto.requests;

import java.util.List;
import java.util.Objects;

public class AppointmentMedicalCommissionDtoRequest {

    private int patientId;
    private List<Integer> doctorIds;
    private String room;
    private String date;
    private String time;
    private int duration;

    public AppointmentMedicalCommissionDtoRequest() {
    }

    public AppointmentMedicalCommissionDtoRequest(int patientId, List<Integer> doctorIds, String room, String date, String time, int duration) {
        this.patientId = patientId;
        this.doctorIds = doctorIds;
        this.room = room;
        this.date = date;
        this.time = time;
        this.duration = duration;
    }

    public int getPatientId() {
        return patientId;
    }

    public void setPatientId(int patientId) {
        this.patientId = patientId;
    }

    public List<Integer> getDoctorIds() {
        return doctorIds;
    }

    public void setDoctorIds(List<Integer> doctorIds) {
        this.doctorIds = doctorIds;
    }

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
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

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AppointmentMedicalCommissionDtoRequest)) return false;
        AppointmentMedicalCommissionDtoRequest that = (AppointmentMedicalCommissionDtoRequest) o;
        return getPatientId() == that.getPatientId() &&
                Objects.equals(getDoctorIds(), that.getDoctorIds()) &&
                Objects.equals(getRoom(), that.getRoom()) &&
                Objects.equals(getDate(), that.getDate()) &&
                Objects.equals(getTime(), that.getTime()) &&
                Objects.equals(getDuration(), that.getDuration());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getPatientId(), getDoctorIds(), getRoom(), getDate(), getTime(), getDuration());
    }

}
