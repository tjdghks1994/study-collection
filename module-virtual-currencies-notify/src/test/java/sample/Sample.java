package sample;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class Sample {
    private String name;
    private int age;
    private String address;
    private String phoneNumber;
    private Test test;
}
