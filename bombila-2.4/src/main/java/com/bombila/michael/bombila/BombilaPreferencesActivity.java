package com.bombila.michael.bombila;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class BombilaPreferencesActivity extends AppCompatActivity implements View.OnClickListener {
    EditText etLogin;
    EditText etPassword;
    CheckBox cbOnTime;
    CheckBox cbBochka;
    CheckBox cbBigRoute;
    CheckBox cbMyLocation;
    CheckBox cbCalculate;
    EditText etRadius;
    EditText etCost;
//    EditText etMyLocation;
    TextView tvDirs;
    Button btnDirs;

    SharedPreferences sp;

    String sdirs;
    JSONArray dirs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bombila_preferences);

        etLogin = (EditText) findViewById(R.id.etLogin);
        etPassword = (EditText) findViewById(R.id.etPassword);
        cbOnTime = (CheckBox) findViewById(R.id.cbOnTime);
        cbBochka = (CheckBox) findViewById(R.id.cbBochka);
        cbBigRoute = (CheckBox) findViewById(R.id.cbBigRoute);
        cbMyLocation = (CheckBox) findViewById(R.id.cbMyLocation);
        cbCalculate = (CheckBox) findViewById(R.id.cbCalculate);
        etCost = (EditText) findViewById(R.id.etCost);
        etRadius = (EditText) findViewById(R.id.etRadius);
//        etMyLocation = (EditText) findViewById(R.id.etMyLocation);
        tvDirs = (TextView) findViewById(R.id.tvDirs);
        btnDirs = (Button) findViewById(R.id.btnDirs);

        btnDirs.setOnClickListener(this);
        sp = getSharedPreferences("bombila_pref",MODE_PRIVATE);

        tvDirs.setMovementMethod(new ScrollingMovementMethod());
    }

    @Override
    public void onClick(View v) {
        startActivity(new Intent(this, MyDirectionsActivity.class));
    }

    @Override
    protected void onPause() {
        savePreferences();
        super.onPause();
    }

    @Override
    protected void onResume() {
        loadPreferences();
        super.onResume();
    }

    void savePreferences() {
        Editor ed = sp.edit();
        ed.putString("login", etLogin.getText().toString());
        ed.putString("password", etPassword.getText().toString());
//        ed.putString("my_location", etMyLocation.getText().toString());
        ed.putBoolean("on_time", cbOnTime.isChecked());
        ed.putBoolean("bochka", cbBochka.isChecked());
        ed.putBoolean("big_route", cbBigRoute.isChecked());
        ed.putBoolean("cb_my_location", cbMyLocation.isChecked());
        ed.putBoolean("cb_calculate", cbCalculate.isChecked());

        String s = etCost.getText().toString();
        if(s.equals("")) s = "0.0";
        ed.putString("cost", s);

        s = etRadius.getText().toString();
        if(s.equals("")) s = "0.0";
        ed.putString("radius", s);

        ed.apply();
    }

    void loadPreferences() {
        etLogin.setText(sp.getString("login", ""));
        etPassword.setText(sp.getString("password", ""));
//        etMyLocation.setText(sp.getString("my_location", ""));
        cbOnTime.setChecked(sp.getBoolean("on_time", false));
        cbBochka.setChecked(sp.getBoolean("bochka", false));
        cbBigRoute.setChecked(sp.getBoolean("big_route", false));
        cbMyLocation.setChecked(sp.getBoolean("cb_my_location", false));
        cbCalculate.setChecked(sp.getBoolean("cb_calculate", false));
        etCost.setText(sp.getString("cost", ""));
        etRadius.setText(sp.getString("radius", ""));
        String s = "";
        sdirs = sp.getString("selected_dirs", "[]");
        try {
            dirs = new JSONArray(sdirs);
            for (int i=0; i<dirs.length(); i++) {
                JSONObject obj = dirs.getJSONObject(i);
                String address = obj.getString("address");
                String radius = obj.getString("radius");
                if (i == dirs.length() - 1) s += address + " (" + radius + " км)";
                else s += address + " (" + radius + " км),\n";
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        tvDirs.setText(s);
    }

}
