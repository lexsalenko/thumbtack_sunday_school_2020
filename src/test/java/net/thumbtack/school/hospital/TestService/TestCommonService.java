package net.thumbtack.school.hospital.TestService;

import net.thumbtack.school.hospital.HospitalServer;
import net.thumbtack.school.hospital.dao.AdminDao;
import net.thumbtack.school.hospital.dto.requests.*;
import net.thumbtack.school.hospital.dto.responses.*;
import net.thumbtack.school.hospital.exception.ServerException;
import net.thumbtack.school.hospital.model.*;
import net.thumbtack.school.hospital.service.AdminService;
import net.thumbtack.school.hospital.service.CommonService;
import net.thumbtack.school.hospital.service.DoctorService;
import net.thumbtack.school.hospital.service.PatientService;
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

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.Assert.assertEquals;

@ExtendWith(SpringExtension.class)
@RunWith(SpringRunner.class)
@SpringBootTest(classes = HospitalServer.class, webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class TestCommonService {

    @Autowired
    private DoctorService doctorService;

    @Autowired
    private CommonService commonService;

    @Autowired
    private PatientService patientService;

    @Autowired
    private AdminService adminService;

    @Autowired
    private AdminDao adminDao;

    private RestTemplate template = new RestTemplate();

    private DateTimeFormatter formatterDate = DateTimeFormatter.ofPattern("dd-MM-yyyy");
    private DateTimeFormatter formatterTime = DateTimeFormatter.ofPattern("HH:mm");

    @Before
    public void clearDatabase() {
        commonService.clearDataBase();
        insertRoomsAndSpecialties();
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

    public void insertRoomsAndSpecialties() {
        Room room1 = new Room("1-001");
        Room room2 = new Room("2-036");
        Room room3 = new Room("3-078");
        Room room4 = new Room("1-089");
        Room room5 = new Room("7-045");
        Room room6 = new Room("1-014");
        Room room7 = new Room("1-073");
        Room room8 = new Room("4-013");
        Room room9 = new Room("9-003");
        Room room10 = new Room("6-004");

        List<Room> rooms = new ArrayList<>();
        rooms.add(room1);
        rooms.add(room2);
        rooms.add(room3);
        rooms.add(room4);
        rooms.add(room5);
        rooms.add(room6);
        rooms.add(room7);
        rooms.add(room8);
        rooms.add(room9);
        rooms.add(room10);

        Speciality speciality1 = new Speciality("Dentist");
        Speciality speciality2 = new Speciality("Surgeon");
        Speciality speciality3 = new Speciality("Urologist");
        Speciality speciality4 = new Speciality("Neurologist");
        Speciality speciality5 = new Speciality("Cardiologist");
        Speciality speciality6 = new Speciality("Ophthalmologist");
        Speciality speciality7 = new Speciality("Neurosurgeon");
        Speciality speciality8 = new Speciality("Radiologist");
        Speciality speciality9 = new Speciality("Gynecologist");
        Speciality speciality10 = new Speciality("Immunologist");

        List<Speciality> specialities = new ArrayList<>();
        specialities.add(speciality1);
        specialities.add(speciality2);
        specialities.add(speciality3);
        specialities.add(speciality4);
        specialities.add(speciality5);
        specialities.add(speciality6);
        specialities.add(speciality7);
        specialities.add(speciality8);
        specialities.add(speciality9);
        specialities.add(speciality10);

        commonService.insertRoomsAndSpecialties(specialities, rooms);
    }

    @Test
    public void TestLoginAdmin() throws ServerException {

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


        LoginDtoRequest loginDtoRequest = new LoginDtoRequest(request.getLogin(), request.getPassword());

        adminService.registerAdmin(request, cookies);

        String cookie = UUID.randomUUID().toString();


        RegistrationAdministratorDtoResponse response = (RegistrationAdministratorDtoResponse)commonService.login(loginDtoRequest, cookie);


        assertEquals(response.getFirstName(), request.getFirstName());
        assertEquals(response.getLastName(), request.getLastName());
        assertEquals(response.getPatronymic(), request.getPatronymic());
        assertEquals(response.getPosition(), request.getPosition());

    }


    @Test
    public void TestLoginDoctor() throws ServerException {
        //“Mon”, “Tue”, “Wed”, “Thu”, “Fri”
        List<String> weekDays = new ArrayList<>();
        weekDays.add("Mon");
        weekDays.add("Tue");
        weekDays.add("Wed");
        weekDays.add("Thu");
        weekDays.add("Fri");

        WeekSchedule weekSchedule = new WeekSchedule("09:00", "18:00", weekDays);

        RegistrationDoctorDtoRequest request = new RegistrationDoctorDtoRequest(
                "Obadiah",
                "Stane",
                "Ivanov",
                "Surgeon",
                "1-001",
                "IronMonger1373",
                "ironmonger.1234",
                "16-06-2020",
                "16-08-2020",
                weekSchedule,
                null,
                20
        );

        DateTimeFormatter formatterDate = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        DateTimeFormatter formatterTime = DateTimeFormatter.ofPattern("HH:mm");
        LocalDate dateStart = LocalDate.parse(request.getDateStart(), formatterDate);
        LocalDate dateEnd = LocalDate.parse(request.getDateEnd(), formatterDate);

        List<net.thumbtack.school.hospital.dto.responses.Schedule> schedule = new ArrayList<>();

        for (LocalDate dateDay = dateStart; !dateDay.equals(dateEnd); dateDay = dateDay.plusDays(1)) {
            if (!dateDay.getDayOfWeek().equals(DayOfWeek.SATURDAY) && !dateDay.getDayOfWeek().equals(DayOfWeek.SUNDAY)) {
                List<net.thumbtack.school.hospital.dto.responses.DaySchedule> daySchedule = new ArrayList<>();
                LocalTime timestart = LocalTime.parse(request.getWeekSchedule().getTimeStart(), formatterTime);
                LocalTime timeend = LocalTime.parse(request.getWeekSchedule().getTimeEnd(), formatterTime);
                int duration = request.getDuration();
                for (LocalTime time = timestart; !time.equals(timeend); time = time.plusMinutes(duration)) {
                    daySchedule.add(new net.thumbtack.school.hospital.dto.responses.DaySchedule(time, null));
                }
                schedule.add(new Schedule(dateDay, daySchedule));
            }
        }

        RegistrationDoctorDtoResponse responseExpected = new RegistrationDoctorDtoResponse(
                request.getFirstName(),
                request.getLastName(),
                request.getPatronymic(),
                request.getSpeciality(),
                request.getRoom(),
                schedule
        );

        Cookie[] cookies = new Cookie[1];
        String c = getAdminCookie();
        cookies[0] = new Cookie("JAVASESSIONID", c);


        doctorService.registerDoctor(request, cookies);

        LoginDtoRequest loginDtoRequest = new LoginDtoRequest(request.getLogin(), request.getPassword());

        String cookie = UUID.randomUUID().toString();

        RegistrationDoctorDtoResponse responseActual = (RegistrationDoctorDtoResponse)commonService.login(loginDtoRequest, cookie);

        assertEquals(responseExpected.getFirstName(), responseActual.getFirstName());
        assertEquals(responseExpected.getLastName(), responseActual.getLastName());
        assertEquals(responseExpected.getPatronymic(), responseActual.getPatronymic());
        assertEquals(responseExpected.getRoom(), responseActual.getRoom());
        assertEquals(responseExpected.getSpeciality(), responseActual.getSpeciality());
        assertEquals(responseExpected.getSchedule(), responseActual.getSchedule());

    }

    @Test
    public void TestLoginPatient() {

        String c = UUID.randomUUID().toString();

        RegistrationPatientDtoRequest request = new RegistrationPatientDtoRequest(
                "Obadiah",
                "Stane",
                "Ivanov",
                "stanIvanov@mail.ru",
                "Mira St. 43",
                "8904235687",
                "IronMonger1373",
                "ironmonger.1234"
        );

        patientService.registerPatient(request, c);

        LoginDtoRequest loginDtoRequest = new LoginDtoRequest(request.getLogin(), request.getPassword());

        String cookie = UUID.randomUUID().toString();

        RegistrationPatientDtoResponse response = (RegistrationPatientDtoResponse)commonService.login(loginDtoRequest, cookie);

        assertEquals(request.getFirstName(), response.getFirstName());
        assertEquals(request.getLastName(), response.getLastName());
        assertEquals(request.getPatronymic(), response.getPatronymic());
        assertEquals(request.getEmail(), response.getEmail());
        assertEquals(request.getAddress(), response.getAddress());
        assertEquals(request.getPhone(), response.getPhone());
    }

    @Test
    public void TestGetInformationAboutCurrentUser(){
        String patientCookie = UUID.randomUUID().toString();

        RegistrationPatientDtoRequest request = new RegistrationPatientDtoRequest(
                "Obadiah",
                "Stane",
                "Ivanov",
                "stanIvanov@mail.ru",
                "Mira St. 43",
                "8904235687",
                "IronMonger1373",
                "ironmonger.1234"
        );

        patientService.registerPatient(request, patientCookie);

        Cookie[] cookies = new Cookie[1];
        cookies[0] = new Cookie("JAVASESSIONID", patientCookie);

        RegistrationPatientDtoResponse response = (RegistrationPatientDtoResponse)commonService.getInformationAboutCurrentUser(cookies);

        assertEquals(request.getFirstName(), response.getFirstName());
        assertEquals(request.getLastName(), response.getLastName());
        assertEquals(request.getPatronymic(), response.getPatronymic());
        assertEquals(request.getEmail(), response.getEmail());
        assertEquals(request.getAddress(), response.getAddress());
        assertEquals(request.getPhone(), response.getPhone());

    }

}
