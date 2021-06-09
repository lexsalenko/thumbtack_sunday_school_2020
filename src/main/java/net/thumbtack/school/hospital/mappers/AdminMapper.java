package net.thumbtack.school.hospital.mappers;


import net.thumbtack.school.hospital.model.Admin;
import net.thumbtack.school.hospital.model.User;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.mapping.FetchType;

import java.util.List;

@Mapper
public interface AdminMapper {

    @Insert("INSERT INTO admin (userid, position) VALUES ( #{userid}, #{admin.position} )")
    @Options(useGeneratedKeys = true, keyProperty = "admin.id")
    Integer insert(@Param("userid") int userid, @Param("admin") Admin admin);


    @Select("SELECT id, userid, position FROM admin WHERE id = #{id}")
    @Results({
            @Result(property = "id", column = "id"),
            @Result(property = "user", column = "userid", javaType = User.class,
                    one = @One(select = "net.thumbtack.school.hospital.mappers.UserMapper.getById", fetchType = FetchType.LAZY)),
            @Result(property = "position", column = "position")
    })
    Admin getById(int id);

    @Select("SELECT id FROM admin WHERE userid = #{uid}")
    Integer getAdminIdByUserId(int uid);

    @Select("SELECT id, userid, position FROM admin WHERE userid = #{uid}")
    @Results({
            @Result(property = "id", column = "id"),
            @Result(property = "user", column = "userid", javaType = User.class,
                    one = @One(select = "net.thumbtack.school.hospital.mappers.UserMapper.getById", fetchType = FetchType.LAZY)),
            @Result(property = "position", column = "position")
    })
    Admin getByUserId(int uid);


    @Select("SELECT id, userid, position FROM admin")
    @Results({
            @Result(property = "id", column = "id"),
            @Result(property = "user", column = "userid", javaType = User.class,
                    one = @One(select = "net.thumbtack.school.hospital.mappers.UserMapper.getById", fetchType = FetchType.LAZY)),
            @Result(property = "position", column = "position")
    })
    List<Admin> getAll();


    @Update("UPDATE admin SET position = #{admin.position} WHERE id = #{admin.id}")
    void update(@Param("admin") Admin admin);


    @Delete("DELETE FROM admin WHERE id = #{id}")
    void delete(int id);


    @Delete("DELETE FROM admin")
    void deleteAll();

}
