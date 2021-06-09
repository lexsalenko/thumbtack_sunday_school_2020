package net.thumbtack.school.hospital.TestDataBase;

import net.thumbtack.school.hospital.HospitalServer;
import net.thumbtack.school.hospital.dao.DoctorDao;
import net.thumbtack.school.hospital.dto.requests.WeekSchedule;
import net.thumbtack.school.hospital.model.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = HospitalServer.class, webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class TestDoctorDao {

    @Autowired
    private DoctorDao doctorDao;

    @Before()
    public void clearDatabase() {
        doctorDao.deleteAll();
        doctorDao.deleteAllRooms();
        doctorDao.deleteAllSpecialties();
    }


    @Test
    public void TestInsertDoctor() {
        User user1 = new User(UserType.DOCTOR,"Obadiah", "Stane", "Ivanov", "IronMonger1373", "ironmonger.1234");
        Room room1 = new Room("6-004");
        Speciality speciality1 = new Speciality("Surgeon");

        room1 = doctorDao.insertRoom(room1);
        speciality1 = doctorDao.insertSpeciality(speciality1);


        List<String> weekDays = new ArrayList<>();
        weekDays.add("Mon");
        weekDays.add("Tue");
        weekDays.add("Wed");
        weekDays.add("Thu");
        weekDays.add("Fri");

        WeekSchedule weekSchedule = new WeekSchedule("09:00", "18:00", weekDays);

        DateTimeFormatter formatterDate = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        DateTimeFormatter formatterTime = DateTimeFormatter.ofPattern("HH:mm");
        LocalDate dateStart = LocalDate.parse("16-06-2020", formatterDate);
        LocalDate dateEnd = LocalDate.parse("16-08-2020", formatterDate);

        List<DaySchedule> dayScheduleList = new ArrayList<>();

        for (LocalDate dateDay = dateStart; !dateDay.equals(dateEnd); dateDay = dateDay.plusDays(1)) {
            if (!dateDay.getDayOfWeek().equals(DayOfWeek.SATURDAY) && !dateDay.getDayOfWeek().equals(DayOfWeek.SUNDAY)) {
                List<Appointment> appointmentList = new ArrayList<>();
                LocalTime timestart = LocalTime.parse("09:00", formatterTime);
                LocalTime timeend = LocalTime.parse("14:00", formatterTime);
                int duration = 20;
                for (LocalTime time = timestart; !time.equals(timeend); time = time.plusMinutes(duration)) {
                    appointmentList.add(new Appointment(time));
                }
                dayScheduleList.add(new DaySchedule(dateDay, appointmentList));
            }
        }




        Doctor doctor1 = new Doctor(
                user1,speciality1, room1, dateStart, dateEnd, dayScheduleList, 20
        );

        doctor1 = doctorDao.insert(doctor1);

        Doctor actualDoctor = doctorDao.getById(doctor1.getId());

        assertEquals(doctor1, actualDoctor);
    }


    @Test
    public void TestUpdateDoctor() {
        User user1 = new User(UserType.DOCTOR,"Obadiah", "Stane", "Ivanov", "IronMonger1373", "ironmonger.1234");
        Room room1 = new Room("6-004");
        Speciality speciality1 = new Speciality("Surgeon");

        room1 = doctorDao.insertRoom(room1);
        speciality1 = doctorDao.insertSpeciality(speciality1);


        List<String> weekDays = new ArrayList<>();
        weekDays.add("Mon");
        weekDays.add("Tue");
        weekDays.add("Wed");
        weekDays.add("Thu");
        weekDays.add("Fri");

        //WeekSchedule weekSchedule = new WeekSchedule("09:00", "18:00", weekDays);

        DateTimeFormatter formatterDate = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        DateTimeFormatter formatterTime = DateTimeFormatter.ofPattern("HH:mm");
        LocalDate dateStart = LocalDate.parse("16-06-2020", formatterDate);
        LocalDate dateEnd = LocalDate.parse("16-08-2020", formatterDate);

        List<DaySchedule> dayScheduleList = new ArrayList<>();

        for (LocalDate dateDay = dateStart; !dateDay.equals(dateEnd); dateDay = dateDay.plusDays(1)) {
            if (!dateDay.getDayOfWeek().equals(DayOfWeek.SATURDAY) && !dateDay.getDayOfWeek().equals(DayOfWeek.SUNDAY)) {
                List<Appointment> appointmentList = new ArrayList<>();
                LocalTime timestart = LocalTime.parse("09:00", formatterTime);
                LocalTime timeend = LocalTime.parse("14:00", formatterTime);
                int duration = 20;
                for (LocalTime time = timestart; !time.equals(timeend); time = time.plusMinutes(duration)) {
                    appointmentList.add(new Appointment(time));
                }
                dayScheduleList.add(new DaySchedule(dateDay, appointmentList));
            }
        }


        Doctor doctor1 = new Doctor(
                user1,speciality1, room1, dateStart, dateEnd, dayScheduleList, 20
        );

        doctor1 = doctorDao.insert(doctor1);

        doctor1.getUser().setLastName("Stane Lee");
        doctor1.getUser().setLogin("DeadWood55");
        doctor1.getRoom().setRoom("7-045");

        doctor1 = doctorDao.update(doctor1);

        Doctor actualDoctor = doctorDao.getById(doctor1.getId());

        assertEquals(doctor1.getId(), actualDoctor.getId());
        assertEquals(doctor1.getUser().getFirstName(), actualDoctor.getUser().getFirstName());
        assertEquals(doctor1.getUser().getLastName(), actualDoctor.getUser().getLastName());
        assertEquals(doctor1.getUser().getPatronymic(), actualDoctor.getUser().getPatronymic());
        assertEquals(doctor1.getUser().getLogin(), actualDoctor.getUser().getLogin());
        assertEquals(doctor1.getUser().getPassword(), actualDoctor.getUser().getPassword());
        assertEquals(doctor1.getDaySchedule(), actualDoctor.getDaySchedule());

    }



    @Test
    public void TestDeleteDoctor() {
        User user1 = new User(UserType.DOCTOR,"Obadiah", "Stane", "Ivanov", "IronMonger1373", "ironmonger.1234");
        Room room1 = new Room("6-004");
        Speciality speciality1 = new Speciality("Surgeon");

        room1 = doctorDao.insertRoom(room1);
        speciality1 = doctorDao.insertSpeciality(speciality1);


        List<String> weekDays = new ArrayList<>();
        weekDays.add("Mon");
        weekDays.add("Tue");
        weekDays.add("Wed");
        weekDays.add("Thu");
        weekDays.add("Fri");

        WeekSchedule weekSchedule = new WeekSchedule("09:00", "18:00", weekDays);

        DateTimeFormatter formatterDate = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        DateTimeFormatter formatterTime = DateTimeFormatter.ofPattern("HH:mm");
        LocalDate dateStart = LocalDate.parse("16-06-2020", formatterDate);
        LocalDate dateEnd = LocalDate.parse("16-08-2020", formatterDate);

        List<DaySchedule> dayScheduleList = new ArrayList<>();

        for (LocalDate dateDay = dateStart; !dateDay.equals(dateEnd); dateDay = dateDay.plusDays(1)) {
            if (!dateDay.getDayOfWeek().equals(DayOfWeek.SATURDAY) && !dateDay.getDayOfWeek().equals(DayOfWeek.SUNDAY)) {
                List<Appointment> appointmentList = new ArrayList<>();
                LocalTime timestart = LocalTime.parse("09:00", formatterTime);
                LocalTime timeend = LocalTime.parse("14:00", formatterTime);
                int duration = 20;
                for (LocalTime time = timestart; !time.equals(timeend); time = time.plusMinutes(duration)) {
                    appointmentList.add(new Appointment(time));
                }
                dayScheduleList.add(new DaySchedule(dateDay, appointmentList));
            }
        }

        Doctor doctor1 = new Doctor(
                user1,speciality1, room1, dateStart, dateEnd, dayScheduleList, 20
        );

        doctor1 = doctorDao.insert(doctor1);

        doctorDao.delete(doctor1.getId());

        List<Doctor> actualDoctors = doctorDao.getAll();

        assertFalse(actualDoctors.contains(doctor1));
        assertEquals(actualDoctors.size(), 0);
    }




}
