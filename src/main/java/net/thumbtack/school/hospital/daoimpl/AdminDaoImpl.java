package net.thumbtack.school.hospital.daoimpl;

import net.thumbtack.school.hospital.dao.AdminDao;
import net.thumbtack.school.hospital.mappers.AdminMapper;
import net.thumbtack.school.hospital.mappers.SessionMapper;
import net.thumbtack.school.hospital.mappers.UserMapper;
import net.thumbtack.school.hospital.model.Admin;
import net.thumbtack.school.hospital.model.Login;
import net.thumbtack.school.hospital.model.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;

import java.util.List;


public class AdminDaoImpl implements AdminDao {

    private static final Logger LOGGER = LoggerFactory.getLogger(AdminDaoImpl.class);

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private AdminMapper adminMapper;



    @Override
    public Admin insert(Admin admin) {
        LOGGER.debug("DAO insert Admin {}", admin);
        try {
            userMapper.insert(admin.getUser());
            adminMapper.insert(admin.getUser().getId(), admin);
        } catch (DataAccessException ex) {
            LOGGER.info("Can't not insert Admin {} {}", admin, ex);
            throw ex;
        }
        return admin;
    }

    @Override
    public Admin getById(int id) {
        LOGGER.debug("DAO get Admin by Id {}", id);
        try {
            return adminMapper.getById(id);
        } catch (DataAccessException ex) {
            LOGGER.info("Can't get Admin {}", ex);
            throw ex;
        }
    }

    @Override
    public List<Admin> getAll() {
        LOGGER.debug("DAO get all Admin");
        try {
            return adminMapper.getAll();
        } catch (DataAccessException ex) {
            LOGGER.info("Can't get all Admin {}", ex);
            throw ex;
        }
    }

    @Override
    public Admin update(Admin admin) {
        LOGGER.debug("DAO update Admin {]", admin);
        try {
            userMapper.update(admin.getUser());
            adminMapper.update(admin);
        } catch (DataAccessException ex) {
            LOGGER.info("Can't update Admin {} {}", admin, ex);
            throw ex;
        }
        return admin;
    }

    @Override
    public void delete(int id) {
        LOGGER.debug("DAO delete Admin");
        try {
            adminMapper.delete(id);
        } catch (DataAccessException ex) {
            LOGGER.info("Can't delete Admin {}", ex);
            throw ex;
        }
    }

    @Override
    public void deleteAll() {
        LOGGER.debug("DAO delete all Admin");
        try {
            adminMapper.deleteAll();
        } catch (DataAccessException ex) {
            LOGGER.info("Can't delete all Admin {}", ex);
            throw ex;
        }
    }

    @Override
    public List<Login> getAllLoginsAndPasswords() {
        LOGGER.debug("DAO Get all logins and passwords  {}");
        try {
            return userMapper.getAllLoginsAndPasswords();
        } catch (DataAccessException ex) {
            LOGGER.info("Can't get Admin {}", ex);
            throw ex;
        }
    }

    @Override
    public Admin getByUserId(int userid) {
        LOGGER.debug("DAO Get all logins and passwords Admin {}");
        try {
            return adminMapper.getByUserId(userid);
        } catch (DataAccessException ex) {
            LOGGER.info("Can't get Admin {}", ex);
            throw ex;
        }
    }

    public Integer getAdminIdByUserId(int uid){
        LOGGER.debug("DAO Get all logins and passwords Admin {}");
        try {
            System.out.println();
            System.out.println("U  = " + uid);
            System.out.println();
            return adminMapper.getAdminIdByUserId(uid);
        } catch (DataAccessException ex) {
            LOGGER.info("Can't get Admin {}", ex);
            throw ex;
        }
    }


}
