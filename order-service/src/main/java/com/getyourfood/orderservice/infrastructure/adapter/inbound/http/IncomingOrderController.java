package com.getyourfood.orderservice.infrastructure.adapter.inbound.http;

import com.getyourfood.orderservice.application.CreateOrderUseCase;
import com.getyourfood.orderservice.infrastructure.adapter.inbound.http.dto.OrderRequestDto;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@RestController
public class IncomingOrderController {

    private final CreateOrderUseCase createOrderUseCase;

    public IncomingOrderController(CreateOrderUseCase createOrderUseCase) {
        this.createOrderUseCase = createOrderUseCase;
    }

    @PostMapping("/v1/orders/new-order")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Order Placed Successfully", content = @Content(schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "400", description = "Invalid Order Request", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content),
    })
    public ResponseEntity<?> invoke(@RequestBody OrderRequestDto orderRequestDto){
        createOrderUseCase.invoke(orderRequestDto);
        return ResponseEntity.ok().build();
    }
}
