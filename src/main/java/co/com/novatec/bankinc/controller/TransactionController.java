package co.com.novatec.bankinc.controller;

import java.util.UUID;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import co.com.novatec.bankinc.entity.Transaction;
import co.com.novatec.bankinc.exception.ExceptionResponse;
import co.com.novatec.bankinc.request.AnulationTransactionRequest;
import co.com.novatec.bankinc.request.PurchaseRequest;
import co.com.novatec.bankinc.services.TransactionServices;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

@RestController
@RequestMapping("/transaction")
public class TransactionController {

	private TransactionServices transactionServices;

	public TransactionController(TransactionServices transactionServices) {
		this.transactionServices = transactionServices;
	}

	@GetMapping("/{transactionId}")
	@Operation(summary = "Buscar una transacción", description = "Busca una transacción por su ID.")
	@ApiResponse(responseCode = "200", description = "Transacción encontrada", content = @Content(schema = @Schema(implementation = Transaction.class)))
	@ApiResponse(responseCode = "404", description = "Transacción no encontrada", content = @Content(schema = @Schema(implementation = ExceptionResponse.class)))
	public Transaction findById(@PathVariable("transactionId") UUID transactionId) {
		return transactionServices.findById(transactionId);
	}

	@PostMapping
	@Operation(summary = "Realizar una compra", description = "Realiza una compra utilizando una solicitud de compra.")
	@ApiResponse(responseCode = "200", description = "Compra realizada exitosamente", content = @Content(schema = @Schema(implementation = Transaction.class)))
	@ApiResponse(responseCode = "400", description = "Solicitud inválida", content = @Content(schema = @Schema(implementation = ExceptionResponse.class)))
	@ApiResponse(responseCode = "403", description = "Tarjeta inactiva", content = @Content(schema = @Schema(implementation = ExceptionResponse.class)))
	@ApiResponse(responseCode = "409", description = "Saldo insuficiente para realizar la compra", content = @Content(schema = @Schema(implementation = ExceptionResponse.class)))
	public Transaction purchase(@RequestBody PurchaseRequest purchaseRequest) {
		return transactionServices.purchase(purchaseRequest);
	}

	@PostMapping("/anulation")
	@Operation(summary = "Anular una transacción", description = "Anula una transacción previamente realizada.")
	@ApiResponse(responseCode = "200", description = "Transacción anulada exitosamente", content = @Content(schema = @Schema(implementation = AnulationTransactionRequest.class)))
	@ApiResponse(responseCode = "400", description = "Solicitud inválida", content = @Content(schema = @Schema(implementation = ExceptionResponse.class)))
	@ApiResponse(responseCode = "404", description = "Transacción no encontrada", content = @Content(schema = @Schema(implementation = ExceptionResponse.class)))
	@ApiResponse(responseCode = "403", description = "Tarjeta inválida para la anulación", content = @Content(schema = @Schema(implementation = ExceptionResponse.class)))
	@ApiResponse(responseCode = "409", description = "Tiempo máximo de anulación excedido", content = @Content(schema = @Schema(implementation = ExceptionResponse.class)))
	public Transaction anulation(AnulationTransactionRequest request) {
		return transactionServices.anulation(request);
	}
}
