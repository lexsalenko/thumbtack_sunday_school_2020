package net.thumbtack.school.hospital.TestService;


import net.thumbtack.school.hospital.HospitalServer;
import net.thumbtack.school.hospital.dao.AdminDao;
import net.thumbtack.school.hospital.dao.DoctorDao;
import net.thumbtack.school.hospital.dao.PatientDao;
import net.thumbtack.school.hospital.dto.requests.*;
import net.thumbtack.school.hospital.dto.responses.MakeAnAppointmentWithDoctorResponse;
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
import static org.junit.Assert.assertNull;

@ExtendWith(SpringExtension.class)
@RunWith(SpringRunner.class)
@SpringBootTest(classes = HospitalServer.class, webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class TestPatientService {

    @Autowired
    private CommonService commonService;

    @Autowired
    private PatientService patientService;

    @Autowired
    private DoctorService doctorService;

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
    public void TestRegisterPatient() {

        String cookie = UUID.randomUUID().toString();

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

        RegistrationPatientDtoResponse response = patientService.registerPatient(request, cookie);

        assertEquals(request.getFirstName(), response.getFirstName());
        assertEquals(request.getLastName(), response.getLastName());
        assertEquals(request.getPatronymic(), response.getPatronymic());
        assertEquals(request.getEmail(), response.getEmail());
        assertEquals(request.getAddress(), response.getAddress());
        assertEquals(request.getPhone(), response.getPhone());

    }


    @Test
    public void TestGetInformationAboutPatient() {
        String cookie = UUID.randomUUID().toString();

        RegistrationPatientDtoRequest requestRegister = new RegistrationPatientDtoRequest(
                "Obadiah",
                "Stane",
                "Ivanov",
                "stanIvanov@mail.ru",
                "Mira St. 43",
                "8904235687",
                "IronMonger1373",
                "ironmonger.1234"
        );

        RegistrationPatientDtoResponse responseReqister = patientService.registerPatient(requestRegister, cookie);

        Cookie[] cookies = new Cookie[1];
        String c = getAdminCookie();
        cookies[0] = new Cookie("JAVASESSIONID", c);

        RegistrationPatientDtoResponse responseGetInfo = patientService.getInformationAboutPatient(responseReqister.getId(), cookies);

        assertEquals(responseReqister.getFirstName(), responseGetInfo.getFirstName());
        assertEquals(responseReqister.getLastName(), responseGetInfo.getLastName());
        assertEquals(responseReqister.getPatronymic(), responseGetInfo.getPatronymic());
        assertEquals(responseReqister.getEmail(), responseGetInfo.getEmail());
        assertEquals(responseReqister.getAddress(), responseGetInfo.getAddress());
        assertEquals(responseReqister.getPhone(), responseGetInfo.getPhone());

    }

    @Test
    public void TestEditingPatientProfile(){
        String cookie = UUID.randomUUID().toString();

        RegistrationPatientDtoRequest requestRegister = new RegistrationPatientDtoRequest(
                "Obadiah",
                "Stane",
                "Ivanov",
                "stanIvanov@mail.ru",
                "Mira St. 43",
                "8904235687",
                "IronMonger1373",
                "ironmonger.1234"
        );

        RegistrationPatientDtoResponse responseReqister = patientService.registerPatient(requestRegister, cookie);

        LoginDtoRequest loginDtoRequest = new LoginDtoRequest(
                "IronMonger1373",
                "ironmonger.1234"
        );


        Cookie[] cookies = new Cookie[1];
        cookies[0] = new Cookie("JAVASESSIONID", cookie);

        EditingPatientProfileDtoRequest requestEditing = new EditingPatientProfileDtoRequest(
                "Ivan",
                "Stane",
                "Ivanov",
                "stanIvanov@mail.ru",
                "Mira St. 43",
                "8904235687",
                "ironmonger.1234",
                "IronMongerqwert1373"
        );

        RegistrationPatientDtoResponse responseEditing = patientService.editingPatientProfile(requestEditing, cookies);

        assertEquals(requestEditing.getFirstName(), responseEditing.getFirstName());
        assertEquals(requestEditing.getLastName(), responseEditing.getLastName());
        assertEquals(requestEditing.getPatronymic(), responseEditing.getPatronymic());
        assertEquals(requestEditing.getEmail(), responseEditing.getEmail());
        assertEquals(requestEditing.getAddress(), responseEditing.getAddress());
        assertEquals(requestEditing.getPhone(), responseEditing.getPhone());

    }

    @Test
    public void TestMakeAnAppointmentWithDoctor() throws ServerException {

        String cookie = UUID.randomUUID().toString();

        RegistrationPatientDtoRequest requestPatientRegister = new RegistrationPatientDtoRequest(
                "Obadiah",
                "Stane",
                "Ivanov",
                "stanIvanov@mail.ru",
                "Mira St. 43",
                "8904235687",
                "IronMonger1373",
                "ironmonger.1234"
        );

        RegistrationPatientDtoResponse responsePatientReqister = patientService.registerPatient(requestPatientRegister, cookie);

        Cookie[] cookiesMainAdmin = new Cookie[1];
        String cMainAdmin = getAdminCookie();
        cookiesMainAdmin[0] = new Cookie("JAVASESSIONID", cMainAdmin);

        // Doctor

        List<String> weekDays1 = new ArrayList<>();
        weekDays1.add("Mon");
        weekDays1.add("Tue");
        weekDays1.add("Wed");
        weekDays1.add("Thu");
        weekDays1.add("Fri");

        WeekSchedule weekSchedule1 = new WeekSchedule("09:00", "18:00", weekDays1);

        RegistrationDoctorDtoRequest requestDoctorRegister = new RegistrationDoctorDtoRequest(
                "Obadiah",
                "Stane",
                "Ivanov",
                "Surgeon",
                "1-001",
                "IronMonger1373",
                "ironmonger.1234",
                "16-06-2020",
                "17-06-2020",
                weekSchedule1,
                null,
                20
        );


        LocalDate dateStart1 = LocalDate.parse(requestDoctorRegister.getDateStart(), formatterDate);
        LocalDate dateEnd1 = LocalDate.parse(requestDoctorRegister.getDateEnd(), formatterDate);

        List<net.thumbtack.school.hospital.dto.responses.Schedule> schedule1 = new ArrayList<>();

        for (LocalDate dateDay = dateStart1; !dateDay.equals(dateEnd1); dateDay = dateDay.plusDays(1)) {
            if (!dateDay.getDayOfWeek().equals(DayOfWeek.SATURDAY) && !dateDay.getDayOfWeek().equals(DayOfWeek.SUNDAY)) {
                List<net.thumbtack.school.hospital.dto.responses.DaySchedule> daySchedule = new ArrayList<>();
                LocalTime timestart = LocalTime.parse(requestDoctorRegister.getWeekSchedule().getTimeStart(), formatterTime);
                LocalTime timeend = LocalTime.parse(requestDoctorRegister.getWeekSchedule().getTimeEnd(), formatterTime);
                int duration = requestDoctorRegister.getDuration();
                for (LocalTime time = timestart; !time.equals(timeend); time = time.plusMinutes(duration)) {
                    daySchedule.add(new net.thumbtack.school.hospital.dto.responses.DaySchedule(time, null));
                }
                schedule1.add(new Schedule(dateDay, daySchedule));
            }
        }

        RegistrationDoctorDtoResponse responseDoctorReqister = doctorService.registerDoctor(requestDoctorRegister, cookiesMainAdmin);

        MakeAnAppointmentWithDoctorRequest requestMakeAnAppointmentWithDoctor = new MakeAnAppointmentWithDoctorRequest(
                responseDoctorReqister.getId(),
                null,
                "16-06-2020",
                "10:20"
        );

        Cookie[] cookies = new Cookie[1];
        cookies[0] = new Cookie("JAVASESSIONID", cookie);

        patientService.makeAnAppointmentWithDoctor(requestMakeAnAppointmentWithDoctor, cookies);

        //

        Doctor doctor = doctorDao.getById(responseDoctorReqister.getId());
        Patient patient = patientDao.getById(responsePatientReqister.getId());


        LocalDate appointmentDate = LocalDate.parse("16-06-2020", formatterDate);
        LocalTime appointmentTime = LocalTime.parse("10:20", formatterTime);

        String ticket =
                "D<" + responseDoctorReqister.getId() + ">" +
                        appointmentDate.getDayOfWeek() + appointmentDate.getMonth() + appointmentDate.getYear() +
                        appointmentTime.getHour() + appointmentTime.getMinute();


        List<Schedule> schedule = new ArrayList<>();
        for (DaySchedule dSchedule : doctor.getDaySchedule()) {
            List<net.thumbtack.school.hospital.dto.responses.DaySchedule> daySchedule = new ArrayList<>();
            for (Appointment appointment : dSchedule.getAppointmentList()) {
                if (appointment.getPatient() != null) {
                    net.thumbtack.school.hospital.dto.responses.Patient p = new net.thumbtack.school.hospital.dto.responses.Patient(
                            patient.getId(),
                            patient.getUser().getFirstName(),
                            patient.getUser().getLastName(),
                            patient.getUser().getPatronymic(),
                            patient.getEmail(),
                            patient.getAddress(),
                            patient.getPhone()
                    );
                    daySchedule.add(new net.thumbtack.school.hospital.dto.responses.DaySchedule(appointment.getTime(), p));
                } else {
                    daySchedule.add(new net.thumbtack.school.hospital.dto.responses.DaySchedule(appointment.getTime(), null));
                }
            }
            schedule.add(new Schedule(dSchedule.getDateDay(), daySchedule));
        }

        RegistrationDoctorDtoResponse responseMakeAnAppointmentWithDoctorExpected = new RegistrationDoctorDtoResponse(
                doctor.getId(),
                doctor.getUser().getFirstName(),
                doctor.getUser().getLastName(),
                doctor.getUser().getPatronymic(),
                doctor.getSpeciality().getSpeciality(),
                doctor.getRoom().getRoom(),
                schedule
        );

        //

        RegistrationDoctorDtoResponse responseMakeAnAppointmentWithDoctorActual = doctorService.getInformationAboutDoctor(
                responseDoctorReqister.getId(),
                "yes",
                "16-06-2020",
                "17-06-2020",
                cookies
        );


        assertEquals(responseMakeAnAppointmentWithDoctorExpected.getFirstName(), responseMakeAnAppointmentWithDoctorActual.getFirstName());
        assertEquals(responseMakeAnAppointmentWithDoctorExpected.getLastName(), responseMakeAnAppointmentWithDoctorActual.getLastName());
        assertEquals(responseMakeAnAppointmentWithDoctorExpected.getPatronymic(), responseMakeAnAppointmentWithDoctorActual.getPatronymic());
        assertEquals(responseMakeAnAppointmentWithDoctorExpected.getSpeciality(), responseMakeAnAppointmentWithDoctorActual.getSpeciality());
        assertEquals(responseMakeAnAppointmentWithDoctorExpected.getRoom(), responseMakeAnAppointmentWithDoctorActual.getRoom());
        assertEquals(responseMakeAnAppointmentWithDoctorExpected.getSchedule(), responseMakeAnAppointmentWithDoctorActual.getSchedule());

    }


    @Test
    public void CancelingDoctorAppointment() throws ServerException
    {
        String cookie = UUID.randomUUID().toString();

        RegistrationPatientDtoRequest requestPatientRegister = new RegistrationPatientDtoRequest(
                "Obadiah",
                "Stane",
                "Ivanov",
                "stanIvanov@mail.ru",
                "Mira St. 43",
                "8904235687",
                "IronMonger1373",
                "ironmonger.1234"
        );

        RegistrationPatientDtoResponse responsePatientReqister = patientService.registerPatient(requestPatientRegister, cookie);

        Cookie[] cookiesMainAdmin = new Cookie[1];
        String cMainAdmin = getAdminCookie();
        cookiesMainAdmin[0] = new Cookie("JAVASESSIONID", cMainAdmin);

        // Doctor

        List<String> weekDays1 = new ArrayList<>();
        weekDays1.add("Mon");
        weekDays1.add("Tue");
        weekDays1.add("Wed");
        weekDays1.add("Thu");
        weekDays1.add("Fri");

        WeekSchedule weekSchedule1 = new WeekSchedule("09:00", "18:00", weekDays1);

        RegistrationDoctorDtoRequest requestDoctorRegister = new RegistrationDoctorDtoRequest(
                "Obadiah",
                "Stane",
                "Ivanov",
                "Surgeon",
                "1-001",
                "IronMonger1373",
                "ironmonger.1234",
                "16-06-2020",
                "17-06-2020",
                weekSchedule1,
                null,
                20
        );


        LocalDate dateStart1 = LocalDate.parse(requestDoctorRegister.getDateStart(), formatterDate);
        LocalDate dateEnd1 = LocalDate.parse(requestDoctorRegister.getDateEnd(), formatterDate);

        List<net.thumbtack.school.hospital.dto.responses.Schedule> schedule1 = new ArrayList<>();

        for (LocalDate dateDay = dateStart1; !dateDay.equals(dateEnd1); dateDay = dateDay.plusDays(1)) {
            if (!dateDay.getDayOfWeek().equals(DayOfWeek.SATURDAY) && !dateDay.getDayOfWeek().equals(DayOfWeek.SUNDAY)) {
                List<net.thumbtack.school.hospital.dto.responses.DaySchedule> daySchedule = new ArrayList<>();
                LocalTime timestart = LocalTime.parse(requestDoctorRegister.getWeekSchedule().getTimeStart(), formatterTime);
                LocalTime timeend = LocalTime.parse(requestDoctorRegister.getWeekSchedule().getTimeEnd(), formatterTime);
                int duration = requestDoctorRegister.getDuration();
                for (LocalTime time = timestart; !time.equals(timeend); time = time.plusMinutes(duration)) {
                    daySchedule.add(new net.thumbtack.school.hospital.dto.responses.DaySchedule(time, null));
                }
                schedule1.add(new Schedule(dateDay, daySchedule));
            }
        }

        RegistrationDoctorDtoResponse responseDoctorReqister = doctorService.registerDoctor(requestDoctorRegister, cookiesMainAdmin);

        MakeAnAppointmentWithDoctorRequest requestMakeAnAppointmentWithDoctor = new MakeAnAppointmentWithDoctorRequest(
                responseDoctorReqister.getId(),
                null,
                "16-06-2020",
                "10:20"
        );

        Cookie[] cookies = new Cookie[1];
        cookies[0] = new Cookie("JAVASESSIONID", cookie);

        patientService.makeAnAppointmentWithDoctor(requestMakeAnAppointmentWithDoctor, cookies);

        //

        Doctor doctor = doctorDao.getById(responseDoctorReqister.getId());
        Patient patient = patientDao.getById(responsePatientReqister.getId());


        LocalDate appointmentDate = LocalDate.parse("16-06-2020", formatterDate);
        LocalTime appointmentTime = LocalTime.parse("10:20", formatterTime);

        String ticket =
                "D<" + responseDoctorReqister.getId() + ">" +
                        appointmentDate.getDayOfWeek() + appointmentDate.getMonth() + appointmentDate.getYear() +
                        appointmentTime.getHour() + appointmentTime.getMinute();


        List<Schedule> schedule = new ArrayList<>();
        for (DaySchedule dSchedule : doctor.getDaySchedule()) {
            List<net.thumbtack.school.hospital.dto.responses.DaySchedule> daySchedule = new ArrayList<>();
            for (Appointment appointment : dSchedule.getAppointmentList()) {
                if (appointment.getPatient() != null) {
                    net.thumbtack.school.hospital.dto.responses.Patient p = new net.thumbtack.school.hospital.dto.responses.Patient(
                            patient.getId(),
                            patient.getUser().getFirstName(),
                            patient.getUser().getLastName(),
                            patient.getUser().getPatronymic(),
                            patient.getEmail(),
                            patient.getAddress(),
                            patient.getPhone()
                    );
                    daySchedule.add(new net.thumbtack.school.hospital.dto.responses.DaySchedule(appointment.getTime(), p));
                } else {
                    daySchedule.add(new net.thumbtack.school.hospital.dto.responses.DaySchedule(appointment.getTime(), null));
                }
            }
            schedule.add(new Schedule(dSchedule.getDateDay(), daySchedule));
        }

        RegistrationDoctorDtoResponse responseMakeAnAppointmentWithDoctorExpected = new RegistrationDoctorDtoResponse(
                doctor.getId(),
                doctor.getUser().getFirstName(),
                doctor.getUser().getLastName(),
                doctor.getUser().getPatronymic(),
                doctor.getSpeciality().getSpeciality(),
                doctor.getRoom().getRoom(),
                schedule
        );

        //

        RegistrationDoctorDtoResponse responseMakeAnAppointmentWithDoctorActual = doctorService.getInformationAboutDoctor(
                responseDoctorReqister.getId(),
                "yes",
                "16-06-2020",
                "17-06-2020",
                cookies
        );


        assertEquals(responseMakeAnAppointmentWithDoctorExpected.getFirstName(), responseMakeAnAppointmentWithDoctorActual.getFirstName());
        assertEquals(responseMakeAnAppointmentWithDoctorExpected.getLastName(), responseMakeAnAppointmentWithDoctorActual.getLastName());
        assertEquals(responseMakeAnAppointmentWithDoctorExpected.getPatronymic(), responseMakeAnAppointmentWithDoctorActual.getPatronymic());
        assertEquals(responseMakeAnAppointmentWithDoctorExpected.getSpeciality(), responseMakeAnAppointmentWithDoctorActual.getSpeciality());
        assertEquals(responseMakeAnAppointmentWithDoctorExpected.getRoom(), responseMakeAnAppointmentWithDoctorActual.getRoom());
        assertEquals(responseMakeAnAppointmentWithDoctorExpected.getSchedule(), responseMakeAnAppointmentWithDoctorActual.getSchedule());

        // Canceling Doctor Appointment

        patientService.cancelingAnAppointmentWithDoctor(ticket, cookies);

        RegistrationDoctorDtoResponse responseСancelingAnAppointmentWithDoctor = doctorService.getInformationAboutDoctor(
                responseDoctorReqister.getId(),
                "yes",
                "16-06-2020",
                "17-06-2020",
                cookies
        );



        assertEquals(responseDoctorReqister.getFirstName(), responseСancelingAnAppointmentWithDoctor.getFirstName());
        assertEquals(responseDoctorReqister.getLastName(), responseСancelingAnAppointmentWithDoctor.getLastName());
        assertEquals(responseDoctorReqister.getPatronymic(), responseСancelingAnAppointmentWithDoctor.getPatronymic());
        assertEquals(responseDoctorReqister.getSpeciality(), responseСancelingAnAppointmentWithDoctor.getSpeciality());
        assertEquals(responseDoctorReqister.getRoom(), responseСancelingAnAppointmentWithDoctor.getRoom());
        assertEquals(responseDoctorReqister.getSchedule(), responseСancelingAnAppointmentWithDoctor.getSchedule());


    }




}
