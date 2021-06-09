package net.thumbtack.school.hospital.mappers;

import net.thumbtack.school.hospital.model.Session;
import net.thumbtack.school.hospital.model.User;
import org.apache.ibatis.annotations.*;

@Mapper
public interface SessionMapper {

    @Insert("INSERT INTO `session` ( id, personid, cookie ) VALUES ( #{session.id}, #{session.personId}, #{session.cookie} )")
    @Options(useGeneratedKeys = true, keyProperty = "session.id")
    Integer insert(@Param("session") Session session);

    @Select("SELECT id, personid, cookie FROM session WHERE id = #{id}")
    @Results({
            @Result(property = "id", column = "id"),
            @Result(property = "personId", column = "personid"),
            @Result(property = "cookie", column = "cookie"),
    })
    Session getById(int id);

    @Select("SELECT id, personid, cookie FROM session WHERE cookie = #{cookie}")
    @Results({
            @Result(property = "id", column = "id"),
            @Result(property = "personId", column = "personid"),
            @Result(property = "cookie", column = "cookie"),
    })
    Session getBySessionId(String cookie);

    @Delete("DELETE FROM session WHERE cookie = #{cookie}")
    void delete(String cookie);

}
