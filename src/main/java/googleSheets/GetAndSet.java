/*package googleSheets;

import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.model.Sheet;
import com.google.api.services.sheets.v4.model.ValueRange;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GetAndSet extends GoogleApiSheets {
    public static ValueRange response;
    public static List<Object> responseValues = new ArrayList<>();  //все подряд
    public static List<List<Object>> response5Values = new ArrayList<>();

    static String blockName;
    public static List<String> blocksNames = new ArrayList<>(); //массив с названиями Гугл-листов

    public static List<Object> lessons = new ArrayList<>();     // массив всех уроков,всех листов
    public static List<Object> lessons2 = new ArrayList<>();     // массив всех уроков,всех листов
    public static List<Object> lessons3 = new ArrayList<>();     // массив всех уроков,всех листов

    public static List<Object> questions = new ArrayList<>();   // массив всех вопросов,всех листов
    public static List<Object> questions2 = new ArrayList<>();   // массив всех вопросов,всех листов
    public static List<Object> questions3 = new ArrayList<>();   // массив всех вопросов,всех листов

    public static List<Object> asks = new ArrayList<>();        // массив всех ответов,всех листов
    public static List<Object> asks2 = new ArrayList<>();        // массив всех ответов,всех листов
    public static List<Object> asks3 = new ArrayList<>();

    public static List<Object> videoURLs = new ArrayList<>();   // массив всех ссылок на видео,всех листов
    public static List<Object> videoURLs2 = new ArrayList<>();   // массив всех ссылок на видео,всех листов
    public static List<Object> videoURLs3 = new ArrayList<>();   // массив всех ссылок на видео,всех листов

    public static List<Object> list1 = new ArrayList<>();
    public static List<Object> list2 = new ArrayList<>();
    public static List<Object> list3 = new ArrayList<>();

    public static List<List<Object>> teachBlocks = new ArrayList<>();   // Все Листы и уроки
    static int counts = 0;

    public static void getAll(List<Sheet> sheets, String spreadSheetId, Sheets service) throws IOException {
        for (int j = 0; j < sheets.size(); j++) {

            Sheet sheet = sheets.get(j);

            blockName = sheet
                    .getProperties()
                    .getTitle();
            blocksNames.add(blockName);// blockName - назание Гугл-Листа
            //blocksNames.get(0)-номер листа//!A1:A-название блока обучения
            response = service
                    .spreadsheets()
                    .values()
                    .get(spreadSheetId, blocksNames.get(j)) //blocksNames.get(0)-номер лисат//!A1:A-название блока обучения
                    .execute();
            responseValues.add(response);

            List<List<Object>> response5Values = response.getValues();

            lessons.add("\n" + blockName + "\n");
            asks.add("\n" + blockName + "\n");
            questions.add("\n" + blockName + "\n");
            videoURLs.add("\n" + blockName + "\n");
            if (j==0) {
                list1.add("\n" + blockName + "\n");
            }
            if (j==1) {
                list2.add("\n" + blocksNames.get(1) + "\n");
            }
            if (j==2) {
                list3.add("\n" + blocksNames.get(2) + "\n");
            }
          //  list2.add("\n" + blocksNames.get(1) + "\n");
         //   list3.add("\n" + blocksNames.get(2) + "\n");

            for (int i = 3; i < response5Values.size(); i = i + 2) {
                int count = 1;
                responseValues.add(response5Values.get(i));
              //  blockName = (String) response5Values.get(0).get(0);
                String a = (String) response5Values.get(i).get(0);
                String b = (String) response5Values.get(i).get(1);
                String c = (String) response5Values.get(i).get(2);

                String e = "\n Урок №" + count + "\nВопрос №" + a + "\nОтвет №" + count + ". " + b + "\nСсылка на видео к уроку №" + count + ". " + c;
                count++;
                if (counts == 1) {

                    list2.add("\n" + a + "\n" + b + "\n" + c);
                    teachBlocks.add(list2);
                    questions2.add("\nВопрос №" + a);
                    asks2.add("\nОтвет №" + count + ". " + b);
                    videoURLs2.add("\nСсылка на видео к уроку №" + count + ". " + c);
                    lessons2.add(e);

                }

               // System.out.println(list2);
                if (counts == 2) {
                    list3.add("\n" + a + "\n" + b + "\n" + c);
                    questions3.add("\nВопрос №" + a);
                    asks3.add("\nОтвет №" + count + ". " + b);
                    videoURLs3.add("\nСсылка на видео к уроку №" + count + ". " + c);
                    lessons3.add(e);
                    teachBlocks.add(list3);

                }
                if (counts == 0) {
                    questions.add("\nВопрос №" + a);
                    asks.add(b);
                    videoURLs.add(c);
                    lessons.add(e);
                    list1.add("\n" + a + "\n" + b + "\n" + c);
                    teachBlocks.add(list1);

                }

            }

            counts++;
        }

    }
}*/


