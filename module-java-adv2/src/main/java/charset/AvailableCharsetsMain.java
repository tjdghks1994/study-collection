package charset;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Set;
import java.util.SortedMap;

public class AvailableCharsetsMain {
    public static void main(String[] args) {
        // 이용 가능한 모든 CHARSET 자바 + OS
        SortedMap<String, Charset> charsets = Charset.availableCharsets();
        for (String charsetName : charsets.keySet()) {
            System.out.println("charsetName = " + charsetName);
        }

        System.out.println("=================================");

        // 문자로 조회 가능 (대소문자 구분 x)
        Charset ms949 = Charset.forName("MS949");
        System.out.println("ms949 = " + ms949);

        // 별칭 조회
        Set<String> aliases = ms949.aliases();
        for (String alias : aliases) {
            System.out.println("alias = " + alias);
        }

        System.out.println("=================================");
        Charset utf8 = Charset.forName("UTF-8");
        System.out.println("utf-8 = " + utf8);

        Charset utf8static = StandardCharsets.UTF_8;
        System.out.println("utf8static = " + utf8static);

        // 시스템의 기본 Charset 조회
        Charset defaultCharset = Charset.defaultCharset();
        System.out.println("defaultCharset = " + defaultCharset);
    }
}
