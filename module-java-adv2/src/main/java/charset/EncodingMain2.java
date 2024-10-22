package charset;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

import static java.nio.charset.StandardCharsets.*;

public class EncodingMain2 {
    private static final Charset EUC_KR = Charset.forName("EUC-KR");
    private static final Charset MS_949 = Charset.forName("MS949");

    public static void main(String[] args) {
        System.out.println("== 영문 ASCII 인코딩 ==");
        test("A", US_ASCII, US_ASCII);
        test("A", US_ASCII, ISO_8859_1);
        test("A", US_ASCII, EUC_KR);
        test("A", US_ASCII, MS_949);
        test("A", US_ASCII, UTF_8);
        test("A", US_ASCII, UTF_16BE);  // 디코딩 실패

        System.out.println("== 한글 인코딩 - 기본 ==");
        test("가", US_ASCII, US_ASCII);      // 불가 - 한글을 지원하지 않음
        test("가", ISO_8859_1, ISO_8859_1);  // 불가 - 한글을 지원하지 않음
        test("가", EUC_KR, EUC_KR);
        test("가", MS_949, MS_949);
        test("가", UTF_8, UTF_8);
        test("가", UTF_16BE, UTF_16BE);

        System.out.println("== 한글 인코딩 - 복잡한 문자 ==");
        test("뷁", EUC_KR, EUC_KR);  // 불가
        test("뷁", MS_949, MS_949);
        test("뷁", UTF_8, UTF_8);
        test("뷁", UTF_16BE, UTF_16BE);

        System.out.println("== 한글 인코딩 - 디코딩이 다른 경우 ==");
        test("가", EUC_KR, MS_949);  // MS949는 EUC-KR을 확장했으므로 인코딩, 디코딩 모두 가능
        test("뷁", MS_949, EUC_KR);  // 인코딩은 가능하나 디코딩은 불가
        test("가", EUC_KR, UTF_8);   // 불가 - EUC-KR은 2byte, UTF-8은 3byte
        test("가", MS_949, UTF_8);   // 불가 - MS949은 2byte, UTF-8은 3byte
        test("가", UTF_8, MS_949);   // 불가

        System.out.println("== 영문 인코딩 - 디코딩이 다른 경우 ==");
        test("A", EUC_KR, UTF_8);
        test("A", MS_949, UTF_8);
        test("A", UTF_8, MS_949);
        test("A", UTF_8, UTF_16BE); // 불가 - UTF-8은 1byte, UTF-16 2byte

    }

    private static void test(String text, Charset encodingCharset, Charset decodingCharset) {
        byte[] encoded = text.getBytes(encodingCharset);
        String decoded = new String(encoded, decodingCharset);

        System.out.printf("%s -> [%s] 인코딩 -> %s %sbyte -> [%s] 디코딩 -> %s\n",
                text, encodingCharset, Arrays.toString(encoded), encoded.length, decodingCharset, decoded
        );
    }
}
