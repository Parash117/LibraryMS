package com.example.libms;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

public class JsonParsers {

    public JSONObject registerUser(String url, HashMap<String, String> postDataParams) {
        String json = null;
        JSONObject jobj = null;
        HttpURLConnection connection = null;
        InputStream in = null;
        String line = null;
        try {
            Log.e("url", "registerUser: " + url);
            URL u;
            u = new URL(url);
            connection = (HttpURLConnection) u.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Accept-Charset", "UTF-8");
            connection.setDoOutput(true);
            connection.setConnectTimeout(10000);
            connection.connect();

            DataOutputStream wr = new DataOutputStream(connection.getOutputStream());
            wr.writeBytes(getPostDataString(postDataParams));
            try {
                wr.flush();
                wr.close();
                Log.e("connection", "registerUser: write successful");
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
           e.printStackTrace();
        }
        try {
            int status = connection.getResponseCode();
            if (status >= 200)
                in = connection.getInputStream();
            else
                in = connection.getErrorStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            StringBuilder sb = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
            json = sb.toString();
            Log.d("resposedata", "registerUser: " + json.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            if (json != null) {
                jobj = new JSONObject(json);
            } else {
                return jobj;
            }
        } catch (JSONException e) {
        } finally {
            connection.disconnect();
        }
        return jobj;
    }

    public String getPostDataString(HashMap<String, String> params) throws UnsupportedEncodingException {
        StringBuilder result = new StringBuilder();
        boolean first = true;
        for (Map.Entry<String, String> entry : params.entrySet()) {
            if (first)
                first = false;
            else
                result.append("&");
            result.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
            // Log.e("connection", "getPostDataString: "+result);
        }

        return result.toString();


    }

    public JSONArray fetchdata(String url) {
        String line;
        String json;
        JSONArray jsonArray=null;
        try {
            HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Accept-Charset", "UTF-8");
            connection.setDoOutput(true);
            connection.setConnectTimeout(10000);
            connection.connect();


            InputStream in = connection.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            StringBuilder sb = new StringBuilder();
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
            json = sb.toString();
            try {
                jsonArray = new JSONArray(json);
            } catch (Exception e) {
                e.printStackTrace();
            }
            Log.i("result: ", sb + "");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return jsonArray;
    }
}
