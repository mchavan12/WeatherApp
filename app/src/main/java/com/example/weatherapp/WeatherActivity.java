package com.example.weatherapp;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.squareup.picasso.Picasso;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WeatherActivity extends AppCompatActivity {

    private TextView cityW, temperatureW, weatherConditionW, humidityW, maxTemperatureW, minTemperatureW, pressureW, windW;
    private ImageView imageViewW;
    private EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);

        cityW=findViewById(R.id.textViewCity);
        temperatureW=findViewById(R.id.textViewTemp);
        weatherConditionW=findViewById(R.id.textViewWeatherCondition);
        humidityW=findViewById(R.id.Humidity);
        maxTemperatureW=findViewById(R.id.MaxTemp);
        minTemperatureW=findViewById(R.id.MinTemp);
        pressureW=findViewById(R.id.Pressure);
        windW=findViewById(R.id.Wind);
        imageViewW=findViewById(R.id.myimage);
        FloatingActionButton fabs = findViewById(R.id.fab);
        Button search = findViewById(R.id.search);
        editText = findViewById(R.id.editTextCity);

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                String cityName = editText.getText().toString();
                getWeatherData(cityName);
                if (editText.length() == 0) {
                    Toast.makeText(WeatherActivity.this, "Please enter a city.", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
    public void getWeatherData(String name)
    {
        WeatherAPI weatherAPI = RetrofitWeather.getClient().create(WeatherAPI.class);
        Call<OpenWeatherMap> call = weatherAPI.getWeatherWithCityName(name);
        call.enqueue(new Callback<OpenWeatherMap>() {
            @Override
            public void onResponse(@NonNull Call<OpenWeatherMap> call, @NonNull Response<OpenWeatherMap> response) {

                if(response.isSuccessful())
                {
                    assert response.body() != null;
                    cityW.setText(response.body().getName() + ", " + response.body().getSys().getCountry());
                    temperatureW.setText(response.body().getMain().getTemp() + " °C");
                    weatherConditionW.setText(response.body().getWeather().get(0).getDescription());
                    humidityW.setText(" : " + response.body().getMain().getHumidity() + "%");
                    maxTemperatureW.setText(" : " + response.body().getMain().getTempMax() + " °C");
                    minTemperatureW.setText(" : " + response.body().getMain().getTempMin() + " °C");
                    pressureW.setText(" : " + response.body().getMain().getPressure());
                    windW.setText(" : " + response.body().getWind().getSpeed());

                    String iconCode = response.body().getWeather().get(0).getIcon();
                    Picasso.get().load("https://openweathermap.org/img/wn/" + iconCode + "@2x.png")
                            .placeholder(R.drawable.ic_launcher_background)
                            .into(imageViewW);
                }
                else
                {
                    Toast.makeText(WeatherActivity.this,"City not found, please try again.",Toast.LENGTH_LONG).show();
                }

            }

            @Override
            public void onFailure(@NonNull Call<OpenWeatherMap> call, @NonNull Throwable t) {

            }
        });
    }
}