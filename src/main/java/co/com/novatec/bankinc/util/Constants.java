package co.com.novatec.bankinc.util;

import java.math.BigDecimal;

public class Constants {

	/**
	 * Ocultar constructor
	 */
	private Constants() {
		throw new IllegalStateException("Constant class");
	}

	// Valor máximo de horas para anular una transacción (24 horas).
	public static final int MAX_HOURS_ANULATION_VALUE = 24;

	// Longitud del número de la tarjeta (16 dígitos).
	public static final int LENGTH_NUMBER_CARD = 16;

	// Número de años adicionales a la fecha actual para calcular la fecha de vencimiento de una tarjeta (3 años).
	public static final int NUMBER_YEARS_ADD_EXPIRATION_DATE = 3;

	// Saldo inicial predeterminado para una tarjeta (0). Es de tipo BigDecimal.
	public static final BigDecimal DEFAULT_INIT_BALANCE = BigDecimal.ZERO;

}
