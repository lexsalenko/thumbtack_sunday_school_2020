package net.thumbtack.school.hospital.exception;

public class ServerException extends Exception {

    private ServerErrorCode serverErrorCode;
    private String field;
    private String message;

    public ServerException(ServerErrorCode serverErrorCode, String field, String message) {
        this.serverErrorCode = serverErrorCode;
        this.field = field;
        this.message = message;
    }

    public ServerErrorCode getServerErrorCode() {
        return serverErrorCode;
    }

    public String getField() {
        return field;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
