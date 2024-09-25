package com.sample.cafekiosk.unit;

import com.sample.cafekiosk.unit.beverage.Americano;
import com.sample.cafekiosk.unit.beverage.Latte;
import com.sample.cafekiosk.unit.order.Order;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

class CafeKioskTest {

    @Test

    void add_manual_test() {
        CafeKiosk cafeKiosk = new CafeKiosk();
        cafeKiosk.add(new Americano());

        System.out.println(">>> 담긴 음료 수 : " + cafeKiosk.getBeverages().size());
        System.out.println(">>> 담긴 음료 : " + cafeKiosk.getBeverages().get(0).getName());
    }

    @Test
//    @DisplayName("음료 1개 추가 테스트")
    // DisplayName은 섬세하게 작성하자
    // - 명사의 나열보다는 문장 형태로 작성해라
    // - 테스트 행위에 대한 결과까지 기술해라
    // - 도메인 용어를 사용하여 한층 추상화된 내용들 담자
    // - 테스트의 현상을 중점으로 기술하지 말아라 ( ex - 성공한다, 실패한다 등 )
    @DisplayName("음료 1개를 추가하면 주문 목록에 담긴다.")
    // 만약 Junit5가 아니라면 메서드명을 한글로 활용해라
    void add() {
        CafeKiosk cafeKiosk = new CafeKiosk();
        cafeKiosk.add(new Americano());

        assertThat(cafeKiosk.getBeverages().size()).isEqualTo(1);
        assertThat(cafeKiosk.getBeverages().get(0).getName()).isEqualTo("아메리카노");
        assertThat(cafeKiosk.getBeverages()).hasSize(1);
    }

    @Test
    void addSeveralBeverages() {
        CafeKiosk cafeKiosk = new CafeKiosk();
        Americano americano = new Americano();
        cafeKiosk.add(americano, 2);

        assertThat(cafeKiosk.getBeverages().get(0)).isEqualTo(americano);
        assertThat(cafeKiosk.getBeverages().get(1)).isEqualTo(americano);
    }

    @Test
    void addZeroBeverages() {
        CafeKiosk cafeKiosk = new CafeKiosk();
        Americano americano = new Americano();

        assertThatThrownBy(() -> cafeKiosk.add(americano, 0))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("음료는 1잔 이상 주문하실 수 있습니다.");


    }

    @Test
    void remove() {
        CafeKiosk cafeKiosk = new CafeKiosk();
        Americano americano = new Americano();

        cafeKiosk.add(americano);
        assertThat(cafeKiosk.getBeverages()).hasSize(1);

        cafeKiosk.remove(americano);
        assertThat(cafeKiosk.getBeverages()).isEmpty();
    }

    @Test
    @DisplayName("주문 목록에 담긴 상품들의 총 금액을 계산할 수 있다.")
    void calculateTotalPrice() {
        // given - 시나리오에 필요한 모든 준비 과정 ( 객체, 값, 상태 .. )
        CafeKiosk cafeKiosk = new CafeKiosk();
        Americano americano = new Americano();
        Latte latte = new Latte();

        cafeKiosk.add(americano);
        cafeKiosk.add(latte);

        // when - 시나리오 행동 진행
        int totalPrice = cafeKiosk.calculateTotalPrice();

        // then - 시나리오 진행에 대한 결과 명시, 검증
        assertThat(totalPrice).isEqualTo(americano.getPrice()+latte.getPrice());
    }

    @Test
    void clear() {
        CafeKiosk cafeKiosk = new CafeKiosk();
        Americano americano = new Americano();
        Latte latte = new Latte();

        cafeKiosk.add(americano);
        cafeKiosk.add(latte);
        assertThat(cafeKiosk.getBeverages()).hasSize(2);

        cafeKiosk.clear();
        assertThat(cafeKiosk.getBeverages()).isEmpty();
    }

    @Test
    void createOrder() {
        CafeKiosk cafeKiosk = new CafeKiosk();
        Americano americano = new Americano();

        cafeKiosk.add(americano);

        Order order = cafeKiosk.createOrder();

        assertThat(order.getBeverageList()).hasSize(1);
        assertThat(order.getBeverageList().get(0).getName()).isEqualTo("아메리카노");
    }

    @Test
    void createOrderWithCurrentTime() {
        CafeKiosk cafeKiosk = new CafeKiosk();
        Americano americano = new Americano();

        cafeKiosk.add(americano);

        Order order = cafeKiosk.createOrder(LocalDateTime.of(2024, 9, 19, 10, 0));

        assertThat(order.getBeverageList()).hasSize(1);
        assertThat(order.getBeverageList().get(0).getName()).isEqualTo("아메리카노");
    }

    @Test
    void createOrderWithOutsideOpenTime() {
        CafeKiosk cafeKiosk = new CafeKiosk();
        Americano americano = new Americano();

        cafeKiosk.add(americano);

        assertThatThrownBy(() -> cafeKiosk.createOrder(LocalDateTime.of(2024, 9, 19, 9, 59)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("주문 시간이 아닙니다. 관리자에게 문의하세요.");
    }
}