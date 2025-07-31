package kr.hhplus.be.server.controller.product;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.hhplus.be.server.dto.ResponseDto;
import kr.hhplus.be.server.dto.product.ProductRequestDto;
import kr.hhplus.be.server.dto.product.ProductResponseDto;
import kr.hhplus.be.server.service.product.ProductService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/products")
@Tag(name = "상품 API", description = "상품 관련 API입니다.")
public class ProductController {

    private final ProductService productService;

    @Autowired
    public ProductController(ProductService productService){
        this.productService = productService;
    }

    @GetMapping
    @Operation(
        summary = "상품 목록 조회",
        description = "전체 상품 목록을 조회합니다.",
        responses = {
            @ApiResponse(responseCode = "200", description = "상품 목록 조회 성공",
                content = @Content(schema = @Schema(implementation = ResponseDto.class))),
            @ApiResponse(responseCode = "500", description = "서버 오류 발생",
                content = @Content)
        }
    )
    public ResponseEntity<ResponseDto> getProductInfo(ProductRequestDto requestDto){
        ResponseDto res = new ResponseDto();

        try {
            List<ProductResponseDto> list = productService.getProductList(requestDto);
            res.setData(list);
        } catch(Exception e) {
            e.printStackTrace();
            res.setMessage("상품조회에 실패하였습니다.");
            res.setCode(500);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(res);
        }
        return ResponseEntity.ok(res);
    }

}
