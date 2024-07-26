package kz.services.epicgamesfreegamebot.service;

public interface TelegramBotService {
    void newsletterCheck(Long id);
    void sendFreeGame(Long chatId);
    void startCommandReceived(Long chatId, String name);
    void sendPhotoWithCaption(Long chatId, String photoUrl, String caption);
    void sendMessage(Long chatId, String textToSend);
    void sendKeyboardMessage(Long chatId, String text);
    void saveNewsletter(Long chatId, boolean answer);
}
