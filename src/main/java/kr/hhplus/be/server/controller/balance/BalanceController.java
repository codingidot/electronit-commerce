package kr.hhplus.be.server.controller.balance;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.hhplus.be.server.dto.ResponseDto;
import kr.hhplus.be.server.dto.balance.ChargeRequestDto;
import kr.hhplus.be.server.mock.FakeData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/wallet")
@Tag(name = "Balance", description = "잔액 관련 API")
public class BalanceController {

    private final FakeData fakeData;

    @Autowired
    public BalanceController(FakeData fakeData){
        this.fakeData = fakeData;
    }

    @GetMapping("/balance/{userId}")
    @Operation(
        summary = "잔액 조회",
        description = "사용자의 현재 잔액을 조회합니다.",
        responses = {
            @ApiResponse(responseCode = "200", description = "잔액 조회 성공"),
            @ApiResponse(responseCode = "500", description = "잔액 조회 실패")
        }
    )
    public ResponseEntity<ResponseDto> getTotalBalance(
        @PathVariable("userId") Long userId) {

        ResponseDto res = new ResponseDto();
        try {
            res.setData(fakeData.getBalanceInfo(userId));
        } catch(Exception e) {
            e.printStackTrace();
            res.setCode(500);
            res.setMessage("잔액조회에 실패하였습니다.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(res);
        }
        return ResponseEntity.ok(res);
    }

    @PostMapping("/charge")
    @Operation(
        summary = "잔액 충전",
        description = "요청된 금액을 사용자의 잔액에 충전합니다.",
        requestBody = @RequestBody(
            description = "충전 요청 정보",
            required = true,
            content = @Content(
                schema = @Schema(implementation = ChargeRequestDto.class),
                examples = @ExampleObject(
                    value = "{\"userId\": 1, \"amount\": 10000}"
                )
            )
        ),
        responses = {
            @ApiResponse(responseCode = "200", description = "충전 성공"),
            @ApiResponse(responseCode = "500", description = "충전 실패")
        }
    )
    public ResponseEntity<ResponseDto> chargeBalance(
        @org.springframework.web.bind.annotation.RequestBody ChargeRequestDto chargeRequestDto) {

        ResponseDto res = new ResponseDto();
        try {
            res.setData(fakeData.chargeBalance(chargeRequestDto));
        } catch(Exception e) {
            e.printStackTrace();
            res.setCode(500);
            res.setMessage("잔액충전에 실패하였습니다.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(res);
        }
        return ResponseEntity.ok(res);
    }
}
