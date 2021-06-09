package net.thumbtack.school.hospital.daoimpl;

import net.thumbtack.school.hospital.dao.CommonDao;
import net.thumbtack.school.hospital.mappers.SessionMapper;
import net.thumbtack.school.hospital.model.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;

public class CommonDaoImpl implements CommonDao {

    private static final Logger LOGGER = LoggerFactory.getLogger(CommonDaoImpl.class);

    @Autowired
    private SessionMapper sessionMapper;

    @Override
    public Session insertSession(Session session){
        LOGGER.debug("DAO Get all logins and passwords Admin {}");
        try {
            sessionMapper.insert(session);
        } catch (DataAccessException ex) {
            LOGGER.info("Can't get Admin {}", ex);
            throw ex;
        }
        return session;
    }

    public Session getBySessionId(String sessionid){
        LOGGER.debug("DAO Get all logins and passwords Admin {}");
        try {
            return sessionMapper.getBySessionId(sessionid);
        } catch (DataAccessException ex) {
            LOGGER.info("Can't get Admin {}", ex);
            throw ex;
        }
    }


    @Override
    public void deleteSession(String token) {
        LOGGER.debug("DAO Get all logins and passwords Admin {}");
        try {
            sessionMapper.delete(token);
        } catch (DataAccessException ex) {
            LOGGER.info("Can't get Admin {}", ex);
            throw ex;
        }
    }

}
