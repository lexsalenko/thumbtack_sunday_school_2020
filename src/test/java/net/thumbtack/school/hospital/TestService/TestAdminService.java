package net.thumbtack.school.hospital.TestService;


import net.thumbtack.school.hospital.HospitalServer;
import net.thumbtack.school.hospital.dao.AdminDao;
import net.thumbtack.school.hospital.dto.requests.EditingAdministratorProfileDtoRequest;
import net.thumbtack.school.hospital.dto.requests.LoginDtoRequest;
import net.thumbtack.school.hospital.dto.requests.RegistrationAdministratorDtoRequest;
import net.thumbtack.school.hospital.dto.responses.RegistrationAdministratorDtoResponse;
import net.thumbtack.school.hospital.exception.ServerException;
import net.thumbtack.school.hospital.model.Admin;
import net.thumbtack.school.hospital.model.User;
import net.thumbtack.school.hospital.model.UserType;
import net.thumbtack.school.hospital.service.AdminService;
import net.thumbtack.school.hospital.service.CommonService;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.Cookie;
import java.time.format.DateTimeFormatter;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

@ExtendWith(SpringExtension.class)
@RunWith(SpringRunner.class)
@SpringBootTest(classes = HospitalServer.class, webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class TestAdminService {

    @Autowired
    private CommonService commonService;

    @Autowired
    private AdminService adminService;

    @Autowired
    private AdminDao adminDao;

    private RestTemplate template = new RestTemplate();

    @Before
    public void clearDatabase() {
        commonService.clearDataBase();
    }

    public String getAdminCookie() {
        User user = new User(UserType.ADMIN, "Petr", "Petrov", "Petrovich", "PetroPetrov12345", "PetroPetrov12345.@qwerty");
        Admin admin = new Admin(user, "Administrator");

        admin = adminDao.insert(admin);

        LoginDtoRequest loginDtoRequest = new LoginDtoRequest(
                "PetroPetrov12345",
                "PetroPetrov12345.@qwerty"
        );

        final String url = "http://localhost:8080/api/sessions/login";

        HttpEntity<LoginDtoRequest> request = new HttpEntity<>(loginDtoRequest);
        HttpEntity<String> response = template.exchange(url, HttpMethod.POST, request, String.class);
        HttpHeaders headers = response.getHeaders();
        headers.get("JAVASESSIONID");

        String set_cookie = headers.getFirst(HttpHeaders.SET_COOKIE);

        int start = set_cookie.indexOf('=');
        int end = set_cookie.length();

        return set_cookie.substring(start + 1, end);
    }


    @Test
    public void TestRegisterAdmin() throws ServerException {

        Cookie[] cookies = new Cookie[1];
        String c = getAdminCookie();
        cookies[0] = new Cookie("JAVASESSIONID", c);

        RegistrationAdministratorDtoRequest request = new RegistrationAdministratorDtoRequest(
                "Obadiah",
                "Stane",
                "Ivanov",
                "Administrator",
                "IronMonger1373",
                "ironmonger.1234"
        );

        RegistrationAdministratorDtoResponse response = adminService.registerAdmin(request, cookies);

        assertEquals(response.getFirstName(), request.getFirstName());
        assertEquals(response.getLastName(), request.getLastName());
        assertEquals(response.getPatronymic(), request.getPatronymic());
        assertEquals(response.getPosition(), request.getPosition());

    }

    @Test
    public void TestEditAdminProfile() throws ServerException {
        Cookie[] cookiesMainAdmin = new Cookie[1];
        String cMainAdmin = getAdminCookie();
        cookiesMainAdmin[0] = new Cookie("JAVASESSIONID", cMainAdmin);

        RegistrationAdministratorDtoRequest requestRegister = new RegistrationAdministratorDtoRequest(
                "Obadiah",
                "Stane",
                "Ivanov",
                "Administrator",
                "IronMonger1373",
                "ironmonger.1234"
        );

        RegistrationAdministratorDtoResponse responseRegister = adminService.registerAdmin(requestRegister, cookiesMainAdmin);

        LoginDtoRequest loginDtoRequest = new LoginDtoRequest(
                "IronMonger1373",
                "ironmonger.1234"
        );

        final String url = "http://localhost:8080/api/sessions/login";

        HttpEntity<LoginDtoRequest> request = new HttpEntity<>(loginDtoRequest);
        HttpEntity<String> response = template.exchange(url, HttpMethod.POST, request, String.class);
        HttpHeaders headers = response.getHeaders();
        headers.get("JAVASESSIONID");

        String set_cookie = headers.getFirst(HttpHeaders.SET_COOKIE);

        System.out.println("S = " + set_cookie);
        int start = set_cookie.indexOf('=');
        int end = set_cookie.length();

        Cookie[] cookies = new Cookie[1];
        String c = set_cookie.substring(start + 1, end);
        cookies[0] = new Cookie("JAVASESSIONID", c);

        EditingAdministratorProfileDtoRequest requestEditing = new EditingAdministratorProfileDtoRequest(
                "Obadiah",
                "Stane",
                "Ivanov",
                "Administrator",
                "ironmonger.1234",
                "DeadDeadDed.1234"
        );

        RegistrationAdministratorDtoResponse responseEditing = adminService.editingAdministratorProfile(requestEditing, cookies);


        assertEquals(responseEditing.getFirstName(), requestEditing.getFirstName());
        assertEquals(responseEditing.getLastName(), requestEditing.getLastName());
        assertEquals(responseEditing.getPatronymic(), requestEditing.getPatronymic());
        assertEquals(responseEditing.getPosition(), requestEditing.getPosition());

    }

}
