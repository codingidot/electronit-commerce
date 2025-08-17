package kr.hhplus.be.server.coupon.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.hhplus.be.server.coupon.dto.CouponIssueRequest;
import kr.hhplus.be.server.coupon.service.CouponService;
import kr.hhplus.be.server.dto.ResponseDto;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/coupons")
@Tag(name = "Coupon", description = "쿠폰 관련 API")
public class CouponController {

    private final CouponService couponService;

    @Autowired
    public CouponController(CouponService couponService) {
        this.couponService = couponService;
    }

    @PostMapping("/issue")
    @Operation(
        summary = "쿠폰 발급",
        description = "유저 ID와 쿠폰 ID를 입력받아 쿠폰을 발급합니다.",
        requestBody = @RequestBody(
            description = "쿠폰 발급 요청",
            required = true,
            content = @Content(
                schema = @Schema(implementation = CouponIssueRequest.class),
                examples = @ExampleObject(
                    value = "{\"userId\": 1, \"couponId\": 100}"
                )
            )
        ),
        responses = {
            @ApiResponse(responseCode = "200", description = "쿠폰 발급 성공"),
            @ApiResponse(responseCode = "500", description = "쿠폰 발급 실패")
        }
    )
    public ResponseEntity<ResponseDto> issueCoupon(
        @org.springframework.web.bind.annotation.RequestBody CouponIssueRequest requestDto) {

        ResponseDto res = new ResponseDto();
        try {
        	couponService.issueCoupon(requestDto);
        } catch (Exception e) {
            e.printStackTrace();
            res.setCode(500);
            res.setMessage("쿠폰발급에 실패하였습니다.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(res);
        }
        return ResponseEntity.ok(res);
    }
}
