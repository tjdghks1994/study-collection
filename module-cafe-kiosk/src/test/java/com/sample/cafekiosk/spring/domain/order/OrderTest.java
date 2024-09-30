package com.sample.cafekiosk.spring.domain.order;

import com.sample.cafekiosk.spring.domain.product.Product;
import com.sample.cafekiosk.spring.domain.product.ProductType;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static com.sample.cafekiosk.spring.domain.product.ProductSellingStatus.SELLING;
import static org.junit.jupiter.api.Assertions.*;

class OrderTest {
    @DisplayName("주문 생성 시 상품 리스트에서 주문의 총 금액을 계산한다.")
    @Test
    void calculateTotalPrice() {
        // given
        List<Product> products = List.of(
                createProduct("001", 1000, "item1"),
                createProduct("002", 2000, "item2")
        );

        // when
        Order order = Order.create(products, LocalDateTime.now());

        // then
        Assertions.assertThat(order.getTotalPrice()).isEqualTo(3000);
    }

    @DisplayName("주문 생성 시 주문 상태는 INIT 이다.")
    @Test
    void init() {
        // given
        List<Product> products = List.of(
                createProduct("001", 1000, "item1"),
                createProduct("002", 2000, "item2")
        );

        // when
        Order order = Order.create(products, LocalDateTime.now());

        // then
        Assertions.assertThat(order.getOrderStatus()).isEqualByComparingTo(OrderStatus.INIT);
    }

    @DisplayName("주문 생성 시 주문 등록 시간을 기록한다.")
    @Test
    void registeredDateTime() {
        // given
        LocalDateTime registeredDateTime = LocalDateTime.now();
        List<Product> products = List.of(
                createProduct("001", 1000, "item1"),
                createProduct("002", 2000, "item2")
        );

        // when
        Order order = Order.create(products, registeredDateTime);

        // then
        Assertions.assertThat(order.getRegisteredDateTime()).isEqualTo(registeredDateTime);
    }

    private Product createProduct(String productNumber, int price, String name) {
        return Product.builder()
                .type(ProductType.HANDMADE)
                .productNumber(productNumber)
                .price(price)
                .sellingStatus(SELLING)
                .name(name)
                .build();
    }
}