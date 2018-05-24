package com.bombila.michael.bombila;

import android.os.AsyncTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class _Script extends AsyncTask<String,Void,String> {
    @Override
    protected String doInBackground(String... args) {
        String resultString = "";
        String pars = "";
        for (int i=1; i<args.length; i++) {
            if (i == args.length-1) {
                pars += "par" + String.valueOf(i) + "=" + args[i];
            } else {
                pars += "par" + String.valueOf(i) + "=" + args[i] + "&";
            }
        }
        try {
            URL url = new URL(args[0]);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setDoOutput(true);
            OutputStream os = conn.getOutputStream();
            byte[] data = pars.getBytes("UTF-8");
            os.write(data); os.flush(); os.close();

            data = null;
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            InputStream is = conn.getInputStream();
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = is.read(buffer)) != -1) { baos.write(buffer, 0, bytesRead); }
            data = baos.toByteArray();
            baos.flush(); baos.close(); is.close();
            resultString = new String(data, "UTF-8");
            conn.disconnect();
        } catch (MalformedURLException e) { resultString = "MalformedURLException:" + e.getMessage();
        } catch (IOException e) { resultString = "IOException:" + e.getMessage();
        } catch (Exception e) { resultString = "Exception:" + e.getMessage();
        }
        return resultString;
    }
}