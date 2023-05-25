package com.example.myandroidapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;



import android.widget.Button;
import android.widget.TextView;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    //    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//    }

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

    public void goMainActivity2(View v) {
        Intent intent = new Intent(this, MainActivity2.class);
        startActivity(intent);
    }

    String[] data = {"Практически никаких \n" +
            "(занятия спортом отсутствуют)", "Средние (занятия спортом 1-3 раза\n" +
            "в неделю) ", "Высокие (занятия спортом 4-7 раз\n" +
            "в неделю) "};


    // Объявим переменные компонентов
    Button button;
    EditText editText;
    EditText editAge;
    EditText editHeight;
    EditText editWeight;

    SharedPreferences mSharedPrefN;
    SharedPreferences mSharedPrefW;

    private static final String MY_NAME = "my_name";
    private static final String MY_WEIGHT = "my_weight";
    final String LAST_NAME = "";
    final String LAST_WEIGHT = "";



    // Переменная для работы с БД
    private DatabaseHelper mDBHelper;
    private SQLiteDatabase mDb;


    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Spinner spinner = findViewById(R.id.spinner);
        // Создаем адаптер ArrayAdapter с помощью массива строк и стандартной разметки элемета spinner
        ArrayAdapter<String> adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, data);
        // Определяем разметку для использования при выборе элемента
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Применяем адаптер к элементу spinner
        spinner.setAdapter(adapter);

        AdapterView.OnItemSelectedListener itemSelectedListener = new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                // Получаем выбранный объект
                String item = (String)parent.getItemAtPosition(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        };
        spinner.setOnItemSelectedListener(itemSelectedListener);



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


        // Найдем компоненты в XML разметке
        button = (Button) findViewById(R.id.button);
        editText = (EditText) findViewById(R.id.editTextTextPersonName);
        editAge = (EditText) findViewById(R.id.editTextNumber);
        editHeight = (EditText) findViewById(R.id.editTextNumber2);
        editWeight = (EditText) findViewById(R.id.editTextNumber3);






        // Пропишем обработчик клика кнопки
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                String query = "INSERT INTO user (name, age, height, weight, activity) " +
//                        "VALUES (name1, age1, height1, weight1, '')";
//                mDb.execSQL(query);
                String name1 = editText.getText().toString();
                String age1 = editAge.getText().toString();
                String height1 = editHeight.getText().toString();
                String weight1 = editWeight.getText().toString();
                mSharedPrefN = getSharedPreferences(MY_NAME, Context.MODE_PRIVATE);
                mSharedPrefW = getSharedPreferences(MY_WEIGHT, Context.MODE_PRIVATE);
                SharedPreferences.Editor mEditorN = mSharedPrefN.edit();
                SharedPreferences.Editor mEditorW = mSharedPrefW.edit();
                mEditorN.putString("LAST_NAME", name1);
                mEditorN.commit();
                mEditorW.putString("LAST_WEIGHT", weight1);
                mEditorW.commit();
                Toast.makeText(MainActivity.this, "Вход прошел успешно!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(), MainActivity2.class);
//                intent.putExtra("LOGIN_USER", login);
                startActivity(intent);
            }
        });

    }
}