package com.sample.cafekiosk.spring.api.service.order;

import com.sample.cafekiosk.spring.client.mail.MailSendClient;
import com.sample.cafekiosk.spring.domain.history.mail.MailSendHistory;
import com.sample.cafekiosk.spring.domain.history.mail.MailSendHistoryRepository;
import com.sample.cafekiosk.spring.domain.order.Order;
import com.sample.cafekiosk.spring.domain.order.OrderRepository;
import com.sample.cafekiosk.spring.domain.order.OrderStatus;
import com.sample.cafekiosk.spring.domain.orderproduct.OrderProductRepository;
import com.sample.cafekiosk.spring.domain.product.Product;
import com.sample.cafekiosk.spring.domain.product.ProductRepository;
import com.sample.cafekiosk.spring.domain.product.ProductType;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static com.sample.cafekiosk.spring.domain.product.ProductSellingStatus.SELLING;
import static com.sample.cafekiosk.spring.domain.product.ProductType.HANDMADE;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

@SpringBootTest
class OrderStatisticsServiceTest {

    @Autowired
    private OrderStatisticsService orderStatisticsService;

    @Autowired
    private OrderProductRepository orderProductRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private MailSendHistoryRepository mailSendHistoryRepository;

    @MockBean
    private MailSendClient mailSendClient;

    @AfterEach
    void tearDown() {
        orderProductRepository.deleteAllInBatch();
        orderRepository.deleteAllInBatch();
        productRepository.deleteAllInBatch();
        mailSendHistoryRepository.deleteAllInBatch();
    }

    @DisplayName("결제완료 주문들을 조회하여 매출 통계 메일을 전송한다.")
    @Test
    void sendOrderStatisticsMail() {
        // given
        LocalDateTime now = LocalDateTime.of(2023, 3, 5, 0, 0);
        Product product1 = createProduct(HANDMADE, "001", 1000, "아메리카노");
        Product product2 = createProduct(HANDMADE, "002", 2000, "카페라떼");
        Product product3 = createProduct(HANDMADE, "003", 3000, "팥빙수");
        List<Product> products = List.of(product1, product2, product3);
        productRepository.saveAll(products);

        Order order1 = createPaymentCompletedOrder(LocalDateTime.of(2023, 3, 4, 23, 59, 59), products);
        Order order2 = createPaymentCompletedOrder(now, products);
        Order order3 = createPaymentCompletedOrder(LocalDateTime.of(2023, 3, 5, 23, 59, 59), products);
        Order order4 = createPaymentCompletedOrder(LocalDateTime.of(2023, 3, 6, 0, 0), products);

        // stubbing !!
        Mockito.when(mailSendClient.sendEmail(any(String.class), any(String.class), any(String.class), any(String.class)))
                .thenReturn(true);

        // when
        boolean result = orderStatisticsService.sendOrderStatisticsMail(
                LocalDate.of(2023, 3, 5), "test@test.com"
        );

        // then
        assertThat(result).isTrue();

        List<MailSendHistory> histories = mailSendHistoryRepository.findAll();
        assertThat(histories).hasSize(1)
                .extracting("content")
                .contains("총 매출 합계는 12000원 입니다.");
    }

    private Order createPaymentCompletedOrder(LocalDateTime now, List<Product> products) {
        Order order = Order.builder()
                .products(products)
                .orderStatus(OrderStatus.PAYMENT_COMPLETED)
                .registeredDateTime(now)
                .build();

        return orderRepository.save(order);
    }

    private Product createProduct(ProductType type, String productNumber, int price, String name) {
        return Product.builder()
                .type(type)
                .productNumber(productNumber)
                .price(price)
                .sellingStatus(SELLING)
                .name(name)
                .build();
    }
}