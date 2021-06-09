package net.thumbtack.school.hospital.service;

import net.thumbtack.school.hospital.dao.*;
import net.thumbtack.school.hospital.daoimpl.*;
import net.thumbtack.school.hospital.dto.requests.LoginDtoRequest;
import net.thumbtack.school.hospital.dto.responses.*;
import net.thumbtack.school.hospital.model.*;
import net.thumbtack.school.hospital.model.DaySchedule;
import net.thumbtack.school.hospital.model.Patient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.Cookie;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class CommonService {

    private CommonDao commonDao;

    private UserDao userDao;

    private AdminDao adminDao;

    private PatientDao patientDao;

    private DoctorDao doctorDao;

    @Autowired
    public CommonService(CommonDaoImpl commonDao, UserDaoImpl userDao, AdminDaoImpl adminDao, PatientDaoImpl patientDao, DoctorDaoImpl doctorDao) {
        this.commonDao = commonDao;
        this.userDao = userDao;
        this.adminDao = adminDao;
        this.patientDao = patientDao;
        this.doctorDao = doctorDao;
    }

    public CommonResponse login(LoginDtoRequest loginDtoRequest, String token) {
        List<Login> loginsAndPasswords = userDao.getAllLoginsAndPasswords();

        for (Login login : loginsAndPasswords) {
            if (loginDtoRequest.getLogin().equals(login.getLogin()) && loginDtoRequest.getPassword().equals(login.getPassword())) {
                User user = userDao.getById(login.getUserid());
                commonDao.insertSession(new Session(user.getId(), token));
                if (user.getUserType().equals(UserType.ADMIN)) {
                    int adminId = adminDao.getAdminIdByUserId(login.getUserid());
                    Admin admin = adminDao.getById(adminId);
                    return new RegistrationAdministratorDtoResponse(
                            admin.getId(),
                            admin.getUser().getFirstName(),
                            admin.getUser().getLastName(),
                            admin.getUser().getPatronymic(),
                            admin.getPosition()
                    );
                }
                if (user.getUserType().equals(UserType.DOCTOR)) {
                    Doctor doctor = doctorDao.getByUserId(login.getUserid());
                    List<Schedule> schedule = new ArrayList<>();
                    for (DaySchedule dSchedule : doctor.getDaySchedule()) {
                        List<net.thumbtack.school.hospital.dto.responses.DaySchedule> daySchedule = new ArrayList<>();
                        for (Appointment appointment : dSchedule.getAppointmentList()) {
                            if(appointment.getPatient() != null) {
                                net.thumbtack.school.hospital.dto.responses.Patient patient = new net.thumbtack.school.hospital.dto.responses.Patient(
                                        appointment.getPatient().getId(),
                                        appointment.getPatient().getUser().getFirstName(),
                                        appointment.getPatient().getUser().getLastName(),
                                        appointment.getPatient().getUser().getPatronymic(),
                                        appointment.getPatient().getEmail(),
                                        appointment.getPatient().getAddress(),
                                        appointment.getPatient().getPhone()
                                );

                            daySchedule.add(new net.thumbtack.school.hospital.dto.responses.DaySchedule(appointment.getTime(), patient));
                            } else {
                                daySchedule.add(new net.thumbtack.school.hospital.dto.responses.DaySchedule(appointment.getTime(), null));
                            }
                        }
                        schedule.add(new Schedule(dSchedule.getDateDay(), daySchedule));
                    }

                    return new RegistrationDoctorDtoResponse(
                            doctor.getUser().getFirstName(),
                            doctor.getUser().getLastName(),
                            doctor.getUser().getPatronymic(),
                            doctor.getSpeciality().getSpeciality(),
                            doctor.getRoom().getRoom(),
                            schedule
                    );
                }
                if (user.getUserType().equals(UserType.PATIENT)) {
                    Patient patient = patientDao.getByUserId(user.getId());
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
            }
        }
        return null;
    }

    public void logout(Cookie[] cookies) {

        String cookie = "";
        for(Cookie c : cookies) {
            if(c.getName().equals("JAVASESSIONID")) {
                cookie = c.getValue();
            }
        }

        commonDao.deleteSession(cookie);
    }

    public CommonResponse getInformationAboutCurrentUser(Cookie[] cookies) {

        String cookie = "";
        for(Cookie c : cookies) {
            if(c.getName().equals("JAVASESSIONID")) {
                cookie = c.getValue();
            }
        }

        int id = commonDao.getBySessionId(cookie).getPersonId();
        User user = userDao.getById(id);
        commonDao.insertSession(new Session(user.getId(), cookie));
        if (user.getUserType().equals(UserType.ADMIN)) {
            Admin admin = adminDao.getByUserId(user.getId());
            return new RegistrationAdministratorDtoResponse(
                    admin.getId(),
                    admin.getUser().getFirstName(),
                    admin.getUser().getLastName(),
                    admin.getUser().getPatronymic(),
                    admin.getPosition()
            );
        }
        if (user.getUserType().equals(UserType.DOCTOR)) {
            Doctor doctor = doctorDao.getByUserId(user.getId());
            return new RegistrationDoctorDtoResponse(
                    doctor.getUser().getFirstName(),
                    doctor.getUser().getLastName(),
                    doctor.getUser().getPatronymic(),
                    doctor.getSpeciality().getSpeciality(),
                    doctor.getRoom().getRoom(),
                    null // исправить на нормальное расписание
            );
        }
        if (user.getUserType().equals(UserType.PATIENT)) {
            Patient patient = patientDao.getByUserId(user.getId());
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

        return null;

    }

    public void clearDataBase() {
        userDao.deleteAll();
        adminDao.deleteAll();
        patientDao.deleteAll();
        doctorDao.deleteAll();
        doctorDao.deleteAllSpecialties();
        doctorDao.deleteAllRooms();
    }

    public void insertRoomsAndSpecialties(List<Speciality> specialities, List<Room> rooms) {
        doctorDao.insertRooms(rooms);
        doctorDao.insertSpecialties(specialities);
    }


}
