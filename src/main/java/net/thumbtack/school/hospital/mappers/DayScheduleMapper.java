package net.thumbtack.school.hospital.mappers;

import net.thumbtack.school.hospital.model.Admin;
import net.thumbtack.school.hospital.model.DaySchedule;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.mapping.FetchType;

import java.util.List;

@Mapper
public interface DayScheduleMapper {

    @Insert("INSERT INTO `daySchedule` (doctorid, dateDay) VALUES ( #{doctorid}, #{daySchedule.dateDay} )")
    @Options(useGeneratedKeys = true, keyProperty = "daySchedule.id")
    Integer insert(@Param("doctorid") int doctorid, @Param("daySchedule") DaySchedule daySchedule);


    @Update("UPDATE `daySchedule` SET doctorid = #{doctorid}, dateDay = #{daySchedule.dateDay} WHERE id = #{daySchedule.id}")
    void update(@Param("doctorid") int doctorid, @Param("daySchedule") DaySchedule daySchedule);

    DaySchedule getById(int id);

    @Select("SELECT id, doctorid, dateDay FROM daySchedule WHERE doctorid = #{id}")
    @Results({
            @Result(property = "id", column = "id"),
            @Result(property = "dateDay", column = "dateDay"),
            @Result(property = "appointmentList", column = "id", javaType = List.class,
                    many = @Many(select = "net.thumbtack.school.hospital.mappers.AppointmentMapper.getByScheduleId", fetchType = FetchType.EAGER)),

    })
    List<DaySchedule> getByDoctorId(int id);


    @Delete("DELETE FROM daySchedule WHERE id = #{id}")
    void delete(int id);

    @Delete("DELETE FROM daySchedule WHERE doctorid = #{id}")
    void deleteByDoctorId(int id);

    @Delete("DELETE FROM daySchedule")
    void deleteAll();

}
