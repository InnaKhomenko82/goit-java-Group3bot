package googleSheets;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.Sheets.Builder;
import com.google.api.services.sheets.v4.model.Sheet;
import com.google.api.services.sheets.v4.model.Spreadsheet;
import com.google.auth.http.HttpCredentialsAdapter;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.common.collect.Lists;
import lombok.SneakyThrows;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.security.GeneralSecurityException;
import java.util.*;

public class GoogleSheetsApiController {
        private static final String CREDENTIALS_FILE_PATH = "/credentials.json";
        private static final String PROPERTIES_FILE_PATH = "/application.properties";
        private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
        private static Properties appProp;

        public static ArrayList<String> blocksNames;

        public GoogleSheetsApiController() {
        }

        private static HttpRequestInitializer getCredentials() throws IOException {
            InputStream in = GoogleSheetsApiController.class.getResourceAsStream("/credentials.json");
            if (in == null) {
                throw new FileNotFoundException("Файл не найден");
            } else {
                GoogleCredentials credentials = GoogleCredentials.fromStream(in).createScoped(Lists.newArrayList(Collections.singletonList("https://www.googleapis.com/auth/spreadsheets.readonly")));
                return new HttpCredentialsAdapter(credentials);
            }
        }

        public static Properties getProperties() throws IOException {
            if (appProp != null) {
                return appProp;
            } else {
                InputStream in = GoogleSheetsApiController.class.getResourceAsStream("/application.properties");
                if (in == null) {
                    throw new FileNotFoundException("Файл не найден");
                } else {
                    appProp = new Properties();
                    appProp.load(in);
                    return appProp;
                }
            }
        }

        public static Sheets service() throws GeneralSecurityException, IOException {
            HttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();
            return (new Builder(httpTransport, JSON_FACTORY, getCredentials())).setApplicationName("Google API").build();
        }

        @SneakyThrows
        public static ArrayList <String> Blocks(){
            HttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();
            String spreadsheetID = getProperties().getProperty("spreadsheet_id");
            Sheets service = (new Builder(httpTransport, JSON_FACTORY, getCredentials())).setApplicationName("Google API").build();
            Spreadsheet sheetMetadata = (Spreadsheet)service.spreadsheets().get(spreadsheetID).execute();
            List<Sheet> sheets = sheetMetadata.getSheets();
            blocksNames = new ArrayList <String>();
            for (int i=0; i<sheets.size(); i++) {
                blocksNames.add(sheets.get(i).getProperties().getTitle());
            }
            return blocksNames;
        }

        public static void main(String[] args) throws GeneralSecurityException, IOException {
            String spreadsheetID = getProperties().getProperty("spreadsheet_id");
            String range = getProperties().getProperty("cell_range");
            HttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();
            Sheets service = (new Builder(httpTransport, JSON_FACTORY, getCredentials())).setApplicationName("Google API").build();
            Spreadsheet sheetMetadata = (Spreadsheet)service.spreadsheets().get(spreadsheetID).execute();
            List<Sheet> sheets = sheetMetadata.getSheets();


            /*sheets.forEach((sheet) -> {
                System.out.println(((SheetProperties)sheet.get("properties")).get("title"));
            });*/

            /*String range1 = (String)((SheetProperties)((Sheet)sheets.get(0)).get("properties")).get("title");
            ValueRange response = (ValueRange)service.spreadsheets().values().get(spreadsheetID, range1 + range).execute();
            List<List<Object>> values = response.getValues();
            if (values != null && !values.isEmpty()) {
                Iterator var10 = values.iterator();

                while(var10.hasNext()) {
                    List row = (List)var10.next();
                    if (!row.isEmpty()) {
                        System.out.printf("%s, %s, %s\n", row.get(0), row.get(1), row.get(2));
                    }
                }

            } else {
                System.out.println("нет данных");
            }*/
        }
    }