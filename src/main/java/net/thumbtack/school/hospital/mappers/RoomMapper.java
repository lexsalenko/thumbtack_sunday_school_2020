package net.thumbtack.school.hospital.mappers;

import net.thumbtack.school.hospital.model.Room;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface RoomMapper {

    @Insert("INSERT INTO room (id, room) VALUES ( #{room.id}, #{room.room} )")
    @Options(useGeneratedKeys = true, keyProperty = "room.id")
    Integer insert(@Param("room") Room room);

    @Select("SELECT id, room FROM room WHERE id = #{id}")
    @Results({
            @Result(property = "id", column = "id"),
            @Result(property = "room", column = "room")
    })
    Room getById(int id);

    @Select("SELECT id, room from room")
    @Results({
            @Result(property = "id", column = "id"),
            @Result(property = "room", column = "room")
    })
    List<Room> getAllRooms();

    @Delete("DELETE FROM room")
    void deleteAll();

}
