package kz.services.epicgamesfreegamebot.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FreeGameDto {
    private String title;
    private String imageUrl;
    private String link;
    private BigDecimal price;
    private String description;
    private LocalDateTime startDate;
    private LocalDateTime endDate;

    @Override
    public String toString() {
        return "Названия: " + getTitle() + "\n" +
                "Описание: " + getDescription() + "\n" +
                "Цена до раздачи: " + getPrice().toString() + " ₸" + "\n" +
                checkDate(getStartDate(), getEndDate()) + "\n" +
                "Ссылки на игры в будущем";
    }

    private String checkDate(LocalDateTime startDate, LocalDateTime endDate) {
        if (startDate == null || endDate == null) return "По какойто причине дата раздачи неизвестна, уточните на сайте Epic games";
        else {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d MMMM", new Locale("ru"));
            return "Раздача проходит с " + formatter.format(startDate.toLocalDate()) +
                    " до " + formatter.format(endDate.toLocalDate());
        }
    }
}
