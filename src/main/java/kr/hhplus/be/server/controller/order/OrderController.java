package kr.hhplus.be.server.controller.order;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import kr.hhplus.be.server.dto.ResponseDto;
import kr.hhplus.be.server.dto.order.OrderRequestDto;
import kr.hhplus.be.server.mock.FakeData;

@RestController
@RequestMapping("/orders")
@Tag(name = "주문 API", description = "상품 주문 관련 API")
public class OrderController {

    private final FakeData fakeData;

    @Autowired
    public OrderController(FakeData fakeData) {
        this.fakeData = fakeData;
    }

    @PostMapping
    @Operation(
        summary = "상품 주문",
        description = "사용자가 상품을 주문하는 API입니다.",
        requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
            required = true,
            description = "주문 요청 정보",
            content = @Content(
                schema = @Schema(implementation = OrderRequestDto.class),
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
    public ResponseEntity<ResponseDto> orderPoroduct(@RequestBody OrderRequestDto orderRequestDto) {
        ResponseDto res = new ResponseDto();
        return ResponseEntity.ok(res);
    }
}
