package com.bombila.michael.bombila;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class OnLineActivity extends AppCompatActivity implements OnClickListener {

    TextView tvNET;
    TextView tvGPS;
    TextView tvAddress;
    TextView tvBalance;
    TextView tvPilot;
    TextView tvSettings;
    TextView tvOrdersInfo;
    ListView lv;
    TextView tvAssign, __tvAssign;
    Button btnOnPlace,__btnOnPlace;
    Button btnOnRoad, __btnOnRoad;
    Button btnCall, __btnCall;
    Button btnCancel, __btnCancel;
    LinearLayout llOnLine, llOnPlace, __llOnPlace;
    ProgressBar __progressBar;

    int order_id = 0;
    int fmanid = 0;
    String session = "";
    String ftaxi = "";
    String os = "android";
    String android = "6.0.1";
    String number = "";
    int version = 17;
    String version_name = "2.5.1";
    String model = "";
    String imei = "";
    String mac = "";

    boolean __clickBtnCancel = false;
    boolean __clickBtnOnPlace = false;
    boolean __clickBtnOnRoad = false;
    boolean clickBtnCancel = false;
    boolean clickBtnOnPlace = false;
    boolean clickBtnOnRoad = false;
    boolean clickOrder = false;

    boolean settings = true;
    boolean pilot = false;
    boolean on_time = false;
    boolean bochka = false;
    boolean big_route = false;
    boolean cb_my_location = false;
    boolean cb_calculate = false;

    double latitude = 0.0;
    double longitude = 0.0;
    long locationTime = 0L;
    double currlatitude = 0.0;
    double currlongitude = 0.0;
    double __currlatitude = 0.0;
    double __currlongitude = 0.0;
    long currlocationTime = 0L;
    String scripts_host = "";
    String callNumber = "";
    String my_location = "";
    String city = "";
    String service = "TaxOid";
    String login = "";
    String password = "";
    String user = "";
    String referer = "";
    String sdirs = "";
    String driver_info = "";
    Double cost = 0.0;
    Double radius = 0.0;
    Double pay = 0.0;
    Double balance = 0.0;
    _IdLtLn dirsLtLn[] = null;
    JSONArray dirCoords = new JSONArray();
    JSONArray dirRadiuses = new JSONArray();
    JSONArray dirs;
    long deltaTime = 10800L;

    ArrayList<Map<String, String>> dataLv = new ArrayList<>();
    SimpleAdapter sAdapter = null;

    MediaPlayer mp = null;
    SharedPreferences sp;
    Daemon daemon = new Daemon();
    private LocationManager locationManager;
//**************************************************************************************************
    private LocationListener locationListener = new LocationListener() {
//**************************************************************************************************
        @Override
        public void onLocationChanged(Location location) {
            if (location == null) return;
            currlatitude = location.getLatitude();
            currlongitude = location.getLongitude();
            currlocationTime = location.getTime();
            String addr = getAddress();
            if(addr != null) {
                tvAddress.setText(addr);
            }
            if (location.getProvider().equals(LocationManager.GPS_PROVIDER)) {
                tvGPS.setTextColor(0xFF99CC00);
//                tvNET.setTextColor(0xFFCC0000);
                if(addr == null) {
                    String gps = "GPS: " + String.valueOf(currlatitude) + ", "
                            + String.valueOf(currlongitude);
                    tvAddress.setText(gps);
                }
            }
            if (location.getProvider().equals(LocationManager.NETWORK_PROVIDER)) {
                tvNET.setTextColor(0xFF99CC00);
//                tvGPS.setTextColor(0xFFCC0000);
                if (addr == null) {
                    String net = "NETWORK: " + String.valueOf(currlatitude) + ", "
                            + String.valueOf(currlongitude);
                    tvAddress.setText(net);
                }
            }
        }
        @Override
        public void onProviderDisabled(String provider) {}
        @Override
        public void onProviderEnabled(String provider) {}
        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {}
    };

//--------------------------------------------------------------------------------------------------
    @Override
    protected void onCreate(Bundle savedInstanceState) {
//--------------------------------------------------------------------------------------------------
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_on_line);

        String s1 = new SimpleDateFormat("yyyy-MM-dd' 'HH:mm:ss", Locale.getDefault())
                .format(new Date(1525375930049L)); //order_time
        String s2 = new SimpleDateFormat("yyyy-MM-dd' 'HH:mm:ss", Locale.getDefault())
                .format(new Date(1525376255085L));//accept
        String s3 = new SimpleDateFormat("yyyy-MM-dd' 'HH:mm:ss", Locale.getDefault())
                .format(new Date(1525376550616L));//place
        String s4 = new SimpleDateFormat("yyyy-MM-dd' 'HH:mm:ss", Locale.getDefault())
                .format(new Date(1525379631365L));//close

        mp = MediaPlayer.create(this, R.raw.order_accepted);

        tvBalance = (TextView) findViewById(R.id.tvBalance);
        tvPilot = (TextView) findViewById(R.id.tvPilot);
        tvNET = (TextView) findViewById(R.id.tvNET);
        tvGPS = (TextView) findViewById(R.id.tvGPS);
        tvAddress = (TextView) findViewById(R.id.tvAddress);
        tvSettings = (TextView) findViewById(R.id.tvSettings);
        tvOrdersInfo = (TextView) findViewById(R.id.tvOrdersInfo);
        lv = (ListView) findViewById(R.id.lv);
        tvAssign = (TextView) findViewById(R.id.tvAssign);
        btnOnPlace = (Button) findViewById(R.id.btnOnPlace);
        btnOnRoad = (Button) findViewById(R.id.btnOnRoad);
        btnCall = (Button) findViewById(R.id.btnCall);
        btnCancel = (Button) findViewById(R.id.btnCancel);
        __tvAssign = (TextView) findViewById(R.id.__tvAssign);
        __btnOnPlace = (Button) findViewById(R.id.__btnOnPlace);
        __btnOnRoad = (Button) findViewById(R.id.__btnOnRoad);
        __btnCall = (Button) findViewById(R.id.__btnCall);
        __btnCancel = (Button) findViewById(R.id.__btnCancel);
        llOnLine = (LinearLayout) findViewById(R.id.llOnLine);
        llOnPlace = (LinearLayout) findViewById(R.id.llOnPlace);
        __llOnPlace = (LinearLayout) findViewById(R.id.__llOnPlace);

        __progressBar = (ProgressBar) findViewById(R.id.__progressBar);

        tvPilot.setOnClickListener(this);
        tvAddress.setOnClickListener(this);
        tvOrdersInfo.setOnClickListener(this);
        btnOnPlace.setOnClickListener(this);
        btnOnRoad.setOnClickListener(this);
        btnCall.setOnClickListener(this);
        btnCancel.setOnClickListener(this);
        __btnOnPlace.setOnClickListener(this);
        __btnOnRoad.setOnClickListener(this);
        __btnCall.setOnClickListener(this);
        __btnCancel.setOnClickListener(this);

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        sp = getSharedPreferences("bombila_pref", MODE_PRIVATE);
        loadSharaPreferences();

        model = Build.MANUFACTURER + " " + Build.MODEL;
        TelephonyManager telephonyManager = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
        imei = telephonyManager.getDeviceId();
        WifiManager wifiManager = (WifiManager) getApplicationContext()
                .getSystemService(Context.WIFI_SERVICE);
        mac = wifiManager.getConnectionInfo().getMacAddress();

        String settings = "Предварительные: ";
        if (on_time) {
            settings += "ДА\n";
        } else {
            settings += "НЕТ\n";
        }
        settings += "Заказы из бочки: ";
        if (bochka) {
            settings += "ДА\n";
        } else {
            settings += "НЕТ\n";
        }
        settings += "Через адрес: ";
        if (big_route) {
            settings += "ДА\n";
        } else {
            settings += "НЕТ\n";
        }
        settings += "Непросчитанные вверху: ";
        if (cb_calculate) {
            settings += "ДА\n";
        } else {
            settings += "НЕТ\n";
        }
        settings += "Сумма: " + String.valueOf(cost) + " грн." + "\n";
        settings += "Радиус: " + String.valueOf(radius) + " км" + "\n\n";
        try {
            dirs = new JSONArray(sdirs);
            for (int i = 0; i < dirs.length(); i++) {
                JSONObject obj = dirs.getJSONObject(i);
                String address = obj.getString("address");
                String radius = obj.getString("radius");
                if (i == dirs.length() - 1) settings += address + " (" + radius + " км)";
                else settings += address + " (" + radius + " км)\n";

                JSONArray coords = new JSONArray();
                String sCoords = obj.getString("coords");
                String[] s_coords = sCoords.split(" ");
                coords.put(0, Double.parseDouble(s_coords[1]));
                coords.put(1, Double.parseDouble(s_coords[0]));
                dirCoords.put(coords);

                dirRadiuses.put(Double.parseDouble(radius));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        tvSettings.setText(settings);

        String[] from = {"address", "distance", "time", "fot", "price", "route"};
        int[] to = {R.id.tvOrderAddress, R.id.tvOrderDistance, R.id.tvOrderTime,
                R.id.tvOrderBochka, R.id.tvOrderPrice, R.id.tvOrderRoute};

        sAdapter = new SimpleAdapter(this, dataLv, R.layout.item, from, to);
        lv.setAdapter(sAdapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Map<String, String> m = dataLv.get(position);
                order_id = Integer.parseInt(m.get("order_id"));
                clickOrder = true;
            }
        });

        __dialogDriverInfo();

        if (cb_my_location) {
            PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
            try {
                startActivityForResult(builder.build(this), 1);
            } catch (GooglePlayServicesRepairableException e) {
                e.printStackTrace();
            } catch (GooglePlayServicesNotAvailableException e) {
                e.printStackTrace();
            }
        }
    }
