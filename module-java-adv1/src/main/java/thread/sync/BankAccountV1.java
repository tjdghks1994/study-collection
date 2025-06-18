package thread.sync;

import util.MyLogger;
import util.ThreadUtils;

import static util.MyLogger.*;
import static util.ThreadUtils.*;

public class BankAccountV1 implements BankAccount {

    volatile private int balance;

    public BankAccountV1(int initialBalance) {
        this.balance = initialBalance;
    }

    @Override
    public boolean withdraw(int amount) {
        log("거래 시작 : " + getClass().getSimpleName());

        log("[검증 시작] 출금액 : " + amount + ", 잔액 : " + balance);
        // 잔고가 출금액 보다 적으면 진행 X
        if (balance < amount) {
            log("[검증 실패] 출금액 : " + amount + ", 잔액 : " + balance);
            return false;
        }
        // 잔고가 출금액 보다 많으면 진행 O
        log("[검증 완료] 출금액 : " + amount + ", 잔액 : " + balance);
        sleep(1000); // 출금 걸리는 시간으로 가정
        balance = balance - amount;
        log("[출금 완료] 출금액 : " + amount + ", 잔액 : "  + balance);

        log("거래 종료 : " + getClass().getSimpleName());
        return true;
    }

    @Override
    public int getBalance() {
        return this.balance;
    }
}
