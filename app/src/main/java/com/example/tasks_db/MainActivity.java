package com.example.tasks_db;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private EditText taskInput;
    private Button addButton;
    private ListView taskListView;
    private ArrayList<String> taskList;
    private ArrayAdapter<String> adapter;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);

            // Инициализация UI элементов
            taskInput = findViewById(R.id.taskInput);
            addButton = findViewById(R.id.addButton);
            taskListView = findViewById(R.id.taskListView);
            
            // Инициализация базы данных
            dbHelper = new DatabaseHelper(this);
            taskList = new ArrayList<>();
            
            // Загрузка задач из базы данных
            taskList.addAll(dbHelper.getAllTasks());
            
            // Настройка адаптера
            adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, taskList);
            taskListView.setAdapter(adapter);

            // Обработчик нажатия кнопки
            addButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String task = taskInput.getText().toString().trim();
                    if (!task.isEmpty()) {
                        dbHelper.addTask(task);
                        taskList.add(task);
                        adapter.notifyDataSetChanged();
                        taskInput.setText("");
                    }
                }
            });
        } catch (Exception e) {
            Toast.makeText(this, "Ошибка: " + e.getMessage(), Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        if (dbHelper != null) {
            dbHelper.close();
        }
        super.onDestroy();
    }
} 