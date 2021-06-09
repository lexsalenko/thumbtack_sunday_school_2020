package net.thumbtack.school.hospital.dao;

import net.thumbtack.school.hospital.model.*;

import java.util.List;

public interface PatientDao {

    Patient insert(Patient admin);

    Patient getById(int id);

    List<Patient> getAll();

    Patient update(Patient patient);

    void delete(int id);

    void deleteAll();

    List<Login> getAllLoginsAndPasswords();

    Patient getByUserId(int userid);

    List<Appointment> getByPatientId(int id);

}
