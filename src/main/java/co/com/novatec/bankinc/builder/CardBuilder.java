package co.com.novatec.bankinc.builder;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;

import co.com.novatec.bankinc.entity.Card;
import co.com.novatec.bankinc.entity.Client;
import co.com.novatec.bankinc.entity.Product;
import co.com.novatec.bankinc.enums.ECardStates;

/**
 * Builder para la construcción de objetos de tipo tarjeta.
 */
public class CardBuilder {

	private String cardNumber;
	private Client client;
	private ECardStates cardState;
	private Product product;
	private String balance;
	private LocalDateTime createDate;
	private LocalDate expirationDate;
	private LocalDateTime lastModifiedDate;

	/**
	 * Establece el número de tarjeta.
	 *
	 * @param cardNumber el número de tarjeta
	 * @return una referencia al propio objeto CardBuilder
	 */
	public CardBuilder cardNumber(String cardNumber) {
		this.cardNumber = cardNumber;
		return this;
	}

	/**
	 * Establece el cliente asociado a la tarjeta.
	 *
	 * @param client el cliente asociado
	 * @return una referencia al propio objeto CardBuilder
	 */
	public CardBuilder client(Client client) {
		this.client = client;
		return this;
	}

	/**
	 * Establece el estado de la tarjeta.
	 *
	 * @param cardState el estado de la tarjeta
	 * @return una referencia al propio objeto CardBuilder
	 */
	public CardBuilder cardState(ECardStates cardState) {
		this.cardState = cardState;
		return this;
	}

	/**
	 * Establece el producto asociado a la tarjeta.
	 *
	 * @param product el producto asociado
	 * @return una referencia al propio objeto CardBuilder
	 */
	public CardBuilder product(Product product) {
		this.product = product;
		return this;
	}

	/**
	 * Establece el saldo de la tarjeta.
	 *
	 * @param balance el saldo de la tarjeta
	 * @return una referencia al propio objeto CardBuilder
	 */
	public CardBuilder balance(BigDecimal balance) {
		this.balance = balance.setScale(3, RoundingMode.HALF_UP).toEngineeringString();
		return this;
	}

	/**
	 * Establece la fecha de creación de la tarjeta como la fecha y hora actual.
	 *
	 * @return una referencia al propio objeto CardBuilder
	 */
	public CardBuilder createDate() {
		this.createDate = LocalDateTime.now();
		return this;
	}

	/**
	 * Establece la fecha de creación de la tarjeta.
	 *
	 * @param createDate la fecha de creación de la tarjeta
	 * @return una referencia al propio objeto CardBuilder
	 */
	public CardBuilder createDate(LocalDateTime createDate) {
		this.createDate = createDate;
		return this;
	}

	/**
	 * Establece la fecha de vencimiento de la tarjeta como la fecha actual.
	 *
	 * @return una referencia al propio objeto CardBuilder
	 */
	public CardBuilder expirationDate() {
		this.expirationDate = LocalDate.now();
		return this;
	}

	/**
	 * Establece la fecha de vencimiento de la tarjeta.
	 *
	 * @param expirationDate la fecha de vencimiento de la tarjeta
	 * @return una referencia al propio objeto CardBuilder
	 */
	public CardBuilder expirationDate(LocalDate expirationDate) {
		this.expirationDate = expirationDate;
		return this;
	}

	/**
	 * Establece la fecha de última modificación de la tarjeta como la fecha y hora actual.
	 *
	 * @return una referencia al propio objeto CardBuilder
	 */
	public CardBuilder lastModifiedDate() {
		this.lastModifiedDate = LocalDateTime.now();
		return this;
	}

	/**
	 * Establece la fecha de última modificación de la tarjeta.
	 *
	 * @param lastModifiedDate la fecha de última modificación de la tarjeta
	 * @return una referencia al propio objeto CardBuilder
	 */
	public CardBuilder lastModifiedDate(LocalDateTime lastModifiedDate) {
		this.lastModifiedDate = lastModifiedDate;
		return this;
	}

	/**
	 * Establece el estado de la tarjeta como activo.
	 *
	 * @return una referencia al propio objeto CardBuilder
	 */
	public CardBuilder active() {
		this.cardState = ECardStates.ACTIVE;
		return this;
	}

	/**
	 * Establece el estado de la tarjeta como inactivo.
	 *
	 * @return una referencia al propio objeto CardBuilder
	 */
	public CardBuilder inactive() {
		this.cardState = ECardStates.INACTIVE;
		return this;
	}

	/**
	 * Establece el estado de la tarjeta como bloqueado.
	 *
	 * @return una referencia al propio objeto CardBuilder
	 */
	public CardBuilder blocked() {
		this.cardState = ECardStates.BLOCKED;
		return this;
	}

	/**
	 * Construye un objeto Card con los valores establecidos en el builder.
	 *
	 * @return un objeto Card construido
	 */
	public Card build() {
		return new Card(cardNumber, client, cardState, product, balance, createDate, expirationDate,
				lastModifiedDate);
	}

}
