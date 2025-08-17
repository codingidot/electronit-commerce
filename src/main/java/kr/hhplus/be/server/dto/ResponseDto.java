package kr.hhplus.be.server.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResponseDto<T>{

    private int code = 200;
    private String message = "success";
    private T data;
}
