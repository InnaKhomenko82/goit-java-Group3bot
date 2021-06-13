package registration;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.SneakyThrows;
import org.apache.commons.validator.routines.EmailValidator;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import java.io.*;
import java.util.*;
import java.util.stream.Collectors;
import telegram.TelegramController;

public class Registration {

    private HashMap<Long, String> requestBase = new HashMap<>();
    private HashMap<Long, User> tempUser = new HashMap<>();
    private ArrayList<User> tempUserBase = new ArrayList<>();

    private static final String ENTER_REQUEST = "Enter";
    private static final String REGISTRATION_REQUEST = "Registration";
    private static final String DESTINATION = "UserBase/user.json";
    private static final String GROUP_NAME_REQUEST = "Group";
    private static final String USER_NAME_REQUEST = "Name";
    private static final String USER_SURNAME_REQUEST = "Surname";
    private static final String STUDY_START_REQUEST = "Go";

    public void handleTextUpdate(Update update, TelegramController telegramController) { //обработка "/start" и текстового сообщения от пользователя при наличии запроса от бота
        long chatId = update.getMessage().getChatId();
        if (update.getMessage().getText().equals("/start")) {
            startMessage(update, telegramController);
        }
        else {
            String lastRequest = requestBase.get(chatId);
            switch (lastRequest) {
                case ENTER_REQUEST:
                    enterRequest(update, telegramController);
                    break;
                case REGISTRATION_REQUEST:
                    emailRegistrar(update, telegramController);
                    break;
                case GROUP_NAME_REQUEST:
                    groupRegistrar(update, telegramController);
                    break;
                case USER_NAME_REQUEST:
                    nameRegistrar(update, telegramController);
                    break;
                case USER_SURNAME_REQUEST:
                    surnameRegistrar(update, telegramController);
                    break;
            }
        }
    }
    public void handleCallbackQuery(Update update, TelegramController telegramController) { //реакции на колбеки по кнопкам
        long chatId = update.getCallbackQuery().getFrom().getId();
        String callbackQuery = update.getCallbackQuery().getData();

        switch (callbackQuery) {
            case REGISTRATION_REQUEST:
                requestBase.put(chatId, REGISTRATION_REQUEST);
                sendText(chatId, "Регистрирую новичка))\nУкажи e-mail, по которому я смогу узнавать тебя", 0, telegramController);
                break;
            case ENTER_REQUEST:
                requestBase.put(chatId, ENTER_REQUEST);
                sendText(chatId, "Введи адрес электронной почты", 0, telegramController);
                break;
            case STUDY_START_REQUEST:
                sendText(chatId, "Гоу учиться! Я создал!", 0, telegramController);
                System.out.println(update.getCallbackQuery().getData());
                //сюда можно дописать логику старта обучения

                break;
        }
    }
    private void sendText(long chatId, String messageText, int isKeyboard, TelegramController telegramController) { //отправка сообщения пользователю
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
        telegramController.sendMethod(sendMessageRequest);
    }
    private ReplyKeyboard createRegistrationKeyboard() { //клавиатура для регистрации
        InlineKeyboardMarkup keyboard = new InlineKeyboardMarkup();
        keyboard.setKeyboard(
                Collections.singletonList(
                        Arrays.asList(
                                InlineKeyboardButton.builder().text("Регистрация").callbackData(REGISTRATION_REQUEST).build(),
                                InlineKeyboardButton.builder().text("Вход").callbackData(ENTER_REQUEST).build()
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
                                InlineKeyboardButton.builder().text("Старт").callbackData(STUDY_START_REQUEST).build()
                        )
                )
        );
        return keyboard;
    }
    private void startMessage(Update update, TelegramController telegramController) { //ответ на первичный старт
        long chatId = update.getMessage().getChatId();
        String messageText = "Если ты здесь впервые - нажимай Регистрация.\n" +
                "Если ты уже тёртый калач - продолжаем. Жми Вход!";
        sendText(chatId, messageText,1,telegramController);
    }
    private void enterRequest(Update update, TelegramController telegramController) { //проверка e-mail перед входом
        long chatId = update.getMessage().getChatId();
        String email = update.getMessage().getText().trim();
        if (isEmailValid(email)) {
            if (isNoUserCoincidence(email)) {
                sendText(chatId, "Пользователь с таким e-mail не зарегистрирован. Попробуй ще раз или пройдите регистрацию", 1, telegramController);
            }
            else {
                sendText(chatId, "Вход выполнен. Если готов учиться - жми Старт!", 2, telegramController);
            }
        }
        else {
            sendText(chatId, "Адрес электронной почты указан неверно. Попробуй еще раз", 1, telegramController);
        }
    }
    private void emailRegistrar(Update update, TelegramController telegramControoler) { //регистрация e-mail
        long chatId = update.getMessage().getChatId();
        String email = update.getMessage().getText().trim();
        if (isEmailValid(email)) {
            if (!isNoUserCoincidence(email)) {
                sendText(chatId, "Пользователь с таким e-mail уже зарегистрирован. Попробуй выполнить Вход", 1, telegramControoler);
            }
            else {
                User user = new User();
                user.setEmail(email);
                user.setChatId(chatId);
                tempUser.put(chatId, user);
                requestBase.put(chatId, GROUP_NAME_REQUEST);
                sendText(chatId, "Укажи название своей группы", 0, telegramControoler);
            }
        }
        else {
            sendText(chatId, "Адрес электронной почты указан неверно. Попробуй еще раз.", 1, telegramControoler);
        }
    }
    private void groupRegistrar(Update update, TelegramController telegramController) { //регистрация имени группы
        long chatId = update.getMessage().getChatId();
        String group = update.getMessage().getText().trim();
        User user = tempUser.get(chatId);
        user.setGroupName(group);
        tempUser.put(chatId, user);
        requestBase.put(chatId, USER_NAME_REQUEST);
        sendText(chatId, "Укажи своё имя", 0, telegramController);
    }
    private void nameRegistrar(Update update, TelegramController telegramController) { //регистрация имени пользователя
        long chatId = update.getMessage().getChatId();
        String name = update.getMessage().getText().trim();
        User user = tempUser.get(chatId);
        user.setUserName(name);
        tempUser.put(chatId, user);
        requestBase.put(chatId, USER_SURNAME_REQUEST);
        sendText(chatId, "Укажи свою фамилию", 0, telegramController);
    }
    private void surnameRegistrar(Update update, TelegramController telegramController) { //регистрация фамилии пользователя
        long chatId = update.getMessage().getChatId();
        String surName = update.getMessage().getText().trim();
        User user = tempUser.get(chatId);
        user.setUserSurname(surName);
        tempUser.put(chatId, user);
        putUsersToBase(chatId, tempUser);
        requestBase.put(chatId, STUDY_START_REQUEST);
        sendText(chatId, "Регистрация завершена. Если готов учиться - жми Старт!", 2, telegramController);
    }
    private void getUsersFromBase() { //получение пользователей из файла-базы во временное хранилище
        File file = new File(DESTINATION);
        if (!file.exists()) { //если файла-базы еще нет создаем его и пару пустых пользователей в нем для последующего корректного чтения этого файла
            file.getParentFile().mkdir();
            try {
                file.createNewFile();
                //System.out.println("\nСоздан файл " + file.getName() + file.getPath());
            } catch (IOException e) {
                System.err.println(e.getMessage());
            }
            tempUserBase.add(new User.UserBuilder()
                    .email("anon@gmail.com")
                    .groupName("JavaCore2")
                    .chatId(3654L)
                    .currentBlock("Java")
                    .currentQuestion(5)
                    .build()
            );
            write(DESTINATION, tempUserBase);
        }
        else {
            try (BufferedReader toRead = new BufferedReader(new FileReader(file))) {
                tempUserBase.addAll(Arrays.asList(new Gson().fromJson(toRead.lines().collect(Collectors.joining()), User[].class)));
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }
    }
    @SneakyThrows
    public static <T> void write (String fileName, List <T> list){
        try (FileWriter fileJson = new FileWriter(fileName)){
            fileJson.write(new Gson().toJson(list));
            //System.out.println("\nДанные записаны в файл " + fileName);
        }
    }
    private void putUsersToBase(long chatId, HashMap<Long, User> tempUser) { //перезапись файла-хранилища с новым пользователем
        tempUserBase.add(tempUser.get(chatId));
        Gson json = new GsonBuilder().create();
        File file = new File(DESTINATION);
        try (BufferedWriter toWrite = new BufferedWriter(new FileWriter(file))) {
            List<String> jsonArr = tempUserBase.stream()
                    .map(json::toJson)
                    .collect(Collectors.toList());
            toWrite.append(jsonArr.toString());
        }
        catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
    private boolean isEmailValid(String email) { //проверка валидности e-mail
        EmailValidator eValidator = EmailValidator.getInstance();
        return eValidator.isValid(email);
    }
    private boolean isNoUserCoincidence (String email) { //проверка отсутствия совпадения переданного e-mail с базой пользователей. Возвращает true при отсутствии совпадений
        getUsersFromBase();
        long coincidence = tempUserBase.stream()
                .filter(user -> user.getEmail().equals(email))
                .count();
        //System.out.println(coincidence);
        return coincidence == 0;
    }
}