package com.example.navertestbot;

import android.app.DownloadManager;
import android.content.AsyncQueryHandler;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.RequestFuture;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

public class Constants {
    public static final int LEFT_MSG = 0;
    public static final int RIGHT_MSG = 1;

    public final static String Chatbot_URL = "https://6e0e011f4b514540b7793dd5005492bd.apigw.ntruss.com/custom/v1/3611/d1df14c67ffdde8e9444cba9b473cb9959a356162d3212753963acb6513eb132";

    public final static String Chatbot_svKey = "RlN3QXZuQ1hRTE5PU0pNSndzT3JJUkFKcmpvQ0dCV1k=";

    public static class ChatBot extends AsyncTask<String, Void, String> {
        private Context mCtx;

        public ChatBot(Context ctx) {
            mCtx = ctx;
        }

        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        protected String doInBackground(String... params) {
            String body = null;
            Date today_tmp = new Date();
            long ts = today_tmp.getTime();

            Log.v("timestamp", "" + ts);

            body = "{";
            body += "\"version\": \"v2\",";
            body += "\"userId\": \"U47b00b58c90f8e47428af8b7bddcda3d\",";
            body += "\"timestamp\": "+ts+",";
            body += "\"bubbles\": [ {";
            body += "\"type\": \"text\",";

            if(params[0] == null) {

                body += "\"data\" : { \"description\" : \"postback text of welcome action\" } } ],";
                body += "\"event\": \"open\"";
                body += "}";

            }else {

                body += "\"data\" : { \"description\" : \""+params[0]+"\" } } ],";
                body += "\"event\": \"send\"";
                body += "}";

            }
            try {

            SecretKeySpec signingKey = new SecretKeySpec(Constants.Chatbot_svKey.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
            Mac mac = null;
            mac = Mac.getInstance("HmacSHA256");
            mac.init(signingKey);
            byte[] rawHmac = mac.doFinal(body.getBytes(StandardCharsets.UTF_8));
            String signatureHeader = Base64.getEncoder().encodeToString(rawHmac);

            URL url = new URL(Constants.Chatbot_URL);

            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("POST");
            con.setRequestProperty("Content-Type", "application/json;UTF-8");
            con.setRequestProperty("X-NCP-CHATBOT_SIGNATURE", signatureHeader);
            con.setDoInput(true); //  HERE
            con.setDoOutput(true);


            OutputStream os = con.getOutputStream();
            byte[] outputInBytes = body.getBytes(StandardCharsets.UTF_8);
            os.write(outputInBytes);
            os.close();

            con.connect();


            BufferedReader rd;
            if (con.getResponseCode() >= 200 && con.getResponseCode() <= 300) {
                rd = new BufferedReader(new InputStreamReader(con.getInputStream(), StandardCharsets.UTF_8));
            } else {
                rd = new BufferedReader(new InputStreamReader(con.getErrorStream()));
            }
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = rd.readLine()) != null) {
                sb.append(line);
            }

            Log.v("server_respond", con.getResponseMessage() + ", " + sb.toString());
            rd.close();
            con.disconnect();

            return sb.toString();//서버로 부터 받은 값을 리턴해줌 아마 OK!!가 들어올것임

            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            } catch (InvalidKeyException e) {
                e.printStackTrace();
            } catch (ProtocolException e) {
                e.printStackTrace();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

    }

    public static String ext_from_openMsg(String str){
        JSONObject ret = null;
        try {
            ret = new JSONObject(str);
            return ret.getJSONArray("bubbles").getJSONObject(0).getJSONObject("data").getJSONObject("cover").getJSONObject("data").getString("description");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
    public static String ext_from_sendMsg(String str){ //단순 질문, not scenario
        JSONObject ret = null;
        try {
            ret = new JSONObject(str);
            return ret.getJSONArray("bubbles").getJSONObject(0).getJSONObject("data").getString("description");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
}
