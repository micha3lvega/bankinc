package co.com.novatec.bankinc.exception.handler;

import java.util.Date;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import co.com.novatec.bankinc.exception.ExceptionResponse;
import co.com.novatec.bankinc.exception.InsufficientBalanceException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@ControllerAdvice
public class InsufficientBalanceExceptionHandler {

	@ExceptionHandler(value = { InsufficientBalanceException.class })
	public ResponseEntity<ExceptionResponse> exceptionHandler(InsufficientBalanceException ex) {

		var response = new ExceptionResponse();

		response.setDate(new Date());
		response.setMessage(ex.getMessage());
		response.setResponseCode(HttpStatus.BAD_REQUEST);

		// Agregar error
		if ((ex.getCause() != null) && (ex.getCause().getLocalizedMessage() != null)) {
			response.setMessage(ex.getCause().getLocalizedMessage());
		} else if ((ex.getMessage() != null) && !ex.getMessage().isBlank()) {
			response.setMessage(ex.getMessage());
		} else {
			response.setMessage("Saldo insuficiente");
		}

		log.error("InsufficientBalanceException: {}", ex.getMessage(), ex);
		return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);

	}

}
