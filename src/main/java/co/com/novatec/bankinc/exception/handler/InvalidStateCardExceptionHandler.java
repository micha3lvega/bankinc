package co.com.novatec.bankinc.exception.handler;

import java.util.Date;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import co.com.novatec.bankinc.exception.ExceptionResponse;
import co.com.novatec.bankinc.exception.InvalidStateCardException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@ControllerAdvice
public class InvalidStateCardExceptionHandler {

	@ExceptionHandler(value = { InvalidStateCardException.class })
	public ResponseEntity<ExceptionResponse> exceptionHandler(InvalidStateCardException ex) {

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
			response.setMessage("Estado de la tarjeta invalido");
		}

		log.error("InvalidStateCardException: {}", ex.getMessage(), ex);
		return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);

	}

}
