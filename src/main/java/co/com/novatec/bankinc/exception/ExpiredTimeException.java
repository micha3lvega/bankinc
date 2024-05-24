package co.com.novatec.bankinc.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class ExpiredTimeException extends RuntimeException {

	private static final long serialVersionUID = 5494844324438228341L;

	public ExpiredTimeException() {
		super("El tiempo ha expirado");
	}

	public ExpiredTimeException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public ExpiredTimeException(String message, Throwable cause) {
		super(message, cause);
	}

	public ExpiredTimeException(String message) {
		super(message);
	}

	public ExpiredTimeException(Throwable cause) {
		super(cause);
	}

}
