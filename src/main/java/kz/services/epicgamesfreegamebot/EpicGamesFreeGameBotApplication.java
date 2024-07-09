package kz.services.epicgamesfreegamebot;

import kz.services.epicgamesfreegamebot.service.FreeGameService;
import kz.services.epicgamesfreegamebot.service.TranslateService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@RequiredArgsConstructor
public class EpicGamesFreeGameBotApplication implements CommandLineRunner {
    private final FreeGameService freeGameService;
    public static void main(String[] args) {
        SpringApplication.run(EpicGamesFreeGameBotApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        freeGameService.getFreeGames();
    }
}
