package net.thumbtack.school.hospital.validators;

import net.thumbtack.school.hospital.dto.requests.RegistrationAdministratorDtoRequest;
import net.thumbtack.school.hospital.exception.ServerErrorCode;
import net.thumbtack.school.hospital.exception.ServerException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class AdminValidator {

    @Value("${max_name_length}")
    private int max_name_length;

    @Value("${min_password_length}")
    private int min_password_length;



    public Optional<RegistrationAdministratorDtoRequest> validate(final RegistrationAdministratorDtoRequest admin) throws ServerException {
        if (!isValid(admin)) {
            return Optional.empty();
        }
        return Optional.of(admin);
    }



    public boolean isValid(final RegistrationAdministratorDtoRequest admin) throws ServerException {
        if (admin == null) {
            throw new ServerException(
                    ServerErrorCode.EMPTY_ADMIN_REQUEST,
                    "admin",
                    ServerErrorCode.EMPTY_ADMIN_REQUEST.getErrorString()
            );
        }

        if(admin.getLogin().length() > max_name_length) {
            throw new ServerException(
                    ServerErrorCode.LOGIN_IS_TOO_LONG,
                    "login",
                    ServerErrorCode.LOGIN_IS_TOO_LONG.getErrorString()
            );
        }

        if(admin.getPassword().length() > max_name_length) {
            throw new ServerException(
                    ServerErrorCode.PASSWORD_IS_TOO_LONG,
                    "password",
                    ServerErrorCode.PASSWORD_IS_TOO_LONG.getErrorString()
            );
        }

        if(admin.getFirstName().length() > max_name_length) {
            throw new ServerException(
                    ServerErrorCode.FIRST_NAME_IS_TOO_LONG,
                    "firstName",
                    ServerErrorCode.FIRST_NAME_IS_TOO_LONG.getErrorString()
            );
        }

        if(admin.getLastName().length() > max_name_length) {
            throw new ServerException(
                ServerErrorCode.LAST_NAME_IS_TOO_LONG,
                "lastName",
                ServerErrorCode.LAST_NAME_IS_TOO_LONG.getErrorString()
            );
        }

        if(admin.getPatronymic().length() > max_name_length) {
            throw new ServerException(
                    ServerErrorCode.PATRONYMIC_IS_TOO_LONG,
                    "lastName",
                    ServerErrorCode.PATRONYMIC_IS_TOO_LONG.getErrorString()
            );
        }

        if(admin.getPassword().length() < min_password_length) {
            throw new ServerException(
                    ServerErrorCode.PASSWORD_IS_TOO_SHORT,
                    "password",
                    ServerErrorCode.PASSWORD_IS_TOO_SHORT.getErrorString()
            );
        }


        return true;
    }

}
