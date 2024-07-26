package kz.services.epicgamesfreegamebot.service;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
public class TelegramBotServiceImplTest {

    @InjectMocks
    private TelegramBotServiceImpl telegramBotService;

    @Test
    public void keyboardConstructor() {
        ReplyKeyboardMarkup replyKeyboardMarkup = telegramBotService.keyboardConstructor("Test1", "Test2", "Test3");

        ReplyKeyboardMarkup replyKeyboardMarkupTest = new ReplyKeyboardMarkup();
        List<KeyboardRow> keyboardRowList = new ArrayList<>();
        KeyboardRow keyboardRow = new KeyboardRow();

        keyboardRow.addAll(Arrays.asList("Test1", "Test2", "Test3"));

        keyboardRowList.add(keyboardRow);

        replyKeyboardMarkupTest.setKeyboard(keyboardRowList);
        replyKeyboardMarkup.setResizeKeyboard(true);

        Assertions.assertEquals(replyKeyboardMarkupTest.getKeyboard(), replyKeyboardMarkup.getKeyboard());
    }
}
