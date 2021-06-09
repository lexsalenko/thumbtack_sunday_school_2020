package net.thumbtack.school.hospital.mappers;

import net.thumbtack.school.hospital.model.Login;
import net.thumbtack.school.hospital.model.User;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface UserMapper {

    @Insert("INSERT INTO user ( userType, firstName, lastName, patronymic, login, password) VALUES" +
            " ( #{user.userType},#{user.firstName}, #{user.lastName}, #{user.patronymic}, #{user.login}, #{user.password} )")
    @Options(useGeneratedKeys = true, keyProperty = "user.id")
    Integer insert(@Param("user") User user);

    @Select("SELECT id, userType, firstName, lastName, patronymic, login, password FROM `user` WHERE id = #{id}")
    @Results({
            @Result(property = "id", column = "id"),
            @Result(property = "userType", column = "userType"),
            @Result(property = "firstName", column = "firstName"),
            @Result(property = "lastName", column = "lastName"),
            @Result(property = "patronymic", column = "patronymic"),
            @Result(property = "login", column = "login"),
            @Result(property = "password", column = "password")
    })
    User getById(int id);

    @Select("SELECT id, login, password FROM user")
    @Results({
            @Result(property = "userid", column = "id"),
            @Result(property = "login", column = "login"),
            @Result(property = "password", column = "password")
    })
    List<Login> getAllLoginsAndPasswords();

    @Update("UPDATE `user` SET userType = #{user.userType}, firstName = #{user.firstName}, lastName = #{user.lastName}, patronymic = #{user.patronymic}, login = #{user.login}, password = #{user.password} WHERE id = #{user.id}")
    void update(@Param("user") User user);

    @Delete("DELETE FROM user WHERE id = #{id}")
    void delete(int id);

    @Delete("DELETE FROM user")
    void deleteAll();

}
