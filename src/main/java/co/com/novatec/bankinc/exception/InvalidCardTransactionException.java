package co.com.novatec.bankinc.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.FORBIDDEN)
public class InvalidCardTransactionException extends RuntimeException {

	private static final long serialVersionUID = -7514450850439383665L;

	public InvalidCardTransactionException() {
		super("La tarjeta no pertenece a la transaccion");
	}

	public InvalidCardTransactionException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public InvalidCardTransactionException(String message, Throwable cause) {
		super(message, cause);
	}

	public InvalidCardTransactionException(String message) {
		super(message);
	}

	public InvalidCardTransactionException(Throwable cause) {
		super(cause);
	}

}
