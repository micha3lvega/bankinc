package co.com.novatec.bankinc.request;

import java.io.Serializable;

import org.springframework.validation.annotation.Validated;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Validated
@NoArgsConstructor
@AllArgsConstructor
public class CardActiveRequest implements Serializable {

	private static final long serialVersionUID = -4131581473197663824L;
	private String cardId;

}
