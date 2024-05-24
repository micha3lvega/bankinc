package co.com.novatec.bankinc.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class NoActiveException extends RuntimeException {

	private static final long serialVersionUID = -4930821935893173828L;

	public NoActiveException() {
	}

	public NoActiveException(String message) {
		super(message);
	}

	public NoActiveException(Throwable cause) {
		super(cause);
	}

	public NoActiveException(String message, Throwable cause) {
		super(message, cause);
	}

	public NoActiveException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
