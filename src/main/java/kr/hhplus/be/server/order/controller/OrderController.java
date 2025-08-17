package kr.hhplus.be.server.order.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.hhplus.be.server.dto.ResponseDto;
import kr.hhplus.be.server.order.dto.OrderRequest;
import kr.hhplus.be.server.order.service.OrderFacade;
import kr.hhplus.be.server.order.service.OrderService;

@RestController
@RequestMapping("/orders")
@Tag(name = "주문 API", description = "상품 주문 관련 API")
public class OrderController {

    private final OrderFacade orderFacade;
    private final OrderService orderService;

    @Autowired
    public OrderController(OrderFacade orderFacade, OrderService orderService) {
        this.orderFacade = orderFacade;
        this.orderService = orderService; 
    }

    @PostMapping
    @Operation(
        summary = "상품 주문",
        description = "사용자가 상품을 주문하는 API입니다.",
        requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
            required = true,
            description = "주문 요청 정보",
            content = @Content(
                schema = @Schema(implementation = OrderRequest.class),
                examples = @ExampleObject(value = "{ \"userId\": 1, \"productId\": 100, \"quantity\": 2 }")
            )
        ),
        responses = {
            @ApiResponse(responseCode = "200", description = "주문 성공",
                content = @Content(schema = @Schema(implementation = ResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청",
                content = @Content),
            @ApiResponse(responseCode = "500", description = "서버 오류",
                content = @Content)
        }
    )
    public ResponseEntity<ResponseDto> orderPoroduct(@RequestBody OrderRequest orderRequestDto) {
        ResponseDto res = new ResponseDto();
        try {
        	orderFacade.order(orderRequestDto);
        }catch(Exception e) {
        	res.setCode(500);
        	res.setMessage(e.getMessage());
        	return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(res);
        }
        return ResponseEntity.ok(res);
    }
   
    @GetMapping("/orderInfo")
    public ResponseEntity<ResponseDto> getOrderInfo(@RequestParam Long orderId){
    	ResponseDto res = new ResponseDto();
        try {
        	res.setData(orderService.getOrderInfo(orderId));
        }catch(Exception e) {
        	res.setCode(500);
        	res.setMessage(e.getMessage());
        	return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(res);
        }
        return ResponseEntity.ok(res);
   }
}
