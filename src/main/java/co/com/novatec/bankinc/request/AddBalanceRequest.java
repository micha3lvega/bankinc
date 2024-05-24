package co.com.novatec.bankinc.request;

import java.io.Serializable;

import javax.validation.constraints.DecimalMin;

import org.springframework.validation.annotation.Validated;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Validated
@NoArgsConstructor
@AllArgsConstructor
public class AddBalanceRequest implements Serializable {

	private static final long serialVersionUID = -6247475301147518362L;

	private String cardId;

	@DecimalMin(value = "0", message = "El balance debe ser mayor a cero")
	private String balance;


}
