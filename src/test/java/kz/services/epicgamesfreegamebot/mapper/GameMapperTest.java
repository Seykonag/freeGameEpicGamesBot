package kz.services.epicgamesfreegamebot.mapper;

import kz.services.epicgamesfreegamebot.dto.FreeGameDto;
import kz.services.epicgamesfreegamebot.model.Game;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.Arrays;
import java.util.List;

public class GameMapperTest {
    @Test
    public void freeGameDto() {
        FreeGameDto freeGameDto = GameMapper.freeGameDto(
                Game.builder()
                        .title("Test")
                        .description("Test")
                        .price(new BigDecimal(100))
                        .startDate(LocalDateTime.of(2004, Month.APRIL, 21, 10, 0))
                        .endDate(LocalDateTime.of(2004, Month.APRIL, 21, 20, 0))
                        .imageUrl("testUrl")
                        .build()
        );

        FreeGameDto freeGameDtoAnswer = FreeGameDto.builder()
                .title("Test")
                .description("Test")
                .price(new BigDecimal(100))
                .startDate(LocalDateTime.of(2004, Month.APRIL, 21, 10, 0))
                .endDate(LocalDateTime.of(2004, Month.APRIL, 21, 20, 0))
                .imageUrl("testUrl")
                .build();

        Assertions.assertEquals(freeGameDtoAnswer, freeGameDto);
    }

    @Test
    public void freeGameDtoList() {
        List<Game> games = Arrays.asList(
                Game.builder()
                        .title("Test1")
                        .description("Test1")
                        .price(new BigDecimal(100))
                        .startDate(LocalDateTime.of(2004, Month.APRIL, 21, 10, 0))
                        .endDate(LocalDateTime.of(2004, Month.APRIL, 21, 20, 0))
                        .imageUrl("testUrl")
                        .build()
                ,
                Game.builder()
                        .title("Test2")
                        .description("Test2")
                        .price(new BigDecimal(100))
                        .startDate(LocalDateTime.of(2004, Month.APRIL, 21, 10, 0))
                        .endDate(LocalDateTime.of(2004, Month.APRIL, 21, 20, 0))
                        .imageUrl("testUrl")
                        .build()
                );

        List<FreeGameDto> freeGameDtoL = GameMapper.freeGameDtoList(games);
        List<FreeGameDto> freeGameDtoAnswers = Arrays.asList(
                FreeGameDto.builder()
                        .title("Test1")
                        .description("Test1")
                        .price(new BigDecimal(100))
                        .startDate(LocalDateTime.of(2004, Month.APRIL, 21, 10, 0))
                        .endDate(LocalDateTime.of(2004, Month.APRIL, 21, 20, 0))
                        .imageUrl("testUrl")
                        .build()
                ,
                FreeGameDto.builder()
                        .title("Test2")
                        .description("Test2")
                        .price(new BigDecimal(100))
                        .startDate(LocalDateTime.of(2004, Month.APRIL, 21, 10, 0))
                        .endDate(LocalDateTime.of(2004, Month.APRIL, 21, 20, 0))
                        .imageUrl("testUrl")
                        .build()
        );

        Assertions.assertEquals(freeGameDtoAnswers, freeGameDtoL);
    }
}
