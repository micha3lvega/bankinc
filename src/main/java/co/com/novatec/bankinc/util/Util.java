package co.com.novatec.bankinc.util;

import java.security.SecureRandom;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

/**
 * Clase de utilidades que proporciona métodos auxiliares para operaciones
 * comunes.
 */
public class Util {

	/**
	 * Ocultar constructor
	 */
	private Util() {
		throw new IllegalStateException("Utility class");
	}

	/**
	 * Genera una cadena de números aleatoria con la longitud especificada.
	 *
	 * @param length la longitud deseada de la cadena de números
	 * @return una cadena de números aleatoria
	 */
	public static String generateRandomStringNumber(int length) {
		var random = new SecureRandom();
		var builder = new StringBuilder();
		for (var i = 0; i < length; i++) {
			var number = random.nextInt(10);
			builder.append(number);
		}
		return builder.toString();
	}

	/**
	 * Convierte una instancia de Date a LocalDate.
	 *
	 * @param date la fecha a convertir
	 * @return la fecha convertida a LocalDate
	 */
	public static LocalDate dateToLocalDateTime(Date date) {
		if (date != null) {
			return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
		}
		return LocalDate.now();
	}

}
