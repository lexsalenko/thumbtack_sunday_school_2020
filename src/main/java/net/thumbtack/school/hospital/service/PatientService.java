package net.thumbtack.school.hospital.service;

import net.thumbtack.school.hospital.dao.CommonDao;
import net.thumbtack.school.hospital.dao.DoctorDao;
import net.thumbtack.school.hospital.dao.PatientDao;
import net.thumbtack.school.hospital.dao.UserDao;
import net.thumbtack.school.hospital.daoimpl.CommonDaoImpl;
import net.thumbtack.school.hospital.daoimpl.DoctorDaoImpl;
import net.thumbtack.school.hospital.daoimpl.PatientDaoImpl;
import net.thumbtack.school.hospital.daoimpl.UserDaoImpl;
import net.thumbtack.school.hospital.dto.requests.EditingPatientProfileDtoRequest;
import net.thumbtack.school.hospital.dto.requests.MakeAnAppointmentWithDoctorRequest;
import net.thumbtack.school.hospital.dto.requests.RegistrationPatientDtoRequest;
import net.thumbtack.school.hospital.dto.responses.GetListAppointmentTicketsWithDoctorDtoResponse;
import net.thumbtack.school.hospital.dto.responses.MakeAnAppointmentWithDoctorResponse;
import net.thumbtack.school.hospital.dto.responses.RegistrationPatientDtoResponse;
import net.thumbtack.school.hospital.exception.ServerErrorCode;
import net.thumbtack.school.hospital.exception.ServerException;
import net.thumbtack.school.hospital.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.Cookie;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
@Transactional
public class PatientService {

    private PatientDao patientDao;

    private CommonDao commonDao;

    private UserDao userDao;

    private DoctorDao doctorDao;

    private DateTimeFormatter formatterDate = DateTimeFormatter.ofPattern("dd-MM-yyyy");
    private DateTimeFormatter formatterTime = DateTimeFormatter.ofPattern("HH:mm");

    @Autowired
    public PatientService(PatientDaoImpl patientDao, CommonDaoImpl commonDao, UserDaoImpl userDao, DoctorDaoImpl doctorDao) {
        this.commonDao = commonDao;
        this.userDao = userDao;
        this.patientDao = patientDao;
        this.doctorDao = doctorDao;
    }

    public RegistrationPatientDtoResponse registerPatient(RegistrationPatientDtoRequest patientDtoRequest, String cookie) {

        Patient patient = new Patient(
                new User(
                        UserType.PATIENT,
                        patientDtoRequest.getFirstName(),
                        patientDtoRequest.getLastName(),
                        patientDtoRequest.getPatronymic(),
                        patientDtoRequest.getLogin(),
                        patientDtoRequest.getPassword()
                ),
                patientDtoRequest.getEmail(),
                patientDtoRequest.getAddress(),
                patientDtoRequest.getPhone()
        );

        patient = patientDao.insert(patient);
        commonDao.insertSession(new Session(patient.getUser().getId(), cookie));
        return new RegistrationPatientDtoResponse(
                patient.getId(),
                patient.getUser().getFirstName(),
                patient.getUser().getLastName(),
                patient.getUser().getPatronymic(),
                patient.getEmail(),
                patient.getAddress(),
                patient.getPhone()
        );
    }

    public RegistrationPatientDtoResponse getInformationAboutPatient(int patientId, Cookie[] cookies) {

        String cookie = "";
        for (Cookie c : cookies) {
            if (c.getName().equals("JAVASESSIONID")) {
                cookie = c.getValue();
            }
        }

        int id = commonDao.getBySessionId(cookie).getPersonId();
        User user = userDao.getById(id);

        if (user.getUserType().equals(UserType.ADMIN) || user.getUserType().equals(UserType.DOCTOR)) {
            Patient patient = patientDao.getById(patientId);
            return new RegistrationPatientDtoResponse(
                    patient.getId(),
                    patient.getUser().getFirstName(),
                    patient.getUser().getLastName(),
                    patient.getUser().getPatronymic(),
                    patient.getEmail(),
                    patient.getAddress(),
                    patient.getPhone()
            );
        } else {
            // error
        }
        return null;
    }

