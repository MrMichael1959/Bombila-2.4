package com.bombila.michael.bombila;

import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutionException;

public class CreateDirectionActivity extends AppCompatActivity {
    EditText etCity;
    EditText etAddress;
    EditText etDirRadius;
    TextView tvDataCheck;
    Button btnCheck;
    Button btnCreate;

    String scripts_host;
    String country = "Украина";
    String city;
    String address;
    String sCoords;
    String sRadius;

    SharedPreferences sp;

    JSONObject dir;
    String sdirs;
    JSONArray dirs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_direction);

        etCity = (EditText) findViewById(R.id.etCity);
        etAddress = (EditText) findViewById(R.id.etAddress);
        etDirRadius = (EditText) findViewById(R.id.etDirRadius);
        tvDataCheck = (TextView) findViewById(R.id.tvDataCheck);
        btnCheck = (Button) findViewById(R.id.btnCheck);
        btnCreate = (Button) findViewById(R.id.btnCreate);

        sp = getSharedPreferences("bombila_pref",MODE_PRIVATE);
        scripts_host = sp.getString("scripts_host", "");
    }

    public void check(View view) {
        city = etCity.getText().toString();
        address = etAddress.getText().toString();
        sRadius = etDirRadius.getText().toString();
        if (sRadius.equals("")) sRadius = "0.0";

        _IdLtLn coord = getLatLongFromAddress(city + ", " + address);

/*        _IdLtLn coord = new _IdLtLn(0,0.0,0.0);
        try {
            coord = new GetCoords().execute(city + ", " + address).get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
*/
        String addr = getAddress(coord.lat, coord.lon);
        sCoords = String.valueOf(coord.lon) + " " + String.valueOf(coord.lat);
        tvDataCheck.setText(addr + "\n" + sCoords);
    }

    public void create(View view) {
        dir = new JSONObject();
        try {
            dir.put("address", address);
            dir.put("coords", sCoords);
            dir.put("radius", sRadius);

            sdirs = sp.getString("sdirs", "[]");
            dirs = new JSONArray(sdirs);
            dirs.put(dir);

            SharedPreferences.Editor ed = sp.edit();
            ed.putString("sdirs", dirs.toString());
            ed.apply();

            finish();

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    String getAddress(double lat, double lon){
        if(lat==0.0 || lon==0.0) { return null; }
        Geocoder coder = new Geocoder(this, Locale.getDefault());
        List<Address> addresses;
        String address = null;
        try {
            addresses = coder.getFromLocation(lat, lon, 1);
            if (addresses==null || addresses.size()==0) { return null; }
            address = addresses.get(0).getAddressLine(0);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return address;
    }

    _IdLtLn getLatLongFromAddress(String address) {
        _IdLtLn coord = new _IdLtLn(0, 0.0, 0.0);

        Geocoder geoCoder = new Geocoder(this, Locale.getDefault());
        try
        {
            List<Address> addresses = geoCoder
                    .getFromLocationName(country + ", " + address , 1);
            if (addresses.size() > 0)
            {
                coord.lat = addresses.get(0).getLatitude();
                coord.lon = addresses.get(0).getLongitude();
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        return coord;
    }

    String toScript(String... args) {
        String resultString;
        String pars = "";
        for (int i = 1; i < args.length; i++) {
            if (i == args.length - 1) {
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
            os.write(data);
            os.flush();
            os.close();

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            InputStream is = conn.getInputStream();
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = is.read(buffer)) != -1) {
                baos.write(buffer, 0, bytesRead);
            }
            data = baos.toByteArray();
            baos.flush();
            baos.close();
            is.close();
            resultString = new String(data, "UTF-8");
            conn.disconnect();
        } catch (MalformedURLException e) {
            resultString = "MalformedURLException:" + e.getMessage();
        } catch (IOException e) {
            resultString = "IOException:" + e.getMessage();
        } catch (Exception e) {
            resultString = "Exception:" + e.getMessage();
        }
        return resultString;
    }

    _IdLtLn getCoords(String address) {
        String resultString = "";

//        resultString = toScript("https://ivied-launch.000webhostapp.com/test.php", address);
        resultString = toScript("http://yandex.j.scaleforce.net/test.php", address);
/*
        try {
            String myURL = "https://geocode-maps.yandex.ru/1.x/?format=json&geocode=";
            String par = URLEncoder.encode(city + ", " + address, "UTF-8");

            try {
                URL url = new URL(myURL + par);
                URLConnection conn;
                conn = url.openConnection();
                conn.connect();
                if (((HttpURLConnection)conn).getResponseCode() != 200) {
                    return new _IdLtLn(0, 0.0, 0.0);
                }

                byte[] buffer = new byte[8192];
                int bytesRead;
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                while ((bytesRead = conn.getInputStream().read(buffer)) != -1) {
                    baos.write(buffer, 0, bytesRead);
                }
                resultString = new String(baos.toByteArray(), "UTF-8");
            } catch (MalformedURLException e) {
                resultString = "MalformedURLException:" + e.getMessage();
            } catch (IOException e) {
                resultString = "IOException:" + e.getMessage();
            } catch (Exception e) {
                resultString = "Exception:" + e.getMessage();
            }
        } catch (Exception e) { e.printStackTrace(); }
*/
        try {
            JSONObject obj = new JSONObject(resultString);
            resultString = obj.getJSONObject("response").
                    getJSONObject("GeoObjectCollection").
                    getJSONArray("featureMember").
                    getJSONObject(0).
                    getJSONObject("GeoObject").
                    getJSONObject("Point").
                    getString("pos");
        } catch (JSONException e) { e.printStackTrace(); }

        String[] coords = resultString.split(" ");

        Double lat;
        Double lon;
        try {
            lat = Double.parseDouble(coords[1]);
            lon = Double.parseDouble(coords[0]);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return new _IdLtLn(0, 0.0, 0.0);
        } catch (NullPointerException e) {
            e.printStackTrace();
            return new _IdLtLn(0, 0.0, 0.0);
        }

        return new _IdLtLn(0, lat, lon);
    }

//**************************************************************************************************
    public class GetCoords extends AsyncTask<String,Void,_IdLtLn> {
//**************************************************************************************************
    @Override
        protected _IdLtLn doInBackground(String... args) {
//            return getLatLongFromAddress(args[0]);
        return getCoords(args[0]);
        }
    }
}
