package net.thumbtack.school.hospital.model;

public class Session {

    private int id;
    private int personId;
    private String cookie;

    public Session() {
    }

    public Session(int id, int personId, String cookie) {
        this.id = id;
        this.personId = personId;
        this.cookie = cookie;
    }

    public Session(int personId, String cookie) {
        this.id = 0;
        this.personId = personId;
        this.cookie = cookie;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPersonId() {
        return personId;
    }

    public void setPersonId(int personId) {
        this.personId = personId;
    }

    public String getCookie() {
        return cookie;
    }

    public void setCookie(String cookie) {
        this.cookie = cookie;
    }
}
