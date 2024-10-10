package com.example.cryptoautomation.http;

import com.slack.api.Slack;
import com.slack.api.webhook.Payload;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class SlackHttpClient {
    public void send(String message) {
        try {
            Slack slack = Slack.getInstance();
            Payload payload = Payload.builder()
                    .text(message)
                    .build();

            slack.send("https://hooks.slack.com/services/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", payload);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
