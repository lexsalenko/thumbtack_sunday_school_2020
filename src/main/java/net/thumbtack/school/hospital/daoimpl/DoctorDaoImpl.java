package net.thumbtack.school.hospital.daoimpl;

import net.thumbtack.school.hospital.dao.DoctorDao;
import net.thumbtack.school.hospital.mappers.*;
import net.thumbtack.school.hospital.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;

import java.util.List;


public class DoctorDaoImpl implements DoctorDao {

    private static final Logger LOGGER = LoggerFactory.getLogger(DoctorDaoImpl.class);

    @Autowired
    private DoctorMapper doctorMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private RoomMapper roomMapper;

    @Autowired
    private SpecialityMapper specialityMapper;

    @Autowired
    private DayScheduleMapper dayScheduleMapper;

    @Autowired
    private AppointmentMapper appointmentMapper;

    @Override
    public Doctor insert(Doctor doctor) {
        LOGGER.debug("DAO insert Doctor {}", doctor);
        try {
            userMapper.insert(doctor.getUser());
            doctorMapper.insert(doctor, doctor.getUser().getId(), doctor.getSpeciality().getId(), doctor.getRoom().getId());
            for(DaySchedule daySchedule : doctor.getDaySchedule()) {
                dayScheduleMapper.insert(doctor.getId(), daySchedule);
                for(Appointment appointment : daySchedule.getAppointmentList()) {
                    appointmentMapper.insert(appointment, daySchedule.getId());
                }
            }
        } catch (DataAccessException ex) {
            LOGGER.info("Can't not insert Doctor {} {}", doctor, ex);
            throw ex;
        }
        return doctor;
    }

    @Override
    public Doctor getById(int id) {
        LOGGER.debug("DAO get Doctor by Id {}", id);
        try {
            return doctorMapper.getById(id);
        } catch (DataAccessException ex) {
            LOGGER.info("Can't get Doctor {}", ex);
            throw ex;
        }
    }

    @Override
    public  List<Doctor> getDoctorsByPatientId(int id) {
        LOGGER.debug("DAO get Doctor by Id {}", id);
        try {
            return appointmentMapper.getDoctorsByPatientId(id);
        } catch (DataAccessException ex) {
            LOGGER.info("Can't get Doctor {}", ex);
            throw ex;
        }
    }

    @Override
    public List<Doctor> getAll() {
        LOGGER.debug("DAO get all Doctor");
        try {
            return doctorMapper.getAll();
        } catch (DataAccessException ex) {
            LOGGER.info("Can't get all Doctor {}", ex);
            throw ex;
        }
    }

    @Override
    public Doctor update(Doctor doctor) {
        LOGGER.debug("DAO update Doctor {]", doctor);
        try {
            userMapper.update(doctor.getUser());
            doctorMapper.update(doctor, doctor.getUser().getId(), doctor.getSpeciality().getId(), doctor.getRoom().getId());
            for(DaySchedule daySchedule : doctor.getDaySchedule()) {
                dayScheduleMapper.update(doctor.getId(), daySchedule);
                for(Appointment appointment : daySchedule.getAppointmentList()) {
                    appointmentMapper.update(appointment, appointment.getPatient(), daySchedule.getId());
                }
            }
        } catch (DataAccessException ex) {
            LOGGER.info("Can't update Doctor {} {}", doctor, ex);
            throw ex;
        }
        return doctor;
    }

    @Override
    public void delete(int id) {
        LOGGER.debug("DAO delete Doctor");
        try {
            Doctor doctor = doctorMapper.getById(id);
            for(DaySchedule daySchedule : doctor.getDaySchedule()) {
                for(Appointment appointment : daySchedule.getAppointmentList()) {
                    appointmentMapper.delete(appointment.getId());
                }
                dayScheduleMapper.delete(daySchedule.getId());
            }
            doctorMapper.delete(id);
        } catch (DataAccessException ex) {
            LOGGER.info("Can't delete Doctor {}", ex);
            throw ex;
        }
    }

    @Override
    public void insertSchedule(Doctor doctor) {
        LOGGER.debug("DAO delete Doctor");
        try {
            for(DaySchedule daySchedule : doctor.getDaySchedule()) {
                dayScheduleMapper.insert(doctor.getId(), daySchedule);
                for(Appointment appointment : daySchedule.getAppointmentList()) {
                    appointmentMapper.insert(appointment, daySchedule.getId());
                }
            }
        } catch (DataAccessException ex) {
            LOGGER.info("Can't delete Doctor {}", ex);
            throw ex;
        }
    }

    @Override
    public void deleteSchedule(Doctor doctor) {
        LOGGER.debug("DAO delete Doctor");
        try {
            for(DaySchedule daySchedule : doctor.getDaySchedule()) {
                for(Appointment appointment : daySchedule.getAppointmentList()) {
                    appointmentMapper.delete(appointment.getId());
                }
                dayScheduleMapper.delete(daySchedule.getId());
            }
        } catch (DataAccessException ex) {
            LOGGER.info("Can't delete Doctor {}", ex);
            throw ex;
        }
    }

    @Override
    public void deleteAll() {
        LOGGER.debug("DAO delete all Doctor");
        try {
            doctorMapper.deleteAll();
            dayScheduleMapper.deleteAll();
            appointmentMapper.deleteAll();
        } catch (DataAccessException ex) {
            LOGGER.info("Can't delete all Doctor {}", ex);
            throw ex;
        }
    }

