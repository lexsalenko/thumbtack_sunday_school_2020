package net.thumbtack.school.hospital.dao;

import net.thumbtack.school.hospital.model.Admin;
import net.thumbtack.school.hospital.model.Login;
import net.thumbtack.school.hospital.model.Session;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface AdminDao {

    Admin insert(Admin admin);

    Admin getById(int id);

    List<Admin> getAll();

    Admin update(Admin admin);

    void delete(int id);

    void deleteAll();

    List<Login> getAllLoginsAndPasswords();

    Admin getByUserId(int userid);

    Integer getAdminIdByUserId(int uid);

}
