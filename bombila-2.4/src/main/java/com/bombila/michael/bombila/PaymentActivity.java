package com.bombila.michael.bombila;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.concurrent.ExecutionException;

public class PaymentActivity extends AppCompatActivity {
    EditText editCard;
    EditText editCost;
    TextView tvDate;
    TextView tvTime;
    Button button;

    String login = "";
    String password = "";
    String card = "";
    String cost = "";
    String strDateAndTime = "";
    String scripts_host = "";

    SharedPreferences sp;

    Calendar dateAndTime = Calendar.getInstance();
    Long myTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        editCard = (EditText) findViewById(R.id.editCard);
        editCost = (EditText) findViewById(R.id.editCost);
        tvDate = (TextView) findViewById(R.id.tvDate);
        tvTime = (TextView) findViewById(R.id.tvTime);
        button = (Button)findViewById(R.id.button);

        sp = getSharedPreferences("bombila_pref",MODE_PRIVATE);

    }

    public void toSend(View v) {
        card = editCard.getText().toString();
        cost = editCost.getText().toString();
        login = sp.getString("login", "");
        password = sp.getString("password", "");
        scripts_host = sp.getString("scripts_host", "");

        String result = "";
            try {
                result = new _Script().execute(scripts_host + "send_payment.php",
                        login, password, strDateAndTime, card, cost).get();
            } catch (ExecutionException | InterruptedException e) {
                e.printStackTrace();
            }
        if (result.equals("error")) {
            result = "Ошибка. Повторите попытку !!!";
            Toast toast = Toast.makeText(PaymentActivity.this, result, Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        }
        if (result.equals("success")) {
            result = "Данные отправлены !!!";
            Toast toast = Toast.makeText(PaymentActivity.this, result, Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        }
        finish();
    }

    // отображаем диалоговое окно для выбора даты
    public void setDate(View v) {
        new DatePickerDialog(PaymentActivity.this, d,
                dateAndTime.get(Calendar.YEAR),
                dateAndTime.get(Calendar.MONTH),
                dateAndTime.get(Calendar.DAY_OF_MONTH))
                .show();
    }

    // отображаем диалоговое окно для выбора времени
    public void setTime(View v) {
        new TimePickerDialog(PaymentActivity.this, t,
                dateAndTime.get(Calendar.HOUR_OF_DAY),
                dateAndTime.get(Calendar.MINUTE), true)
                .show();
    }
    // установка начальных даты и времени
    private void setInitialDateTime() {
        Long l = dateAndTime.getTimeInMillis();
        String d = new SimpleDateFormat("yyyy-MM-dd").format(l);
        String t = new SimpleDateFormat("HH:mm").format(l);

        String text = "Дата: " + d;
        tvDate.setText(text);

        text = "Время: " + t;
        tvTime.setText(text);

        strDateAndTime = d + " " + t;
    }

    // установка обработчика выбора времени
    TimePickerDialog.OnTimeSetListener t=new TimePickerDialog.OnTimeSetListener() {
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            dateAndTime.set(Calendar.HOUR_OF_DAY, hourOfDay);
            dateAndTime.set(Calendar.MINUTE, minute);
            setInitialDateTime();
            myTime = dateAndTime.getTimeInMillis()/1000;
            Log.d("=======>>>", String.valueOf(myTime));
        }
    };

    // установка обработчика выбора даты
    DatePickerDialog.OnDateSetListener d=new DatePickerDialog.OnDateSetListener() {
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            dateAndTime.set(Calendar.YEAR, year);
            dateAndTime.set(Calendar.MONTH, monthOfYear);
            dateAndTime.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            setInitialDateTime();
            myTime = dateAndTime.getTimeInMillis()/1000;
            Log.d("=======>>>", String.valueOf(myTime));
        }
    };
}
