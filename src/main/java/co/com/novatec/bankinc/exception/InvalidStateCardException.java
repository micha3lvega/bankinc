package co.com.novatec.bankinc.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.FORBIDDEN)
public class InvalidStateCardException extends RuntimeException {

	private static final long serialVersionUID = -7514450850439383665L;

	public InvalidStateCardException() {
		super("El estado de la tarjeta no es valido");
	}

	public InvalidStateCardException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public InvalidStateCardException(String message, Throwable cause) {
		super(message, cause);
	}

	public InvalidStateCardException(String message) {
		super(message);
	}

	public InvalidStateCardException(Throwable cause) {
		super(cause);
	}

}
