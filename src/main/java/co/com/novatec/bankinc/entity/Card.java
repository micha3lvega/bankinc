package co.com.novatec.bankinc.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.validation.constraints.Size;

import org.springframework.validation.annotation.Validated;

import co.com.novatec.bankinc.enums.ECardStates;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Validated
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "cards")
public class Card implements Serializable {

	private static final long serialVersionUID = -2240856398525535025L;

	@Id
	@Column(name = "card_number", unique = true, length = 16)
	@Size(min = 16, max = 16, message = "El número de tarjeta debe tener exactamente 16 dígitos")
	private String cardNumber;

	@ManyToOne
	@JoinColumn(name = "client_id")
	private Client client;

	@Column(name = "state")
	@Enumerated(EnumType.STRING)
	private ECardStates state;

	@ManyToOne
	@JoinColumn(name = "product_id")
	private Product product;

	@Column(name = "balance")
	private String balance;

	@Column(name = "create_date")
	private LocalDateTime createDate;

	@Column(name = "expiration_date")
	private LocalDate expirationDate;

	@Column(name = "last_modified_date")
	private LocalDateTime lastModifiedDate;

	/**
	 * Metodo encargado de llenar los campos createDate y lastModifiedDate
	 */
	@PrePersist
	private void prePersist() {

		if (createDate == null) {
			createDate = LocalDateTime.now();
		}

		lastModifiedDate = LocalDateTime.now();

	}

	public Card balance(BigDecimal balance) {
		this.balance = balance.setScale(3, RoundingMode.HALF_UP).toEngineeringString();
		return this;
	}

}
