package co.com.novatec.bankinc.controller;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import co.com.novatec.bankinc.entity.Card;
import co.com.novatec.bankinc.exception.ExceptionResponse;
import co.com.novatec.bankinc.request.AddBalanceRequest;
import co.com.novatec.bankinc.request.CardActiveRequest;
import co.com.novatec.bankinc.response.GetBalanceResponse;
import co.com.novatec.bankinc.services.CardServices;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

@RestController
@RequestMapping("/card")
public class CardController {

	private CardServices cardServices;

	public CardController(CardServices cardServices) {
		this.cardServices = cardServices;
	}

	@GetMapping("/{productId}/number")
	@Operation(summary = "Obtener una tarjeta", description = "Obtiene una tarjeta generada para un producto específico.")
	@ApiResponse(responseCode = "200", description = "Tarjeta generada exitosamente")
	public Card getCard(@PathVariable("productId") Long productId) {
		return cardServices.generateCard(productId);
	}

	@Operation(summary = "Obtener el saldo de una tarjeta", description = "Obtiene el saldo actual de una tarjeta.")
	@ApiResponse(responseCode = "200", description = "Saldo obtenido exitosamente", content = @Content(schema = @Schema(implementation = GetBalanceResponse.class)))
	@ApiResponse(responseCode = "404", description = "Tarjeta no encontrada", content = @Content(schema = @Schema(implementation = ExceptionResponse.class)))
	public GetBalanceResponse getBalance(@PathVariable("cardId") String cardId) {
		return cardServices.getBalance(cardId);
	}

	@PostMapping("/enroll")
	@Operation(summary = "Activar una tarjeta", description = "Activa una tarjeta activa.")
	@ApiResponse(responseCode = "200", description = "Tarjeta activada exitosamente", content = @Content(schema = @Schema(implementation = Card.class)))
	@ApiResponse(responseCode = "400", description = "Solicitud inválida", content = @Content(schema = @Schema(implementation = ExceptionResponse.class)))
	public Card enroll(@RequestBody CardActiveRequest request) {
		return cardServices.enroll(request);
	}

	@PostMapping("/balance")
	@Operation(summary = "Recargar saldo de una tarjeta", description = "Recarga saldo a una tarjeta existente.")
	@ApiResponse(responseCode = "200", description = "Saldo recargado exitosamente", content = @Content(schema = @Schema(implementation = Card.class)))
	@ApiResponse(responseCode = "400", description = "Solicitud inválida", content = @Content(schema = @Schema(implementation = ExceptionResponse.class)))
	@ApiResponse(responseCode = "404", description = "Tarjeta no encontrada", content = @Content(schema = @Schema(implementation = ExceptionResponse.class)))
	public Card reloadBalanceCard(@RequestBody AddBalanceRequest request) {
		return cardServices.reloadBalanceCard(request);
	}


	@DeleteMapping("/{cardId}")
	@Operation(summary = "Bloquear una tarjeta", description = "Bloquea una tarjeta existente.")
	@ApiResponse(responseCode = "200", description = "Tarjeta bloqueada exitosamente", content = @Content(schema = @Schema(implementation = Card.class)))
	@ApiResponse(responseCode = "404", description = "Tarjeta no encontrada", content = @Content(schema = @Schema(implementation = ExceptionResponse.class)))
	public Card blockCard(@PathVariable("cardId") String cardId) {
		return cardServices.blockCard(cardId);
	}

}
