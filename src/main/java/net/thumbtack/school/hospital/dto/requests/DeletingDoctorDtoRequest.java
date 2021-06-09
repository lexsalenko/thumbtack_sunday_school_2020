package net.thumbtack.school.hospital.dto.requests;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;

public class DeletingDoctorDtoRequest {

    @NotNull
    private String date;

    public DeletingDoctorDtoRequest(@NotNull String date) {
        this.date = date;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

}
