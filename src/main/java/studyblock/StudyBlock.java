package studyblock;
import java.util.ArrayList;

import googleSheets.GoogleSheetsApiController;
import lombok.SneakyThrows;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import telegram.TelegramController;
public class StudyBlock {
    public void handleCallbackQuery(Update update, TelegramController telegramController) { //реакция на нажатие кнопок

    }
    public void handleTextUpdate(Update update, TelegramController telegramController) { // обработка Go

        System.out.println("handleTextUpdate");

        if (update.hasCallbackQuery() && update.getCallbackQuery().getData().equals("Go")) {
            long chatId = update.getCallbackQuery().getFrom().getId();
            String messageText = "Выбери интересующий тебя блок:";
            sendText(chatId, messageText, 2, telegramController);
        }

        if (update.hasMessage() && update.getMessage().getText().equals("Раздел Java")) {
            List<String> blockQuestions = GoogleSheetsApiController.Questions();
            long chatId = update.getMessage().getChatId();
            String messageText = blockQuestions.get(1);
            sendText(chatId, messageText, 1, telegramController);
        }
    }

    private void sendText(long chatId, String messageText, int isKeyboard, TelegramController telegramController) { //отправка сообщения пользователю
        SendMessage sendMessageRequest = new SendMessage();
        sendMessageRequest.setChatId(Long.toString(chatId));
        sendMessageRequest.setText(messageText);
        switch (isKeyboard) {
            case 1:
                sendMessageRequest.setReplyMarkup(createUnitKeyboard());
                break;
            case 2:
                sendMessageRequest.setReplyMarkup(createBlockKeyboard());
        }
        telegramController.sendMethod(sendMessageRequest);
    }
    @SneakyThrows
    private ReplyKeyboard createBlockKeyboard() {
        List<String> blocksNames = GoogleSheetsApiController.Blocks();
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(true);

        List<KeyboardRow> keyboard = new ArrayList<>();
        for (int i=0; i<blocksNames.size(); i++){
           KeyboardRow keyboardRow = new KeyboardRow();
           keyboardRow.add("Раздел " + blocksNames.get(i));
           keyboard.add(keyboardRow);
        }
        replyKeyboardMarkup.setKeyboard(keyboard);
        return replyKeyboardMarkup;
    }

    private ReplyKeyboard createUnitKeyboard() { //клавиатура для юнитов
        List<String> blockQuestions = GoogleSheetsApiController.Questions();
        List<String> blockAnswers = GoogleSheetsApiController.Answers();
        List<String> blockVideos = GoogleSheetsApiController.Videos();
        InlineKeyboardMarkup keyboard = new InlineKeyboardMarkup();
        keyboard.setKeyboard(
                Collections.singletonList(
                        Arrays.asList(
                                InlineKeyboardButton.builder().text("\uD83D\uDD3D ответ").callbackData("answer").build(),
                                InlineKeyboardButton.builder().text("\uD83C\uDFA6 видео").url(blockVideos.get(1)).callbackData("video").build(),
                                InlineKeyboardButton.builder().text("▶️next").callbackData("next").build()
                        )
                )
        );
        return keyboard;
    }
}