//-------------------------------------------------------------------------------------
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//-------------------------------------------------------------------------------------
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                Place pp = PlacePicker.getPlace(this, data);
                String address = pp.getAddress().toString();

                String[] arr = address.split(", ");
                if (arr.length < 2) {
                    Toast.makeText(this, "Адрес не определен.", Toast.LENGTH_LONG).show();
                    return;
                }
                try {
                    List<Address> addresses = new Geocoder(this, Locale.getDefault())
                            .getFromLocation(pp.getLatLng().latitude, pp.getLatLng().longitude,1);
                    if (addresses.size() == 0) {
                        Toast.makeText(this, "Адрес не определен.", Toast.LENGTH_LONG).show();
                        return;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                my_location = arr[0] + ", " + arr[1];
                __currlatitude  = pp.getLatLng().latitude;
                __currlongitude = pp.getLatLng().longitude;
            } else {
                cb_my_location = false;
            }
        }
    }
//--------------------------------------------------------------------------------------------------
    @Override
    protected void onResume() {
//--------------------------------------------------------------------------------------------------
        super.onResume();
        if (cb_my_location) {
            tvAddress.setText(my_location);
            return;
        }

        try {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                    1000 * 10, 10, locationListener);
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,
                    1000 * 10, 10, locationListener);
        } catch(SecurityException e) {
            e.printStackTrace();
        }
    }
//--------------------------------------------------------------------------------------------------
    @Override
    protected void onPause() {
//--------------------------------------------------------------------------------------------------
        super.onPause();
        try {
            locationManager.removeUpdates(locationListener);
        } catch(SecurityException e){e.printStackTrace();}
    }
//--------------------------------------------------------------------------------------------------
    @Override
    public void onBackPressed() {
//--------------------------------------------------------------------------------------------------
        super.onBackPressed();
        daemon.cancel(false);
    }
