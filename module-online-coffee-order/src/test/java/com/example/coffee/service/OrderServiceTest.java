package com.example.coffee.service;

import com.example.coffee.domain.CreateOrder;
import com.example.coffee.domain.StoreProduct;
import com.example.coffee.repository.OrderRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {
    @Mock
    OrderRepository orderRepository;

    @Mock
    StoreService storeService;

    @InjectMocks
    OrderService orderService;

    @Test
    @DisplayName("구매 수량이 재고 수량보다 적을 때, 정상 주문이 가능하다")
    void stockQuantityTest_success() {
        // given
        int buyQuantity = 5;    // 구매 수량
        int stockQuantity = 50; // 재고
        HashMap<Integer, Integer> map = new HashMap<>();
        map.put(1, buyQuantity);
        CreateOrder createOrder = CreateOrder.builder()
                .storeId(1)
                .customerId(1)
                .quantityByProduct(map)
                .build();
        StoreProduct stock = StoreProduct.builder()
                .stockQuantity(stockQuantity)
                .build();
        Mockito.when(storeService.getStoreProduct(1, 1)).thenReturn(stock);
        // when
        orderService.newOrder(createOrder);
        // then
        Assertions.assertThat(stock.getStockQuantity()).isEqualTo(stockQuantity - buyQuantity);
    }

    @Test
    @DisplayName("구매 수량이 재고 수량보다 많을 때, 주문이 실패한다")
    void stockQuantityTest_fail() {
        // given
        int buyQuantity = 500;    // 구매 수량
        int stockQuantity = 50; // 재고
        HashMap<Integer, Integer> map = new HashMap<>();
        map.put(1, buyQuantity);    // 구매 아이템 아이디, 수량
        CreateOrder createOrder = CreateOrder.builder()
                .storeId(1)
                .customerId(1)
                .quantityByProduct(map)
                .build();
        StoreProduct stock = StoreProduct.builder()
                .stockQuantity(stockQuantity)
                .build();
        Mockito.when(storeService.getStoreProduct(1, 1)).thenReturn(stock);
        // when & then
        Assertions.assertThatThrownBy(() -> orderService.newOrder(createOrder))
                .isInstanceOf(RuntimeException.class);
    }
}