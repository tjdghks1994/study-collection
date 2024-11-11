package io.file.text;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Stream;

public class RealTextFileV2 {
    private static final String PATH = "module-java-adv2/temp/hello2.txt";

    public static void main(String[] args) throws IOException {
        String writeString = "abc\n가나다";
        System.out.println("=== Write String ===");
        System.out.println(writeString);

        Path path = Path.of(PATH);

        // 파일에 쓰기
        Files.writeString(path, writeString, StandardCharsets.UTF_8);
        // 파일에서 읽기 - 한줄씩 읽기
        System.out.println("=== Read String ===");
        // 파일이 아주 큰 경우 한 번에 모든 파일을 메모리에 올리는 것 보다, 파일을 부분 부분 나누어 메모리에
        // 올리는 것이 더 나은 선택일 수 있어, 아래와 같이 파일을 스트림 단위로 나누어 조회할 수 있다.
        Stream<String> lineStream = Files.lines(path, StandardCharsets.UTF_8);
        lineStream.forEach(line -> System.out.println(line));
        lineStream.close();

        // 아래 코드는 모든 파일의 내용을 한 번에 메모리에 올리는 방법이다.
        // 파일의 용량이 너무 크다면 OOM 이 발생할 수 있다.
//        List<String> lines = Files.readAllLines(path, StandardCharsets.UTF_8);
//        for (String line : lines) {
//            System.out.println(line);
//        }


    }
}
