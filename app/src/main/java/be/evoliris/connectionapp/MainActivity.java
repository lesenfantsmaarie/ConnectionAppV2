package be.evoliris.connectionapp;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

import be.evoliris.connectionapp.model.Weather;
import be.evoliris.connectionapp.model.WeatherObject;
import be.evoliris.connectionapp.task.GetAsyncTask;
import be.evoliris.connectionapp.task.GetImgAsyncTask;

public class MainActivity extends AppCompatActivity implements GetAsyncTask.GetAsyncTaskCallback {

    private Button btnMainRequest;
    private ProgressBar pbMainLoading;
    private TextView tvMainResult;
    private EditText et_main_name;
    private TextView tv_main_description;
    private ImageView iv_main_img;
    private TextView tv_main_humidity;
    private TextView tv_main_temperature;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnMainRequest = (Button) findViewById(R.id.btn_main_request);
        pbMainLoading = (ProgressBar) findViewById(R.id.pb_main_loading);
        tvMainResult = (TextView) findViewById(R.id.tv_main_result);
        et_main_name = (EditText) findViewById(R.id.et_main_name);
        tv_main_description = (TextView) findViewById(R.id.tv_main_description);
        iv_main_img = (ImageView) findViewById(R.id.iv_main_img);
        tv_main_humidity = (TextView) findViewById(R.id.tv_main_humidity);
        tv_main_temperature = (TextView) findViewById(R.id.tv_main_temperature);

        btnMainRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ConnectivityManager cm = (ConnectivityManager)
                        getSystemService(Context.CONNECTIVITY_SERVICE);

                NetworkInfo activeNetwork = cm.getActiveNetworkInfo();

                boolean isConnected = activeNetwork != null &&
                        activeNetwork.isConnectedOrConnecting();

                if (isConnected) {
                    GetAsyncTask task = new GetAsyncTask(MainActivity.this);
                    task.execute("http://api.openweathermap.org/data/2.5/weather?q=" + et_main_name.getText().toString() + "&units=metric&appid=36158c42f86471604ca5584a83ed5541");
                } else {
                    Toast.makeText(MainActivity.this, "No connection !",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onPreGet() {
        tvMainResult.setVisibility(View.GONE);
        pbMainLoading.setVisibility(View.VISIBLE);
    }

    @Override
    public void onPostGet(String s) {

        Gson gson = new Gson();
        WeatherObject wo = gson.fromJson(s, WeatherObject.class);

        GetImgAsyncTask getImgAsyncTask = new GetImgAsyncTask(iv_main_img);
        getImgAsyncTask.execute(wo.getWeather().get(0).getIcon());

        tvMainResult.setText(wo.getName());
        tvMainResult.setVisibility(View.VISIBLE);
        tv_main_description.setText(wo.getWeather().get(0).getDescription() + " " + wo.getWeather().get(0).getMain());
        tv_main_humidity.setText(String.valueOf(wo.getMain().getHumidity()) + " % humidity");
        tv_main_temperature.setText(String.valueOf(wo.getMain().getTemp()) + " °C");
        pbMainLoading.setVisibility(View.GONE);
    }
}
