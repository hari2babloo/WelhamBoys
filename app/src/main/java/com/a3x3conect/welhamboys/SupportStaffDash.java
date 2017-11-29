package com.a3x3conect.welhamboys;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import a3x3conect.com.mycampus365.R;

public class SupportStaffDash extends AppCompatActivity {

    Button atendance, planner, teacher, library, users, courses, sms, transport, survey, logout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.support_staff_dash);

        atendance = (Button) findViewById(R.id.atendance);
        planner = (Button) findViewById(R.id.planner);
        teacher = (Button) findViewById(R.id.teacher);
        library = (Button) findViewById(R.id.library);
        users = (Button) findViewById(R.id.users);
        courses = (Button) findViewById(R.id.courses);
        survey = (Button) findViewById(R.id.survey);
        transport = (Button) findViewById(R.id.transport);
        logout = (Button) findViewById(R.id.logout);


        getSupportActionBar().setTitle("Planner");
        SharedPreferences sp = getSharedPreferences("login", MODE_PRIVATE);

        String id = sp.getString("id", null);
        String pass = sp.getString("name", null);

        getSupportActionBar().setTitle("Welcome " + pass);


        atendance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SupportStaffDash.this, Attendance.class);
                startActivity(intent);

            }
        });

        planner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SupportStaffDash.this, Planner.class);
                startActivity(intent);

            }
        });

        teacher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SupportStaffDash.this, Teacher.class);
                startActivity(intent);

            }
        });

        library.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SupportStaffDash.this, Librar.class);
                startActivity(intent);

            }
        });

        users.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SupportStaffDash.this, Users.class);
                startActivity(intent);

            }
        });

        courses.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SupportStaffDash.this, Courses.class);
                startActivity(intent);

            }
        });
        survey.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SupportStaffDash.this, Survey.class);
                startActivity(intent);

            }
        });
        transport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SupportStaffDash.this, Transport.class);
                startActivity(intent);

            }
        });
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences sp = getSharedPreferences("login", MODE_PRIVATE);
                SharedPreferences.Editor e = sp.edit();
                e.clear();
                e.commit();
                Intent intent = new Intent(SupportStaffDash.this, MainActivity.class);
                startActivity(intent);
                finish();

            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.men, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // action with ID action_refresh was selected
            case R.id.Logoff:
                SharedPreferences sp = getSharedPreferences("login", MODE_PRIVATE);
                SharedPreferences.Editor e = sp.edit();
                e.clear();
                e.commit();
                Intent intent = new Intent(SupportStaffDash.this, MainActivity.class);
                startActivity(intent);
                finish();
                break;

        }

        return true;
    }

    public void onBackPressed() {
        //  super.onBackPressed();
        moveTaskToBack(true);

    }
}
