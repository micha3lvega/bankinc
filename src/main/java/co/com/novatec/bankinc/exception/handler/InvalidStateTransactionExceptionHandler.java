package co.com.novatec.bankinc.exception.handler;

import java.util.Date;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import co.com.novatec.bankinc.exception.ExceptionResponse;
import co.com.novatec.bankinc.exception.InvalidStateTransactionException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@ControllerAdvice
public class InvalidStateTransactionExceptionHandler {

	@ExceptionHandler(value = { InvalidStateTransactionException.class })
	public ResponseEntity<ExceptionResponse> exceptionHandler(InvalidStateTransactionException ex) {

		var response = new ExceptionResponse();

		response.setDate(new Date());
		response.setMessage(ex.getMessage());
		response.setResponseCode(HttpStatus.FORBIDDEN);

		// Agregar error
		if ((ex.getCause() != null) && (ex.getCause().getLocalizedMessage() != null)) {
			response.setMessage(ex.getCause().getLocalizedMessage());
		} else if ((ex.getMessage() != null) && !ex.getMessage().isBlank()) {
			response.setMessage(ex.getMessage());
		} else {
			response.setMessage("El estado de la transaccion es invalido");
		}

		log.error("InvalidStateTransactionException: {}", ex.getMessage(), ex);
		return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);

	}

}
