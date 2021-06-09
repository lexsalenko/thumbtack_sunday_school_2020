package net.thumbtack.school.hospital.endpoint;

import net.thumbtack.school.hospital.dto.requests.EditingPatientProfileDtoRequest;
import net.thumbtack.school.hospital.dto.requests.MakeAnAppointmentWithDoctorRequest;
import net.thumbtack.school.hospital.dto.requests.RegistrationPatientDtoRequest;
import net.thumbtack.school.hospital.dto.responses.MakeAnAppointmentWithDoctorResponse;
import net.thumbtack.school.hospital.dto.responses.RegistrationPatientDtoResponse;
import net.thumbtack.school.hospital.exception.ServerException;
import net.thumbtack.school.hospital.service.PatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.QueryParam;
import java.util.UUID;

@RestController
@RequestMapping("/api")
public class PatientServiceEndPoint {

    private final PatientService patientService;

    @Autowired
    public PatientServiceEndPoint(PatientService patientService) {
        this.patientService = patientService;
    }

    @PostMapping(value = "/patients/registerPatient", produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RegistrationPatientDtoResponse> registerPatient(
            @RequestBody RegistrationPatientDtoRequest patientDtoRequest, HttpServletResponse response, HttpServletRequest request) {

        String cookie = UUID.randomUUID().toString();

        RegistrationPatientDtoResponse registrationPatientDtoResponse =
                patientService.registerPatient(patientDtoRequest, cookie);

        return new ResponseEntity<>(registrationPatientDtoResponse, HttpStatus.OK);
    }


    @GetMapping(value = "/patients/getInformationAboutPatient/{patientId}", produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RegistrationPatientDtoResponse> getInforamationAboutPatient(
            @RequestParam("patientId") int patientId,
            HttpServletRequest request) {

        Cookie[] cookies = request.getCookies();

        RegistrationPatientDtoResponse response =
                patientService.getInformationAboutPatient(patientId, cookies);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }


    @GetMapping(value = "/patients/editingPatientProfile", produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RegistrationPatientDtoResponse> editingPatientProfile(
            EditingPatientProfileDtoRequest editingPatientProfileDtoRequest,
            HttpServletRequest request) {

        Cookie[] cookies = request.getCookies();

        RegistrationPatientDtoResponse response =
                patientService.editingPatientProfile(editingPatientProfileDtoRequest, cookies);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }


    @PostMapping(value = "/tickets/makeAnAppointmentWithDoctor", produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<MakeAnAppointmentWithDoctorResponse> makeAnAppointmentWithDoctor(
            @RequestBody MakeAnAppointmentWithDoctorRequest patientDtoRequest,
            HttpServletRequest request) throws ServerException {

        Cookie[] cookies = request.getCookies();

        MakeAnAppointmentWithDoctorResponse registrationPatientDtoResponse =
                patientService.makeAnAppointmentWithDoctor(patientDtoRequest, cookies);

        return new ResponseEntity<>(registrationPatientDtoResponse, HttpStatus.OK);
    }


    @DeleteMapping(value = "/tickets/cancelingAnAppointmentWithDoctor/{ticket}", produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<MakeAnAppointmentWithDoctorResponse> cancelingAnAppointmentWithDoctor(
            @QueryParam("ticket") String ticket,
            HttpServletRequest request) throws ServerException {

        Cookie[] cookies = request.getCookies();

        patientService.cancelingAnAppointmentWithDoctor(ticket, cookies);

        return new ResponseEntity<>(HttpStatus.OK);
    }


}