//--------------------------------------------------------------------------------------------------
    @Override
    public void onClick(View v) {
//--------------------------------------------------------------------------------------------------
        switch (v.getId()) {

            case R.id.tvPilot:
                if (pilot) {
                    pilot = false;
                    tvPilot.setTextColor(0xffcc0000);
                } else {
                    pilot = true;
                    tvPilot.setTextColor(0xFF99CC00);
                }
                break;

            case R.id.tvAddress:
                if (settings) {
                    settings = false;
                    tvSettings.setVisibility(View.GONE);
                } else  {
                    settings = true;
                    tvSettings.setVisibility(View.VISIBLE);
                }
                break;

            case R.id.tvOrdersInfo:
                if (settings) {
                    settings = false;
                    tvSettings.setVisibility(View.GONE);
                } else  {
                    settings = true;
                    tvSettings.setVisibility(View.VISIBLE);
                }
                break;

            case R.id.__btnCancel:
                llOnLine.setVisibility(View.VISIBLE);
                llOnPlace.setVisibility(View.GONE);
                __llOnPlace.setVisibility(View.GONE);
                __clickBtnCancel = true;
                break;

            case R.id.__btnOnPlace:
                __clickBtnOnPlace = true;
                break;

            case R.id.__btnOnRoad:
                llOnLine.setVisibility(View.VISIBLE);
                llOnPlace.setVisibility(View.GONE);
                __llOnPlace.setVisibility(View.GONE);
                __clickBtnOnRoad = true;
                break;

            case R.id.__btnCall:
                if (callNumber != null) {
                    String number = String.format("tel:%s", callNumber);
                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                        return;
                    }
                    startActivity(new Intent(Intent.ACTION_CALL, Uri.parse(number)));
                }
                break;

            case R.id.btnCancel:
                llOnLine.setVisibility(View.VISIBLE);
                llOnPlace.setVisibility(View.GONE);
                clickBtnCancel = true;
                break;

            case R.id.btnOnPlace:
                clickBtnOnPlace = true;
                break;

            case R.id.btnOnRoad:
                llOnLine.setVisibility(View.VISIBLE);
                llOnPlace.setVisibility(View.GONE);
                clickBtnOnRoad = true;
                break;

            case R.id.btnCall:
                if (callNumber != null) {
                    String number = String.format("tel:%s", callNumber);
                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                        return;
                    }
                    startActivity(new Intent(Intent.ACTION_CALL, Uri.parse(number)));
                }
                break;

            default:
                break;
        }
    }
//--------------------------------------------------------------------------------------------------
    void loadSharaPreferences() {
//--------------------------------------------------------------------------------------------------
        scripts_host = sp.getString("scripts_host", "");
        login = sp.getString("login", "");
        password = sp.getString("password", "");
        cost = Double.valueOf(sp.getString("cost", "0.0"));
        radius = Double.valueOf(sp.getString("radius", "1.5"));
        sdirs = sp.getString("selected_dirs", "[]");
        on_time = sp.getBoolean("on_time", false);
        bochka = sp.getBoolean("bochka", false);
        big_route = sp.getBoolean("big_route", false);
        cb_my_location = sp.getBoolean("cb_my_location", false);
        cb_calculate = sp.getBoolean("cb_calculate", false);
        my_location = sp.getString("my_location", "");
    }
//--------------------------------------------------------------------------------------------------
    void __dialogMyLocation(){
//--------------------------------------------------------------------------------------------------
        final View view = getLayoutInflater().inflate(R.layout.dialog_my_location, null);
        final EditText et_Address  = (EditText) view.findViewById(R.id.et_Address);

        final AlertDialog dialog;
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(view)
                .setCancelable(false)
                .setTitle("Введите адрес")
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                        my_location  = et_Address.getText().toString();
                        currlocationTime = new Date().getTime();
                        LatLng coords = getLatLng(my_location);
                        currlatitude = coords.latitude;
                        currlongitude = coords.longitude;
                    }
                });

        dialog = builder.create();
        dialog.show();
    }
//--------------------------------------------------------------------------------------------------
    void __dialogDriverInfo(){
//--------------------------------------------------------------------------------------------------
        final View view = getLayoutInflater().inflate(R.layout.dialog_driver_info, null);
        final EditText et_Model  = (EditText) view.findViewById(R.id.et_Model);
        final EditText et_Color  = (EditText) view.findViewById(R.id.et_Color);
        final EditText et_Number = (EditText) view.findViewById(R.id.et_Number);
        final EditText et_Phone  = (EditText) view.findViewById(R.id.et_Phone);

        driver_info = getSharedPreferences("bombila_pref", MODE_PRIVATE)
                .getString("driver_info", "");
        if (!driver_info.equals("")) {
            String[] arr = driver_info.split("!");
            et_Model .setText(arr[0]);
            et_Color .setText(arr[1]);
            et_Number.setText(arr[2]);
            et_Phone .setText(arr[3]);
        }

        final AlertDialog dialog;
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(view)
                .setCancelable(false)
                .setTitle("Данные автовладельца")
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                        String model  = et_Model .getText().toString();
                        String color  = et_Color .getText().toString();
                        String number = et_Number.getText().toString();
                        String phone  = et_Phone .getText().toString()
                                .replace("+","")
                                .replace("-","")
                                .replace("(","")
                                .replace(")","")
                                .replace(" ","");
                        phone = "+" + phone;
                        if (model .length() < 3 ||
                            color .length() < 3 ||
                            number.length() < 3 ||
                            phone.length() < 12) {
                            __dialogDriverInfo();
                            return;
                        }
                        driver_info = model+"!"+color+"!"+number+"!"+phone;
                        getSharedPreferences("bombila_pref", MODE_PRIVATE)
                                .edit()
                                .putString("driver_info", driver_info)
                                .apply();
                        daemon.execute();
                    }
                });

        dialog = builder.create();
        dialog.show();
    }
