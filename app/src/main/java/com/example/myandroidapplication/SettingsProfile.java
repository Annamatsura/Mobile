package com.example.myandroidapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import java.io.IOException;

public class SettingsProfile extends AppCompatActivity {

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



    // Переменная для работы с БД
    private DatabaseHelper mDBHelper;
    private SQLiteDatabase mDb;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings_profile);

//        Spinner spinner = findViewById(R.id.spinner2);
        Spinner spinner = findViewById(R.id.spinner2);
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


        ////////////////////
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
        button = (Button) findViewById(R.id.button5);
        editText = (EditText) findViewById(R.id.editTextTextPersonName3);
        editAge = (EditText) findViewById(R.id.editTextNumber5);
        editHeight = (EditText) findViewById(R.id.editTextNumber6);
        editWeight = (EditText) findViewById(R.id.editTextNumber7);

        String name1 = editText.getText().toString();
        String age1 = editAge.getText().toString();
        String height1 = editHeight.getText().toString();
        String weight1 = editWeight.getText().toString();




        // Пропишем обработчик клика кнопки
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String query = "UPDATE user SET name = name1 WHERE id = 1";
                mDb.execSQL(query);

                String query2 = "UPDATE user SET age = age1 WHERE id = 1";
                mDb.execSQL(query2);

                String query3 = "UPDATE user SET height = height1 WHERE id = 1";
                mDb.execSQL(query3);

                String query4 = "UPDATE user SET weight = weight1 WHERE id = 1";
                mDb.execSQL(query4);

            }
        });

    }

}