package net.thumbtack.school.hospital.service;

import net.thumbtack.school.hospital.dto.responses.ErrorDtoResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.ArrayList;
import java.util.List;

@ControllerAdvice
public class GlobalErrorHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public Error handleValidation(MethodArgumentNotValidException exc) {
        final Error error = new Error();
        exc.getBindingResult().getFieldErrors().forEach(fieldError-> {
            error.getAllErrors().add(
                    new ErrorDtoResponse(
                            fieldError.getCode(),
                            fieldError.getField(),
                            fieldError.getDefaultMessage()
                    )
            );
        });
        return error;
    }

    public static class Error {
        private List<ErrorDtoResponse> allErrors = new ArrayList<>();

        public List<ErrorDtoResponse> getAllErrors() {
            return allErrors;
        }

        public void setAllErrors(List<ErrorDtoResponse> allErrors) {
            this.allErrors = allErrors;
        }
    }
}
