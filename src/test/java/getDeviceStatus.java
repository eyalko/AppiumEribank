import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class getDeviceStatus {

    private static final String filePath = "C:\\Users\\eyal.kopelevich\\Desktop\\jsonTestFile.json";


    public static String getStatus(String urlBase, String user, String password, String udid) throws UnirestException {

        String url = urlBase + "/api/v1/devices";
        HttpResponse<String> response = Unirest.get(url)
                .basicAuth(user, password)
                .asString();
        String json = response.getBody();


//        Map<String, String>[] devices = new Map[120];
        try {
            JSONParser jsonParser = new JSONParser();
            JSONObject jsonObject = (JSONObject) jsonParser.parse(json);

            // get a String from the JSON object
            String firstName = (String) jsonObject.get("status");
            System.out.println("Status of the API: " + firstName);

            // get an array from the JSON object
            JSONArray data = (JSONArray) jsonObject.get("data");

            Iterator i = data.iterator();
//            int j = 0;

            // take each value from the json array separately
            while (i.hasNext()) {
                JSONObject innerObj = (JSONObject) i.next();
//                devices[j] = new HashMap<>();
                for (Object key : innerObj.keySet()) {
                    //based on you key types
                    String keyStr = (String) key;
//                    Object keyvalueObj = innerObj.get(keyStr);
//                    String keyvalue;
                    if (keyStr == udid) {
                        return innerObj.get(keyStr).toString();
                    }
//                    if (keyvalueObj == null) {
//                        keyvalue = "null";
//                    } else {
//                        keyvalue = innerObj.get(keyStr).toString();
//                    }
//                    devices[j].put(keyStr, keyvalue);
                }
//                j++;

            }

        } catch (ParseException ex) {
            ex.printStackTrace();
        } catch (NullPointerException ex) {
            ex.printStackTrace();
        }

        return "offline";
    }

}