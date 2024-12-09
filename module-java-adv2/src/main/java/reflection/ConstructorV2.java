package reflection;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class ConstructorV2 {
    public static void main(String[] args) throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        Class<?> basicDataClass = Class.forName("reflection.data.BasicData");

        Constructor<?> constructor = basicDataClass.getDeclaredConstructor(String.class);
        constructor.setAccessible(true);
        Object instance = constructor.newInstance("hello"); // 객체 생성
        System.out.println("instance = " + instance);

        Method method = basicDataClass.getDeclaredMethod("call");
        method.invoke(instance);
    }
}
