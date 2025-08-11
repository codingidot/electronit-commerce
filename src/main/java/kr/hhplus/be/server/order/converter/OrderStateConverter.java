package kr.hhplus.be.server.order.converter;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import kr.hhplus.be.server.order.enums.OrderState;

@Converter(autoApply = true)
public class OrderStateConverter implements AttributeConverter<OrderState, String>{

	@Override
    public String convertToDatabaseColumn(OrderState attribute) {
        return attribute != null ? attribute.getCode() : null;
    }

    @Override
    public OrderState convertToEntityAttribute(String dbData) {
        if (dbData == null) return null;
        for (OrderState state : OrderState.values()) {
            if (state.getCode().equals(dbData)) {
                return state;
            }
        }
        throw new IllegalArgumentException("Unknown code: " + dbData);
    }
}
