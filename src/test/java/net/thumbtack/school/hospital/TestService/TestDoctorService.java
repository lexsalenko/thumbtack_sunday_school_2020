package net.thumbtack.school.hospital.TestService;

import net.thumbtack.school.hospital.HospitalServer;
import net.thumbtack.school.hospital.dao.AdminDao;
import net.thumbtack.school.hospital.dao.DoctorDao;
import net.thumbtack.school.hospital.dao.PatientDao;
import net.thumbtack.school.hospital.dto.requests.*;
import net.thumbtack.school.hospital.dto.responses.AppointmentMedicalCommissionDtoResponse;
import net.thumbtack.school.hospital.dto.responses.RegistrationDoctorDtoResponse;
import net.thumbtack.school.hospital.dto.responses.RegistrationPatientDtoResponse;
import net.thumbtack.school.hospital.dto.responses.Schedule;
import net.thumbtack.school.hospital.exception.ServerException;
import net.thumbtack.school.hospital.model.*;
import net.thumbtack.school.hospital.model.DaySchedule;
import net.thumbtack.school.hospital.service.AdminService;
import net.thumbtack.school.hospital.service.CommonService;
import net.thumbtack.school.hospital.service.DoctorService;
import net.thumbtack.school.hospital.service.PatientService;
import org.junit.Assert;
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
import java.util.*;

import static org.junit.Assert.assertEquals;

