package net.thumbtack.school.hospital.endpoint;


import net.thumbtack.school.hospital.dto.requests.EditingAdministratorProfileDtoRequest;
import net.thumbtack.school.hospital.dto.requests.RegistrationAdministratorDtoRequest;
import net.thumbtack.school.hospital.dto.responses.RegistrationAdministratorDtoResponse;
import net.thumbtack.school.hospital.exception.ServerException;
import net.thumbtack.school.hospital.service.AdminService;
import net.thumbtack.school.hospital.validators.AdminValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class AdminServiceEndPoint {

    private final AdminService adminService;

    private final AdminValidator adminValidator;

    @Autowired
    public AdminServiceEndPoint(AdminService adminService, AdminValidator adminValidator) {
        this.adminService = adminService;
        this.adminValidator = adminValidator;
    }

    @PostMapping(value = "/admins/registerAdmin", produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RegistrationAdministratorDtoResponse> registerAdmin(
            @Valid @RequestBody
                    RegistrationAdministratorDtoRequest administratorDtoRequest,
            HttpServletRequest request) throws ServerException {

        Optional<RegistrationAdministratorDtoRequest> optResult = adminValidator.validate(administratorDtoRequest);
        if (!optResult.isPresent()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Incorrect parameters");
        } else {

            Cookie[] cookies = request.getCookies();

            RegistrationAdministratorDtoResponse registrationAdministratorDtoResponse
                    = adminService.registerAdmin(administratorDtoRequest, cookies);

            return new ResponseEntity<>(registrationAdministratorDtoResponse, HttpStatus.OK);
        }


    }


    @PutMapping(value = "/admins/editingAdministratorProfile", produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RegistrationAdministratorDtoResponse> editingAdministratorProfile(
            @Valid @RequestBody
                    EditingAdministratorProfileDtoRequest editingAdministratorProfileDtoRequest,
            HttpServletRequest request) {

        Cookie[] cookies = request.getCookies();

        RegistrationAdministratorDtoResponse registrationAdministratorDtoResponse
                = adminService.editingAdministratorProfile(editingAdministratorProfileDtoRequest, cookies);

        return new ResponseEntity<>(registrationAdministratorDtoResponse, HttpStatus.OK);
    }


}
