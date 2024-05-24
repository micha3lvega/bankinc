package co.com.novatec.bankinc.entity;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import co.com.novatec.bankinc.enums.ETransactionState;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@Table(name = "transactions")
public class Transaction implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "transaccion_id", columnDefinition = "BINARY(16)")
	private UUID transactionId;

	@ManyToOne
	@JoinColumn(name = "card_id")
	private Card card;

	@Column(name = "state")
	@Enumerated(EnumType.STRING)
	private ETransactionState state;

	@Column(name = "amount")
	private String amount;

	@Column(name = "create_date")
	private LocalDateTime createDate;

}
