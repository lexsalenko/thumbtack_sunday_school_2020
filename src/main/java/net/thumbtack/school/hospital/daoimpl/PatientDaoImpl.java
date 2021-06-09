package net.thumbtack.school.hospital.daoimpl;

import net.thumbtack.school.hospital.dao.PatientDao;
import net.thumbtack.school.hospital.mappers.AppointmentMapper;
import net.thumbtack.school.hospital.mappers.PatientMapper;
import net.thumbtack.school.hospital.mappers.SessionMapper;
import net.thumbtack.school.hospital.mappers.UserMapper;
import net.thumbtack.school.hospital.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;

import java.util.List;

public class PatientDaoImpl implements PatientDao {

    private static final Logger LOGGER = LoggerFactory.getLogger(PatientDaoImpl.class);

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private PatientMapper patientMapper;

    @Autowired
    private AppointmentMapper appointmentMapper;

    @Override
    public Patient insert(Patient patient) {
        LOGGER.debug("DAO insert Patient {}", patient);
        try {
            userMapper.insert(patient.getUser());
            patientMapper.insert(patient.getUser().getId(), patient);
        } catch (RuntimeException ex) {
            LOGGER.info("Can't not insert Patient {} {}", patient, ex);
            throw ex;
        }
        return patient;
    }

    @Override
    public Patient getById(int id) {
        LOGGER.debug("DAO get Patient by Id {}", id);
        try {
            return patientMapper.getById(id);
        } catch (RuntimeException ex) {
            LOGGER.info("Can't get Patient {}", ex);
            throw ex;
        }
    }

    @Override
    public List<Patient> getAll() {
        LOGGER.debug("DAO get all Patient");
        try {
            return patientMapper.getAll();
        } catch (RuntimeException ex) {
            LOGGER.info("Can't get all Patient {}", ex);
            throw ex;
        }
    }

    @Override
    public Patient update(Patient patient) {
        LOGGER.debug("DAO update Patient {]", patient);
        try {
            userMapper.update(patient.getUser());
            patientMapper.update(patient);
        } catch (RuntimeException ex) {
            LOGGER.info("Can't update Patient {} {}", patient, ex);
            throw ex;
        }
        return patient;
    }

    @Override
    public void delete(int id) {
        LOGGER.debug("DAO delete Patient");
        try {
            patientMapper.delete(id);
        } catch (RuntimeException ex) {
            LOGGER.info("Can't delete Patient {}", ex);
            throw ex;
        }
    }

    @Override
    public void deleteAll() {
        LOGGER.debug("DAO delete all Patient");
        try {
            patientMapper.deleteAll();
        } catch (RuntimeException ex) {
            LOGGER.info("Can't delete all Patient {}", ex);
            throw ex;
        }
    }


    @Override
    public List<Login> getAllLoginsAndPasswords() {
        LOGGER.debug("DAO get Patient by Id {}");
        try {
            return userMapper.getAllLoginsAndPasswords();
        } catch (RuntimeException ex) {
            LOGGER.info("Can't get Patient {}", ex);
            throw ex;
        }
    }


    @Override
    public Patient getByUserId(int userid) {
        LOGGER.debug("DAO get Patient by Id {}", userid);
        try {
            return patientMapper.getByUserId(userid);
        } catch (RuntimeException ex) {
            LOGGER.info("Can't get Patient {}", ex);
            throw ex;
        }
    }

    @Override
    public List<Appointment> getByPatientId(int id) {
        LOGGER.debug("DAO get appointment list by patient Id {}", id);
        try {
            return appointmentMapper.getByPatientId(id);
        } catch (RuntimeException ex) {
            LOGGER.info("Can't get appointment list {}", ex);
            throw ex;
        }
    }


}
