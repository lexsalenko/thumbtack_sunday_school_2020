package net.thumbtack.school.hospital.service;

import net.thumbtack.school.hospital.dao.CommonDao;
import net.thumbtack.school.hospital.dao.DoctorDao;
import net.thumbtack.school.hospital.dao.PatientDao;
import net.thumbtack.school.hospital.dao.UserDao;
import net.thumbtack.school.hospital.daoimpl.CommonDaoImpl;
import net.thumbtack.school.hospital.daoimpl.DoctorDaoImpl;
import net.thumbtack.school.hospital.daoimpl.PatientDaoImpl;
import net.thumbtack.school.hospital.daoimpl.UserDaoImpl;
import net.thumbtack.school.hospital.dto.requests.AppointmentMedicalCommissionDtoRequest;
import net.thumbtack.school.hospital.dto.requests.ChangingDoctorScheduleDtoRequest;
import net.thumbtack.school.hospital.dto.requests.DeletingDoctorDtoRequest;
import net.thumbtack.school.hospital.dto.requests.RegistrationDoctorDtoRequest;
import net.thumbtack.school.hospital.dto.responses.AppointmentMedicalCommissionDtoResponse;
import net.thumbtack.school.hospital.dto.responses.RegistrationDoctorDtoResponse;
import net.thumbtack.school.hospital.dto.responses.Schedule;
import net.thumbtack.school.hospital.exception.ServerErrorCode;
import net.thumbtack.school.hospital.exception.ServerException;
import net.thumbtack.school.hospital.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.Cookie;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Service
@Transactional
public class DoctorService {

    private DoctorDao doctorDao;

    private CommonDao commonDao;

    private PatientDao patientDao;

    private UserDao userDao;

    private DateTimeFormatter formatterDate = DateTimeFormatter.ofPattern("dd-MM-yyyy");
    private DateTimeFormatter formatterTime = DateTimeFormatter.ofPattern("HH:mm");

    @Autowired
    public DoctorService(DoctorDaoImpl doctorDao, CommonDaoImpl commonDao, PatientDaoImpl patientDao, UserDaoImpl userDao) {
        this.doctorDao = doctorDao;
        this.commonDao = commonDao;
        this.patientDao = patientDao;
        this.userDao = userDao;
    }