    @Override
    public List<Login> getAllLoginsAndPasswords() {
        LOGGER.debug("DAO Get all logins and passwords  {}");
        try {
            return userMapper.getAllLoginsAndPasswords();
        } catch (DataAccessException ex) {
            LOGGER.info("Can't get Admin {}", ex);
            throw ex;
        }
    }


    @Override
    public Doctor getByUserId(int userid) {
        LOGGER.debug("DAO Get all logins and passwords Admin {}");
        try {
            return doctorMapper.getByUserId(userid);
        } catch (DataAccessException ex) {
            LOGGER.info("Can't get Admin {}", ex);
            throw ex;
        }
    }

    @Override
    public List<Doctor> getAllBySpeciality(int id) {
        LOGGER.debug("DAO Get all logins and passwords Admin {}");
        try {
            return doctorMapper.getAllBySpeciality(id);
        } catch (DataAccessException ex) {
            LOGGER.info("Can't get Admin {}", ex);
            throw ex;
        }
    }

    public Speciality getSpecialityBySpecialityName(String speciality) {
        LOGGER.debug("DAO Get all logins and passwords Admin {}");
        try {
            return specialityMapper.getBySpeciality(speciality);
        } catch (DataAccessException ex) {
            LOGGER.info("Can't get Admin {}", ex);
            throw ex;
        }
    }

    public List<Doctor> getAllDoctorsBySpecialityName(String speciality) {
        LOGGER.debug("DAO Get all logins and passwords Admin {}");
        try {
            Speciality s = getSpecialityBySpecialityName(speciality);
            return doctorMapper.getAllDoctorsBySpecialityName(s.getId());
        } catch (DataAccessException ex) {
            LOGGER.info("Can't get Admin {}", ex);
            throw ex;
        }
    }


    // Room

    @Override
    public Room insertRoom(Room room) {
        LOGGER.debug("DAO insert Room {}", room);
        try {
            roomMapper.insert(room);
        } catch (DataAccessException ex) {
            LOGGER.info("Can't not insert Room {} {}", room, ex);
            throw ex;
        }
        return room;
    }

    @Override
    public Room getRoomById(int id) {
        LOGGER.debug("DAO get Doctor by Id {}", id);
        try {
            return roomMapper.getById(id);
        } catch (DataAccessException ex) {
            LOGGER.info("Can't get Doctor {}", ex);
            throw ex;
        }
    }

    @Override
    public List<Room> insertRooms(List<Room> rooms) {
        LOGGER.debug("DAO insert Rooms {}", rooms);
        try {
            for (Room room : rooms) {
                roomMapper.insert(room);
            }
        } catch (DataAccessException ex) {
            LOGGER.info("Can't not insert Rooms {} {}", rooms, ex);
            throw ex;
        }
        return rooms;
    }

    @Override
    public List<Room> getAllRooms() {
        LOGGER.debug("DAO get all Rooms");
        try {
            return roomMapper.getAllRooms();
        } catch (DataAccessException ex) {
            LOGGER.info("Can't get all Rooms {}", ex);
            throw ex;
        }
    }

    @Override
    public void deleteAllRooms() {
        LOGGER.debug("DAO delete all Rooms");
        try {
            roomMapper.deleteAll();
        } catch (DataAccessException ex) {
            LOGGER.info("Can't delete all Rooms {}", ex);
            throw ex;
        }
    }


    // Speciality

    @Override
    public Speciality insertSpeciality(Speciality speciality) {
        LOGGER.debug("DAO insert Speciality {}", speciality);
        try {
            specialityMapper.insert(speciality);
        } catch (DataAccessException ex) {
            LOGGER.info("Can't not insert Speciality {} {}", speciality, ex);
            throw ex;
        }
        return speciality;
    }

    @Override
    public Speciality getSpecialityById(int id) {
        LOGGER.debug("DAO get Doctor by Id {}", id);
        try {
            return specialityMapper.getById(id);
        } catch (DataAccessException ex) {
            LOGGER.info("Can't get Doctor {}", ex);
            throw ex;
        }
    }

    @Override
    public List<Speciality> insertSpecialties(List<Speciality> specialities) {
        LOGGER.debug("DAO insert Specialties {}", specialities);
        try {
            for (Speciality speciality : specialities) {
                specialityMapper.insert(speciality);
            }
        } catch (DataAccessException ex) {
            LOGGER.info("Can't not insert Specialties {} {}", specialities, ex);
            throw ex;
        }
        return specialities;
    }

    @Override
    public List<Speciality> getAllSpecialties() {
        LOGGER.debug("DAO get all Specialties");
        try {
            return specialityMapper.getAllSpecialties();
        } catch (DataAccessException ex) {
            LOGGER.info("Can't get all Specialties {}", ex);
            throw ex;
        }
    }


    @Override
    public void deleteAllSpecialties() {
        LOGGER.debug("DAO delete all Specialties");
        try {
            specialityMapper.deleteAll();
        } catch (DataAccessException ex) {
            LOGGER.info("Can't delete all Specialties {}", ex);
            throw ex;
        }
    }

    public void deleteAppointment(int id){
        LOGGER.debug("DAO delete all Specialties");
        try {
            appointmentMapper.delete(id);
        } catch (DataAccessException ex) {
            LOGGER.info("Can't delete all Specialties {}", ex);
            throw ex;
        }
    }

    public Appointment insertAppointment(Appointment appointment, int dayScheduleId) {
        LOGGER.debug("DAO delete all Specialties");
        try {
            appointmentMapper.insert(appointment, dayScheduleId);
        } catch (DataAccessException ex) {
            LOGGER.info("Can't delete all Specialties {}", ex);
            throw ex;
        }
        return appointment;
    }


}
