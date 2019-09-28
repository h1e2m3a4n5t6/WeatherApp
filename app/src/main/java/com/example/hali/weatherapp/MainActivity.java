package com.example.hali.weatherapp;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

// testing for changes in this file
public class MainActivity extends AppCompatActivity {


    public class getJSONQuery extends AsyncTask < String , Void , String >
    {

        @Override
        protected String doInBackground(String... params) {
            StringBuilder stringBuilder ;
            try {
                URL url = new URL(params[0]);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                try {
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                    stringBuilder = new StringBuilder();
                    String line;
                    while ((line = bufferedReader.readLine()) != null) {

                        stringBuilder.append(line).append("\n");
                    }
                    bufferedReader.close();
                    Log.i("The Code 1 is ",stringBuilder.toString());
                }
                finally{
                    urlConnection.disconnect();
                }
            }
            catch(Exception e) {
                Log.e("ERROR", e.getMessage(), e);

                return "Error in Fetching the Query";
            }
            return stringBuilder.toString();
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String res =" ";
        getJSONQuery DT = new getJSONQuery();
        try {

            res = DT.execute("https://query.yahooapis.com/v1/public/yql?q=select%20*%20from%20weather.forecast%20where%20woeid%20in%20(select%20woeid%20from%20geo.places(1)%20where%20text%3D%22nome%2C%20ak%22)&format=json&env=store%3A%2F%2Fdatatables.org%2Falltableswithkeys").get();
            Log.i("Fetched JSONQuery is ",res);

            JSONObject jsonObject = new JSONObject(res);

            JSONObject query = jsonObject.getJSONObject("query");
            Log.i("Query is ",query.toString());

            JSONObject results = query.getJSONObject("results");
            Log.i("Query -> result is ",results.toString());

            JSONObject channel = results.getJSONObject("channel");
            Log.i("result -> channel is ",channel.toString());

            JSONObject items = channel.getJSONObject("item");
            Log.i("items is ",items.toString());

            String forecast = items.getString("forecast");
            Log.i("forecast is ",forecast);

            JSONArray forecastarray = new JSONArray(forecast);
            for(int i=0;i<forecastarray.length();i++)
            {
                JSONObject obj = forecastarray.getJSONObject(i);
                Log.i("forecast ["+i+"]",obj.toString());
            }

        }
        catch (Exception e)
        {
            Log.i("Error","Error");
            e.getStackTrace();
        }


    }
}
