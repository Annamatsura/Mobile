package com.example.myandroidapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.IOException;

public class MainActivity2 extends AppCompatActivity {

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        }
    }

    public void goMenu(View v) {
        Intent intent = new Intent(this, Menu.class);
        startActivity(intent);
    }


    // Объявим переменные компонентов

    TextView textView;

    EditText editProduct;
    TextView textProductCalorie;
    TextView textYourCalories;
    Button buttonProduct;

    EditText editYourCalorieToday;
    Button buttonAddYourCalorieToday;
    private static final String MY_NAME = "my_name";
    private static final String MY_WEIGHT = "my_weight";
    String LAST_NAME = "";
    String LAST_WEIGHT = "";

    SharedPreferences mSharedPrefN;
    SharedPreferences mSharedPrefW;

    // Переменная для работы с БД
    private DatabaseHelper mDBHelper;
    private SQLiteDatabase mDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);



        mDBHelper = new DatabaseHelper(this);

        try {
            mDBHelper.updateDataBase();
        } catch (IOException mIOException) {
            throw new Error("UnableToUpdateDatabase");
        }

        try {
            mDb = mDBHelper.getWritableDatabase();
        } catch (SQLException mSQLException) {
            throw mSQLException;
        }


        mSharedPrefN = getSharedPreferences(MY_NAME, Context.MODE_PRIVATE);
        mSharedPrefW = getSharedPreferences(MY_WEIGHT, Context.MODE_PRIVATE);
        LAST_NAME = mSharedPrefN.getString("LAST_NAME", "");
        LAST_WEIGHT = mSharedPrefW.getString("LAST_WEIGHT", "");
        // Найдем компоненты в XML разметке
        textView = (TextView) findViewById(R.id.textView4); // setText() from bd user
        textView.setText("Здравствуйте, " + LAST_NAME + "! Ваш текущий вес: " + LAST_WEIGHT +
                " Рекомендуемая доза калорий на сегодня: 1400");
//        updateTextHead();

        editProduct = (EditText) findViewById(R.id.editTextTextPersonName2);
        String editPr = editProduct.getText().toString();
        textProductCalorie = (TextView) findViewById(R.id.textView6); // setText() from bd products
        buttonProduct = (Button) findViewById(R.id.button2);

        textYourCalories = (TextView) findViewById(R.id.textView7); // setText() from bd calories
//        updateTextProduct();

        editYourCalorieToday = (EditText) findViewById(R.id.editTextNumber4);
        String yourCalorieToday = editYourCalorieToday.getText().toString();
        buttonAddYourCalorieToday = (Button) findViewById(R.id.button3);

        // Пропишем обработчик клика кнопки
        buttonAddYourCalorieToday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String query = "INSERT INTO calories (name) " +
                        "VALUES (yourCalorieToday)";
                mDb.execSQL(query);
                updateTextProduct();

            }
        });


        // Пропишем обработчик клика кнопки
        buttonProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String product = "";

                Cursor cursor = mDb.rawQuery("SELECT * FROM products", null);
                cursor.moveToFirst();
                while (!cursor.isAfterLast()) {
                    if (cursor.getString(1) == editPr){
                        product += cursor.getString(2);
                    }

                    cursor.moveToNext();
                }
                cursor.close();

                if (product == ""){
                    textView.setText("Продукт не найден");
                } else {
                    textView.setText(product);
                }
            }
        });
    }

    void updateTextProduct(){
        String product = "";
        Integer kkal = 0;
        // Отправляем запрос в БД
        Cursor cursor = mDb.rawQuery("SELECT * FROM calories", null);
        cursor.moveToFirst();

        // Пробегаем по всем клиентам
        while (!cursor.isAfterLast()) {

            kkal += Integer.parseInt(cursor.getString(1));
            // Переходим к следующему клиенту
            cursor.moveToNext();
        }
        cursor.close();

        product = Integer.toString(kkal);

        if (product == ""){
            textYourCalories.setText("Вы употребили калорий  за сегодня: 0");
        } else {
            String product2 = "Вы употребили калорий  за сегодня: " + product;
            textYourCalories.setText(product2);
        }
    }

    void updateTextHead(){
        String user = "";
        Integer userKkal = 0;

        // Отправляем запрос в БД
        Cursor cursor = mDb.rawQuery("SELECT * FROM user", null);
        cursor.moveToFirst();

        // Пробегаем по всем клиентам
        while (!cursor.isAfterLast()) {

            user += Integer.parseInt(cursor.getString(1));
            // Переходим к следующему клиенту
            cursor.moveToNext();
        }
        cursor.close();

        userKkal = Integer.parseInt(cursor.getString(3)) / Integer.parseInt(cursor.getString(4));
        if (userKkal > 3){
            user = "Здравствуйте, Иван! Ваш текущий вес: " + cursor.getString(4) +
                    " Рекомендуемая доза калорий на сегодня: 1400";
        } else {
            user = "Здравствуйте, Иван! Ваш текущий вес: " + cursor.getString(4) +
                    " Рекомендуемая доза калорий на сегодня: 4000";
        }

        textView.setText(user);
    }
}