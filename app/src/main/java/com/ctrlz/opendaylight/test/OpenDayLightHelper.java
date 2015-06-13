package com.ctrlz.opendaylight.test;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.util.Base64;
import org.json.JSONObject;

public class OpenDayLightHelper {


    public static boolean installFlow(JSONObject postData, String user,
                                      String password, String baseURL) {

        StringBuffer result = new StringBuffer();
        try {

            if (!baseURL.contains("http")) {
                baseURL = "http://" + baseURL;
            }
            //URL where the RESTful Web Service is located
            baseURL = baseURL
                    + "/controller/nb/v2/flowprogrammer/default/node/OF13/"
                    + postData.getJSONObject("node").get("id") + "/staticFlow/"
                    + postData.get("name");

            URL url = new URL(baseURL);

            // Create authentication string and encode it to Base64
            String authStr = user + ":" + password;
            String encodedAuthStr = new String(Base64.encode(authStr.getBytes(),Base64.NO_WRAP));

            // Create Http connection
            HttpURLConnection connection = (HttpURLConnection) url
                    .openConnection();

            // Set connection properties
            connection.setRequestMethod("PUT");
            connection.setRequestProperty("Authorization", "Basic "
                    + encodedAuthStr);
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setUseCaches(false);
            connection.setDoInput(true);
            connection.setDoOutput(true);

            // Set Post Data
            OutputStream os = connection.getOutputStream();
            os.write(postData.toString().getBytes());
            os.close();

            // Get the response from connection's inputStream
            InputStream content = (InputStream) connection.getInputStream();
            BufferedReader in = new BufferedReader(new InputStreamReader(
                    content));
            String line = "";
            while ((line = in.readLine()) != null) {
                result.append(line);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        if ("success".equalsIgnoreCase(result.toString())) {
            return true;
        } else {
            return false;
        }
    }
}
