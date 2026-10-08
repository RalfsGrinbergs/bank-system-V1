package banksystem.exceptions;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;


@ControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<String> entityNotFound(EntityNotFoundException e) {
    log.warn("Handle exception {}", e.getMessage());
    return ResponseEntity
            .status(404)
            .body(e.getMessage());
    }
    @ExceptionHandler(RoleException.class)
        public ResponseEntity<String> roleIsNotAllowed(RoleException e) {
            log.warn("Role is not allowed {}", e.getMessage());
            return ResponseEntity
                    .status(403)
                    .body(e.getMessage());

    }
    @ExceptionHandler(TransactionException.class)
        public ResponseEntity<String> transactionError(TransactionException e) {
            log.warn("Transaction error {}", e.getMessage());
            return ResponseEntity
                    .status(400)
                    .body(e.getMessage());
        }
        @ExceptionHandler(UserCreatingException.class)
        public ResponseEntity<String> userCreatingError(UserCreatingException e) {
            log.warn("User creating error {}", e.getMessage());
            return ResponseEntity
                    .status(400)
                    .body(e.getMessage());
        }

}
