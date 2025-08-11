package kr.hhplus.be.server.order.enums;

public enum OrderState {
	
	REQUEST("10", "주문요청"),
    ORDER_PLACED("20", "주문발주"),
    ORDER_RECEIVED("30", "주문접수"),
    SHIPPING("40", "배송"),
    DELIVERED("50", "인수완료");

    private final String code;
    private final String description;

    OrderState(String code, String description) {
        this.code = code;
        this.description = description;
    }

    public String getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }
}