    public RegistrationDoctorDtoResponse registerDoctor(RegistrationDoctorDtoRequest doctorDtoRequest, Cookie[] cookies) throws ServerException {


        String cookie = "";
        for (Cookie c : cookies) {
            if (c.getName().equals("JAVASESSIONID")) {
                cookie = c.getValue();
            }
        }

        int id = commonDao.getBySessionId(cookie).getPersonId();
        User adminUser = userDao.getById(id);
        if (!adminUser.getUserType().equals(UserType.ADMIN)) {
            throw new ServerException(
                    ServerErrorCode.ACCESS_DENIED,
                    "userType",
                    "User " + adminUser.getLogin() + " access denied"

            );
        }

        Room room = isContainsRoom(doctorDtoRequest.getRoom());
        Speciality speciality = isContainsSpeciality(doctorDtoRequest.getSpeciality());

        if (room == null) {
            throw new ServerException(
                    ServerErrorCode.ROOM_DOES_NOT_EXIST,
                    "room",
                    "Room " + doctorDtoRequest.getRoom() + " doesn't exist"
            );
        }

        if (speciality == null) {
            throw new ServerException(
                    ServerErrorCode.SPECIALITY_DOES_NOT_EXIST,
                    "speciality",
                    "Speciality " + doctorDtoRequest.getSpeciality() + " doesn't exist"
            );
        }


        User user = new User(
                UserType.DOCTOR,
                doctorDtoRequest.getFirstName(),
                doctorDtoRequest.getLastName(),
                doctorDtoRequest.getPatronymic(),
                doctorDtoRequest.getLogin(),
                doctorDtoRequest.getPassword());

        LocalDate dateStart = LocalDate.parse(doctorDtoRequest.getDateStart(), formatterDate);
        LocalDate dateEnd = LocalDate.parse(doctorDtoRequest.getDateEnd(), formatterDate);

        List<DaySchedule> dayScheduleList = new ArrayList<>();

        if (doctorDtoRequest.getWeekSchedule() != null) {

            // Врач принимает ежедневно в одно и тоже время
            if (doctorDtoRequest.getWeekSchedule().getWeekDays() == null || doctorDtoRequest.getWeekSchedule().getWeekDays().size() == 0) {
                for (LocalDate dateDay = dateStart; !dateDay.equals(dateEnd); dateDay = dateDay.plusDays(1)) {
                    if (!dateDay.getDayOfWeek().equals(DayOfWeek.SATURDAY) && !dateDay.getDayOfWeek().equals(DayOfWeek.SUNDAY)) {
                        List<Appointment> appointmentList = new ArrayList<>();
                        LocalTime timestart = LocalTime.parse(doctorDtoRequest.getWeekSchedule().getTimeStart(), formatterTime);
                        LocalTime timeend = LocalTime.parse(doctorDtoRequest.getWeekSchedule().getTimeEnd(), formatterTime);
                        int duration = doctorDtoRequest.getDuration();
                        for (LocalTime time = timestart; !time.equals(timeend); time = time.plusMinutes(duration)) {
                            appointmentList.add(new Appointment(time));
                        }
                        dayScheduleList.add(new DaySchedule(dateDay, appointmentList));
                    }
                }

                // Врач принимает по определенным дням в одно и тоже время
            } else {
                List<DayOfWeek> doctorAppointmentDays = new ArrayList<>();
                for (String dayString : doctorDtoRequest.getWeekSchedule().getWeekDays()) {
                    doctorAppointmentDays.add(DayOfWeek.of(getDay(dayString)));
                }

                for (LocalDate dateDay = dateStart; !dateDay.equals(dateEnd); dateDay = dateDay.plusDays(1)) {
                    if (!dateDay.getDayOfWeek().equals(DayOfWeek.SATURDAY) && !dateDay.getDayOfWeek().equals(DayOfWeek.SUNDAY)) {
                        if (doctorAppointmentDays.contains(dateDay.getDayOfWeek())) {

                            List<Appointment> appointmentList = new ArrayList<>();
                            LocalTime timestart = LocalTime.parse(doctorDtoRequest.getWeekSchedule().getTimeStart(), formatterTime);
                            LocalTime timeend = LocalTime.parse(doctorDtoRequest.getWeekSchedule().getTimeEnd(), formatterTime);
                            int duration = doctorDtoRequest.getDuration();
                            for (LocalTime time = timestart; !time.equals(timeend); time = time.plusMinutes(duration)) {
                                appointmentList.add(new Appointment(time));
                            }
                            dayScheduleList.add(new DaySchedule(dateDay, appointmentList));
                        }
                    }
                }
            }
            // Доктор принимает либо ежедневно либо нет, но в разное время
        } else {
            Map<DayOfWeek, net.thumbtack.school.hospital.dto.requests.DaySchedule> doctorAppointmentDays = new HashMap<>();
            for (net.thumbtack.school.hospital.dto.requests.DaySchedule daySchedule : doctorDtoRequest.getWeekDaysSchedule()) {
                doctorAppointmentDays.put(DayOfWeek.of(getDay(daySchedule.getWeekDay())), daySchedule);
            }

            for (LocalDate dateDay = dateStart; !dateDay.equals(dateEnd); dateDay = dateDay.plusDays(1)) {
                if (!dateDay.getDayOfWeek().equals(DayOfWeek.SATURDAY) && !dateDay.getDayOfWeek().equals(DayOfWeek.SUNDAY)) {
                    if (doctorAppointmentDays.keySet().contains(dateDay.getDayOfWeek())) {

                        List<Appointment> appointmentList = new ArrayList<>();
                        LocalTime timestart = LocalTime.parse(doctorAppointmentDays.get(dateDay.getDayOfWeek()).getTimeStart(), formatterTime);
                        LocalTime timeend = LocalTime.parse(doctorAppointmentDays.get(dateDay.getDayOfWeek()).getTimeEnd(), formatterTime);
                        int duration = doctorDtoRequest.getDuration();
                        for (LocalTime time = timestart; !time.equals(timeend); time = time.plusMinutes(duration)) {
                            appointmentList.add(new Appointment(time));
                        }
                        dayScheduleList.add(new DaySchedule(dateDay, appointmentList));

                    }
                }
            }
        }

        int duration = doctorDtoRequest.getDuration();

        Doctor doctor = new Doctor(
                user,
                speciality,
                room,
                dateStart,
                dateEnd,
                dayScheduleList,
                duration);

        doctor = doctorDao.insert(doctor);

        List<Schedule> schedule = new ArrayList<>();
        for (DaySchedule dSchedule : doctor.getDaySchedule()) {
            List<net.thumbtack.school.hospital.dto.responses.DaySchedule> daySchedule = new ArrayList<>();
            for (Appointment appointment : dSchedule.getAppointmentList()) {
                daySchedule.add(new net.thumbtack.school.hospital.dto.responses.DaySchedule(appointment.getTime(), null));
            }
            schedule.add(new Schedule(dSchedule.getDateDay(), daySchedule));
        }

        return new RegistrationDoctorDtoResponse(
                doctor.getId(),
                doctor.getUser().getFirstName(),
                doctor.getUser().getLastName(),
                doctor.getUser().getPatronymic(),
                doctor.getSpeciality().getSpeciality(),
                doctor.getRoom().getRoom(),
                schedule
        );


    }

