package reflection;

import reflection.data.BasicData;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class MethodV2 {
    public static void main(String[] args) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        // 정적 메서드 호출 - 일반적인 메서드 호출
        BasicData basicData = new BasicData();
        basicData.call();   // 코드를 변경하지 않는 이상 정적이다.

        // 동적 메서드 호출 - 리플렉션 사용
        Class<? extends BasicData> basicDataClass = basicData.getClass();
        String methodName = "hello";

        Method helloMethod = basicDataClass.getDeclaredMethod(methodName, String.class);
        Object result = helloMethod.invoke(basicData, "hi");
        System.out.println("result = " + result);
    }
}
