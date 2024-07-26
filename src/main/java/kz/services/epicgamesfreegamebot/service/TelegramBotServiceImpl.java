package kz.services.epicgamesfreegamebot.service;

import jakarta.transaction.Transactional;
import kz.services.epicgamesfreegamebot.component.TelegramBot;
import kz.services.epicgamesfreegamebot.dto.FreeGameDto;
import kz.services.epicgamesfreegamebot.mapper.GameMapper;
import kz.services.epicgamesfreegamebot.model.Chat;
import kz.services.epicgamesfreegamebot.model.Game;
import kz.services.epicgamesfreegamebot.repository.GameRepository;
import kz.services.epicgamesfreegamebot.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TelegramBotServiceImpl implements TelegramBotService {
    private final GameRepository gameRepository;
    private final UserRepository userRepository;
    @Setter
    private TelegramBot telegramBot;

    @Transactional
    public void newsletterCheck(Long id) {
        Chat user = userRepository.getReferenceById(id);
        if (user.isNewsletter()) {
            SendMessage message = new SendMessage();
            message.setChatId(String.valueOf(id));
            message.setText("Вы подписаны на рассылку, хотите отписаться?");

            message.setReplyMarkup(keyboardConstructor("Отписаться", "Назад"));
            try {
                telegramBot.execute(message);
            } catch (TelegramApiException exc) {
                // Обработка исключения
            }
        }
        else {
            SendMessage message = new SendMessage();
            message.setChatId(String.valueOf(id));
            message.setText("Вы не подписаны на рассылку, хотите подписаться?");

            message.setReplyMarkup(keyboardConstructor("Подписаться", "Назад"));
            try {
                telegramBot.execute(message);
            } catch (TelegramApiException exc) {
                // Обработка исключения
            }
        }
    }

    @Transactional
    public void saveNewsletter(Long id, boolean answer) { userRepository.getReferenceById(id).setNewsletter(answer); }

    public void sendFreeGame(Long chatId) {
        try {
            List<Game> allGames = gameRepository.findAll();
            List<FreeGameDto> games = freeGameDtoList(allGames);

            sendMessage(chatId, "Сейчас в раздаче: ");

            for (FreeGameDto game : games) {
                sendPhotoWithCaption(chatId, game.getImageUrl(), game.toString());
            }

            sendMessage(chatId, "Следующим в раздаче будет:");
            List<FreeGameDto> nextGames = nextGames(GameMapper.freeGameDtoList(allGames));

            for (FreeGameDto nextGame: nextGames) {
                sendPhotoWithCaption(chatId, nextGame.getImageUrl(), nextGame.toString());
            }

        } catch (NullPointerException exc) {
            throw new RuntimeException("Это пиздец братишка");
        }
    }

    public void sendFreeGame(Long chatId, String text) {
        try {
            List<FreeGameDto> games = freeGameDtoList(gameRepository.findAll());

            sendMessage(chatId, text);

            for (FreeGameDto game: games) {
                sendPhotoWithCaption(chatId, game.getImageUrl(), game.toString());
            }
        } catch (NullPointerException exc) {
            throw new RuntimeException("Это пиздец братишка");
        }
    }

    public void startCommandReceived(Long chatId, String name) {
        String answer = "Привет, " + name + " этот бот позволяет посмотреть еженедельные раздачи игр " +
                "Epic games не тратя кучу времени для захода в приложение, у бота есть еженедельная рассылка новыйх игр";
        sendKeyboardMessage(chatId, answer);
    }

    public void sendPhotoWithCaption(Long chatId, String photoUrl, String caption) {
        SendPhoto sendPhotoRequest = new SendPhoto();
        sendPhotoRequest.setChatId(String.valueOf(chatId));
        sendPhotoRequest.setPhoto(new InputFile(photoUrl));
        sendPhotoRequest.setCaption(caption);
        try {
            telegramBot.execute(sendPhotoRequest);
        } catch (TelegramApiException exc) {
            // Обработка исключения
        }
    }

    public void sendPhoto(Long chatId, String photoUrl) {
        SendPhoto sendPhotoRequest = new SendPhoto();
        sendPhotoRequest.setChatId(String.valueOf(chatId));
        sendPhotoRequest.setPhoto(new InputFile(photoUrl));
        try {
            telegramBot.execute(sendPhotoRequest);
        } catch (TelegramApiException exc) {
            // Обработка исключения
        }
    }

    public void sendMessage(Long chatId, String textToSend){
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(String.valueOf(chatId));
        sendMessage.setText(textToSend);
        try {
            telegramBot.execute(sendMessage);
        } catch (TelegramApiException exc) {
            // Обработка исключения
        }
    }

    public void sendKeyboardMessage(Long chatId, String text) {
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText(text);

        message.setReplyMarkup(keyboardConstructor("Бесплатные игры", "Рассылка"));
        try {
            telegramBot.execute(message);
        } catch (TelegramApiException exc) {
            // Обработка исключения
        }
    }

    public ReplyKeyboardMarkup keyboardConstructor(String... rows) {
        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
        List<KeyboardRow> keyboard = new ArrayList<>();
        KeyboardRow rowAdd = new KeyboardRow();

        for (String row: rows) rowAdd.add(row);

        keyboard.add(rowAdd);

        keyboardMarkup.setKeyboard(keyboard);
        keyboardMarkup.setResizeKeyboard(true);

        return keyboardMarkup;
    }

    private List<FreeGameDto> nextGames(List<FreeGameDto> games) {
        LocalDateTime now = LocalDateTime.now();
        List<FreeGameDto> nextGames = new ArrayList<>();
        LocalDateTime nextStartDate = null;

        for (FreeGameDto game : games) {
            if (game.getStartDate().isAfter(now)) {
                if (nextStartDate == null || game.getStartDate().isBefore(nextStartDate)) {
                    nextStartDate = game.getStartDate();
                    nextGames.clear();
                    nextGames.add(game);
                } else if (game.getStartDate().isEqual(nextStartDate)) {
                    nextGames.add(game);
                }
            }
        }
        return nextGames;
    }


    private List<FreeGameDto> freeGameDtoList(List<Game> games) {
        List<FreeGameDto> gamesDto = new ArrayList<>();

        for (Game game: games) {
            if (game.getStartDate().isBefore(LocalDateTime.now()) && game.getEndDate().isAfter(LocalDateTime.now())) {
                gamesDto.add(GameMapper.freeGameDto(game));
            }
        }

        return gamesDto;
    }
}
