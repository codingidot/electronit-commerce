package kr.hhplus.be.server.controller.balance;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import kr.hhplus.be.server.dto.ResponseDto;
import kr.hhplus.be.server.dto.balance.ChargeRequestDto;
import kr.hhplus.be.server.mock.FakeData;

@RestController
@RequestMapping("/wallet")
public class BalanceController {
	
	private FakeData fakeData;

    @Autowired
    public BalanceController(FakeData fakeData){
        this.fakeData = fakeData;
    }
	
	@GetMapping("/balance/{userId}")
	public ResponseEntity<ResponseDto> getTotalBalance(@PathVariable("userId") Long userId) {
		ResponseDto res = new ResponseDto();
		try {
			res.setData(fakeData.getBalanceInfo(userId));
		}catch(Exception e) {
			e.printStackTrace();
			res.setCode(500);
			res.setMessage("잔액조회에 실패하였습니다.");
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(res);
		}
		return ResponseEntity.ok(res);
	}
	
	@PostMapping("/charge")
	public ResponseEntity<ResponseDto> chargeBalance(@RequestBody ChargeRequestDto chargeRequestDto){
		ResponseDto res = new ResponseDto();
		try {
			res.setData(fakeData.chargeBalance(chargeRequestDto));
		}catch(Exception e) {
			e.printStackTrace();
			res.setCode(500);
			res.setMessage("잔액충전에 실패하였습니다.");
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(res);
		}
		return ResponseEntity.ok(res);
	}
}
