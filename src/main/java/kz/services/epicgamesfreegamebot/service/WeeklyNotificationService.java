package kz.services.epicgamesfreegamebot.service;

import kz.services.epicgamesfreegamebot.model.Chat;
import kz.services.epicgamesfreegamebot.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class WeeklyNotificationService {
    private final UserRepository repository;
    private final TelegramBotServiceImpl telegramBotService;

    @Scheduled(cron = "0 0 12 * * FRI") // Каждая пятница в 12:00, потом скорее всего нужно будет исправить
    public void sendWeeklyNotifications() {
        List<Long> chats = getSubscribedChatIds();

        for (Long chat: chats) telegramBotService.sendFreeGame(chat, "Еженедельная рассылка бесплатных игр!!!");
    }

    private List<Long> getSubscribedChatIds() {
        List<Chat> chats = repository.findAllByNewsletterTrue();
        List<Long> ids = new ArrayList<>();

        for (Chat chat: chats) ids.add(chat.getChatId());
        return ids;
    }
}
