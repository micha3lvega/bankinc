package co.com.novatec.bankinc.request;

import java.io.Serializable;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.springframework.validation.annotation.Validated;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Validated
@NoArgsConstructor
@AllArgsConstructor
public class PurchaseRequest implements Serializable {

	private static final long serialVersionUID = -6795091229544735993L;

	@NotNull(message = "El cardId no puede ser nulo")
	@Size(min = 16, max = 16, message = "El cardId debe tener 16 dígitos")
	private String cardId;

	@NotNull(message = "El precio no puede ser nulo")
	@DecimalMin(value = "0.0", inclusive = false, message = "El precio debe ser mayor que cero")
	private Double price;

}
