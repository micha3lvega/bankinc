package co.com.novatec.bankinc.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.FORBIDDEN)
public class InvalidStateTransactionException extends RuntimeException {

	private static final long serialVersionUID = -7514450850439383665L;

	public InvalidStateTransactionException() {
		super("El estado de la transaccion es invalido");
	}

	public InvalidStateTransactionException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public InvalidStateTransactionException(String message, Throwable cause) {
		super(message, cause);
	}

	public InvalidStateTransactionException(String message) {
		super(message);
	}

	public InvalidStateTransactionException(Throwable cause) {
		super(cause);
	}

}
