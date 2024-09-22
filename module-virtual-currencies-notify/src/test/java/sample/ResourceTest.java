package sample;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;


public class ResourceTest {

    @Test
    void contextLoads() throws IOException {
        ClassPathResource classPathResource = new ClassPathResource("/data/sample.json");

        StringBuilder sb = new StringBuilder();
        BufferedReader reader = new BufferedReader(new InputStreamReader(classPathResource.getInputStream()));

        String s;
        while ((s = reader.readLine()) != null) {
            sb.append(s);
        }

        ObjectMapper mapper = new ObjectMapper();
        Sample sample = mapper.readValue(sb.toString(), Sample.class);

        System.out.println(sample);
    }
}
