package ys.simplechatApp.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Controller;
import ys.simplechatApp.Message.*;

import java.util.UUID;

import static ys.simplechatApp.Message.MessageType.*;

@Slf4j
@Controller
@RequiredArgsConstructor
public class WebSocketController {
    private final SimpMessageSendingOperations simpMessageSendingOperations;

    @MessageMapping("/room/{roomId}")
    public void message(@DestinationVariable String roomId, InputMessageDto inputMessageDto) {
        MessageType messageType = inputMessageDto.type();
        String user = inputMessageDto.user();
        SendMessageDto sendMessageDto;
        if (messageType == null) {
            return;
        }
        if (messageType.equals(CONNECT)) {
            sendMessageDto = makeSystemMessage(user + "님이 입장하셨습니다.");
        } else if (messageType.equals(DISCONNECT)) {
            sendMessageDto = makeSystemMessage(user + "님이 퇴장하셨습니다");
        } else {
            sendMessageDto = SendMessageDto.builder()
                    .id(UUID.randomUUID().toString())
                    .user(new User(user))
                    .chat(inputMessageDto.message())
                    .build();
        }
        simpMessageSendingOperations.convertAndSend("/sub/room/" + roomId, sendMessageDto);
        log.info("message={}", sendMessageDto);
    }

    private SendMessageDto makeSystemMessage(String chatMessage) {
        return SendMessageDto.builder()
                .id(UUID.randomUUID().toString())
                .user(new User("system"))
                .chat(chatMessage)
                .build();
    }
}
