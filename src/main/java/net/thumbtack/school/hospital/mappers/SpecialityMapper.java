package net.thumbtack.school.hospital.mappers;

import net.thumbtack.school.hospital.model.Speciality;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface SpecialityMapper {

    @Insert("INSERT INTO speciality (id, speciality) VALUES ( #{speciality.id}, #{speciality.speciality} )")
    @Options(useGeneratedKeys = true, keyProperty = "speciality.id")
    Integer insert(@Param("speciality") Speciality speciality);

    @Select("SELECT id, speciality FROM speciality WHERE id = #{id}")
    @Results({
            @Result(property = "id", column = "id"),
            @Result(property = "speciality", column = "speciality")
    })
    Speciality getById(int id);

    @Select("SELECT id, speciality from speciality")
    @Results({
            @Result(property = "id", column = "id")
    })
    List<Speciality> getAllSpecialties();

    @Select("SELECT id, speciality FROM speciality WHERE speciality = #{speciality}")
    @Results({
            @Result(property = "id", column = "id"),
            @Result(property = "speciality", column = "speciality")
    })
    Speciality getBySpeciality(String speciality);

    @Delete("DELETE FROM speciality")
    void deleteAll();

}
