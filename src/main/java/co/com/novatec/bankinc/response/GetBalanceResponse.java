package co.com.novatec.bankinc.response;

import java.io.Serializable;

import org.springframework.validation.annotation.Validated;

import co.com.novatec.bankinc.entity.Card;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Validated
@NoArgsConstructor
@AllArgsConstructor
public class GetBalanceResponse implements Serializable {

	private static final long serialVersionUID = 5813729201752780420L;

	private String cardId;
	private String balance;
	private String state;

	public GetBalanceResponse(Card card) {

		if (card != null) {

			this.balance = card.getBalance();
			this.cardId = card.getCardNumber();
			this.state = card.getState().name();

		}

	}
}
