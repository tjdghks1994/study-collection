package com.example.payment.controller;

import com.example.payment.entity.Payment;
import com.example.payment.service.PaymentService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
@RequestMapping("/payment")
public class PaymentViewController {

    private final PaymentService paymentService;

    public PaymentViewController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @GetMapping
    public String payment() {
        return "payment";
    }

    @GetMapping("/my-order")
    public ModelAndView myOrder() {
        // PaymentService를 통해 데이터를 가져옴
        List<Payment> paymentList = paymentService.getAllPayments();  // 결제 데이터를 가져오는 서비스 호출

        // ModelAndView를 사용하여 데이터와 뷰를 함께 반환
        ModelAndView mav = new ModelAndView("myorder");
        mav.addObject("paymentList", paymentList);  // 가져온 데이터를 "paymentList"로 뷰에 전달

        return mav;
    }
}
