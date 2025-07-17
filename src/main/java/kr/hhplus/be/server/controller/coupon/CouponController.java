package kr.hhplus.be.server.controller.coupon;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import kr.hhplus.be.server.dto.ResponseDto;
import kr.hhplus.be.server.dto.coupon.CouponIssueRequestDto;
import kr.hhplus.be.server.mock.FakeData;

@RestController
@RequestMapping("/coupons")
public class CouponController {
	
	private FakeData fakeData;

    @Autowired
    public CouponController(FakeData fakeData){
        this.fakeData = fakeData;
    }
	
	@PostMapping("/issue")
	public ResponseEntity<ResponseDto> issueCoupon(@RequestBody CouponIssueRequestDto requestDto ){
		ResponseDto res = new ResponseDto();
		try {
			res.setData(fakeData.issueCoupon(requestDto));
		}catch(Exception e) {
			e.printStackTrace();
			res.setCode(500);
			res.setMessage("잔액충전에 실패하였습니다.");
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(res);
		}
		return ResponseEntity.ok(res);
	}
}
