package net.thumbtack.school.hospital.mappers;

import net.thumbtack.school.hospital.model.*;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.mapping.FetchType;

import java.util.List;

@Mapper
public interface DoctorMapper {

    @Insert("INSERT INTO doctor (userid, specialityid, roomid, datestart, dateend, duration) VALUES (#{userid}, #{specialityid}, #{roomid}, #{doctor.dateStart}, #{doctor.dateEnd}, #{doctor.duration} )")
    @Options(useGeneratedKeys = true, keyProperty = "doctor.id")
    Integer insert(@Param("doctor") Doctor doctor, @Param("userid") int userid, @Param("specialityid") int specialityid, @Param("roomid") int roomid);


    @Select("SELECT id, userid, specialityid, roomid, datestart, dateend, duration FROM doctor WHERE id = #{id}")
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
    Doctor getById(int id);

    @Select("SELECT id, userid, specialityid, roomid, datestart, dateend, duration FROM doctor WHERE userid = #{userid}")
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
                    many = @Many(select = "net.thumbtack.school.hospital.mappers.DayScheduleMapper.getByDoctorId", fetchType = FetchType.LAZY)),
            @Result(property = "duration", column = "duration")
    })
    Doctor getByUserId(int userid);


    @Select("SELECT id, userid, specialityid, roomid, datestart, dateend, duration FROM doctor")
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
                    many = @Many(select = "net.thumbtack.school.hospital.mappers.DayScheduleMapper.getByDoctorId", fetchType = FetchType.LAZY)),
            @Result(property = "duration", column = "duration")
    })
    List<Doctor> getAll();

    @Select("SELECT id, userid, specialityid, roomid, datestart, dateend, duration FROM doctor WHERE specialityid = #{id}")
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
                    many = @Many(select = "net.thumbtack.school.hospital.mappers.DayScheduleMapper.getByDoctorId", fetchType = FetchType.LAZY)),
            @Result(property = "duration", column = "duration")
    })
    List<Doctor> getAllBySpeciality(int id);


    @Update("UPDATE doctor SET userid = #{userid}, specialityid = #{specialityid}, roomid = #{roomid}, datestart = #{doctor.dateStart}, dateend = #{doctor.dateEnd}, duration = #{doctor.duration} WHERE specialityid = #{doctor.id}")
    void update(@Param("doctor") Doctor doctor, @Param("userid") int userid, @Param("specialityid") int specialityid, @Param("roomid") int roomid);


    @Select("SELECT id, userid, specialityid, roomid, datestart, dateend, duration FROM doctor WHERE specialityid = #{id}")
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
                    many = @Many(select = "net.thumbtack.school.hospital.mappers.DayScheduleMapper.getByDoctorId", fetchType = FetchType.LAZY)),
            @Result(property = "duration", column = "duration")
    })
    List<Doctor> getAllDoctorsBySpecialityName(int id);

    @Delete("DELETE FROM doctor WHERE id = #{id}")
    void delete(int id);


    @Delete("DELETE FROM doctor")
    void deleteAll();

}