    public RegistrationDoctorDtoResponse getInformationAboutDoctor(int doctorId, String schedule, String startDate, String endDate, Cookie[] cookies) {

        String cookie = "";
        for (Cookie c : cookies) {
            if (c.getName().equals("JAVASESSIONID")) {
                cookie = c.getValue();
            }
        }

        LocalDate dStart = LocalDate.parse(startDate, formatterDate);
        LocalDate dEnd = LocalDate.parse(endDate, formatterDate);

        int id = commonDao.getBySessionId(cookie).getPersonId();
        Patient patient = patientDao.getByUserId(id);
        Doctor doctor = doctorDao.getById(doctorId);

        if (schedule.equals("no")) {
            return new RegistrationDoctorDtoResponse(
                    doctor.getId(),
                    doctor.getUser().getFirstName(),
                    doctor.getUser().getLastName(),
                    doctor.getUser().getPatronymic(),
                    doctor.getSpeciality().getSpeciality(),
                    doctor.getRoom().getRoom(),
                    null
            );
        } else {
            List<Schedule> scheduleList = new ArrayList<>();

            for (DaySchedule dSchedule : doctor.getDaySchedule()) {
                if (dSchedule.getDateDay().compareTo(dStart) >= 0 && dSchedule.getDateDay().compareTo(dEnd) <= 0) {
                    List<net.thumbtack.school.hospital.dto.responses.DaySchedule> daySchedule = new ArrayList<>();
                    for (Appointment appointment : dSchedule.getAppointmentList()) {
                        if (patient != null && patient.equals(appointment.getPatient())) {
                            net.thumbtack.school.hospital.dto.responses.Patient p = new net.thumbtack.school.hospital.dto.responses.Patient(
                                    appointment.getPatient().getId(),
                                    appointment.getPatient().getUser().getFirstName(),
                                    appointment.getPatient().getUser().getLastName(),
                                    appointment.getPatient().getUser().getPatronymic(),
                                    appointment.getPatient().getEmail(),
                                    appointment.getPatient().getAddress(),
                                    appointment.getPatient().getPhone());
                            daySchedule.add(new net.thumbtack.school.hospital.dto.responses.DaySchedule(appointment.getTime(), p));
                        } else {
                            daySchedule.add(new net.thumbtack.school.hospital.dto.responses.DaySchedule(appointment.getTime(), null));
                        }
                    }
                    scheduleList.add(new Schedule(dSchedule.getDateDay(), daySchedule));
                }
            }

            return new RegistrationDoctorDtoResponse(
                    doctor.getId(),
                    doctor.getUser().getFirstName(),
                    doctor.getUser().getLastName(),
                    doctor.getUser().getPatronymic(),
                    doctor.getSpeciality().getSpeciality(),
                    doctor.getRoom().getRoom(),
                    scheduleList
            );
        }

    }


    public List<RegistrationDoctorDtoResponse> getInformationAboutDoctors(String schedule, String speciality, String startDate, String endDate, Cookie[] cookies) {
        List<Doctor> doctors;

        if (speciality != null) {
            Speciality s = doctorDao.getSpecialityBySpecialityName(speciality);
            doctors = doctorDao.getAllBySpeciality(s.getId());
        } else {
            doctors = doctorDao.getAll();
        }

        List<RegistrationDoctorDtoResponse> response = new ArrayList<>();

        for (Doctor doctor : doctors) {
            response.add(getInformationAboutDoctor(doctor.getId(), schedule, startDate, endDate, cookies));
        }

        return response;
    }