    public RegistrationPatientDtoResponse editingPatientProfile(EditingPatientProfileDtoRequest request, Cookie[] cookies) {

        String cookie = "";
        for (Cookie c : cookies) {
            if (c.getName().equals("JAVASESSIONID")) {
                cookie = c.getValue();
            }
        }

        int id = commonDao.getBySessionId(cookie).getPersonId();
        User user = userDao.getById(id);

        if (!user.getUserType().equals(UserType.PATIENT)) {
            // error
        }

        Patient patient = patientDao.getByUserId(user.getId());

        if (!patient.getUser().getPassword().equals(request.getOldPassword())) {
            return null;
        }


        patient = new Patient(
                patient.getId(),
                new User(
                        patient.getUser().getId(),
                        UserType.PATIENT,
                        request.getFirstName(),
                        request.getLastName(),
                        request.getPatronymic(),
                        patient.getUser().getLogin(),
                        request.getNewPassword()
                ),
                request.getEmail(),
                request.getAddress(),
                request.getPhone()
        );

        patient = patientDao.update(patient);

        return new RegistrationPatientDtoResponse(
                patient.getId(),
                patient.getUser().getFirstName(),
                patient.getUser().getLastName(),
                patient.getUser().getPatronymic(),
                patient.getEmail(),
                patient.getAddress(),
                patient.getPhone()
        );
    }


    public MakeAnAppointmentWithDoctorResponse makeAnAppointmentWithDoctor(MakeAnAppointmentWithDoctorRequest request, Cookie[] cookies) throws ServerException {

        String cookie = "";
        for (Cookie c : cookies) {
            if (c.getName().equals("JAVASESSIONID")) {
                cookie = c.getValue();
            }
        }

        int id = commonDao.getBySessionId(cookie).getPersonId();
        User user = userDao.getById(id);
        Patient patient = null;
        if (user.getUserType().equals(UserType.PATIENT)) {
            patient = patientDao.getByUserId(user.getId());
        } else {
            throw new ServerException(
                    ServerErrorCode.ACCESS_DENIED,
                    "patient",
                    ServerErrorCode.ACCESS_DENIED.getErrorString()
            );
        }


        LocalDate appointmentDate = LocalDate.parse(request.getDate(), formatterDate);
        LocalTime appointmentTime = LocalTime.parse(request.getTime(), formatterTime);

        String ticket;
        int doctorId;
        String firstName;
        String lastName;
        String patronymic;
        String speciality;
        String room;
        String date;
        String time;

        if (request.getDoctorId() != 0 && request.getSpeciality() != null) {
            if (request.getSpeciality() == null) {
                throw new ServerException(
                        ServerErrorCode.TOO_MANY_PARAMETERS,
                        "parameters",
                        ServerErrorCode.TOO_MANY_PARAMETERS.getErrorString()
                );
            }
        } else if (request.getDoctorId() != 0) {

            Doctor doctor = doctorDao.getById(request.getDoctorId());
            ticket =
                    "D<" + doctor.getId() + ">" +
                            appointmentDate.getDayOfWeek() + appointmentDate.getMonth() + appointmentDate.getYear() +
                            appointmentTime.getHour() + appointmentTime.getMinute();
            for (DaySchedule daySchedule : doctor.getDaySchedule()) {
                if (daySchedule.getDateDay().equals(appointmentDate)) {
                    for (Appointment appointment : daySchedule.getAppointmentList()) {
                        if (appointment.getTime().equals(appointmentTime) && appointment.getPatient() == null) {
                            appointment.setPatient(new Patient(
                                    patient.getId(),
                                    new User(
                                            patient.getUser().getId(),
                                            patient.getUser().getUserType(),
                                            patient.getUser().getFirstName(),
                                            patient.getUser().getLastName(),
                                            patient.getUser().getPatronymic(),
                                            patient.getUser().getLogin(),
                                            patient.getUser().getPassword()
                                    ),
                                    patient.getEmail(),
                                    patient.getAddress(),
                                    patient.getPhone()
                            ));
                            appointment.setTicket(ticket);
                        }
                    }
                }
            }


            doctorId = doctor.getId();
            firstName = doctor.getUser().getFirstName();
            lastName = doctor.getUser().getLastName();
            patronymic = doctor.getUser().getPatronymic();
            speciality = doctor.getSpeciality().getSpeciality();
            room = doctor.getRoom().getRoom();
            date = request.getDate();
            time = request.getTime();

            doctorDao.update(doctor);

            return new MakeAnAppointmentWithDoctorResponse(
                    ticket,
                    doctorId,
                    firstName,
                    lastName,
                    patronymic,
                    speciality,
                    room,
                    date,
                    time
            );
        } else if (request.getSpeciality() != null) {
            List<Doctor> doctorList = doctorDao.getAllDoctorsBySpecialityName(request.getSpeciality());
            Random rand = new Random();
            Doctor doctor = doctorList.get(rand.nextInt(doctorList.size()));

            ticket = "D<" + doctor.getId() + ">" +
                    appointmentDate.getDayOfWeek() + appointmentDate.getMonth() + appointmentDate.getYear() +
                    appointmentTime.getHour() + appointmentTime.getMinute();
            for (DaySchedule daySchedule : doctor.getDaySchedule()) {
                if (daySchedule.getDateDay().equals(appointmentDate)) {
                    for (Appointment appointment : daySchedule.getAppointmentList()) {
                        if (appointment.getTime().equals(appointmentTime) && appointment.getPatient() == null) {
                            appointment.setPatient(new Patient(
                                    patient.getId(),
                                    new User(
                                            patient.getUser().getId(),
                                            patient.getUser().getUserType(),
                                            patient.getUser().getFirstName(),
                                            patient.getUser().getLastName(),
                                            patient.getUser().getPatronymic(),
                                            patient.getUser().getLogin(),
                                            patient.getUser().getPassword()
                                    ),
                                    patient.getEmail(),
                                    patient.getAddress(),
                                    patient.getPhone()
                            ));
                            appointment.setTicket(ticket);
                        }
                    }
                }
            }


            doctorId = doctor.getId();
            firstName = doctor.getUser().getFirstName();
            lastName = doctor.getUser().getLastName();
            patronymic = doctor.getUser().getPatronymic();
            speciality = doctor.getSpeciality().getSpeciality();
            room = doctor.getRoom().getRoom();
            date = request.getDate();
            time = request.getTime();

            doctorDao.update(doctor);

            return new MakeAnAppointmentWithDoctorResponse(
                    ticket,
                    doctorId,
                    firstName,
                    lastName,
                    patronymic,
                    speciality,
                    room,
                    date,
                    time
            );
        } else {
            // error
        }

        return null;
    }

