package net.thumbtack.school.hospital.service;

import net.thumbtack.school.hospital.dao.AdminDao;
import net.thumbtack.school.hospital.dao.CommonDao;
import net.thumbtack.school.hospital.dao.UserDao;
import net.thumbtack.school.hospital.daoimpl.AdminDaoImpl;
import net.thumbtack.school.hospital.daoimpl.CommonDaoImpl;
import net.thumbtack.school.hospital.daoimpl.UserDaoImpl;
import net.thumbtack.school.hospital.dto.requests.DeletingDoctorDtoRequest;
import net.thumbtack.school.hospital.dto.requests.EditingAdministratorProfileDtoRequest;
import net.thumbtack.school.hospital.dto.requests.LoginDtoRequest;
import net.thumbtack.school.hospital.dto.requests.RegistrationAdministratorDtoRequest;
import net.thumbtack.school.hospital.dto.responses.RegistrationAdministratorDtoResponse;
import net.thumbtack.school.hospital.exception.ServerErrorCode;
import net.thumbtack.school.hospital.exception.ServerException;
import net.thumbtack.school.hospital.model.*;
import net.thumbtack.school.hospital.validators.AdminValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.Cookie;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class AdminService {

    private AdminDao adminDao;

    private CommonDao commonDao;

    private UserDao userDao;

    @Autowired
    public AdminService(AdminDaoImpl adminDao, UserDaoImpl userDao, CommonDaoImpl commonDao) {
        this.adminDao = adminDao;
        this.userDao = userDao;
        this.commonDao = commonDao;
    }


    public RegistrationAdministratorDtoResponse registerAdmin(RegistrationAdministratorDtoRequest administratorDtoRequest, Cookie[] cookies) throws ServerException {



            String cookie = "";
            for (Cookie c : cookies) {
                if (c.getName().equals("JAVASESSIONID")) {
                    cookie = c.getValue();
                }
            }

            int id = commonDao.getBySessionId(cookie).getPersonId();
            User user = userDao.getById(id);
            if (!user.getUserType().equals(UserType.ADMIN)) {
                throw new ServerException(
                        ServerErrorCode.ACCESS_DENIED,
                        "admin",
                        ServerErrorCode.ACCESS_DENIED.getErrorString()
                );
            }


            Admin admin = new Admin(
                    new User(
                            UserType.ADMIN,
                            administratorDtoRequest.getFirstName(),
                            administratorDtoRequest.getLastName(),
                            administratorDtoRequest.getPatronymic(),
                            administratorDtoRequest.getLogin(),
                            administratorDtoRequest.getPassword()),
                    administratorDtoRequest.getPosition()
            );

            admin = adminDao.insert(admin);

            return new RegistrationAdministratorDtoResponse(
                    admin.getId(),
                    admin.getUser().getFirstName(),
                    admin.getUser().getLastName(),
                    admin.getUser().getPatronymic(),
                    admin.getPosition()
            );

    }


    public RegistrationAdministratorDtoResponse editingAdministratorProfile(EditingAdministratorProfileDtoRequest request, Cookie[] cookies) {


        String cookie = "";
        for(Cookie c : cookies) {
            if(c.getName().equals("JAVASESSIONID")) {
                cookie = c.getValue();
            }
        }

        int id = commonDao.getBySessionId(cookie).getPersonId();
        User user = userDao.getById(id);
        Admin admin = adminDao.getByUserId(user.getId());

        if(!admin.getUser().getPassword().equals(request.getOldPassword())) {
            return null;
        }

        if(!user.getUserType().equals(UserType.ADMIN)) {
            // error
        }

        admin.setUser(new User(
                admin.getUser().getId(),
                admin.getUser().getUserType(),
                request.getFirstName(),
                request.getLastName(),
                request.getPatronymic(),
                admin.getUser().getLogin(),
                request.getNewPassword()));

        admin.setPosition(request.getPosition());

        admin = adminDao.update(admin);

        return new RegistrationAdministratorDtoResponse(
                admin.getId(),
                admin.getUser().getFirstName(),
                admin.getUser().getLastName(),
                admin.getUser().getPatronymic(),
                admin.getPosition()
        );
    }




}