    public RegistrationDoctorDtoResponse changeDoctorSchedule(ChangingDoctorScheduleDtoRequest request, int id, Cookie[] cookies) {

        String cookie = "";
        for (Cookie c : cookies) {
            if (c.getName().equals("JAVASESSIONID")) {
                cookie = c.getValue();
            }
        }

        int userid = commonDao.getBySessionId(cookie).getPersonId();
        User user = userDao.getById(userid);

        if (!user.getUserType().equals(UserType.ADMIN)) {
            // error
        }

        Doctor doctor = doctorDao.getById(id);

        LocalDate dateStart = LocalDate.parse(request.getDateStart(), formatterDate);
        LocalDate dateEnd = LocalDate.parse(request.getDateEnd(), formatterDate);

        ///
        boolean isScheduleIsSet = false; // Расписание не установлено
        boolean isScheduleHaveAppointment = false; // На данный период не существуют приемов пациентов
        for (DaySchedule daySchedule : doctor.getDaySchedule()) {
            if (daySchedule.getDateDay().compareTo(dateStart) >= 0 && daySchedule.getDateDay().compareTo(dateEnd) <= 0) {
                if (daySchedule.getAppointmentList() != null) {
                    isScheduleIsSet = true; // Расписание установлено
                    for (Appointment appointment : daySchedule.getAppointmentList()) {
                        if (appointment.getPatient() != null) {
                            isScheduleHaveAppointment = true; // Приемы пациентов существуют
                        }
                    }
                }
            }
        }

        // Формирование расписания
        List<DaySchedule> dayScheduleList = new ArrayList<>();

        if (request.getWeekSchedule() != null) {

            // Врач принимает ежедневно в одно и тоже время
            if (request.getWeekSchedule().getWeekDays() == null || request.getWeekSchedule().getWeekDays().size() == 0) {
                for (LocalDate dateDay = dateStart; !dateDay.equals(dateEnd); dateDay = dateDay.plusDays(1)) {
                    if (!dateDay.getDayOfWeek().equals(DayOfWeek.SATURDAY) && !dateDay.getDayOfWeek().equals(DayOfWeek.SUNDAY)) {
                        List<Appointment> appointmentList = new ArrayList<>();
                        LocalTime timestart = LocalTime.parse(request.getWeekSchedule().getTimeStart(), formatterTime);
                        LocalTime timeend = LocalTime.parse(request.getWeekSchedule().getTimeEnd(), formatterTime);
                        int duration = request.getDuration();
                        for (LocalTime time = timestart; !time.equals(timeend); time = time.plusMinutes(duration)) {
                            appointmentList.add(new Appointment(time));
                        }
                        dayScheduleList.add(new DaySchedule(dateDay, appointmentList));
                    }
                }

                // Врач принимает по определенным дням в одно и тоже время
            } else {
                List<DayOfWeek> doctorAppointmentDays = new ArrayList<>();
                for (String dayString : request.getWeekSchedule().getWeekDays()) {
                    // validator
                    doctorAppointmentDays.add(DayOfWeek.of(getDay(dayString)));
                }

                for (LocalDate dateDay = dateStart; !dateDay.equals(dateEnd); dateDay = dateDay.plusDays(1)) {
                    if (!dateDay.getDayOfWeek().equals(DayOfWeek.SATURDAY) && !dateDay.getDayOfWeek().equals(DayOfWeek.SUNDAY)) {
                        if (doctorAppointmentDays.contains(dateDay.getDayOfWeek())) {

                            List<Appointment> appointmentList = new ArrayList<>();
                            LocalTime timestart = LocalTime.parse(request.getWeekSchedule().getTimeStart(), formatterTime);
                            LocalTime timeend = LocalTime.parse(request.getWeekSchedule().getTimeEnd(), formatterTime);
                            int duration = request.getDuration();
                            for (LocalTime time = timestart; !time.equals(timeend); time = time.plusMinutes(duration)) {
                                appointmentList.add(new Appointment(time));
                            }
                            dayScheduleList.add(new DaySchedule(dateDay, appointmentList));
                        }
                    }
                }
            }
            // Доктор принимает либо ежедневно либо нет, но в разное время
        } else {
            Map<DayOfWeek, net.thumbtack.school.hospital.dto.requests.DaySchedule> doctorAppointmentDays = new HashMap<>();
            for (net.thumbtack.school.hospital.dto.requests.DaySchedule daySchedule : request.getWeekDaysSchedule()) {
                // validator
                doctorAppointmentDays.put(DayOfWeek.of(getDay(daySchedule.getWeekDay())), daySchedule);
            }

            for (LocalDate dateDay = dateStart; !dateDay.equals(dateEnd); dateDay = dateDay.plusDays(1)) {
                if (!dateDay.getDayOfWeek().equals(DayOfWeek.SATURDAY) && !dateDay.getDayOfWeek().equals(DayOfWeek.SUNDAY)) {
                    if (doctorAppointmentDays.keySet().contains(dateDay.getDayOfWeek())) {

                        List<Appointment> appointmentList = new ArrayList<>();
                        LocalTime timestart = LocalTime.parse(doctorAppointmentDays.get(dateDay.getDayOfWeek()).getTimeStart(), formatterTime);
                        LocalTime timeend = LocalTime.parse(doctorAppointmentDays.get(dateDay.getDayOfWeek()).getTimeEnd(), formatterTime);
                        int duration = request.getDuration();
                        for (LocalTime time = timestart; !time.equals(timeend); time = time.plusMinutes(duration)) {
                            appointmentList.add(new Appointment(time));
                        }
                        dayScheduleList.add(new DaySchedule(dateDay, appointmentList));

                    }
                }
            }
        }


        if (!isScheduleIsSet) {
            for (DaySchedule daySchedule : dayScheduleList) {
                doctor.getDaySchedule().add(daySchedule);
            }
        } else if (!isScheduleHaveAppointment) {
            for (DaySchedule daySchedule : new ArrayList<>(doctor.getDaySchedule())) {
                if (daySchedule.getDateDay().compareTo(dateStart) >= 0 && daySchedule.getDateDay().compareTo(dateEnd) <= 0) {
                    doctor.getDaySchedule().remove(daySchedule);
                }
            }
            doctor.getDaySchedule().addAll(dayScheduleList);
        } else {
            // error
        }

        doctorDao.delete(doctor.getId());
        doctor = doctorDao.insert(doctor);


        List<Schedule> schedule = new ArrayList<>();
        for (DaySchedule dSchedule : doctor.getDaySchedule()) {
            List<net.thumbtack.school.hospital.dto.responses.DaySchedule> daySchedule = new ArrayList<>();
            for (Appointment appointment : dSchedule.getAppointmentList()) {
                daySchedule.add(new net.thumbtack.school.hospital.dto.responses.DaySchedule(appointment.getTime(), null));
            }
            schedule.add(new Schedule(dSchedule.getDateDay(), daySchedule));
        }

        return new RegistrationDoctorDtoResponse(
                doctor.getId(),
                doctor.getUser().getFirstName(),
                doctor.getUser().getLastName(),
                doctor.getUser().getPatronymic(),
                doctor.getSpeciality().getSpeciality(),
                doctor.getRoom().getRoom(),
                schedule
        );


    }

