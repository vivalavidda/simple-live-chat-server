package ys.simplechatApp.Message;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@Builder
@AllArgsConstructor
@ToString
public class SendMessageDto {
    @JsonProperty("_id")
    private String id;
    private User user;
    private String chat;
}
