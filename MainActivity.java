package com.example.user.weatherhelp;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ListView list;
    EditText city;
    private ArrayList<String> cities = new ArrayList<String>();
    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        list = findViewById(R.id.list);
        city = findViewById(R.id.city);
        int[] cities_start = getResources().getIntArray(R.array.cities_start);
        APITask task = new APITask();
        task.execute(cities_start);
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, cities);
        list.setAdapter(adapter);
    }

    class APITask extends AsyncTask<int[], Integer, Void>{
        public Weather getTemp(int id){
            String API_KEY = "d79091018cda7ef56338f8756ec7a36a";
            String sampleURL = "https://api.openweathermap.org/data/2.5/weather?id=" + String.valueOf(id) + "&apikey=" + API_KEY;
            try{
                URL url = new URL(sampleURL);
                InputStream stream = (InputStream) url.getContent();
                Gson gson = new Gson();
                Weather weather = gson.fromJson(new InputStreamReader(stream), Weather.class);
                return weather;
            } catch (IOException e) {
                return new Weather();
            }
        }

        @Override
        protected Void doInBackground(int[]... citys) {
            for (int id: citys[0]){
                Weather weather = getTemp(id);
                if (weather != null){
                    String st = "City ID " + String.valueOf(id) + " Temp " + String.valueOf((int)weather.main.temp - 273);
                    cities.add(st);
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
        }
    }

    public void onClick(View v){
        if (v.getId() == R.id.add){
            APITask task = new APITask();
            task.execute(new int[]{Integer.parseInt(city.getText().toString())});
        }
        if (v.getId() == R.id.del){
            adapter.clear();
        }
    }
}
