package net.thumbtack.school.hospital.daoimpl;

import net.thumbtack.school.hospital.dao.UserDao;
import net.thumbtack.school.hospital.mappers.UserMapper;
import net.thumbtack.school.hospital.model.Login;
import net.thumbtack.school.hospital.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;

import java.util.List;

public class UserDaoImpl implements UserDao {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserDaoImpl.class);

    @Autowired
    private UserMapper userMapper;

    public Integer insert(User user){
        LOGGER.debug("DAO delete Admin");
        try {
            return userMapper.insert(user);
        } catch (DataAccessException ex) {
            LOGGER.info("Can't delete Admin {}", ex);
            throw ex;
        }
    }

    public User getById(int id) {
        LOGGER.debug("DAO delete Admin");
        try {
            return userMapper.getById(id);
        } catch (DataAccessException ex) {
            LOGGER.info("Can't delete Admin {}", ex);
            throw ex;
        }
    }

    public List<Login> getAllLoginsAndPasswords() {
        LOGGER.debug("DAO delete Admin");
        try {
            return userMapper.getAllLoginsAndPasswords();
        } catch (DataAccessException ex) {
            LOGGER.info("Can't delete Admin {}", ex);
            throw ex;
        }
    }

    public void update(User user) {
        LOGGER.debug("DAO delete Admin");
        try {
            userMapper.update(user);
        } catch (DataAccessException ex) {
            LOGGER.info("Can't delete Admin {}", ex);
            throw ex;
        }
    }

    public void delete(int id) {
        LOGGER.debug("DAO delete Admin");
        try {
            userMapper.delete(id);
        } catch (DataAccessException ex) {
            LOGGER.info("Can't delete Admin {}", ex);
            throw ex;
        }
    }

    public void deleteAll() {
        LOGGER.debug("DAO delete Admin");
        try {
            userMapper.deleteAll();
        } catch (DataAccessException ex) {
            LOGGER.info("Can't delete Admin {}", ex);
            throw ex;
        }
    }

}
