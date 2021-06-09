package net.thumbtack.school.hospital.endpoint;

import net.thumbtack.school.hospital.dto.requests.LoginDtoRequest;
import net.thumbtack.school.hospital.dto.responses.CommonResponse;
import net.thumbtack.school.hospital.service.CommonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

@RestController
@RequestMapping("/api")
public class CommonServiceEndPoint {

    private final CommonService commonService;

    @Autowired
    public CommonServiceEndPoint(CommonService commonService) {
        this.commonService = commonService;
    }


    @PostMapping(value = "/sessions/login", produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CommonResponse> login(
            @RequestBody
                    LoginDtoRequest loginDtoRequest,
            HttpServletResponse response) {

        String cookie = UUID.randomUUID().toString();

        CommonResponse commonResponse = commonService.login(loginDtoRequest, cookie);

        response.addCookie(new Cookie("JAVASESSIONID", cookie));

        return new ResponseEntity<>(commonResponse, HttpStatus.OK);
    }

    @DeleteMapping(value = "/sessions/logout")
    public ResponseEntity<?> logout(HttpServletRequest request) {

        Cookie[] cookies = request.getCookies();

        commonService.logout(cookies);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping(value = "/account/getInfoAboutCurrentUser")
    public ResponseEntity<CommonResponse> getInfoAboutCurrentUser(HttpServletRequest request) {

        Cookie[] cookies = request.getCookies();

        CommonResponse commonResponse = commonService.getInformationAboutCurrentUser(cookies);

        return new ResponseEntity<>(commonResponse, HttpStatus.OK);
    }

}
