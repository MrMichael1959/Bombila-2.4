package com.bombila.michael.bombila;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.SparseBooleanArray;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class DeleteDirectionActivity extends AppCompatActivity {
    ListView lv;
    String sdirs;
    JSONArray dirs;
    JSONArray new_dirs;
    SharedPreferences sp;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_directions);

        sp = getSharedPreferences("bombila_pref",MODE_PRIVATE);
        lv = (ListView) findViewById(R.id.lv);
        lv.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_multiple_choice, getDirs());
        lv.setAdapter(adapter);

        new_dirs = new JSONArray();
    }

    @Override
    protected void onPause() {
        super.onPause();

        SparseBooleanArray sbArray = lv.getCheckedItemPositions();
        try {
            for (int j=0; j<dirs.length(); j++) {
                boolean b = true;
                for (int i = 0; i < sbArray.size(); i++) {
                    int key = sbArray.keyAt(i);
                    if (sbArray.get(key)) {
                        if (j == key) {
                            b = false;
                            break;
                        }
                    }
                }
                if (b) new_dirs.put(dirs.get(j));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        SharedPreferences.Editor ed = sp.edit();
        ed.putString("sdirs", new_dirs.toString());
        ed.apply();
    }

    String[] getDirs() {
        String[] arr = null;
        sdirs = sp.getString("sdirs", "[]");
        try {
            dirs = new JSONArray(sdirs);
            arr = new String[dirs.length()];
            for (int i=0; i<dirs.length(); i++) {
                JSONObject obj = dirs.getJSONObject(i);
                String address = obj.getString("address");
                String radius = obj.getString("radius");
                arr[i] = address + " (" + radius + "км)";
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return arr;
    }
}