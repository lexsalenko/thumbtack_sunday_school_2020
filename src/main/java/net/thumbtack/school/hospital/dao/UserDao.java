package net.thumbtack.school.hospital.dao;

import net.thumbtack.school.hospital.model.Admin;
import net.thumbtack.school.hospital.model.Login;
import net.thumbtack.school.hospital.model.User;

import java.util.List;

public interface UserDao {

    Integer insert(User user);

    User getById(int id);

    List<Login> getAllLoginsAndPasswords();

    void update(User user);

    void delete(int id);

    void deleteAll();

}
