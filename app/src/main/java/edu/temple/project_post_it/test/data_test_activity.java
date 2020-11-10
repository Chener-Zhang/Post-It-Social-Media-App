package edu.temple.project_post_it.test;

import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import edu.temple.project_post_it.DataBase_Management;
import edu.temple.project_post_it.R;


public class data_test_activity extends AppCompatActivity {

    private TextView mTextView;

    EditText name;
    EditText color;
    EditText hobby;
    EditText age;
    EditText sex;
    Button submit;
    DataBase_Management dataBase_management;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_test_activity);

        name = findViewById(R.id.myname);
        color = findViewById(R.id.mycolor);
        hobby = findViewById(R.id.myhobby);
        age = findViewById(R.id.myage);
        sex = findViewById(R.id.mysex);
        submit = findViewById(R.id.mysubmit);
        dataBase_management = new DataBase_Management();

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("name", name.getText().toString());
                Log.d("color", color.getText().toString());
                Log.d("hobby", hobby.getText().toString());
                Log.d("age", age.getText().toString());
                Log.d("submit", sex.getText().toString());
                test test = new test();
                test.setName(name.getText().toString());
                test.setColor(color.getText().toString());
                test.setHobby(hobby.getText().toString());
                test.setAge(age.getText().toString());

//                dataBase_management.get_data("somethingelse/hahaha");
                dataBase_management.remove_data("sometable/-MLFkj_g9wEsyQhdPnwB/age");
            }
        });

    }
}