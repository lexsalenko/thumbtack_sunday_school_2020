package net.thumbtack.school.hospital.mappers;

import net.thumbtack.school.hospital.model.*;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.mapping.FetchType;

import java.util.List;

public interface AppointmentMapper {

    //id scheduleid patientid time

    @Insert("INSERT INTO appointment ( dayscheduleid, ticket, time ) VALUES ( #{dayscheduleid}, #{appointment.ticket}, #{appointment.time} )")
    @Options(useGeneratedKeys = true, keyProperty = "appointment.id")
    Integer insert(@Param("appointment") Appointment appointment, @Param("dayscheduleid") int dayscheduleid);

    @Update("UPDATE appointment SET dayscheduleid = #{dayscheduleid}, patientid = #{patient.id}, ticket = #{appointment.ticket}, time = #{appointment.time} WHERE id = #{appointment.id}")
    void update(@Param("appointment") Appointment appointment, @Param("patient") Patient patient, @Param("dayscheduleid") int dayscheduleid);

    @Select("SELECT id, dayscheduleid, patientid, ticket, time FROM appointment WHERE dayscheduleid = #{id}")
    @Results({
            @Result(property = "id", column = "id"),
            @Result(property = "patient", column = "patientid", javaType = Patient.class,
                    one = @One(select = "net.thumbtack.school.hospital.mappers.PatientMapper.getById", fetchType = FetchType.EAGER)),
            @Result(property = "ticket", column = "ticket"),
            @Result(property = "time", column = "time")
    })
    Appointment getByScheduleId(int id);

    @Select("SELECT id, dayscheduleid, patientid, ticket, time FROM appointment WHERE patientid = #{id} ")
    @Results({
            @Result(property = "id", column = "id"),
            @Result(property = "patient", column = "patientid", javaType = Patient.class,
                    one = @One(select = "net.thumbtack.school.hospital.mappers.PatientMapper.getById", fetchType = FetchType.EAGER)),
            @Result(property = "ticket", column = "ticket"),
            @Result(property = "time", column = "time")
    })
    List<Appointment> getByPatientId(int id);


    @Select("SELECT id, userid, specialityid, roomid, datestart, dateend, duration FROM doctor WHERE id IN" +
            "(SELECT doctorid FROM daySchedule WHERE id IN " +
            "(SELECT dayscheduleid FROM appointment WHERE patientid = #{id})) ")
    @Results({
            @Result(property = "id", column = "id"),
            @Result(property = "user", column = "userid", javaType = User.class,
                    one = @One(select = "net.thumbtack.school.hospital.mappers.UserMapper.getById", fetchType = FetchType.LAZY)),
            @Result(property = "speciality", column = "specialityid", javaType = Speciality.class,
                    one = @One(select = "net.thumbtack.school.hospital.mappers.SpecialityMapper.getById", fetchType = FetchType.LAZY)),
            @Result(property = "room", column = "roomid", javaType = Room.class,
                    one = @One(select = "net.thumbtack.school.hospital.mappers.RoomMapper.getById", fetchType = FetchType.LAZY)),
            @Result(property = "dateStart", column = "datestart"),
            @Result(property = "dateEnd", column = "dateend"),
            @Result(property = "daySchedule", column = "id", javaType = List.class,
                    many = @Many(select = "net.thumbtack.school.hospital.mappers.DayScheduleMapper.getByDoctorId", fetchType = FetchType.EAGER)),
            @Result(property = "duration", column = "duration")
    })
    List<Doctor> getDoctorsByPatientId(int id);



    @Delete("DELETE FROM appointment WHERE id = #{id}")
    void delete(int id);

    @Delete("DELETE FROM appointment WHERE dayscheduleid = #{id}")
    void deleteByDayScheduleId(int id);

    @Delete("DELETE FROM appointment")
    void deleteAll();
}
