package backend.glloserver.global.exception;

import org.springframework.http.HttpStatus;

public interface ErrorResponse {

    HttpStatus getStatus();
    ErrorMessage getErrorMessage();
}
