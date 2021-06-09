package net.thumbtack.school.hospital.mappers;

import net.thumbtack.school.hospital.model.Patient;
import net.thumbtack.school.hospital.model.User;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.mapping.FetchType;

import java.util.List;

@Mapper
public interface PatientMapper {

    @Insert("INSERT INTO patient (userid, email, address, phone) VALUES ( #{userid}, #{patient.email}, #{patient.address}, #{patient.phone} )")
    @Options(useGeneratedKeys = true, keyProperty = "patient.id")
    Integer insert(@Param("userid") int userid, @Param("patient") Patient patient);


    @Select("SELECT id, userid, email, address, phone FROM patient WHERE id = #{id}")
    @Results({
            @Result(property = "id", column = "id"),
            @Result(property = "user", column = "userid", javaType = User.class,
                    one = @One(select = "net.thumbtack.school.hospital.mappers.UserMapper.getById", fetchType = FetchType.EAGER)),
            @Result(property = "email", column = "email"),
            @Result(property = "address", column = "address"),
            @Result(property = "phone", column = "phone")
    })
    Patient getById(int id);


    @Select("SELECT id, userid, email, address, phone FROM patient WHERE userid = #{userid}")
    @Results({
            @Result(property = "id", column = "id"),
            @Result(property = "user", column = "userid", javaType = User.class,
                    one = @One(select = "net.thumbtack.school.hospital.mappers.UserMapper.getById", fetchType = FetchType.LAZY)),
            @Result(property = "email", column = "email"),
            @Result(property = "address", column = "address"),
            @Result(property = "phone", column = "phone")
    })
    Patient getByUserId(int userid);


    @Select("SELECT id, userid, email, address, phone FROM patient")
    @Results({
            @Result(property = "id", column = "id"),
            @Result(property = "user", column = "userid", javaType = User.class,
                    one = @One(select = "net.thumbtack.school.hospital.mappers.UserMapper.getById", fetchType = FetchType.LAZY)),
            @Result(property = "email", column = "email"),
            @Result(property = "address", column = "address"),
            @Result(property = "phone", column = "phone")
    })
    List<Patient> getAll();


    @Update("UPDATE patient SET email = #{patient.email}, address = #{patient.address}, phone = #{patient.phone} WHERE id = #{patient.id}")
    void update(@Param("patient") Patient patient);


    @Delete("DELETE FROM patient WHERE id = #{id}")
    void delete(int id);


    @Delete("DELETE FROM patient")
    void deleteAll();

}
