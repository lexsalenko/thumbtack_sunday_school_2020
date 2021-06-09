package net.thumbtack.school.hospital.exception;

public enum ServerErrorCode {

    LOGIN_ALREADY_EXIST("Login already exists"),
    USER_ALREADY_EXIST("User already exist"),
    ACCESS_DENIED("Access denied"),
    SPECIALITY_DOES_NOT_EXIST("Speciality does not exist"),
    ROOM_DOES_NOT_EXIST("Room does not exist"),
    PASSWORD_ALREADY_EXIST("Password already exist"),
    LOGIN_IS_TOO_LONG("Login is too long"),
    PASSWORD_IS_TOO_LONG("Password is too long"),
    FIRST_NAME_IS_TOO_LONG("First name is too long"),
    LAST_NAME_IS_TOO_LONG("Last name is too long"),
    PATRONYMIC_IS_TOO_LONG("Patronymic is too long"),
    PASSWORD_IS_TOO_SHORT("Password is too short"),
    EMPTY_ADMIN_REQUEST("Empty admin request"),
    PATIENT_IS_BUSY("The patient is busy"),
    DOCTOR_IS_BUSY("The doctor is busy"),
    TOO_MANY_PARAMETERS("Too many parameters");




    private String message;

    private ServerErrorCode(String message) {
        this.message = message;
    }

    public String getErrorString() {
        return message;
    }


}
