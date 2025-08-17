package kr.hhplus.be.server.order.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
@AllArgsConstructor
@Getter
public enum OrderState {
	
	REQUEST("10", "주문요청"),
    ORDER_PLACED("20", "주문발주"),
    ORDER_RECEIVED("30", "주문접수"),
    SHIPPING("40", "배송"),
    DELIVERED("50", "인수완료");

    private final String code;
    private final String description;

}
