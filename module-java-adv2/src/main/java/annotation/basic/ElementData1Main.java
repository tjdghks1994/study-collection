package annotation.basic;

import java.util.Arrays;

public class ElementData1Main {
    public static void main(String[] args) {
        ElementData1 elementData1 = new ElementData1();
        Class<? extends ElementData1> elementData1Class = elementData1.getClass();
        AnnoElement annotation = elementData1Class.getAnnotation(AnnoElement.class);

        String value = annotation.value();
        System.out.println("value = " + value);

        int count = annotation.count();
        System.out.println("count = " + count);

        String[] tags = annotation.tags();
        System.out.println("tags = " + Arrays.toString(tags));
    }
}