@ExtendWith(SpringExtension.class)
@RunWith(SpringRunner.class)
@SpringBootTest(classes = HospitalServer.class, webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class TestDoctorService {

    @Autowired
    private DoctorService doctorService;

    @Autowired
    private CommonService commonService;

    @Autowired
    private AdminService adminService;

    @Autowired
    private PatientService patientService;

    @Autowired
    private AdminDao adminDao;

    @Autowired
    private DoctorDao doctorDao;

    @Autowired
    private PatientDao patientDao;

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


        System.out.println();
        System.out.println("A =  " + admin.getId());
        System.out.println(admin.getUser().getId());
        System.out.println();

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

        System.out.println("S = " + set_cookie);
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
    public void TestRegisterDoctor() throws ServerException {
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


        RegistrationDoctorDtoResponse responseActual = doctorService.registerDoctor(request, cookies);

        assertEquals(responseExpected.getFirstName(), responseActual.getFirstName());
        assertEquals(responseExpected.getLastName(), responseActual.getLastName());
        assertEquals(responseExpected.getPatronymic(), responseActual.getPatronymic());
        assertEquals(responseExpected.getRoom(), responseActual.getRoom());
        assertEquals(responseExpected.getSpeciality(), responseActual.getSpeciality());
        assertEquals(responseExpected.getSchedule(), responseActual.getSchedule());
    }


    @Test
    public void TestRegisterDoctors() throws ServerException {

        Cookie[] cookies = new Cookie[1];
        String c = getAdminCookie();
        cookies[0] = new Cookie("JAVASESSIONID", c);

        // Doctor 1

        List<String> weekDays1 = new ArrayList<>();
        weekDays1.add("Mon");
        weekDays1.add("Tue");
        weekDays1.add("Wed");
        weekDays1.add("Thu");
        weekDays1.add("Fri");

        WeekSchedule weekSchedule1 = new WeekSchedule("09:00", "18:00", weekDays1);

        RegistrationDoctorDtoRequest request1 = new RegistrationDoctorDtoRequest(
                "Obadiah",
                "Stane",
                "Ivanov",
                "Surgeon",
                "1-001",
                "IronMonger1373",
                "ironmonger.1234",
                "16-06-2020",
                "16-08-2020",
                weekSchedule1,
                null,
                20
        );


        LocalDate dateStart1 = LocalDate.parse(request1.getDateStart(), formatterDate);
        LocalDate dateEnd1 = LocalDate.parse(request1.getDateEnd(), formatterDate);

        List<net.thumbtack.school.hospital.dto.responses.Schedule> schedule1 = new ArrayList<>();

        for (LocalDate dateDay = dateStart1; !dateDay.equals(dateEnd1); dateDay = dateDay.plusDays(1)) {
            if (!dateDay.getDayOfWeek().equals(DayOfWeek.SATURDAY) && !dateDay.getDayOfWeek().equals(DayOfWeek.SUNDAY)) {
                List<net.thumbtack.school.hospital.dto.responses.DaySchedule> daySchedule = new ArrayList<>();
                LocalTime timestart = LocalTime.parse(request1.getWeekSchedule().getTimeStart(), formatterTime);
                LocalTime timeend = LocalTime.parse(request1.getWeekSchedule().getTimeEnd(), formatterTime);
                int duration = request1.getDuration();
                for (LocalTime time = timestart; !time.equals(timeend); time = time.plusMinutes(duration)) {
                    daySchedule.add(new net.thumbtack.school.hospital.dto.responses.DaySchedule(time, null));
                }
                schedule1.add(new Schedule(dateDay, daySchedule));
            }
        }

        RegistrationDoctorDtoResponse responseExpected_1 = new RegistrationDoctorDtoResponse(
                request1.getFirstName(),
                request1.getLastName(),
                request1.getPatronymic(),
                request1.getSpeciality(),
                request1.getRoom(),
                schedule1
        );

        RegistrationDoctorDtoResponse responseActual_1 = doctorService.registerDoctor(request1, cookies);


        // Doctor 2


        WeekSchedule weekSchedule2 = new WeekSchedule("09:00", "18:00", null);

        RegistrationDoctorDtoRequest request2 = new RegistrationDoctorDtoRequest(
                "Antony",
                "Stark",
                "Hovard",
                "Dentist",
                "9-003",
                "IronMan777",
                "ironman.057",
                "16-06-2020",
                "16-08-2020",
                weekSchedule2,
                null,
                30
        );

        LocalDate dateStart2 = LocalDate.parse(request2.getDateStart(), formatterDate);
        LocalDate dateEnd2 = LocalDate.parse(request2.getDateEnd(), formatterDate);

        List<net.thumbtack.school.hospital.dto.responses.Schedule> schedule2 = new ArrayList<>();

        for (LocalDate dateDay = dateStart2; !dateDay.equals(dateEnd2); dateDay = dateDay.plusDays(1)) {
            if (!dateDay.getDayOfWeek().equals(DayOfWeek.SATURDAY) && !dateDay.getDayOfWeek().equals(DayOfWeek.SUNDAY)) {
                List<net.thumbtack.school.hospital.dto.responses.DaySchedule> daySchedule = new ArrayList<>();
                LocalTime timestart = LocalTime.parse(request2.getWeekSchedule().getTimeStart(), formatterTime);
                LocalTime timeend = LocalTime.parse(request2.getWeekSchedule().getTimeEnd(), formatterTime);
                int duration = request2.getDuration();
                for (LocalTime time = timestart; !time.equals(timeend); time = time.plusMinutes(duration)) {
                    daySchedule.add(new net.thumbtack.school.hospital.dto.responses.DaySchedule(time, null));
                }
                schedule2.add(new Schedule(dateDay, daySchedule));
            }
        }

        RegistrationDoctorDtoResponse responseExpected_2 = new RegistrationDoctorDtoResponse(
                request2.getFirstName(),
                request2.getLastName(),
                request2.getPatronymic(),
                request2.getSpeciality(),
                request2.getRoom(),
                schedule2
        );

        RegistrationDoctorDtoResponse responseActual_2 = doctorService.registerDoctor(request2, cookies);

        // Doctor 3

        WeekSchedule weekSchedule3 = new WeekSchedule("09:00", "18:00", null);

        RegistrationDoctorDtoRequest request3 = new RegistrationDoctorDtoRequest(
                "Paper",
                "Pots",
                "Harley",
                "Cardiologist",
                "3-078",
                "IronWoman",
                "ironwoman.123",
                "16-06-2020",
                "16-08-2020",
                weekSchedule3,
                null,
                30
        );

        LocalDate dateStart3 = LocalDate.parse(request3.getDateStart(), formatterDate);
        LocalDate dateEnd3 = LocalDate.parse(request3.getDateEnd(), formatterDate);

        List<net.thumbtack.school.hospital.dto.responses.Schedule> schedule3 = new ArrayList<>();

        for (LocalDate dateDay = dateStart3; !dateDay.equals(dateEnd3); dateDay = dateDay.plusDays(1)) {
            if (!dateDay.getDayOfWeek().equals(DayOfWeek.SATURDAY) && !dateDay.getDayOfWeek().equals(DayOfWeek.SUNDAY)) {
                List<net.thumbtack.school.hospital.dto.responses.DaySchedule> daySchedule = new ArrayList<>();
                LocalTime timestart = LocalTime.parse(request3.getWeekSchedule().getTimeStart(), formatterTime);
                LocalTime timeend = LocalTime.parse(request3.getWeekSchedule().getTimeEnd(), formatterTime);
                int duration = request3.getDuration();
                for (LocalTime time = timestart; !time.equals(timeend); time = time.plusMinutes(duration)) {
                    daySchedule.add(new net.thumbtack.school.hospital.dto.responses.DaySchedule(time, null));
                }
                schedule3.add(new Schedule(dateDay, daySchedule));
            }
        }

        RegistrationDoctorDtoResponse responseExpected_3 = new RegistrationDoctorDtoResponse(
                request3.getFirstName(),
                request3.getLastName(),
                request3.getPatronymic(),
                request3.getSpeciality(),
                request3.getRoom(),
                schedule3
        );

        RegistrationDoctorDtoResponse responseActual_3 = doctorService.registerDoctor(request3, cookies);


        // Doctor 4

        List<net.thumbtack.school.hospital.dto.requests.DaySchedule> weekDaysSchedule4 = new ArrayList<>();
        weekDaysSchedule4.add(new net.thumbtack.school.hospital.dto.requests.DaySchedule("Mon", "09:00", "18:00"));
        weekDaysSchedule4.add(new net.thumbtack.school.hospital.dto.requests.DaySchedule("Tue", "09:00", "14:00"));
        weekDaysSchedule4.add(new net.thumbtack.school.hospital.dto.requests.DaySchedule("Wed", "10:00", "12:00"));
        weekDaysSchedule4.add(new net.thumbtack.school.hospital.dto.requests.DaySchedule("Thu", "13:00", "16:00"));
        weekDaysSchedule4.add(new net.thumbtack.school.hospital.dto.requests.DaySchedule("Fri", "12:00", "17:00"));


        RegistrationDoctorDtoRequest request4 = new RegistrationDoctorDtoRequest(
                "Obadiah",
                "Stane",
                "Ivanov",
                "Surgeon",
                "1-001",
                "IronMonger1373",
                "ironmonger.1234",
                "16-06-2020",
                "16-08-2020",
                null,
                weekDaysSchedule4,
                20
        );


        //

        List<net.thumbtack.school.hospital.dto.responses.Schedule> schedule4 = new ArrayList<>();

        LocalDate dateStart4 = LocalDate.parse(request4.getDateStart(), formatterDate);
        LocalDate dateEnd4 = LocalDate.parse(request4.getDateEnd(), formatterDate);

        Map<DayOfWeek, net.thumbtack.school.hospital.dto.requests.DaySchedule> doctorAppointmentDays = new HashMap<>();
        for (net.thumbtack.school.hospital.dto.requests.DaySchedule daySchedule : request4.getWeekDaysSchedule()) {
            // validator
            doctorAppointmentDays.put(DayOfWeek.of(getDay(daySchedule.getWeekDay())), daySchedule);
        }

        for (LocalDate dateDay = dateStart4; !dateDay.equals(dateEnd4); dateDay = dateDay.plusDays(1)) {
            if (!dateDay.getDayOfWeek().equals(DayOfWeek.SATURDAY) && !dateDay.getDayOfWeek().equals(DayOfWeek.SUNDAY)) {
                if (doctorAppointmentDays.keySet().contains(dateDay.getDayOfWeek())) {

                    List<net.thumbtack.school.hospital.dto.responses.DaySchedule> daySchedule = new ArrayList<>();

                    LocalTime timestart = LocalTime.parse(doctorAppointmentDays.get(dateDay.getDayOfWeek()).getTimeStart(), formatterTime);
                    LocalTime timeend = LocalTime.parse(doctorAppointmentDays.get(dateDay.getDayOfWeek()).getTimeEnd(), formatterTime);
                    int duration = request4.getDuration();
                    for (LocalTime time = timestart; !time.equals(timeend); time = time.plusMinutes(duration)) {
                        daySchedule.add(new net.thumbtack.school.hospital.dto.responses.DaySchedule(time, null));
                    }
                    schedule4.add(new Schedule(dateDay, daySchedule));
                }
            }
        }

        //


        RegistrationDoctorDtoResponse responseExpected_4 = new RegistrationDoctorDtoResponse(
                request4.getFirstName(),
                request4.getLastName(),
                request4.getPatronymic(),
                request4.getSpeciality(),
                request4.getRoom(),
                schedule4
        );

        RegistrationDoctorDtoResponse responseActual_4 = doctorService.registerDoctor(request4, cookies);

        //


        assertEquals(responseExpected_1.getFirstName(), responseActual_1.getFirstName());
        assertEquals(responseExpected_1.getLastName(), responseActual_1.getLastName());
        assertEquals(responseExpected_1.getPatronymic(), responseActual_1.getPatronymic());
        assertEquals(responseExpected_1.getRoom(), responseActual_1.getRoom());
        assertEquals(responseExpected_1.getSpeciality(), responseActual_1.getSpeciality());
        assertEquals(responseExpected_1.getSchedule(), responseActual_1.getSchedule());


        assertEquals(responseExpected_2.getFirstName(), responseActual_2.getFirstName());
        assertEquals(responseExpected_2.getLastName(), responseActual_2.getLastName());
        assertEquals(responseExpected_2.getPatronymic(), responseActual_2.getPatronymic());
        assertEquals(responseExpected_2.getRoom(), responseActual_2.getRoom());
        assertEquals(responseExpected_2.getSpeciality(), responseActual_2.getSpeciality());
        assertEquals(responseExpected_2.getSchedule(), responseActual_2.getSchedule());


        assertEquals(responseExpected_3.getFirstName(), responseActual_3.getFirstName());
        assertEquals(responseExpected_3.getLastName(), responseActual_3.getLastName());
        assertEquals(responseExpected_3.getPatronymic(), responseActual_3.getPatronymic());
        assertEquals(responseExpected_3.getRoom(), responseActual_3.getRoom());
        assertEquals(responseExpected_3.getSpeciality(), responseActual_3.getSpeciality());
        assertEquals(responseExpected_3.getSchedule(), responseActual_3.getSchedule());


        assertEquals(responseExpected_4.getFirstName(), responseActual_4.getFirstName());
        assertEquals(responseExpected_4.getLastName(), responseActual_4.getLastName());
        assertEquals(responseExpected_4.getPatronymic(), responseActual_4.getPatronymic());
        assertEquals(responseExpected_4.getRoom(), responseActual_4.getRoom());
        assertEquals(responseExpected_4.getSpeciality(), responseActual_4.getSpeciality());
        assertEquals(responseExpected_4.getSchedule(), responseActual_4.getSchedule());

    }


    @Test
    public void TestGetInformationAboutDoctor() throws ServerException {
        Cookie[] cookies = new Cookie[1];
        String c = getAdminCookie();
        cookies[0] = new Cookie("JAVASESSIONID", c);

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

        RegistrationDoctorDtoResponse responseActual = doctorService.registerDoctor(request, cookies);

        RegistrationDoctorDtoResponse getInfoResponseActual = doctorService.getInformationAboutDoctor(
                responseActual.getId(),
                "yes",
                "16-06-2020",
                "16-08-2020",
                cookies
        );

        RegistrationDoctorDtoResponse getInfoResponseExpected = responseActual;

        assertEquals(getInfoResponseExpected.getId(), getInfoResponseActual.getId());
        assertEquals(getInfoResponseExpected.getFirstName(), getInfoResponseActual.getFirstName());
        assertEquals(getInfoResponseExpected.getLastName(), getInfoResponseActual.getLastName());
        assertEquals(getInfoResponseExpected.getPatronymic(), getInfoResponseActual.getPatronymic());
        assertEquals(getInfoResponseExpected.getRoom(), getInfoResponseActual.getRoom());
        assertEquals(getInfoResponseExpected.getSpeciality(), getInfoResponseActual.getSpeciality());
        assertEquals(getInfoResponseExpected.getSchedule(), getInfoResponseActual.getSchedule());


    }

    @Test
    public void TestGetInformationAboutDoctors() throws ServerException {
        Cookie[] cookies = new Cookie[1];
        String c = getAdminCookie();
        cookies[0] = new Cookie("JAVASESSIONID", c);

        // Doctor 1

        List<String> weekDays1 = new ArrayList<>();
        weekDays1.add("Mon");
        weekDays1.add("Tue");
        weekDays1.add("Wed");
        weekDays1.add("Thu");
        weekDays1.add("Fri");

        WeekSchedule weekSchedule1 = new WeekSchedule("09:00", "18:00", weekDays1);

        RegistrationDoctorDtoRequest request1 = new RegistrationDoctorDtoRequest(
                "Obadiah",
                "Stane",
                "Ivanov",
                "Surgeon",
                "1-001",
                "IronMonger1373",
                "ironmonger.1234",
                "16-06-2020",
                "16-08-2020",
                weekSchedule1,
                null,
                20
        );

        WeekSchedule weekSchedule2 = new WeekSchedule("09:00", "18:00", null);

        RegistrationDoctorDtoRequest request2 = new RegistrationDoctorDtoRequest(
                "Antony",
                "Stark",
                "Hovard",
                "Dentist",
                "9-003",
                "IronMan777",
                "ironman.057",
                "16-06-2020",
                "16-08-2020",
                weekSchedule2,
                null,
                30
        );

        WeekSchedule weekSchedule3 = new WeekSchedule("09:00", "18:00", null);

        RegistrationDoctorDtoRequest request3 = new RegistrationDoctorDtoRequest(
                "Paper",
                "Pots",
                "Harley",
                "Cardiologist",
                "3-078",
                "IronWoman",
                "ironwoman.123",
                "16-06-2020",
                "16-08-2020",
                weekSchedule3,
                null,
                30
        );

        List<net.thumbtack.school.hospital.dto.requests.DaySchedule> weekDaysSchedule4 = new ArrayList<>();
        weekDaysSchedule4.add(new net.thumbtack.school.hospital.dto.requests.DaySchedule("Mon", "09:00", "18:00"));
        weekDaysSchedule4.add(new net.thumbtack.school.hospital.dto.requests.DaySchedule("Tue", "09:00", "14:00"));
        weekDaysSchedule4.add(new net.thumbtack.school.hospital.dto.requests.DaySchedule("Wed", "10:00", "12:00"));
        weekDaysSchedule4.add(new net.thumbtack.school.hospital.dto.requests.DaySchedule("Thu", "13:00", "16:00"));
        weekDaysSchedule4.add(new net.thumbtack.school.hospital.dto.requests.DaySchedule("Fri", "12:00", "17:00"));


        RegistrationDoctorDtoRequest request4 = new RegistrationDoctorDtoRequest(
                "Obadiah",
                "Stane",
                "Ivanov",
                "Surgeon",
                "1-001",
                "IronMonger1373",
                "ironmonger.1234",
                "16-06-2020",
                "16-08-2020",
                null,
                weekDaysSchedule4,
                20
        );

        RegistrationDoctorDtoResponse responseActual_1 = doctorService.registerDoctor(request1, cookies);
        RegistrationDoctorDtoResponse responseActual_2 = doctorService.registerDoctor(request2, cookies);
        RegistrationDoctorDtoResponse responseActual_3 = doctorService.registerDoctor(request3, cookies);
        RegistrationDoctorDtoResponse responseActual_4 = doctorService.registerDoctor(request4, cookies);

        List<RegistrationDoctorDtoResponse> getInfoResponseActual = doctorService.getInformationAboutDoctors(
                "yes",
                null,
                "16-06-2020",
                "16-08-2020",
                cookies
        );

        List<RegistrationDoctorDtoResponse> getInfoResponseExpected = new ArrayList<>();
        getInfoResponseExpected.add(responseActual_1);
        getInfoResponseExpected.add(responseActual_2);
        getInfoResponseExpected.add(responseActual_3);
        getInfoResponseExpected.add(responseActual_4);

        assertEquals(getInfoResponseActual.size(), getInfoResponseExpected.size());
        Assert.assertTrue(getInfoResponseActual.containsAll(getInfoResponseExpected));
        Assert.assertTrue(getInfoResponseExpected.containsAll(getInfoResponseActual));
    }

    @Test
    public void TestChangeDoctorSchedule() throws ServerException {
        Cookie[] cookies = new Cookie[1];
        String c = getAdminCookie();
        cookies[0] = new Cookie("JAVASESSIONID", c);

        // Doctor 1

        List<net.thumbtack.school.hospital.dto.requests.DaySchedule> weekDaysSchedule = new ArrayList<>();
        weekDaysSchedule.add(new net.thumbtack.school.hospital.dto.requests.DaySchedule("Mon", "09:00", "18:00"));
        weekDaysSchedule.add(new net.thumbtack.school.hospital.dto.requests.DaySchedule("Tue", "09:00", "14:00"));
        weekDaysSchedule.add(new net.thumbtack.school.hospital.dto.requests.DaySchedule("Wed", "10:00", "12:00"));
        weekDaysSchedule.add(new net.thumbtack.school.hospital.dto.requests.DaySchedule("Thu", "13:00", "16:00"));
        weekDaysSchedule.add(new net.thumbtack.school.hospital.dto.requests.DaySchedule("Fri", "12:00", "17:00"));


        RegistrationDoctorDtoRequest requestRegistration = new RegistrationDoctorDtoRequest(
                "Paper",
                "Pots",
                "Harley",
                "Cardiologist",
                "3-078",
                "IronWoman",
                "ironwoman.123",
                "16-09-2020",
                "16-11-2020",
                null,
                weekDaysSchedule,
                30
        );

        RegistrationDoctorDtoResponse responseRegistration = doctorService.registerDoctor(requestRegistration, cookies);

        ////

        List<String> weekDays = new ArrayList<>();
        weekDays.add("Mon");
        weekDays.add("Tue");
        weekDays.add("Wed");

        WeekSchedule weekSchedule = new WeekSchedule("09:00", "14:00", weekDays);

        ChangingDoctorScheduleDtoRequest requestChangeSchedule = new ChangingDoctorScheduleDtoRequest(
                "16-09-2020",
                "16-10-2020",
                weekSchedule,
                null,
                20
        );


        RegistrationDoctorDtoResponse responseChangeScheduleActual = doctorService.changeDoctorSchedule(requestChangeSchedule, responseRegistration.getId(), cookies);
        RegistrationDoctorDtoResponse getInfoResponseActual = doctorService.getInformationAboutDoctor(
                responseChangeScheduleActual.getId(),
                "yes",
                "16-09-2020",
                "16-11-2020",
                cookies
        );

        RegistrationDoctorDtoResponse responseChangeScheduleExpected = getInfoResponseActual;

        System.out.println();
        System.out.println(getInfoResponseActual.toString());
        for(Schedule schedule : getInfoResponseActual.getSchedule()) {
            System.out.println(schedule.getDaySchedule().toString());
        }
        System.out.println(responseChangeScheduleActual.toString());
        for(Schedule schedule : responseChangeScheduleActual.getSchedule()) {
            System.out.println(schedule.getDaySchedule().toString());
        }
        System.out.println();

        assertEquals(responseChangeScheduleExpected.getFirstName(), responseChangeScheduleActual.getFirstName());
        assertEquals(responseChangeScheduleExpected.getLastName(), responseChangeScheduleActual.getLastName());
        assertEquals(responseChangeScheduleExpected.getPatronymic(), responseChangeScheduleActual.getPatronymic());
        assertEquals(responseChangeScheduleExpected.getSpeciality(), responseChangeScheduleActual.getSpeciality());
        assertEquals(responseChangeScheduleExpected.getRoom(), responseChangeScheduleActual.getRoom());
        assertEquals(responseChangeScheduleExpected.getSchedule(), responseChangeScheduleActual.getSchedule());

    }

    @Test
    public void TestDeletingDoctor() throws ServerException {
        Cookie[] cookies = new Cookie[1];
        String c = getAdminCookie();
        cookies[0] = new Cookie("JAVASESSIONID", c);

        List<String> weekDays = new ArrayList<>();
        weekDays.add("Mon");
        weekDays.add("Tue");
        weekDays.add("Wed");
        weekDays.add("Thu");
        weekDays.add("Fri");

        WeekSchedule weekSchedule = new WeekSchedule("09:00", "18:00", weekDays);

        RegistrationDoctorDtoRequest requestRegistration1 = new RegistrationDoctorDtoRequest(
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

        List<net.thumbtack.school.hospital.dto.requests.DaySchedule> weekDaysSchedule = new ArrayList<>();
        weekDaysSchedule.add(new net.thumbtack.school.hospital.dto.requests.DaySchedule("Mon", "09:00", "18:00"));
        weekDaysSchedule.add(new net.thumbtack.school.hospital.dto.requests.DaySchedule("Tue", "09:00", "14:00"));
        weekDaysSchedule.add(new net.thumbtack.school.hospital.dto.requests.DaySchedule("Wed", "10:00", "12:00"));
        weekDaysSchedule.add(new net.thumbtack.school.hospital.dto.requests.DaySchedule("Thu", "13:00", "16:00"));
        weekDaysSchedule.add(new net.thumbtack.school.hospital.dto.requests.DaySchedule("Fri", "12:00", "17:00"));


        RegistrationDoctorDtoRequest requestRegistration2 = new RegistrationDoctorDtoRequest(
                "Paper",
                "Pots",
                "Harley",
                "Cardiologist",
                "3-078",
                "IronWoman",
                "ironwoman.123",
                "16-06-2020",
                "16-08-2020",
                null,
                weekDaysSchedule,
                30
        );

        RegistrationDoctorDtoResponse responseRegistration1 = doctorService.registerDoctor(requestRegistration1, cookies);
        RegistrationDoctorDtoResponse responseRegistration2 = doctorService.registerDoctor(requestRegistration2, cookies);

        doctorService.deletingDoctor(new DeletingDoctorDtoRequest("16-08-2020"), responseRegistration1.getId(), cookies);

        List<RegistrationDoctorDtoResponse> getInfoResponseActual = doctorService.getInformationAboutDoctors(
                "yes",
                null,
                "16-06-2020",
                "16-08-2020",
                cookies
        );

        assertEquals(getInfoResponseActual.get(0).getFirstName(), responseRegistration2.getFirstName());
        assertEquals(getInfoResponseActual.get(0).getLastName(), responseRegistration2.getLastName());
        assertEquals(getInfoResponseActual.get(0).getPatronymic(), responseRegistration2.getPatronymic());
        assertEquals(getInfoResponseActual.get(0).getSpeciality(), responseRegistration2.getSpeciality());
        assertEquals(getInfoResponseActual.get(0).getRoom(), responseRegistration2.getRoom());
        assertEquals(getInfoResponseActual.get(0).getSchedule(), responseRegistration2.getSchedule());
    }

    @Test
    public void AppointmentMedicalCommission() throws ServerException {
        String cookiePatient = UUID.randomUUID().toString();

        // Register Patient

        RegistrationPatientDtoRequest requestPatientRegister = new RegistrationPatientDtoRequest(
                "Alexandr",
                "Stanley",
                "Ivanovich",
                "stanIvanov@mail.ru",
                "Mira St. 43",
                "8904235687",
                "IronVanadium1373",
                "ironvanadium.1234"
        );

        RegistrationPatientDtoResponse responsePatientReqister = patientService.registerPatient(requestPatientRegister, cookiePatient);

        Cookie[] cookiesMainAdmin = new Cookie[1];
        String cMainAdmin = getAdminCookie();
        cookiesMainAdmin[0] = new Cookie("JAVASESSIONID", cMainAdmin);

        // Doctor 1

        List<String> weekDays1 = new ArrayList<>();
        weekDays1.add("Mon");
        weekDays1.add("Tue");
        weekDays1.add("Wed");
        weekDays1.add("Thu");
        weekDays1.add("Fri");

        WeekSchedule weekSchedule1 = new WeekSchedule("09:00", "18:00", weekDays1);

        RegistrationDoctorDtoRequest requestDoctorRegister1 = new RegistrationDoctorDtoRequest(
                "Obadiah",
                "Stane",
                "Ivanov",
                "Surgeon",
                "1-001",
                "IronMonger1373",
                "ironmonger.1234",
                "16-09-2020",
                "16-11-2020",
                weekSchedule1,
                null,
                20
        );

        RegistrationDoctorDtoResponse responseDoctorRegister1 = doctorService.registerDoctor(requestDoctorRegister1, cookiesMainAdmin);

        LoginDtoRequest loginDtoRequest = new LoginDtoRequest(requestDoctorRegister1.getLogin(), requestDoctorRegister1.getPassword());

        String cookieDoctor = UUID.randomUUID().toString();

        RegistrationDoctorDtoResponse responseActual = (RegistrationDoctorDtoResponse)commonService.login(loginDtoRequest, cookieDoctor);


        //

        // Doctor 2


        WeekSchedule weekSchedule2 = new WeekSchedule("09:00", "18:00", null);

        RegistrationDoctorDtoRequest request2 = new RegistrationDoctorDtoRequest(
                "Antony",
                "Stark",
                "Hovard",
                "Dentist",
                "9-003",
                "IronMan777",
                "ironman.057",
                "16-09-2020",
                "16-11-2020",
                weekSchedule2,
                null,
                30
        );


        RegistrationDoctorDtoResponse responseDoctorRegister2 = doctorService.registerDoctor(request2, cookiesMainAdmin);

        // Doctor 3

        WeekSchedule weekSchedule3 = new WeekSchedule("09:00", "18:00", null);

        RegistrationDoctorDtoRequest request3 = new RegistrationDoctorDtoRequest(
                "Paper",
                "Pots",
                "Harley",
                "Cardiologist",
                "3-078",
                "IronWoman",
                "ironwoman.123",
                "16-09-2020",
                "16-11-2020",
                weekSchedule3,
                null,
                30
        );

        RegistrationDoctorDtoResponse responseDoctorRegister3 = doctorService.registerDoctor(request3, cookiesMainAdmin);

        //

        MakeAnAppointmentWithDoctorRequest requestMakeAnAppointmentWithDoctor1 = new MakeAnAppointmentWithDoctorRequest(
                responseDoctorRegister1.getId(),
                null,
                "16-09-2020",
                "10:20"
        );

        MakeAnAppointmentWithDoctorRequest requestMakeAnAppointmentWithDoctor2 = new MakeAnAppointmentWithDoctorRequest(
                responseDoctorRegister1.getId(),
                null,
                "17-09-2020",
                "09:00"
        );

        MakeAnAppointmentWithDoctorRequest requestMakeAnAppointmentWithDoctor3= new MakeAnAppointmentWithDoctorRequest(
                responseDoctorRegister2.getId(),
                null,
                "18-09-2020",
                "09:00"
        );



        Cookie[] cookiesPatient = new Cookie[1];
        cookiesPatient[0] = new Cookie("JAVASESSIONID", cookiePatient);

        patientService.makeAnAppointmentWithDoctor(requestMakeAnAppointmentWithDoctor1, cookiesPatient);
        patientService.makeAnAppointmentWithDoctor(requestMakeAnAppointmentWithDoctor2, cookiesPatient);
        patientService.makeAnAppointmentWithDoctor(requestMakeAnAppointmentWithDoctor3, cookiesPatient);

        //
        List<Integer> doctorIds = new ArrayList<>();
        doctorIds.add(responseDoctorRegister2.getId());
        doctorIds.add(responseDoctorRegister3.getId());
        AppointmentMedicalCommissionDtoRequest request = new AppointmentMedicalCommissionDtoRequest(
          responsePatientReqister.getId(),
                doctorIds,
                responseDoctorRegister1.getRoom(),
                "22-09-2020",
                "09:00",
                30
        );


        Cookie[] cookiesDoctor = new Cookie[1];
        cookiesDoctor[0] = new Cookie("JAVASESSIONID", cookieDoctor);

        AppointmentMedicalCommissionDtoResponse response = doctorService.appointmentMedicalCommission(request, cookiesDoctor);

        String ticket = "СD<";
        for (int d : doctorIds) {
            ticket += d + ">D<";
        }
        ticket += "<" + request.getDate() + request.getTime();



        assertEquals(ticket, response.getTicket());

    }

    private static int getDay(String day) {
        if (day.equalsIgnoreCase("Mon"))
            return 1;
        if (day.equalsIgnoreCase("Tue"))
            return 2;
        if (day.equalsIgnoreCase("Wed"))
            return 3;
        if (day.equalsIgnoreCase("Thu"))
            return 4;
        if (day.equalsIgnoreCase("Fri"))
            return 5;
        return 0;
    }

}



