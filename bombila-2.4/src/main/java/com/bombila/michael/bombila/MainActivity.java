package com.bombila.michael.bombila;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity implements OnClickListener {
    String login = "";
    String password = "";
    String referer = "";

    TextView tvInfo;
    TextView tvOnTime;
    TextView tvCost;
    TextView tvRadius;
    TextView tvDirs;
    TextView tvBochka;
    TextView tvBigRoute;
    TextView tvMyLocation;
    TextView tvCalculate;
    Button btnChange;
    Button btnOnLine;
    Button btnPayment;
    Button btnAddDir;
    Button btnDeleteDir;

    SharedPreferences sp;
    String lastModified;
    String modifiedFile = "http://185.25.119.3/bombila.apk";
    String scripts_host = "http://185.25.119.3/taxoid/";

    String sdirs;
    JSONArray dirs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvInfo = (TextView) findViewById(R.id.tvInfo);
        tvOnTime = (TextView) findViewById(R.id.tvOnTime);
        tvBochka = (TextView) findViewById(R.id.tvBochka);
        tvBigRoute = (TextView) findViewById(R.id.tvBigRoute);
        tvCost = (TextView) findViewById(R.id.tvCost);
        tvRadius = (TextView) findViewById(R.id.tvRadius);
        tvDirs = (TextView) findViewById(R.id.tvDirs);
        tvMyLocation = (TextView) findViewById(R.id.tvMyLocation);
        tvCalculate = (TextView) findViewById(R.id.tvCalculate);

        tvDirs.setMovementMethod(new ScrollingMovementMethod());

        btnChange = (Button) findViewById(R.id.btnChange);
        btnOnLine = (Button) findViewById(R.id.btnOnLine);
        btnPayment = (Button) findViewById(R.id.btnPayment);
        btnAddDir = (Button) findViewById(R.id.btnAddDir);
        btnDeleteDir = (Button) findViewById(R.id.btnDeleteDir);
        btnChange.setOnClickListener(this);
        btnOnLine.setOnClickListener(this);
        btnPayment.setOnClickListener(this);
        btnAddDir.setOnClickListener(this);
        btnDeleteDir.setOnClickListener(this);

        ActivityCompat.requestPermissions(this, new String[]{
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_WIFI_STATE,
                Manifest.permission.INTERNET,
                Manifest.permission.READ_PHONE_STATE,
                Manifest.permission.CALL_PHONE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.INSTALL_PACKAGES,
                Manifest.permission.DELETE_PACKAGES
        }, 1);


        sp = getSharedPreferences("bombila_pref",MODE_PRIVATE);
        SharedPreferences.Editor ed = sp.edit();
        ed.putString("scripts_host", scripts_host);
        ed.apply();

        if(checkUpdate()) {
            _UpdateApp app = new _UpdateApp();
            app.setContext(getApplicationContext());
            app.execute(modifiedFile);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnChange:
                startActivity(new Intent(this, BombilaPreferencesActivity.class));
                break;
            case R.id.btnOnLine:
                startActivity(new Intent(this, OnLineActivity.class));
                break;
            case R.id.btnPayment:
                startActivity(new Intent(this, PaymentActivity.class));
                break;
            case R.id.btnAddDir:
                startActivity(new Intent(this, CreateDirectionActivity.class));
                break;
            case R.id.btnDeleteDir:
                startActivity(new Intent(this, DeleteDirectionActivity.class));
                break;
            default:
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadSharaPreferences();
        login = sp.getString("login", "");
        password = sp.getString("password", "");
        getReferer();
        String s = "";
        try {
            s = (new MyScript()).execute(scripts_host + "admin/get_msg.php", referer).get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        tvInfo.setText(s);
        if (s.equals("")) {
            tvInfo.setVisibility(View.GONE);
        }
        else {
            tvInfo.setVisibility(View.VISIBLE);
        }
    }

    void loadSharaPreferences() {
        String text = "Сумма: " + sp.getString("cost", "");
        tvCost.setText(text);

        text = "Радиус: " + sp.getString("radius", "") + " км";
        tvRadius.setText(text);

        text = "";
        sdirs = sp.getString("selected_dirs", "[]");
        try {
            dirs = new JSONArray(sdirs);
            for (int i=0; i<dirs.length(); i++) {
                JSONObject obj = dirs.getJSONObject(i);
                String address = obj.getString("address");
                String radius = obj.getString("radius");
                if (i == dirs.length() - 1) text += address + " (" + radius + " км)";
                else text += address + " (" + radius + " км),\n";
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        tvDirs.setText(text);

        if (sp.getBoolean("on_time", false)) { tvOnTime.setText("Предварительные: Да"); }
        else { tvOnTime.setText("Предварительные: Нет"); }

        if (sp.getBoolean("bochka", false)) { tvBochka.setText("Заказы из бочки: Да"); }
        else { tvBochka.setText("Заказы из бочки: Нет"); }

        if (sp.getBoolean("big_route", false)) { tvBigRoute.setText("Через адрес: Да"); }
        else { tvBigRoute.setText("Через адрес: Нет"); }

        if (sp.getBoolean("cb_my_location", false)) { tvMyLocation.setText("Мое местоположение: Да"); }
        else { tvMyLocation.setText("Мое местоположение: Нет"); }

        if (sp.getBoolean("cb_calculate", false)) { tvCalculate.setText("Непросчитанные вверху: Да"); }
        else { tvCalculate.setText("Непросчитанные вверху: Нет"); }
    }

    boolean checkUpdate() {
        lastModified = sp.getString("lastModified", "");
        try {
            String s = (new _LastModified()).execute(modifiedFile).get();
            if(lastModified.equals(s)) {
                return false;
            } else {
                lastModified = s;
            }
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        SharedPreferences.Editor ed = sp.edit();
        ed.putString("lastModified", lastModified);
        ed.apply();

        return true;
    }

    void  getReferer() {
        String script = scripts_host + "init.php";
        String result = "";
        try {
            result = (new MyScript()).execute(script, login, password).get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
//        String _user = toScript(script, login, password);
        JSONObject juser;

        try {
            juser = new JSONObject(result);
            referer = juser.getString("referer");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public class MyScript extends AsyncTask<String,Void,String> {
        @Override
        protected String doInBackground(String... args) {
            return Script.toScript(args);
        }
    }
}
