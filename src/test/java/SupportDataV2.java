import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.apache.commons.io.FileUtils;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

public class SupportDataV2 {

//    private String folderPath;
//    public supportDataV2(String folderPath) {
//        this.folderPath = folderPath;
//    }

    private static final String API_V2_SELENIUM_AGENTS = "/api/v2/selenium-agents";
    private static final String API_V2_DHMS = "/api/v2/agents";
    private static final String API_V2_SERVICES = "/api/v2/region-services";
    private static final String api = "/api/v2/configuration/collect-support-data/false/false";
    private static String fileName;
    private static String dateFormat = "EEE MMM d yyyy HH_mm_ss z";


    private static String baseURL = "https://qa-win2016.experitest.com";
    private static String AccessKey = "eyJ4cC51IjoxNTg0ODA0LCJ4cC5wIjoyLCJ4cC5tIjoiTVRVMU5ERXdORFF6TmprMU13IiwiYWxnIjoiSFMyNTYifQ.eyJleHAiOjE4Njk0NjQ0MzYsImlzcyI6ImNvbS5leHBlcml0ZXN0In0.cuilvVOQwSXcXr2uV9pOzuw3yqhjtSrdUsq-Ilj57xQ";


    private static String getAllComponentAPIResult(String component) {

        String ApiUrl = baseURL + component;

        HttpResponse<String> response = null;
        try {
            response = Unirest.get(ApiUrl).header("Authorization","Bearer "+AccessKey)
                    .asString();
        } catch (UnirestException e) {
            e.printStackTrace();
        }
        return Objects.nonNull(response) ? response.getBody() : null;
    }

    private static String getSupportedIDs(String component) {
        JSONArray seleniumAgents = new JSONArray(Objects.requireNonNull(getAllComponentAPIResult(component)));

        StringBuilder seleniumIDs = new StringBuilder("/");
        for (int i = 0; i < seleniumAgents.length(); i++) {
            JSONObject seleniumAgent = seleniumAgents.getJSONObject(i);

            //Check to see if the Selenium online
            if (component.equals(API_V2_SELENIUM_AGENTS)) {
                if (seleniumAgent.get("connected").toString().equals("true")) {
                    seleniumIDs.append(seleniumAgent.get("id")).append(",");
                }
            }
            if (component.equals(API_V2_DHMS)) {
                if (seleniumAgent.get("available").toString().equals("true")) {
                    seleniumIDs.append(seleniumAgent.get("id")).append(",");
                }
            }
            if (component.equals(API_V2_SERVICES)) {
                if (seleniumAgent.get("status").toString().equals("ONLINE")) {
                    seleniumIDs.append(seleniumAgent.get("id")).append(",");
                }
            }
        }
        return seleniumIDs.substring(0, seleniumIDs.length() - 1);
    }

    public static void collect(boolean takeDHMs, boolean takeSeleniums, boolean takeServices, String folderPathToDownload) throws IOException {

        String fullURL, seleniums, dhms, services, devices;
        seleniums = "/-1";
        dhms = "/-1";
        services = "/-1";
        devices = "/-1";
        if (takeDHMs) {
            dhms = getSupportedIDs(API_V2_DHMS);
        }
        if (takeSeleniums) {
            seleniums = getSupportedIDs(API_V2_SELENIUM_AGENTS);
        }
        if (takeServices) {
            services = getSupportedIDs(API_V2_SERVICES);
        }

        fullURL = baseURL + api + dhms + devices + seleniums + services;
        System.out.println("URL to send to api is: " + fullURL);
        fileName = new SimpleDateFormat(dateFormat).format(new Date());
        System.out.println(fileName);


        System.out.println("Starting download Collect Support Data, time is: " + new SimpleDateFormat(dateFormat).format(new Date()));
        try {
            DefaultHttpClient httpClient = new DefaultHttpClient();
            HttpGet getRequest = new HttpGet(fullURL);
            getRequest.addHeader("accept", "application/json");
            getRequest.addHeader("Authorization", "Bearer " + AccessKey);
//            getRequest.addHeader("Authorization", "Basic ZXlhbGs6SmoxMjM0NTY=");

            org.apache.http.HttpResponse response = httpClient.execute(getRequest);

            if (response.getStatusLine().getStatusCode() != 200) {
                throw new RuntimeException("Failed : HTTP error code : "
                        + response.getStatusLine().getStatusCode());
            }

            File targetFile = new File(folderPathToDownload + "/cloud_SupportData " + fileName + ".zip");
            FileUtils.copyInputStreamToFile(response.getEntity().getContent(), targetFile);
            httpClient.getConnectionManager().shutdown();
            System.out.println("Ended download Collect Support Data, time is: " + new SimpleDateFormat(dateFormat).format(new Date()));

        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }


    public static void main(String[] args) {
        try {
            collect(true ,true,true, "C:/Users/eyal.kopelevich/Documents/supportData");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

//    @Override
//    public void run() {
//        try {
//            collect(true ,true,true, this.folderPath);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
}
