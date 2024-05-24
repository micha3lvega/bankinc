package co.com.novatec.bankinc.request;

import java.io.Serializable;
import java.util.UUID;

import org.springframework.validation.annotation.Validated;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Validated
@NoArgsConstructor
@AllArgsConstructor
public class AnulationTransactionRequest implements Serializable {

	private static final long serialVersionUID = 358244368023282724L;
	private String cardId;
	private UUID transactionId;

}
