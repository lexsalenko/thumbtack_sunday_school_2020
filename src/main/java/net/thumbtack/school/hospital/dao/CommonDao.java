package net.thumbtack.school.hospital.dao;

import net.thumbtack.school.hospital.model.Session;

public interface CommonDao {

    Session insertSession(Session session);

    Session getBySessionId(String sessionid);

    void deleteSession(String token);

}
