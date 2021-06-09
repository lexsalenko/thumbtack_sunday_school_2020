package net.thumbtack.school.hospital.model;

import java.util.Objects;

public class Login {

    private int userid;
    private String login;
    private String password;

    public Login() {
    }

    public Login(int userid, String login, String password) {
        this.userid = userid;
        this.login = login;
        this.password = password;
    }

    public int getUserid() {
        return userid;
    }

    public void setUserid(int userid) {
        this.userid = userid;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Login)) return false;
        Login login1 = (Login) o;
        return getUserid() == login1.getUserid() &&
                Objects.equals(getLogin(), login1.getLogin()) &&
                Objects.equals(getPassword(), login1.getPassword());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getUserid(), getLogin(), getPassword());
    }

}
