package kr.hhplus.be.server.controller.order;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import kr.hhplus.be.server.dto.ResponseDto;
import kr.hhplus.be.server.dto.order.OrderRequestDto;
import kr.hhplus.be.server.mock.FakeData;

@RestController
@RequestMapping("/orders")
public class OrderController {
	
	private FakeData fakeData;

    @Autowired
    public OrderController(FakeData fakeData){
        this.fakeData = fakeData;
    }
	
	@PostMapping
	public ResponseEntity<ResponseDto> orderPoroduct(@RequestBody OrderRequestDto orderRequestDto){
		ResponseDto res = new ResponseDto();
		return ResponseEntity.ok(res);
	}
}
