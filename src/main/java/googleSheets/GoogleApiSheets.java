/*package googleSheets;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.SheetsScopes;
import com.google.api.services.sheets.v4.model.Sheet;
import com.google.api.services.sheets.v4.model.Spreadsheet;
import com.google.auth.http.HttpCredentialsAdapter;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.common.collect.Lists;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

public class GoogleApiSheets {
    private static final String CREDENTIALS_FILE = "/credentials.json";
    private static final String PROPERTIES_FILE = "/application.properties";
    private static final JacksonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
    private static Properties appProp;
    public static Sheets service;
    public static String spreadSheetId;
    public static Spreadsheet spreadsheetMetadata;

    public static List<Sheet> sheets = new ArrayList<>();

    public static void mainGoogle() throws GeneralSecurityException, IOException {
        final NetHttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();
        spreadSheetId = getProperties().getProperty("spreadsheet_id");
        service = new Sheets.Builder(httpTransport, JSON_FACTORY, getCredentials())
                .setApplicationName("Google Api")
                .build();
        spreadsheetMetadata = service.spreadsheets().get(spreadSheetId).execute();
        List<Sheet> sheets = spreadsheetMetadata.getSheets();

        GetAndSet.getAll(sheets, spreadSheetId, service);

    }

    private static HttpRequestInitializer getCredentials() throws IOException {
        InputStream in = GoogleApiSheets.class.getResourceAsStream(CREDENTIALS_FILE);
        if (in == null) {
            throw new FileNotFoundException("No File" + CREDENTIALS_FILE);
        }
        GoogleCredentials credentials = GoogleCredentials.fromStream(in)
                .createScoped(Lists.newArrayList(Collections.singletonList(SheetsScopes.SPREADSHEETS_READONLY)));
        return new HttpCredentialsAdapter(credentials);
    }

    private static Properties getProperties() throws IOException {
        if (appProp != null) {
            return appProp;
        }
        InputStream in = GoogleApiSheets.class.getResourceAsStream(PROPERTIES_FILE);
        if (in == null) {
            throw new FileNotFoundException("No FileAgain" + PROPERTIES_FILE);
        }
        appProp = new Properties();
        appProp.load(in);
        return appProp;
    }

}*/
