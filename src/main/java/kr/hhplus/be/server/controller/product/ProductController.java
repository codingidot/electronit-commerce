package kr.hhplus.be.server.controller.product;

import kr.hhplus.be.server.dto.ResponseDto;
import kr.hhplus.be.server.dto.produt.ProductDto;
import kr.hhplus.be.server.dto.produt.ProductRequestDto;
import kr.hhplus.be.server.mock.FakeData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductController {

    private FakeData fakeData;

    @Autowired
    public ProductController(FakeData fakeData){
        this.fakeData = fakeData;
    }

    @GetMapping
    public ResponseEntity<ResponseDto> getProductInfo(ProductRequestDto requestDto){
        ResponseDto res = new ResponseDto();
        
        try {
        	List<ProductDto> list = fakeData.getProductList();
            res.setData(list);
        }catch(Exception e) {
        	e.printStackTrace();
            res.setMessage("상품조회에 실패하였습니다.");
            res.setCode(500); // 커스텀 실패 코드
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(res);
        }
        
        return ResponseEntity.ok(res);
    }

}
