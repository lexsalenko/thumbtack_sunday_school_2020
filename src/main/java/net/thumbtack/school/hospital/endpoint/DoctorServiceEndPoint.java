package net.thumbtack.school.hospital.endpoint;

import net.thumbtack.school.hospital.dao.DoctorDao;
import net.thumbtack.school.hospital.dto.requests.*;
import net.thumbtack.school.hospital.dto.responses.RegistrationAdministratorDtoResponse;
import net.thumbtack.school.hospital.dto.responses.RegistrationDoctorDtoResponse;
import net.thumbtack.school.hospital.exception.ServerException;
import net.thumbtack.school.hospital.service.DoctorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.QueryParam;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api")
public class DoctorServiceEndPoint {

    private final DoctorService doctorService;

    @Autowired
    public DoctorServiceEndPoint(DoctorService doctorService) {
        this.doctorService = doctorService;
    }


    @PostMapping(value = "/doctors/registerDoctor", produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RegistrationDoctorDtoResponse> registerDoctor (
            @RequestBody RegistrationDoctorDtoRequest doctorDtoRequest, HttpServletResponse response, HttpServletRequest request) throws ServerException {

        Cookie cookies[] = request.getCookies();

        RegistrationDoctorDtoResponse registrationDoctorDtoResponse =
                doctorService.registerDoctor(doctorDtoRequest, cookies);


        return new ResponseEntity<>(registrationDoctorDtoResponse, HttpStatus.OK);
    }

    @GetMapping(value = "/doctors/getInfoAboutDoctor/{doctorId}", produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RegistrationDoctorDtoResponse> getInfoAboutDoctor(
            @PathVariable("doctorId") int doctorId,
            @QueryParam("schedule") String schedule,
            @QueryParam("startDate") String startDate,
            @QueryParam("endDate") String endDate,
            HttpServletRequest request) {

        Cookie cookies[] = request.getCookies();

        RegistrationDoctorDtoResponse response =
                doctorService.getInformationAboutDoctor(doctorId, schedule, startDate, endDate, cookies);


        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping(value = "/doctors/getInfoAboutDoctors", produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<RegistrationDoctorDtoResponse>> getInfoAboutDoctors(
            @QueryParam("schedule") String schedule,
            @QueryParam("speciality") String speciality,
            @QueryParam("startDate") String startDate,
            @QueryParam("endDate") String endDate,
            HttpServletRequest request) {

        Cookie cookies[] = request.getCookies();

        List<RegistrationDoctorDtoResponse> response =
                doctorService.getInformationAboutDoctors(schedule, speciality, startDate, endDate, cookies);


        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PutMapping(value = "/doctors/changeDoctorSchedule/{doctorId}", produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RegistrationDoctorDtoResponse> changeDoctorSchedule(
            @PathVariable("doctorId") int doctorId,
            @RequestBody ChangingDoctorScheduleDtoRequest changingDoctorScheduleDtoRequest,
            HttpServletRequest request) {

        Cookie cookies[] = request.getCookies();


        RegistrationDoctorDtoResponse registrationDoctorDtoResponse =
                doctorService.changeDoctorSchedule(changingDoctorScheduleDtoRequest, doctorId, cookies);


        return new ResponseEntity<>(registrationDoctorDtoResponse, HttpStatus.OK);
    }

    @PostMapping(value = "/doctors/deletingDoctor/{doctorId}", produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RegistrationDoctorDtoResponse> deletingDoctor(
            @QueryParam("doctorId") int doctorId,
            @RequestBody DeletingDoctorDtoRequest deletingDoctorDtoRequest,
            HttpServletRequest request) {

        Cookie cookies[] = request.getCookies();

        doctorService.deletingDoctor(deletingDoctorDtoRequest, doctorId, cookies);

        return new ResponseEntity<>(HttpStatus.OK);
    }



}
