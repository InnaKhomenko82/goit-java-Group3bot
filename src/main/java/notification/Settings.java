package notification;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import telegram.TelegramController;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Settings {

    public void handleTextUpdate(Update update, TelegramController telegramController) { // обработка "/settings"
        long chatId = update.getMessage().getChatId();
        if (update.getMessage().getText().equals("/settings")) {
            String messageText = "Хочешь, чтобы я напомнил тебе о себе завтра?\n" +
                    "Тогда выбери удобное время для ежедневных оповещений:";
            sendText(chatId, messageText, 1, telegramController);
        }
    }
    private void sendText(long chatId, String messageText, int isKeyboard, TelegramController telegramController) { //отправка сообщения пользователю
        SendMessage sendMessageRequest = new SendMessage();
        sendMessageRequest.setChatId(Long.toString(chatId));
        sendMessageRequest.setText(messageText);
        switch (isKeyboard) {
            case 1:
                sendMessageRequest.setReplyMarkup(createNotificationTimeKeyboard());
                break;
            case 2:
                sendMessageRequest.setReplyMarkup(createNotificationTimeKeyboard());
        }
        telegramController.sendMethod(sendMessageRequest);
    }

    private ReplyKeyboard createNotificationTimeKeyboard(){

        String[][] buttons = new String[][]{
                {"\uD83D\uDD58 09:00", "\uD83D\uDD59 10:00", "\uD83D\uDD5A 11:00"},
                {"\uD83D\uDD5B 12:00", "\uD83D\uDD50 13:00", "\uD83D\uDD51 14:00"},
                {"\uD83D\uDD52 15:00", "\uD83D\uDD53 16:00", "\uD83D\uDD54 17:00"},
                {"\uD83D\uDD55 18:00", "\uD83D\uDE45 без оповещений"}
        };

        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(true);
        List<KeyboardRow> keyboard = new ArrayList<>();
        for (int i = 0; i < buttons.length; i++){
            List<String> list = Arrays.stream(buttons[i]).collect(Collectors.toList());
            KeyboardRow keyboardRow = new KeyboardRow();
            keyboardRow.addAll(list);
            keyboard.add(keyboardRow);
        }
        replyKeyboardMarkup.setKeyboard(keyboard);
        return replyKeyboardMarkup;
    }
}
