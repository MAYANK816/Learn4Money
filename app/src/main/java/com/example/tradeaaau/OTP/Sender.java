package com.example.tradeaaau.OTP;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

/**
 * An Example Class to use for the submission using HTTP API You can perform
 * your own validations into this Class For username, password,destination,
 * source, dlr, type, message, server and port
 */
public class Sender {
    // Username that is to be used for submission

String url,number,message,result;

    public Sender(String url, String number, String message) {
        this.url = url;
        this.number = number;
        this.message = message;
    }

    private class UpdateLoc extends AsyncTask<String, Void, String> {

        HashMap<String, String> params;

        public UpdateLoc() {
            this.params = params;
        }

        @Override
        protected String doInBackground(String... strings) {

            try {
                trustAllHttpsCertificates();
                URL url1 = new URL(strings[0]);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url1.openConnection();
                httpURLConnection.setRequestMethod("GET");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                httpURLConnection.connect();
//                OutputStream os = httpURLConnection.getOutputStream();
//                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(os, StandardCharsets.UTF_8));
//                os.write(getPostData(params).getBytes());
//                bufferedWriter.flush();
//                bufferedWriter.close();
//                os.close();
                if (httpURLConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    InputStream is = httpURLConnection.getInputStream();
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
                    StringBuilder builder = new StringBuilder();
                    String line = bufferedReader.readLine();
                    while (line != null) {
                        builder.append(line + "\n");
                        line = bufferedReader.readLine();
                    }
                    is.close();
                    result = builder.toString();

                }
            } catch (Exception e) {
                Log.e("exception-->", e.toString());
                e.printStackTrace();
            }
            return result;
        }

        private String getPostData(HashMap<String, String> postparams) {

            StringBuilder builder = new StringBuilder();
            if (postparams != null) {
                for (Map.Entry<String, String> entry : postparams.entrySet()) {
                    try {
                        builder.append("&");
                        builder.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
                        builder.append("=");
                        builder.append(URLEncoder.encode(entry.getValue(), "UTF-8"));

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }


                return builder.toString();
            } else {
                return "";
            }
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

        }
    }

    public void call() {

//        HashMap<String, String> map = new HashMap<>();
////        map.put("username","nex-bokwallet");
//        map.put("username", "nex-tigerpay");
////        map.put("password", "shaad");
//        map.put("password", "tiger");
//        map.put("type", "0");
//        map.put("dlr", "1");
//        map.put("destination", destination);
////        map.put("source", "Bokpay");
//        map.put("source", "ACECIO");
//        map.put("message", message);

        String server = "sms.digimiles.in";

        Calendar c = Calendar.getInstance();
        c.add(Calendar.MINUTE, 4);
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String formattedDate = df.format(c.getTime());

//        Log.e(" DATA", map.toString());

   /*  Log.e("Urlll ","http://sms.nextgenbpo.in/sendsms/bulksms?username=nex-tigerpay&password=tiger&type=0&dlr=1&"+
      "destination="+destination+"&source=TigrPy&message="+message.replaceAll(" ", "%20"));   */


//        Log.e("Urlll ", "http://103.16.101.52:8080/sendsms/bulksms?username=nex-tigerpay&password=tiger&type=0&dlr=1&" +
//                "destination=" + destination + "&source=ACECIO&message=" + message.replaceAll(" ", "%20"));

//http://smsw.co.in/API/WebSMS/Http/v1.0a/index.php?username=learnm&password=V3mYTr-eHAy&sender=LNGLRN&to=8168196670&message=Your OTP to log in to your account is 2306 Do not share your OTP with anyone. LNGLRN&reqid=1&format={json|text}&pe_id=1701166842155123411&template_id=1707166910539917807
        new Sender.UpdateLoc().execute("https://smsw.co.in/API/WebSMS/Http/v1.0a/index.php?username=learnm&password=V3mYTr-eHAy&sender=LNGLRN&to=" + number +message.replaceAll(" ", "%20"));

        /*http://103.16.101.52:8080/sendsms/bulksms?username=nex-tigerpay&password=tiger&type=0&dlr=1&destination=9916011355&source=TigrPy
        &message=Dear Customer, your OTP is 665544 valid for 4 minutes. TigerPay will never call you asking for OTP.
         Sharing your OTP with anyone means you are giving your TigerPay access to them.&entityid=1201159109110077633&tempid=1207160975876846270*/


    }


    /**
     * Below method converts the unicode to hex value
     *
     * @param regText
     * @return
     */
    private static StringBuffer convertToUnicode(String regText) {
        char[] chars = regText.toCharArray();
        StringBuffer hexString = new StringBuffer();
        for (int i = 0; i < chars.length; i++) {
            String iniHexString = Integer.toHexString((int) chars[i]);
            if (iniHexString.length() == 1) {
                iniHexString = "000" + iniHexString;
            } else if (iniHexString.length() == 2) {
                iniHexString = "00" + iniHexString;
            } else if (iniHexString.length() == 3) {
                iniHexString = "0" + iniHexString;
            }
            hexString.append(iniHexString);

        }
        System.out.println(hexString);
        return hexString;
    }

    private static void trustAllHttpsCertificates() throws Exception {
// Create a trust manager that does not validate certificate chains:
        javax.net.ssl.TrustManager[] trustAllCerts =
                new javax.net.ssl.TrustManager[1];
        javax.net.ssl.TrustManager tm = new miTM();
        trustAllCerts[0] = tm;
        javax.net.ssl.SSLContext sc =
                javax.net.ssl.SSLContext.getInstance("SSL");
        sc.init(null, trustAllCerts, null);
        javax.net.ssl.HttpsURLConnection.setDefaultSSLSocketFactory(
                sc.getSocketFactory());
    }

    public static class miTM implements javax.net.ssl.TrustManager,
            javax.net.ssl.X509TrustManager {
        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
            return null;
        }

        public boolean isServerTrusted(
                java.security.cert.X509Certificate[] certs) {
            return true;
        }

        public boolean isClientTrusted(
                java.security.cert.X509Certificate[] certs) {
            return true;
        }

        public void checkServerTrusted(
                java.security.cert.X509Certificate[] certs, String authType)
                throws java.security.cert.CertificateException {
            return;
        }

        public void checkClientTrusted(
                java.security.cert.X509Certificate[] certs, String authType)
                throws java.security.cert.CertificateException {
            return;

        }
    }
}