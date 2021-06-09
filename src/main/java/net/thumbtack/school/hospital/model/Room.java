package net.thumbtack.school.hospital.model;

import java.util.Objects;

public class Room {

    private int id;
    private String room;

    public Room(int id, String room) {
        this.id = id;
        this.room = room;
    }

    public Room(String room) {
        this.id = 0;
        this.room = room;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Room)) return false;
        Room room1 = (Room) o;
        return getId() == room1.getId() &&
                Objects.equals(getRoom(), room1.getRoom());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getRoom());
    }

}
