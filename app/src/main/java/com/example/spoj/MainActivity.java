package com.example.spoj;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.math.RoundingMode;
import java.text.DecimalFormat;

public class MainActivity extends AppCompatActivity {

    EditText editText;
    TextView textView;
    TextView textView1;

    public class DownloadTask extends AsyncTask<String, Void, String>{

        String result1 = "";
        boolean flag=true;

        @Override
        protected String doInBackground(String... urls) {

            result1 = "";
            URL url;
            HttpURLConnection urlConnection = null;

            try{
                //textView.setText("Loading....");
                textView.setVisibility(View.INVISIBLE);

                url = new URL(urls[0]);

                urlConnection = (HttpURLConnection)url.openConnection();

                InputStream in = urlConnection.getInputStream();

                InputStreamReader reader = new InputStreamReader(in);

                int data = reader.read();

                int x=1000;
                for(int i=0;i<8400;i++){
                    data = reader.read();
                }

                for(int i=0;i<x;i++){
                    char current = (char)data;
                    result1 += current;
                    data = reader.read();
                }

                String[] splitResult1 = result1.split("<tr class=\"lightrow\">");
                String check1=splitResult1[1];
                String[] splitResult2 = check1.split("<td class=\"text-center\">");
                String check2=splitResult2[1];
                String[] splitResult3 = check2.split("</td>");

                result1=splitResult3[0];
                return result1;

            } catch (Exception e){
                Log.i("Info", "1");
                //Toast.makeText(getApplicationContext(),"Could not find problem :(",Toast.LENGTH_SHORT).show();
                flag=false;
                return null;
            }
        }

        @Override
        protected void onPostExecute(String s) {

            try {
                if (flag){
                    DecimalFormat df = new DecimalFormat("#.####");
                    df.setRoundingMode(RoundingMode.CEILING);
                    int i=Integer.parseInt(result1);
                    i=40+i;
                    double d=1.0*80/i;
                    textView.setText(df.format(d));
                    textView.setVisibility(View.VISIBLE);
                }
                else{
                    textView.setText("Sorry No problem was found!");
                    textView.setVisibility(View.VISIBLE);
                }
            } catch (Exception e){
                e.printStackTrace();
                Log.i("Info", "2");
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void buttonPress(View view){

        editText = findViewById(R.id.editText);
        textView = findViewById(R.id.textView);
        textView1 = findViewById(R.id.textView1);


        try{

            DownloadTask task = new DownloadTask();

            String problemCode = URLEncoder.encode(editText.getText().toString(), "UTF-8");

            InputMethodManager mgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            mgr.hideSoftInputFromWindow(editText.getWindowToken(), 0);

            String result = null;

            problemCode=problemCode.toUpperCase();
            textView1.setText("Probem :" + problemCode);
            textView1.setVisibility(View.VISIBLE);
            result = task.execute("https://www.spoj.com/ranks/" + problemCode +"/").get();

            //Log.i("Info2", result);

        } catch (Exception e){
            Toast.makeText(getApplicationContext(),"Could not find problem :(",Toast.LENGTH_SHORT).show();
        }
    }
}
