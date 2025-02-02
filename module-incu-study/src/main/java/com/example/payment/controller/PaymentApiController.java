package com.example.payment.controller;

import com.example.payment.entity.Payment;
import com.example.payment.entity.request.PaymentReq;
import com.example.payment.service.PaymentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/payment")
public class PaymentApiController {

    private final PaymentService paymentService;

    public PaymentApiController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping("/portone")
    public ResponseEntity<String> savePortone(@RequestBody PaymentReq paymentRequest) {
        try {
            paymentService.savePayment(Payment.of(paymentRequest));
            return ResponseEntity.ok("Payment processed successfully.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to process payment.");
        }
    }

    /**
     * 모든 결제 내역 조회
     *
     * @return 모든 Payment 엔티티 리스트
     */
    @GetMapping
    public ResponseEntity<List<Payment>> getAllPayments() {
        List<Payment> payments = paymentService.getAllPayments();
        return ResponseEntity.ok(payments);
    }

//    @GetMapping("/list")
//    public ResponseEntity<List<Payment>> getPayments() {
//        List<Payment> payments = paymentService.getAllPayments();   // 결제 데이터 가져오기
//        return ResponseEntity.ok(payments);
//    }
//
//    @GetMapping("/accessToken")
//    public ResponseEntity<Map> getAccessToken() {
//        Map accessToken = paymentService.getAccessToken();
//        return ResponseEntity.ok(accessToken);
//    }
//
//    @GetMapping("/cancle/{uid}")
//    public ResponseEntity<String> canclePayment(@PathVariable("uid") String uid) {
//        String accessToken = paymentService.canclePayment(uid);
//        return ResponseEntity.ok(accessToken);
//    }
}
