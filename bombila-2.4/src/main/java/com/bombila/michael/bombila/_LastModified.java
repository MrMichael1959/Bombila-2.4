package com.bombila.michael.bombila;

import android.os.AsyncTask;
import android.util.Log;

import java.net.HttpURLConnection;
import java.net.URL;

public class _LastModified extends AsyncTask<String,Void,String> {

    @Override
    protected String doInBackground(String... arg0) {
        String lastModified = "";
        try {
            URL url = new URL(arg0[0]);
            HttpURLConnection c = (HttpURLConnection) url.openConnection();

            lastModified = c.getHeaderField("Last-Modified");
        } catch (Exception e) {
            Log.e("UpdateAPP", "Update error! " + e.getMessage());
        }
        return lastModified;
    }
}
