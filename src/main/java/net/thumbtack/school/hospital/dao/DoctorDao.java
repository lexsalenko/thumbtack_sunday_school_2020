package net.thumbtack.school.hospital.dao;

import net.thumbtack.school.hospital.model.*;

import java.util.List;

public interface DoctorDao {

    Doctor insert(Doctor doctor);

    Doctor getById(int id);

    List<Doctor> getAll();

    Doctor update(Doctor doctor);

    void delete(int id);

    void deleteAll();

    List<Login> getAllLoginsAndPasswords();

    Doctor getByUserId(int userid);

    List<Doctor> getAllBySpeciality(int id);

    List<Doctor> getAllDoctorsBySpecialityName(String speciality);

    void deleteSchedule(Doctor doctor);

    void insertSchedule(Doctor doctor);

    void deleteAppointment(int id);

    Appointment insertAppointment(Appointment appointment, int dayScheduleId);

    //

    Room insertRoom(Room room);

    Room getRoomById(int id);

    List<Room> insertRooms(List<Room> rooms);

    void deleteAllRooms();

    Speciality insertSpeciality(Speciality speciality);

    Speciality getSpecialityById(int id);

    List<Speciality> insertSpecialties(List<Speciality> specialities);

    List<Room> getAllRooms();

    List<Speciality> getAllSpecialties();

    void deleteAllSpecialties();

    Speciality getSpecialityBySpecialityName(String speciality);

    List<Doctor> getDoctorsByPatientId(int id);

}