//--------------------------------------------------------------------------------------------------
    LatLng getLatLng(String addr){
//--------------------------------------------------------------------------------------------------
        Geocoder coder = new Geocoder(this, Locale.getDefault());
        List<Address> addresses;
        LatLng latlng = null;
        try {
            addresses = coder.getFromLocationName(addr, 1);
            if (addresses==null || addresses.size()==0) { return null; }
            latlng = new LatLng(addresses.get(0).getLatitude(), addresses.get(0).getLongitude());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return latlng;
    }
//--------------------------------------------------------------------------------------------------
    String getAddress(){
//--------------------------------------------------------------------------------------------------
        if(currlatitude==0.0 || currlongitude==0.0) { return null; }
        Geocoder coder = new Geocoder(this, Locale.getDefault());
        List<Address> addresses;
        String address = null;
        try {
            addresses = coder.getFromLocation(currlatitude, currlongitude, 1);
            if (addresses==null || addresses.size()==0) { return null; }
            address = addresses.get(0).getAddressLine(0);
            String[] arr = address.split(", ");
            address = arr[0] + ", " + arr[1];
        } catch (IOException e) {
            e.printStackTrace();
        }
        return address;
    }
//--------------------------------------------------------------------------------------------------
    String toScript(String... args) {
//--------------------------------------------------------------------------------------------------
        String resultString;
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
//**************************************************************************************************
    private class  Daemon extends AsyncTask<Void, String, Void> {
//**************************************************************************************************
        Socket socket = null;
        //        String server_IP = "94.27.63.94";
        int server_Port = 11000;
        //        String server_IP = "77.222.143.10";
        String server_IP = "94.153.161.234";
//        int server_Port = 10000;

        String count = "";
        String response = "null";
        String status = "null";
        String type = "null";
        String action_response = "";
        JSONArray orders = new JSONArray();
        JSONArray self_orders = new JSONArray();
        JSONArray deleted_orders = new JSONArray();

        JSONArray __deleted_orders = new JSONArray();
        JSONObject __order = null;
        String __status = "";
        long __previosTime;
        long __accept_time = 0L;
        long __place_time = 0L;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
        @Override
        protected Void doInBackground(Void... values) {

            init();
//            initSocket();
            if (cb_my_location) { setMyLocation(); }
            publishProgress("balance");
            if (balance < 0) {
                publishProgress("badBalance", "У Вас отрицательный баланс: " +
                        String.valueOf(balance) + "\nПополните счет.");
                finish();
                return null;
            }

            auth();
            response = "get_orders";
            __previosTime = Calendar.getInstance().getTimeInMillis();

            if (__checkAccept()) {
                publishProgress("__first_accept");
            }

            int __delay = 1;
            while (true) {
                if(isCancelled()) {
                    break;
                }
                double distance = _Sector.getDistance(currlatitude, currlongitude, latitude, longitude);
                if (distance > 1.0) {
                    latitude = currlatitude;
                    longitude = currlongitude;
                    locationTime = currlocationTime;
                }
                if (latitude == 0.0 || longitude == 0.0) {
                    continue;
                }

                if (clickBtnCancel) {
                    clickBtnCancel = false;
                    action("cancel");
                    continue;
                }
                if (clickBtnOnPlace) {
                    clickBtnOnPlace = false;
                    action("point_a");
                    continue;
                }
                if (clickBtnOnRoad) {
                    clickBtnOnRoad = false;
                    action("point_b");
                    continue;
                }
                if (__clickBtnCancel) {
                    __clickBtnCancel = false;
                    try {
                        JSONObject data = new JSONObject(__order.getString("data"));
                        data.remove("accept_time");
                        __order.remove("data");
                        __order.put("data", data);
                        if (__updateOrder(data.toString(),"free")) {
                            __delay = 1;
                            __deleted_orders.put(__order.getLong("id"));
                            __order = null;
                            __accept_time = 0;
                            __place_time = 0;
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    continue;
                }
                if (__clickBtnOnPlace) {
                    __clickBtnOnPlace = false;
                    publishProgress("pbVisible");
                    try {
                        long pt = Calendar.getInstance().getTimeInMillis();
                        JSONObject data = new JSONObject(__order.getString("data"));
                        data.put("place_time", pt);
                        __order.remove("data");
                        __order.put("data", data);
                        if (__updateOrder(data.toString(),"place")) {
                            __delay = 5;
                            __place_time = pt;
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    publishProgress("pbGone");
                    continue;
                }
                if (__clickBtnOnRoad) {
                    __clickBtnOnRoad = false;
                    try {
                        long pt = Calendar.getInstance().getTimeInMillis();
                        JSONObject data = new JSONObject(__order.getString("data"));
                        data.put("close_time", pt);
                        __order.remove("data");
                        __order.put("data", data);
                        if (__updateOrder(data.toString(),"close")) {
                            __order = null;
                            __accept_time = 0;
                            __place_time = 0;
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    continue;
                }


//===> BombilaClient
                if (__order == null) {
                    __bombilaClient();
                }
                else {
                    if (__getOrderStatus().equals("delete")) {
                        __order = null;
                        publishProgress("get_orders");
                        continue;
                    }
                    __orderStatus();
                    sleep(__delay);
                    continue;
                }


//===> get_orders
                if (response.equals("get_orders")) {
                    if (clickOrder) {
                        clickOrder = false;
                        pilot = true;
                        action("assign");
                        continue;
                    }
//getOrdersFromDb();
//                    getOrders();
                    bombilaScript();
                    continue;
                }

//===> assign
                if (response.equals("assign")) {
                    type = "assign";
                    if (status.equals("null")) {
                        revise();
                        continue;
                    }
                    if (status.equals("success")) {
                        action("state");
                        continue;
                    }
                    response = "get_orders";
                    continue;
                }

//===> revise
                if (response.equals("revise")) {
                    if (status.equals("null")) {
                        revise();
                        sleep(1);
                        continue;
                    }
                    if (status.equals("success")) {
                        if (type.equals("assign") || type.equals("point_a")) {
                            action("state");
                            continue;
                        }
                        if (type.equals("cancel") || type.equals("point_b")) {
                            response = "get_orders";
                            continue;
                        }
                    }
                    response = "get_orders";
                    continue;
                }

//===> state
                if (response.equals("state")) {
                    action("state");
                    if (isNotOrder()) {
                        sleep(1);
                        continue;
                    }
                    if (pilot) {
                        pilot = false;
                        publishProgress("first_state");
                        String sBal = setBalance(String.valueOf(balance), String.valueOf(pay),
                                String.valueOf(order_id), action_response.replace('&', ' '));
                        balance = Double.parseDouble(sBal);
                        publishProgress("balance");
                    }
                    publishProgress("state");
                    sleep(6);
                    continue;
                }

//===> cancel
                if (response.equals("cancel")) {
                    type = "cancel";
                    if (status.equals("success")) {
                        response = "get_orders";
                        continue;
                    }
                    revise();
                    continue;
                }

//===> point_a
                if (response.equals("point_a")) {
                    type = "point_a";
                    if (status.equals("success")) {
                        action("state");
                        continue;
                    }
                    revise();
                    continue;
                }

//===> point_b
                if (response.equals("point_b")) {
                    type = "point_b";
                    if (status.equals("success")) {
                        response = "get_orders";
                        continue;
                    }
                    revise();
                }
            }
//            closeSocket();
            finish();

            return null;
        }
        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);

            if(values[0].equals("pbVisible")) {
                __progressBar.setVisibility(View.VISIBLE);
            }
            if(values[0].equals("pbGone")) {
                __progressBar.setVisibility(View.GONE);
            }
            if(values[0].equals("__first_accept")) {
                mp.start();
                tvPilot.setTextColor(0xffcc0000);
                llOnLine.setVisibility(View.GONE);
                llOnPlace.setVisibility(View.GONE);
                __llOnPlace.setVisibility(View.VISIBLE);
                __showOrderInfo();
            }
            if(values[0].equals("__accept")) {
                __showOrderInfo();
            }
            if(values[0].equals("balance")) {
                double b = new BigDecimal(balance).setScale(1, RoundingMode.UP).doubleValue();
                String text = "Бал.: " + String.valueOf(b);
                tvBalance.setText(text);
            }
            if (values[0].equals("badAddress")) {
                Toast toast = Toast.makeText(OnLineActivity.this,
                        values[1], Toast.LENGTH_LONG);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
            }
            if(values[0].equals("badBalance")) {
                Toast toast = Toast.makeText(OnLineActivity.this,
                        values[1], Toast.LENGTH_LONG);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
            }
            if(values[0].equals("badLogin")) {
                Toast toast = Toast.makeText(OnLineActivity.this,
                        "Неверный Логин или Пароль", Toast.LENGTH_LONG);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
            }
            if(values[0].equals("show_orders")) {
                showOrders();
                deleteOrdersFromBochka();
            }
            if(values[0].equals("first_state")) {
                mp.start();
                tvPilot.setTextColor(0xffcc0000);
                llOnLine.setVisibility(View.GONE);
                llOnPlace.setVisibility(View.VISIBLE);
            }
            if(values[0].equals("state")) {
                showOrderInfo();
            }
            if (values[0].equals("get_orders")) {
                llOnLine.setVisibility(View.VISIBLE);
                llOnPlace.setVisibility(View.GONE);
                __llOnPlace.setVisibility(View.GONE);
            }
        }
        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
        }

//--------------------------------------------------------------------------------------------------
        boolean __checkDriverInfo(){
//--------------------------------------------------------------------------------------------------
            String[] arr = driver_info.split("!");
            if (arr[0].equals("") ||
                    arr[1].equals("") ||
                    arr[2].equals("") ||
                    arr[3].equals("") ||
                    arr[3].equals("+")) return false;
            return true;
        }
//--------------------------------------------------------------------------------------------------
        boolean  __checkAccept() {
//--------------------------------------------------------------------------------------------------
            String driver = login + " " + password;
            driver_info = getSharedPreferences("bombila_pref", MODE_PRIVATE)
                    .getString("driver_info", "");
            String res = toScript("http://185.25.119.3/BombilaClient/check_accept.php",driver);
            if (res.equals("error")) return false;

            try {
                __order = new JSONObject(res);
                __status = __order.getString("status");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return true;
        }
//--------------------------------------------------------------------------------------------------
        void __bombilaClient(){
//--------------------------------------------------------------------------------------------------
            if (!pilot || !__checkDriverInfo()) return;

            long currentTime = Calendar.getInstance().getTimeInMillis();
            long delta = currentTime - __previosTime;
            if (delta < 5000) {
                return;
            }
            __previosTime = currentTime;

            String result = toScript("http://185.25.119.3/BombilaClient/get_free_orders.php");
            try {
                JSONObject jresult = new JSONObject(result);
                JSONArray arr_id = jresult.getJSONArray("id");
//            JSONArray arr_phone = jresult.getJSONArray("phone");
                JSONArray arr_data = jresult.getJSONArray("data");
//            JSONArray arr_status = jresult.getJSONArray("status");
                boolean del = false;
                for (int i=0; i<arr_id.length(); i++) {

                    for (int n=0; n<__deleted_orders.length(); n++) {
                        if (arr_id.getLong(i) == __deleted_orders.getLong(n)) del = true;
                    }
                    if (del) continue;

                    JSONObject jdata = new JSONObject(arr_data.getString(i));
                    if (currentTime-jdata.getInt("order_time") < 20*1000) continue;
                    int _cost_total  = jdata.getInt("cost_total");
                    String _on_time  = jdata.getString("on_time");
                    String _PS       = jdata.getString("PS");
                    JSONArray addrs = jdata.getJSONArray("addresses");
                    JSONArray ltlns = jdata.getJSONArray("coordes");

                    if (_cost_total < cost) continue;
                    if (!on_time && !_on_time.equals("")) continue;
                    if (!big_route && addrs.length()>2) continue;

//                String _address = addrs.getString(0);
                    double _latitude = ltlns.getJSONArray(0).getDouble(0);
                    double _longitude = ltlns.getJSONArray(0).getDouble(1);
                    double  d = __getDistance(latitude, longitude, _latitude, _longitude);
                    if (d > radius) continue;

//                String _end_address = addrs.getString(addrs.length()-1);
                    double _end_latitude = ltlns.getJSONArray(ltlns.length()-1).getDouble(0);
                    double _end_longitude = ltlns.getJSONArray(ltlns.length()-1).getDouble(1);
                    int len = dirRadiuses.length();
                    boolean b = false;
                    if (len > 0) {
                        for (int j = 0; j < len; j++) {
                            double dir_latitude = dirCoords.getJSONArray(j).getDouble(0);
                            double dir_longitude = dirCoords.getJSONArray(j).getDouble(1);
                            d = __getDistance(_end_latitude, _end_longitude, dir_latitude, dir_longitude);
                            if (d <= dirRadiuses.getDouble(j)) {
                                b = __accept(arr_id.getLong(i));
                                if (b) break;
                            }
                        }
                    } else {
                        b = __accept(arr_id.getLong(i));
                        if (b) break;
                    }
                    if (b) break;
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
//--------------------------------------------------------------------------------------------------
        public void  __showOrderInfo() {
//--------------------------------------------------------------------------------------------------
            try {
                String info = "";
                String begin_locality   = "";
                String current_locality = "";

                String sdata = __order.getString("data");
                JSONObject jdata = new JSONObject(sdata);
                if (jdata.has("accept_time")) __accept_time = jdata.getLong("accept_time");
                if (jdata.has("place_time")) __place_time = jdata.getLong("place_time");

// info = "ПРЕДВАРИТЕЛЬНЫЙ ЗАКАЗ ";
                String on_time = jdata.getString("on_time");
                if (!on_time.equals("")) {
                    info = "ПРЕДВАРИТЕЛЬНЫЙ ЗАКАЗ " + "[" + on_time + "]\n\n";
                }
// Заказ принят
                SimpleDateFormat spf = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
                info += "Заказ принят: " + spf.format(new Date(__accept_time)) + "\n";
// На месте
                if (__place_time != 0L) {
                    info += "На месте: " + spf.format(new Date(__place_time)) + "\n";
                }
// Откуда
                String address = jdata.getJSONArray("addresses") .getString(0);
                begin_locality = jdata.getJSONArray("localities").getString(0);
                String[] arr = address.split(", ");
                address = arr[0] + ", " + arr[1];
                info += "Откуда: " + address + "\n";
// Куда
                String route = "";
                for (int j = 1; j < jdata.getJSONArray("addresses").length(); j++) {
                    address = jdata.getJSONArray("addresses").getString(j);
                    current_locality = jdata.getJSONArray("localities").getString(j);
                    arr = address.split(", ");
                    if (begin_locality.equals(current_locality)) {
                        address = arr[0] + ", " + arr[1];
                    } else {
                        address = arr[0] + ", " + arr[1] + " (" + current_locality + ")";
                    }
                    route += "\n=>" + address + " ";
                }
                info +=  "Куда: " + route + "\n";
// Стоимость
                String price = String.valueOf(jdata.getDouble("cost_total"));
                info += "Стоимость: " + price + "\n";
// Примечание
                if (!jdata.getString("PS").equals("")) {
                    info += "Примечание: " + jdata.getString("PS") + "\n";
                }
// Телефон
                callNumber = __order.getString("phone");
                if (!callNumber.equals("null")) {
                    info += "Телефон: " + callNumber + "\n";
                }
// Дозвон
/*
            String fdial_t = obj.getString("FDIAL_T");
            if (!fdial_t.equals("null")) {
                long ltime = (Long.parseLong(fdial_t) - deltaTime) * 1000;
                SimpleDateFormat spf = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
                info += "Дозвон: " + spf.format(new Date(ltime)) + "\n";
            }
*/
                long curr_time  = Calendar.getInstance().getTimeInMillis();
                long delta_time = curr_time - __accept_time;

                if (delta_time < 120000) {
                    __btnCancel.setVisibility(View.VISIBLE);
                } else {
                    __btnCancel.setVisibility(View.GONE);
                }
// Осталось
                if (__place_time == 0L) {
                    __btnOnPlace.setVisibility(View.VISIBLE);
                    __btnOnRoad.setVisibility(View.GONE);

                    long d = __accept_time/1000 + 600 - curr_time/1000;
                    String min = String.valueOf(d / 60);
                    String sec = String.valueOf(d % 60);
                    if (sec.length() == 1) sec += "0";
                    info += "\nОсталось: " + min + ":" + sec;
//                if (d < 60) {
//                    __clickBtnOnPlace = true;
//                }
                } else {
                    __btnOnPlace.setVisibility(View.GONE);
                    __btnOnRoad.setVisibility(View.VISIBLE);
                }
                __tvAssign.setText(info);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
//--------------------------------------------------------------------------------------------------
        public double __getDistance(double s1, double d1, double s2, double d2) {
//--------------------------------------------------------------------------------------------------
            return 111.2 * Math.sqrt(Math.pow(s1-s2,2) + Math.pow((d1-d2)* Math.cos(Math.PI*s1/180),2));
        }
//--------------------------------------------------------------------------------------------------
        public boolean __accept(long order_id) {
//--------------------------------------------------------------------------------------------------
            String driver = login + " " + password;
            String id = String.valueOf(order_id);
            driver_info = getSharedPreferences("bombila_pref", MODE_PRIVATE)
                    .getString("driver_info", "");
            String res = toScript("http://185.25.119.3/BombilaClient/accept_order.php",
                    driver, id, driver_info);

            if (res.equals("error")) return false;

            try {
                __order = new JSONObject(res);
                __status = __order.getString("status");
                __accept_time = Calendar.getInstance().getTimeInMillis();
                JSONObject data = new JSONObject(__order.getString("data"));
                data.put("accept_time", __accept_time);
                __order.remove("data");
                __order.put("data", data);
                boolean b = __updateOrder(data.toString(), __status);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return true;
        }
//--------------------------------------------------------------------------------------------------
        boolean isNotOrder() {
            boolean b = false;
            try {
                JSONObject obj = new JSONObject(action_response);
                if (obj.has("order")) {
                    b = obj.getString("order").equals("null");
                } else {
                    b = true;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return b;
        }
        void initSocket() {
            try {
                socket = new Socket(server_IP, server_Port);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        void closeSocket() {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        void setMyLocation() {
            currlocationTime = new Date().getTime();
//            LatLng coords = getLatLng(my_location);
            currlatitude  = __currlatitude;
            currlongitude = __currlongitude;
//            currlatitude = coords.latitude;
//            currlongitude = coords.longitude;
        }
        boolean __updateOrder(String data, String status) {
            String res = "";
            try {
                res = toScript("http://185.25.119.3/BombilaClient/update_order.php",
                        __order.getString("id"), data, status);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return !res.equals("error");
        }
        String __getOrderStatus() {
            String status = "";
            try {
                String res = toScript(
                        "http://185.25.119.3/BombilaClient/get_order_by_id.php",
                        __order.getString("id"));
                status = new JSONObject(res).getString("status");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return status;
        }
        void __orderStatus() {
            if (pilot) {
                pilot = false;
                publishProgress("__first_accept");
                //                        String sBal = setBalance(String.valueOf(balance), String.valueOf(pay),
                //                                String.valueOf(order_id), action_response.replace('&', ' '));
                //                        balance = Double.parseDouble(sBal);
                //                        publishProgress("balance");
            }
            publishProgress("__accept");
        }
        void sleep(int seconds) {
            try { TimeUnit.MILLISECONDS.sleep(seconds * 1000); }
            catch (InterruptedException e) { e.printStackTrace(); }
        }
        void action(String action) {
            JSONObject obj = new JSONObject();
            try {
                obj.put("action", action);
                obj.put("os", os);
                obj.put("ftaxi", ftaxi);
                obj.put("order_id", order_id);
                obj.put("session", session);
                obj.put("fmanid", fmanid);
                obj.put("version", version);

initSocket();
                sendToSocket(obj.toString());
                action_response = getFromSocket();
closeSocket();

                JSONObject jresp = new JSONObject(action_response);
                response = jresp.getString("response");
                if (response.equals("get_orders")) publishProgress("get_orders");
                if (jresp.has("status")) status = jresp.getString("status");
                if (jresp.has("type")) type = jresp.getString("type");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        void revise() {
            JSONObject obj = new JSONObject();
            try {
                obj.put("action", "revise");
                obj.put("order_id", order_id);
                obj.put("type", type);
                obj.put("session", session);
                obj.put("fmanid", fmanid);
                obj.put("version", version);
initSocket();
                sendToSocket(obj.toString());
                String resp = getFromSocket();
closeSocket();
                JSONObject o = new JSONObject(resp);
                response = o.getString("response");
                status = o.getString("status");
                type = o.getString("type");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        String  setBalance(String bal, String pay, String ord_id, String logs) {
            long l = 0L;
            try {
                l = new JSONObject(logs).getJSONObject("order").getLong("F0");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            l = (l - deltaTime) * 1000;
            String script = scripts_host + "set_balance.php";
            String s = new SimpleDateFormat("yyyy-MM-dd' 'HH:mm:ss", Locale.getDefault())
                    .format(new Date(l));
            return toScript(script, city, service, user, referer, s, bal, pay, ord_id, logs);
        }
        void  init() {
            toScript(scripts_host + "driver_info.php", driver_info, login, password);

            String script = scripts_host + "init.php";
            String _user = toScript(script, login, password);
            if (_user.equals("error")) {
                publishProgress("badLogin");
                daemon.cancel(false);
                finish();
                return;
            }
            JSONObject juser;
            String sBal = "";
            String sPay = "";

            try {
                juser = new JSONObject(_user);
                sBal = juser.getString("balance");
                sPay = juser.getString("pay");
                user = juser.getString("user");
                referer = juser.getString("referer");
                city = juser.getString("city");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            balance = Double.parseDouble(sBal);
            pay = Double.parseDouble(sPay);
        }
        void auth() {
            JSONObject obj = new JSONObject();
            JSONArray arr = new JSONArray();
            JSONObject data = new JSONObject();

            try {
                data.put("login", login);
                data.put("password", password);
                arr.put(0, data);

                obj.put("os", os);
                obj.put("model", model);
                obj.put("android", android);
                obj.put("imei", imei);
                obj.put("data", arr);
                obj.put("action", "get_session");
                obj.put("mac", mac);
                obj.put("number", number);
                obj.put("version", version);
                obj.put("version_name", version_name);
            } catch (JSONException e) {
                e.printStackTrace();
            }
initSocket();
            sendToSocket(obj.toString());
            String s = getFromSocket();
closeSocket();
            try {
                JSONObject auth = new JSONObject(s);
                fmanid = auth.getJSONArray("data").getJSONObject(0).getInt("fmanid");
                session = auth.getJSONArray("data").getJSONObject(0).getString("session");
                ftaxi = auth.getJSONArray("money").getJSONObject(0).getString("ftaxi");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        void sendToSocket(String request) {
            request += "\r\n";
            try {
                socket.getOutputStream().write(request.getBytes());
                socket.getOutputStream().flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        String getFromSocket() {
            ByteArrayOutputStream os = new ByteArrayOutputStream();

            int prevChar;
            try {
                prevChar = socket.getInputStream().read();
                int curChar = socket.getInputStream().read();
                while (true) {
                    if (prevChar == 13 && curChar == 10) {
                        break;
                    }
                    os.write(prevChar);
                    prevChar = curChar;
                    curChar = socket.getInputStream().read();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return os.toString();
        }
        void bombilaScript() {
            JSONObject data = new JSONObject();
            JSONArray myCoods = new JSONArray();
            try {
                myCoods.put(0, latitude);
                myCoods.put(1, longitude);

                data.put("os", os);
                data.put("ftaxi", ftaxi);
                data.put("session", session);
                data.put("fmanid", fmanid);
                data.put("version", version);
                data.put("self_orders", self_orders);
                data.put("my_coords", myCoods);
                data.put("pilot", pilot);
                data.put("bochka", bochka);
                data.put("on_time", on_time);
                data.put("big_route", big_route);
                data.put("cost", cost);
                data.put("radius", radius);
                data.put("dir_coords", dirCoords);
                data.put("dir_radiuses", dirRadiuses);

                String script = scripts_host + "bombila.php";
                String server = "tcp://" + server_IP + ":" + server_Port;
                String resp = toScript( script, server, data.toString());
                JSONObject obj = new JSONObject(resp);
                response = obj.getString("response");
                if (response.equals("assign")) {
                    order_id = obj.getInt("order_id");
                    status = obj.getString("status");
                    return;
                }
                if (response.equals("get_orders")) {
                    String order = obj.getJSONArray("data").getJSONObject(0).getString("order");
                    if (order.equals("null")) {
                        if (user.equals(referer)) count = " [" + obj.getInt("i") + "]";
                        updateOrders(resp);
                        publishProgress("show_orders");
                    } else {
                        pilot = true;
                        response = "state";
                        JSONObject ord = new JSONObject(order);
                        order_id = ord.getInt("FID");
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        void updateOrders(String resp) {
            try {
                JSONObject obj = new JSONObject(resp);
                deleted_orders = obj.
                        getJSONArray("data").getJSONObject(0).getJSONArray("deleted_orders");
                JSONArray new_orders;
                new_orders = obj.getJSONArray("data").getJSONObject(0).getJSONArray("orders");
                for (int i=0; i<new_orders.length(); i++) {
                    JSONObject new_order = new_orders.getJSONObject(i);
                    orders.put(new_order);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            deleteOrders();
            addDistanceToOrders();
            getSelfOrders();
        }
        void getSelfOrders() {
            try {
                JSONArray self = new JSONArray();
                for (int i=0; i<orders.length(); i++) {
                    JSONObject obj = orders.getJSONObject(i);
                    String fot = obj.getString("FOT");
                    if (ftaxi.equals(fot)) {
                        Integer fid = obj.getInt("FID");
                        String uhash = obj.getString("UHASH");
                        self.put(String.valueOf(fid) + ":" + uhash);
                    }
                }
                self_orders = self;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        void deleteOrdersFromBochka() {
            JSONArray arr = new JSONArray();
            try {
                for (int i=0; i<orders.length(); i++) {
                    JSONObject obj = orders.getJSONObject(i);
                    if (!obj.getString("FOT").equals(ftaxi)) continue;
                    arr.put(obj);
                }
                orders = arr;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        void deleteOrders() {
            try {
                for (int i=0; i<deleted_orders.length(); i++) {
                    JSONArray arr = new JSONArray();
                    for (int j=0; j<orders.length(); j++) {
                        JSONObject order = orders.getJSONObject(j);
                        String fid = String.valueOf(order.getString("FID"));
                        if (deleted_orders.getString(i).equals(fid)) continue;
                        arr.put(order);
                    }
                    orders = arr;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        void addDistanceToOrders() {
            try {
                for (int i=0; i<orders.length(); i++) {
                    JSONObject order = orders.getJSONObject(i);
                    JSONArray arr = order.getJSONObject("FCOST_DATA").getJSONArray("Coord");
                    double lat = arr.getJSONArray(0).getDouble(0);
                    double lon = arr.getJSONArray(0).getDouble(1);
                    double d = _Sector.getDistance(latitude, longitude, lat, lon);
                    orders.getJSONObject(i).put("distance", d);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        void showOrders() {
            dataLv.clear();
            int length = orders.length();
            String str = "Заказов: " +  String.valueOf(length) + count;
            tvOrdersInfo.setText(str);

            Map<String, String> m;
            for (int i = 0; i < length; i++) {
                try {
                    JSONObject obj = orders.getJSONObject(i);
                    String order_id = obj.getString("FID");
                    String address = obj.getString("FAD_STR");
                    if (!obj.getString("FAD_H").equals("null")) address += " " + obj.getString("FAD_H");
                    if (!obj.getString("FAD_PO").equals("null")) address += " " + obj.getString("FAD_PO");
                    String fpdate = obj.getString("FPDATE");
                    String time = "";
                    if (!fpdate.equals("null")) {
                        long ltime = (Long.parseLong(fpdate) - deltaTime) * 1000;
                        SimpleDateFormat spf = new SimpleDateFormat("HH:mm", Locale.getDefault());
                        time = "[" + spf.format(new Date(ltime)) + "]";
                    }
                    String price = "";
                    if (obj.getJSONObject("FCOST_DATA").has("cost_s")) {
                        price = obj.getJSONObject("FCOST_DATA").getString("cost_s");
                    }
                    double d = new BigDecimal(obj.getDouble("distance")).
                            setScale(2, RoundingMode.UP).doubleValue();
                    String dist = String.valueOf(d);
                    String distance = "(" + dist + " )";
                    if (d < 10.00) dist = "0" + dist;
                    if(cb_calculate && price.equals("")) {
                        dist = "0";
                    }
                    String fot = "";
                    if (!obj.getString("FOT").equals(ftaxi)) fot = "[Б]";
                    String route = "";
                    JSONArray fad_route = obj.getJSONArray("FAD_ROUTE");
                    if (fad_route != null) {
                        for (int j = 0; j < fad_route.length(); j++) {
                            route += "=>" + fad_route.getString(j) + " ";
                        }
                    }
                    m = new HashMap<>();
                    m.put("order_id", order_id);
                    m.put("address", address);
                    m.put("dist", dist);
                    m.put("distance", distance);
                    m.put("time", time);
                    m.put("fot", fot);
                    m.put("price", price);
                    m.put("route", route.replace('&', ' '));
                    dataLv.add(m);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            Collections.sort(dataLv, new Comparator<Map<String, String>>() {
                @Override
                public int compare(Map<String, String> m1, Map<String, String> m2) {
                    String d1 = m1.get("dist");
                    String d2 = m2.get("dist");
                    return d1.compareTo(d2);
                }
            });
            sAdapter.notifyDataSetChanged();
        }
        void showOrderInfo() {
            try {
                String info = "";
                JSONObject o = new JSONObject(action_response);
                JSONObject obj = o.getJSONObject("order");

                String fpdate = obj.getString("FPDATE");
                if (!fpdate.equals("null")) {
                    long ltime = (Long.parseLong(fpdate) - deltaTime) * 1000;
                    SimpleDateFormat spf = new SimpleDateFormat("HH:mm", Locale.getDefault());
                    info = "ПРЕДВАРИТЕЛЬНЫЙ ЗАКАЗ " + "[" + spf.format(new Date(ltime)) + "]\n\n";
                }
                String f0 = obj.getString("F0");
                if (!f0.equals("null")) {
                    long ltime = (Long.parseLong(f0) - deltaTime) * 1000;
                    SimpleDateFormat spf = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
                    info += "Заказ принят: " + spf.format(new Date(ltime)) + "\n";
                }
                String fa = obj.getString("FA");
                if (!fa.equals("null")) {
                    long ltime = (Long.parseLong(fa) - deltaTime) * 1000;
                    SimpleDateFormat spf = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
                    info += "На месте: " + spf.format(new Date(ltime)) + "\n";
                }
                String address = obj.getString("FAD_STR");
                if (!obj.getString("FAD_H").equals("null")) address += " " + obj.getString("FAD_H");
                if (!obj.getString("FAD_PO").equals("null")) address += " " + obj.getString("FAD_PO");
                info += "Откуда: " + address + "\n";

                String route = "";
                JSONArray fad_route = obj.getJSONArray("FAD_ROUTE");
                if (fad_route != null) {
                    for (int j = 0; j < fad_route.length(); j++) {
                        route += "=>" + fad_route.getString(j) + " ";
                    }
                }
                info +=  "Куда: " + route + "\n";

                String price;
                if (obj.getJSONObject("FCOST_DATA").has("cost_s")) {
                    price = obj.getJSONObject("FCOST_DATA").getString("cost_s");
                    info += "Стоимость: " + price + "\n";
                }
                String note = obj.getString("FAD_NOTE");
                if (!note.equals("null")) {
                    info += "Примечание: " + note + "\n";
                }
                callNumber = obj.getString("FTEL");
                if (!callNumber.equals("null")) {
                    info += "Телефон: " + callNumber + "\n";
                }
                String fdial_t = obj.getString("FDIAL_T");
                if (!fdial_t.equals("null")) {
                    long ltime = (Long.parseLong(fdial_t) - deltaTime) * 1000;
                    SimpleDateFormat spf = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
                    info += "Дозвон: " + spf.format(new Date(ltime)) + "\n";
                }

                int cancel = Integer.parseInt(o.getString("cancel"));
                if (cancel != -1) {
                    btnCancel.setVisibility(View.VISIBLE);
                } else {
                    btnCancel.setVisibility(View.GONE);
                }
                String server_time = o.getString("server_time");
                String point = o.getString("point");
                if (point.equals("0")) {
                    btnOnPlace.setVisibility(View.VISIBLE);
                    btnOnRoad.setVisibility(View.GONE);

                    int d = Integer.parseInt(f0) + 600 - Integer.parseInt(server_time);
                    String min = String.valueOf(d / 60);
                    String sec = String.valueOf(d % 60);
                    info += "\nОсталось: " + min + ":" + sec;
                    if (d < 60) {
                        clickBtnOnPlace = true;
                    }
                }
                if (point.equals("A")) {
                    btnOnPlace.setVisibility(View.GONE);
                    btnOnRoad.setVisibility(View.VISIBLE);
                }

                tvAssign.setText(info);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
