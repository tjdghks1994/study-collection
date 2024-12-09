package reflection;

import reflection.data.Calculator;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Scanner;

public class MethodV3 {
    public static void main(String[] args) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Scanner scanner = new Scanner(System.in);
        System.out.print("호출 메서드 : ");
        // 호출 메서드를 동적으로 선택
        String methodName = scanner.nextLine();

        System.out.print("숫자1 : ");
        int num1 = scanner.nextInt();
        System.out.print("숫자2 : ");
        int num2 = scanner.nextInt();

        Calculator calculator = new Calculator();
        Class<? extends Calculator> calculatorClass = calculator.getClass();
        // 동적으로 선택된 메서드
        Method method = calculatorClass.getMethod(methodName, int.class, int.class);
        Object result = method.invoke(calculator, num1, num2);
        System.out.println("result = " + result);
    }
}