    public AppointmentMedicalCommissionDtoResponse appointmentMedicalCommission(AppointmentMedicalCommissionDtoRequest request, Cookie[] cookies) throws ServerException {
        String cookie = "";
        for (Cookie c : cookies) {
            if (c.getName().equals("JAVASESSIONID")) {
                cookie = c.getValue();
            }
        }

        int userid = commonDao.getBySessionId(cookie).getPersonId();
        User user = userDao.getById(userid);

        if (!user.getUserType().equals(UserType.DOCTOR)) {
            // error
        }

        Doctor doc = doctorDao.getByUserId(userid);
        if(!request.getDoctorIds().contains(doc.getId())) {
            request.getDoctorIds().add(doc.getId());
        }

        List<Doctor> doctors = new ArrayList<>();

        for (Integer id : request.getDoctorIds()) {
            doctors.add(doctorDao.getById(id));
        }

        Patient patient = patientDao.getById(request.getPatientId());

        List<Appointment> appointments = patientDao.getByPatientId(patient.getId());



        LocalTime appointmentStartTime = LocalTime.parse(request.getTime(), formatterTime);
        LocalTime appointmentEndTime = LocalTime.parse(request.getTime(), formatterTime).plusMinutes(request.getDuration());

        // проверить свободно ли время у пациента
        /*for(Appointment appointment : appointments) {

            char[] t = appointment.getTicket().toCharArray();


            if (appointment.getTime().equals(appointmentStartTime)) {
                throw new ServerException(
                        ServerErrorCode.PATIENT_IS_BUSY,
                        "patient",
                        appointment.getPatient().getUser().getLogin()
                        );
            }
        }*/



        // проверить свободно ли время у докторов
        for (Doctor doctor : doctors) {
            for (DaySchedule daySchedule : doctor.getDaySchedule()) {
                if (daySchedule.getDateDay().equals(request.getDate())) {
                    for (Appointment appointment : daySchedule.getAppointmentList()) {
                        if (appointment.getTime().equals(appointmentStartTime) && appointment.getPatient() != null) {
                            throw new ServerException(
                                    ServerErrorCode.DOCTOR_IS_BUSY,
                                    "doctor",
                                    doctor.getUser().getLogin()
                            );
                        }
                    }
                }
            }
        }

        String ticket = "СD<";
        for (Doctor d : doctors) {
            ticket += d.getId() + ">D<";
        }
        ticket += "<" + request.getDate() + request.getTime();

        for (Doctor doctor : doctors) {
            for (DaySchedule daySchedule : doctor.getDaySchedule()) {
                if (daySchedule.getDateDay().equals(request.getDate())) {
                    List<Appointment> appointmentList = daySchedule.getAppointmentList();
                    Collections.sort(appointmentList, new Comparator<Appointment>() {
                        @Override
                        public int compare(Appointment o1, Appointment o2) {
                            return o1.getTime().compareTo(o2.getTime());
                        }
                    });

                    LocalTime realBeforeAppointmentTime = null;
                    LocalTime realAfterAppointmentTime = null;

                    int appointmentTimeDuration = (int) appointmentList.get(1).getTime().until(appointmentList.get(0).getTime(), ChronoUnit.MINUTES);
                    for (Appointment appointment : appointmentList) {
                        int tempStartTime = (int) appointmentStartTime.until(appointment.getTime(), ChronoUnit.MINUTES);
                        if (tempStartTime > 0 && tempStartTime < appointmentTimeDuration) {
                            realBeforeAppointmentTime = appointment.getTime();
                            break;
                        }
                    }

                    for (Appointment appointment : appointmentList) {
                        int tempEndTime = (int) appointment.getTime().until(appointmentEndTime, ChronoUnit.MINUTES);
                        if (tempEndTime > 0 && tempEndTime < appointmentTimeDuration) {
                            realAfterAppointmentTime = appointment.getTime();
                            break;
                        }
                    }

                    for (Appointment appointment : appointmentList) {
                        if (appointment.getTime().compareTo(realBeforeAppointmentTime) > 0 && appointment.getTime().compareTo(realAfterAppointmentTime) < 0) {
                            doctorDao.deleteAppointment(appointment.getId());
                        }
                    }

                    Appointment appointment = new Appointment(
                            patient,
                            ticket,
                            appointmentStartTime
                    );

                    doctorDao.insertAppointment(appointment, daySchedule.getId());


                }
            }
        }


        // проверить свободно ли время у пациента
        // проверить свободно ли время у докторов
        // если да, записать пациента к каждому доктору на прием
        return new AppointmentMedicalCommissionDtoResponse(
                ticket,
                patient.getId(),
                request.getDoctorIds(),
                request.getRoom(),
                request.getDate(),
                request.getTime(),
                request.getDuration()
        );
    }

    public void deletingDoctor(DeletingDoctorDtoRequest request, int doctorId, Cookie[] cookies) {

        String cookie = "";
        for (Cookie c : cookies) {
            if (c.getName().equals("JAVASESSIONID")) {
                cookie = c.getValue();
            }
        }

        int id = commonDao.getBySessionId(cookie).getPersonId();
        User user = userDao.getById(id);
        if (user.getUserType().equals(UserType.ADMIN)) {
            // что то сделать с date в request
            doctorDao.delete(doctorId);
        } else {
            // Exception
        }

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


    // validators

    private Speciality isContainsSpeciality(String speciality) {
        for (Speciality s : doctorDao.getAllSpecialties()) {
            if (speciality.equals(s.getSpeciality())) {
                return s;
            }
        }
        return null;
    }

    private Room isContainsRoom(String room) {
        for (Room r : doctorDao.getAllRooms()) {
            if (room.equals(r.getRoom())) {
                return r;
            }
        }
        return null;
    }

}
