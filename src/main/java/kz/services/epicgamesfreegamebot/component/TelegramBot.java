package kz.services.epicgamesfreegamebot.component;

import kz.services.epicgamesfreegamebot.config.BotConfig;
import kz.services.epicgamesfreegamebot.model.Chat;
import kz.services.epicgamesfreegamebot.repository.UserRepository;
import kz.services.epicgamesfreegamebot.service.TelegramBotService;
import kz.services.epicgamesfreegamebot.service.TelegramBotServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;


@Component
@AllArgsConstructor
public class TelegramBot extends TelegramLongPollingBot {
    private final BotConfig config;
    private final TelegramBotService service;
    private final UserRepository userRepository;

    @Autowired
    public void setTelegramBotService(TelegramBotService service) {
        if (service instanceof TelegramBotServiceImpl) {
            ((TelegramBotServiceImpl) service).setTelegramBot(this);
        }
    }

    @Override
    public String getBotUsername() {
        return config.getBotName();
    }

    @Override
    public String getBotToken() {
        return config.getBotToken();
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            String messageText = update.getMessage().getText();
            long chatId = update.getMessage().getChatId();

            if (!userRepository.existsById(chatId)) {
                userRepository.save(new Chat(chatId, false));
            }

            switch (messageText) {
                case "/start":
                    service.startCommandReceived(chatId, update.getMessage().getChat().getFirstName());
                    break;
                case "Бесплатные игры":
                    service.sendFreeGame(chatId);
                    break;
                case "Рассылка":
                    service.newsletterCheck(chatId);
                    break;
                case "Подписаться":
                    service.sendKeyboardMessage(chatId, "Вы подписались");
                    service.saveNewsletter(chatId, true);
                    break;
                case "Отписаться":
                    service.sendKeyboardMessage(chatId, "Вы отписались");
                    service.saveNewsletter(chatId, false);
                    break;
                case "Назад":
                    service.sendKeyboardMessage(chatId, "Главное меню");
                    break;
                default:
                    service.sendKeyboardMessage(chatId, "Извините, я не понял вашего сообщения");
                    break;
            }
        }
    }
}
