package kz.services.epicgamesfreegamebot.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Chat {
    @Id
    private Long chatId;
    private boolean newsletter;
}
