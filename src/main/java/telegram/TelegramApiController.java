package telegram;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import googleSheets.GetAndSet;
import lombok.SneakyThrows;
import org.apache.commons.validator.routines.EmailValidator;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import registration.User;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

public class TelegramApiController extends TelegramLongPollingBot {

    private HashMap<Long, String> requestBase = new HashMap<>();
    private HashMap<Long, User> tempUser = new HashMap<>();
    private ArrayList<User> tempUserBase = new ArrayList<>();

    boolean notification;

    @Override
    public String getBotUsername() {
        return TelegramConstants.BOT_NAME;
    }

    @Override
    public String getBotToken() {
        return TelegramConstants.BOT_TOKEN;
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()){
            handleMessageUpdate(update);
        }
       
        if (update.hasCallbackQuery()) {
            handleCallbackQuery(update);
        }
    }

    private void handleMessageUpdate(Update update) {
        Long chatId = update.getMessage().getChatId();
        String messageText = update.getMessage().getText();
        if (messageText.equals("/start")) {
            //sendText(chatId,"Выбери направление собеседования:", false);
        }
        if (messageText.equals("/settings")) {
            sendText(chatId,"Хочешь, чтобы я напомнил тебе о себе завтра? " +
                    "\nТогда выбери удобное время для ежедневных оповещений:", false);
            notification = true;
        }

        sendText(chatId, (GetAndSet.questions.get(3)).toString(), true);
    }



    private void handleCallbackQuery (Update update) { //реакции на колбеки по кнопкам

        Long chatID = update.getCallbackQuery().getFrom().getId();
        String callbackQuery = update.getCallbackQuery().getData();

        String text = update.getCallbackQuery().getMessage().getText();
        String lastRequest = requestBase.get(chatID);

            switch (callbackQuery){
            case "answer":
                text = (GetAndSet.asks.get(3)).toString();
                break;
            case "next":
                text = "Продолжим...";
                break;
            case "registration":
                requestBase.put(chatID, "registration");
                sendText(chatID, "Начнем регистрацию. Укажи адрес электронной почты, " +
                        "по которому я смогу узнавать тебя в будущем", 0);
                break;
            case "enter":
                requestBase.put(chatID, "enter");
                sendText(chatID, "Введи адрес электронной почты", 0);
                break;
            case "study_start":
                sendText(chatID, "Гоу учиться! Я создал!", 0);
                //сюда можно дописать логику старта обучения
                break;
        }

        sendText(chatID, text, false);
    }

    private ReplyKeyboard createAnswerVideoNextKeyboard(){
        InlineKeyboardMarkup keyboard = new InlineKeyboardMarkup();

        keyboard.setKeyboard(
                Collections.singletonList(
                        Arrays.asList(
                                InlineKeyboardButton.builder().text("\uD83D\uDD3D ответ").callbackData("answer").build(),
                                InlineKeyboardButton.builder().text("\uD83C\uDFA6 видео").url((GetAndSet.videoURLs.get(3)).toString()).callbackData("video").build(),
                                InlineKeyboardButton.builder().text("▶️next").callbackData("next").build()
                        )
                )
        );
        return keyboard;
    }

    private ReplyKeyboard createNotificationTimeKeyboard(){

        String[][] buttons = new String[][]{
                {"\uD83D\uDD58 09:00", "\uD83D\uDD59 10:00", "\uD83D\uDD5A 11:00"},
                {"\uD83D\uDD5B 12:00", "\uD83D\uDD50 13:00", "\uD83D\uDD51 14:00"},
                {"\uD83D\uDD52 15:00", "\uD83D\uDD53 16:00", "\uD83D\uDD54 17:00"},
                {"\uD83D\uDD55 18:00", "\uD83D\uDE45 без уведомлений"}
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

    @SneakyThrows
    private void sendText(long chatID, String text, boolean appendKeyboard) { //отправка сообщения пользователю
        SendMessage sendMessageRequest = new SendMessage();
        sendMessageRequest.setChatId(Long.toString(chatID));
        sendMessageRequest.setText(text);

        if (appendKeyboard){
            sendMessageRequest.setReplyMarkup(createAnswerVideoNextKeyboard());
        }

        if (appendKeyboard && notification == true){
            sendMessageRequest.setReplyMarkup(createNotificationTimeKeyboard());
            notification = false;
        }

        sendApiMethod(sendMessageRequest);
    }

    private void sendText(long chatId, String messageText, int isKeyboard) { //отправка сообщения пользователю
        SendMessage sendMessageRequest = new SendMessage();
        sendMessageRequest.setChatId(Long.toString(chatId));
        sendMessageRequest.setText(messageText);

        switch (isKeyboard) {
            case 1:
                sendMessageRequest.setReplyMarkup(createRegistrationKeyboard());
                break;
            case 2:
                sendMessageRequest.setReplyMarkup(createStartKeyboard());
        }

        try {
            sendApiMethod(sendMessageRequest);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private ReplyKeyboard createRegistrationKeyboard() { //клавиатура для регистрации
        InlineKeyboardMarkup keyboard = new InlineKeyboardMarkup();

        keyboard.setKeyboard(
                Collections.singletonList(
                        Arrays.asList(
                                InlineKeyboardButton.builder().text("Регистрация").callbackData("Registration").build(),
                                InlineKeyboardButton.builder().text("Вход").callbackData("Enter").build()
                        )
                )
        );

        return keyboard;
    }

    private ReplyKeyboard createStartKeyboard() { //клавиатура для подтверждения готовности старта обучения
        InlineKeyboardMarkup keyboard = new InlineKeyboardMarkup();

        keyboard.setKeyboard(
                Collections.singletonList(
                        Collections.singletonList(
                                InlineKeyboardButton.builder().text("Старт").callbackData("Go").build()
                        )
                )
        );

        return keyboard;
    }

}