    public void cancelingAnAppointmentWithDoctor(String ticket, Cookie[] cookies) throws ServerException {
        String cookie = "";
        for (Cookie c : cookies) {
            if (c.getName().equals("JAVASESSIONID")) {
                cookie = c.getValue();
            }
        }

        int id = commonDao.getBySessionId(cookie).getPersonId();
        User user = userDao.getById(id);

        if (user.getUserType().equals(UserType.PATIENT)) {

            List<Doctor> doctorList = doctorDao.getAll();

            for (Doctor doctor : doctorList) {
                for (DaySchedule daySchedule : doctor.getDaySchedule()) {
                    for (Appointment appointment : daySchedule.getAppointmentList()) {
                        if (appointment.getPatient() != null && appointment.getTicket().equals(ticket)) {
                            appointment.setPatient(null);
                            appointment.setTicket(null);
                        }
                    }
                }
                doctorDao.update(doctor);
            }

        } else {
            throw new ServerException(
                    ServerErrorCode.ACCESS_DENIED,
                    "patient",
                    ServerErrorCode.ACCESS_DENIED.getErrorString()
            );
        }


    }

    public List<GetListAppointmentTicketsWithDoctorDtoResponse> getListAppointmentTickets(Cookie[] cookies) throws ServerException {

        String cookie = "";
        for (Cookie c : cookies) {
            if (c.getName().equals("JAVASESSIONID")) {
                cookie = c.getValue();
            }
        }

        int id = commonDao.getBySessionId(cookie).getPersonId();
        User user = userDao.getById(id);
        Patient patient = null;
        if (user.getUserType().equals(UserType.PATIENT)) {
            patient = patientDao.getByUserId(user.getId());
        } else {
            throw new ServerException(
                    ServerErrorCode.ACCESS_DENIED,
                    "patient",
                    ServerErrorCode.ACCESS_DENIED.getErrorString()
            );
        }


        List<Appointment> appointments = patientDao.getByPatientId(patient.getId());

        Map<String, Appointment> appointmentMap = new HashMap<>();
        for (Appointment appointment : appointments) {
            appointmentMap.put(appointment.getTicket(), appointment);
        }

        List<Doctor> doctors = doctorDao.getDoctorsByPatientId(patient.getId());

        List<GetListAppointmentTicketsWithDoctorDtoResponse> response = new ArrayList<>();

        for (Doctor doctor : doctors) {
            for (DaySchedule daySchedule : doctor.getDaySchedule()) {
                for (Appointment appointment : daySchedule.getAppointmentList()) {
                    if (appointmentMap.containsKey(appointment.getTicket())) {
                        response.add(new GetListAppointmentTicketsWithDoctorDtoResponse(
                                appointment.getTicket(),
                                doctor.getRoom().getRoom(),
                                daySchedule.getDateDay().toString(),
                                appointment.getTime().toString(),
                                doctor.getId(),
                                doctor.getUser().getFirstName(),
                                doctor.getUser().getLastName(),
                                doctor.getUser().getPatronymic(),
                                doctor.getSpeciality().getSpeciality()
                        ));
                    }
                }
            }
        }

        return response;


    }


}
