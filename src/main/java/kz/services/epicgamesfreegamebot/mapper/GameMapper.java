package kz.services.epicgamesfreegamebot.mapper;

import kz.services.epicgamesfreegamebot.dto.FreeGameDto;
import kz.services.epicgamesfreegamebot.model.Game;

import java.util.ArrayList;
import java.util.List;

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

    public static List<FreeGameDto> freeGameDtoList(List<Game> games) {
        List<FreeGameDto> freeGameDtoList = new ArrayList<>();

        for (Game game: games) {
            freeGameDtoList.add(freeGameDto(game));
        }

        return freeGameDtoList;
    }
}
