package com.a3x3conect.welhamboys;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.github.pwittchen.reactivenetwork.library.ReactiveNetwork;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import a3x3conect.com.mycampus365.R;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {

    // CONNECTION_TIMEOUT and READ_TIMEOUT are in milliseconds
    public static final int CONNECTION_TIMEOUT = 10000;
    public static final int READ_TIMEOUT = 15000;
    EditText Email,Pass;
    String strusr, strpass, usrnme;
    SharedPreferences sp;
    ProgressDialog pd;
    private Subscription internetConnectivitySubscription;

    //helloworld
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Email = (EditText) findViewById(R.id.email);
        Pass = (EditText) findViewById(R.id.pass);
        strusr = Email.getText().toString();
        strpass = Pass.getText().toString();
        sp=getSharedPreferences("login",MODE_PRIVATE);
         pd = new ProgressDialog(MainActivity.this);
        pd.setMessage("loading...");
        pd.setCancelable(false);
        pd.show();




//        if(sp.contains("id") && sp.contains("pass")){
//            startActivity(new Intent(MainActivity.this,Dashpage.class));
//              //finish current activity
//        }

        internetConnectivitySubscription = ReactiveNetwork.observeInternetConnectivity()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Boolean>() {
                    @Override
                    public void call(Boolean isConnectedToInternet) {
                        if ("true".equalsIgnoreCase(isConnectedToInternet.toString())) {


                        }

                        else if ("false".equalsIgnoreCase(isConnectedToInternet.toString())){
                            pd.dismiss();
                            new AlertDialog.Builder(MainActivity.this).setIcon(R.drawable.logo).setTitle("MyCampus365")
                                    .setMessage("Please Connect to Internet")
                                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                }
                            }).show();
                        }

                    }


                });

        loginCheck();

    }

    private void loginCheck() {


        String pass = sp.getString("name",null);
        String id = sp.getString("id", null);
        pd.dismiss();
        if (pass != null && !pass.isEmpty() && id.equalsIgnoreCase("002")) {


            Toast.makeText(MainActivity.this, "Welcome Back..", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(MainActivity.this, TeacherDash.class));
             //finish current activity
        }
        if (pass != null && !pass.isEmpty() && id.equalsIgnoreCase("001")) {


            Toast.makeText(MainActivity.this, "Welcome Back..", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(MainActivity.this, Dashpage.class));
            //finish current activity
        }
        if (pass != null && !pass.isEmpty() && id.equalsIgnoreCase("003")) {


            Toast.makeText(MainActivity.this, "Welcome Back..", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(MainActivity.this, StudentDash.class));
            //finish current activity
        }

        if (pass != null && !pass.isEmpty() && id.equalsIgnoreCase("004")) {


            Toast.makeText(MainActivity.this, "Welcome Back..", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(MainActivity.this, ParentDash.class));
            //finish current activity
        }

        if (pass != null && !pass.isEmpty() && id.equalsIgnoreCase("006")) {


            Toast.makeText(MainActivity.this, "Welcome Back..", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(MainActivity.this, SupportStaffDash.class));
            //finish current activity
        }
        else {
            ImageButton login = (ImageButton)findViewById(R.id.login);
            login.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    new AsyncFetch().execute("http://mycampus3655151.cloudapp.net/welham/webservices/login.php?username=" + Email.getText().toString() + "&password=" + Pass.getText().toString());

                    // new AsyncFetch().execute("http://myschool365.com/welham/webservices/login.php?username=admsdsin&password=Welcome@123");
                    //  Log.e("URL",urlfinal);
                    pd = new ProgressDialog(MainActivity.this);
                    pd.setMessage("Signing in...");
                    pd.setCancelable(false);
                    // pd.setCanceleable(false);
                    pd.show();
                }
            });
        }




    }

    public void onBackPressed() {
        new AlertDialog.Builder(this).setIcon(R.drawable.logo).setTitle("Welham Boys")
                .setMessage("Do you want to Close the App?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(Intent.ACTION_MAIN);
                        intent.addCategory(Intent.CATEGORY_HOME);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        finish();
                    }
                }).setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        }).show();
    }

    public class AsyncFetch extends AsyncTask<String,String,String> {
        @Override
        protected String doInBackground(String... params) {
            HttpURLConnection connection = null;
            BufferedReader reader = null;
            try {
                URL url = new URL(params[0]);
                connection = (HttpURLConnection)url.openConnection();
                connection.connect();
                InputStream stream = connection.getInputStream();
                reader = new BufferedReader(new InputStreamReader(stream));
                StringBuffer buffer = new StringBuffer();
                String line= " ";
                while ((line=reader.readLine())!=null){
                    buffer.append(line);
                }

                return  buffer.toString();
                //

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (connection!=null){
                    connection.disconnect();
                }

                try {
                    if (reader!=null){
                        reader.close();

                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
           // pdLoading.dismiss();

            pd.dismiss();
            pd.cancel();
            try {

                JSONObject reader = new JSONObject(result);
                String s = reader.getString("id");
               String d = reader.getString("name");
                usrnme = Email.getText().toString();

            //   Toast.makeText(MainActivity.this, s , Toast.LENGTH_SHORT).show();
                if (s.equalsIgnoreCase("001")){

                    SharedPreferences.Editor e=sp.edit();
                    e.putString("id",s);
                    e.putString("name",d);
                    e.putString("username", usrnme);
                    e.commit();

                    Intent intent = new Intent(MainActivity.this,Dashpage.class);
                    startActivity(intent);

                } else if (s.equalsIgnoreCase("002")) {
                    SharedPreferences.Editor e = sp.edit();
                    e.putString("id", s);
                    e.putString("name", d);
                    e.putString("username", usrnme);
                    e.commit();

                    Intent intent = new Intent(MainActivity.this, TeacherDash.class);
                    startActivity(intent);

                } else if (s.equalsIgnoreCase("003")) {
                    SharedPreferences.Editor e = sp.edit();
                    e.putString("id", s);
                    e.putString("name", d);
                    e.putString("username", usrnme);
                    e.commit();

                    Intent intent = new Intent(MainActivity.this, StudentDash.class);
                    startActivity(intent);

                } else if (s.equalsIgnoreCase("004")) {
                    SharedPreferences.Editor e = sp.edit();
                    e.putString("id", s);
                    e.putString("name", d);
                    e.putString("username", usrnme);
                    e.commit();

                    Intent intent = new Intent(MainActivity.this, ParentDash.class);
                    startActivity(intent);

                } else if (s.equalsIgnoreCase("006")) {
                    SharedPreferences.Editor e = sp.edit();
                    e.putString("id", s);
                    e.putString("name", d);
                    e.putString("username", usrnme);
                    e.commit();

                    Intent intent = new Intent(MainActivity.this, SupportStaffDash.class);
                    startActivity(intent);

                }
                else
                {
                    Snackbar.make(findViewById(R.id.root),"Please Enter Valid Credentials   ", Snackbar.LENGTH_LONG)
                            .show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            //this method will be running on UI thread

           //



        }

    }
}