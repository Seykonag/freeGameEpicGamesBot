package kz.services.epicgamesfreegamebot.mapper;

import kz.services.epicgamesfreegamebot.dto.FreeGameDto;
import kz.services.epicgamesfreegamebot.model.Game;

public class GameMapper {
    public static FreeGameDto freeGameDto(Game game) {
        return FreeGameDto.builder()
                .title(game.getTitle())
                .description(game.getDescription())
                .imageUrl(game.getImageUrl())
                .price(game.getPrice())
                .startDate(game.getStartDate())
                .endDate(game.getEndDate())
                .build();
    }
}
