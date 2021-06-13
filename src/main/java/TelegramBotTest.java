import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;
import registration.Registration;
import telegram.TelegramApiController;
import telegram.TelegramController;
import telegram.TelegramControllerImpl;

import java.io.IOException;
import java.security.GeneralSecurityException;

public class TelegramBotTest {
    public static void main(String[] args) throws TelegramApiException {
        TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
        telegramBotsApi.registerBot(new TelegramControllerImpl() {
        });



        //telegramBotsApi.registerBot(new Registration());
        //telegramBotsApi.registerBot(new StudyBlock());

        //telegramBotsApi.registerBot(new TelegramApiController());

        //Метод парсит гуглТаблицу и записывает в АрейЛисты
        //GoogleApiSheets.mainGoogle();

        //NotificationTimer.start();

        //Метод записывает информацию из Гугл таблицы в json.формат ToString
        //WriteToJson.Writer2ToString(WriteToJson.row);

        /*System.out.println(GetAndSet.blocksNames);

        System.out.println(GetAndSet.questions.get(3));

        System.out.println(GetAndSet.asks.get(3));

        System.out.println(GetAndSet.videoURLs.get(3));

         */

        //Метод записывает информацию из Гугл таблицы в json.формат ToArray
        //WriteToJson.Writer1ToArray(WriteToJson.responseValues);

        //Метод записывает информацию из Гугл таблицы в json.формат ToObject
        //WriteToJson.Writer3ToObject(WriteToJson.values2);


    }
}