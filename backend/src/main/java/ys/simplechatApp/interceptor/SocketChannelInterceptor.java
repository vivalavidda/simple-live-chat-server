package ys.simplechatApp.interceptor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class SocketChannelInterceptor implements ChannelInterceptor {
    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        log.info("received Message={}", message);
        logPayload(message);
        return message;
    }

    private void logPayload(Message<?> message) {
        byte[] bytes = (byte[]) message.getPayload();
        if (bytes.length == 0) return;
        String payload = new String(bytes);
        log.info("payload={}", payload);
    }
}