package telegram;

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
import registration.User;

import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.stream.Collectors;

public class TelegramApiController{

    public final Queue<Object> sendQueue = new ConcurrentLinkedQueue<>();
    public final Queue<Object> receiveQueue = new ConcurrentLinkedQueue<>();

    //private final Commandor commandor;

    /*public InterviewPreparationHelperBot(){
        this.commandor = Commandor.getInstance(this);
        new Notification(commandor).send();
    }

    private HashMap<Long, String> requestBase = new HashMap<>();
    private HashMap<Long, User> tempUser = new HashMap<>();

     */
    private ArrayList<User> tempUserBase = new ArrayList<>();

    boolean notification;

    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()){

        }
       
        if (update.hasCallbackQuery()) {
            handleCallbackQuery(update);
        }
    }

    private void handleCallbackQuery (Update update) { //реакции на колбеки по кнопкам

        Long chatID = update.getCallbackQuery().getFrom().getId();
        String callbackQuery = update.getCallbackQuery().getData();

        String text = update.getCallbackQuery().getMessage().getText();

        sendText(chatID, text, false);
    }

    private ReplyKeyboard createAnswerVideoNextKeyboard(){
        InlineKeyboardMarkup keyboard = new InlineKeyboardMarkup();

        keyboard.setKeyboard(
                Collections.singletonList(
                        Arrays.asList(
                                InlineKeyboardButton.builder().text("\uD83D\uDD3D ответ").callbackData("answer").build(),
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

        //sendApiMethod(sendMessageRequest);
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
