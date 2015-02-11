package com.example.khatri.weather.ui;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.khatri.weather.R;
import com.example.khatri.weather.adapter.WeatherAdapter;
import com.example.khatri.weather.entity.WeatherEntity;
import com.example.khatri.weather.utils.Constants;
import com.example.khatri.weather.utils.GPSTracker;
import com.example.khatri.weather.utils.Utility;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by Khatri on 8/2/2015.
 */
public class HomeActivity extends ActionBarActivity implements View.OnClickListener {

    private EditText mEditTextSearch;
    private Button mButtonSearch;
    private TextView mTextViewCityName;
    private ListView mListViewDays;
    private ArrayList<WeatherEntity> mWeatherData = new ArrayList<WeatherEntity>();
    private WeatherAdapter mWeatherAdapter;
    private Geocoder mGeocoder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // register all the views and listeners
        registerViews();
        registerListeners();

        // Get current location and fetch its weather
        String currentCity = getCurrentLocation();
        if(currentCity != null && !TextUtils.isEmpty(currentCity)) {
            getCityWeather(currentCity);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_search:
                Utility.hideKeyboard(HomeActivity.this, mEditTextSearch);
                getCityWeather(mEditTextSearch.getText().toString());
                break;
            default:
                break;
        }
    }

    private void registerViews() {
        mEditTextSearch = (EditText) findViewById(R.id.edittext_search);
        mButtonSearch = (Button) findViewById(R.id.button_search);
        mTextViewCityName = (TextView) findViewById(R.id.city_name);
        mListViewDays = (ListView) findViewById(R.id.listview_days);
    }

    private void registerListeners() {
        mButtonSearch.setOnClickListener(this);
        mEditTextSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                int length = mEditTextSearch.getText().toString().length();
                if(length >= 1) {
                    mButtonSearch.setEnabled(true);
                } else {
                    mButtonSearch.setEnabled(false);
                }
            }
        });

        mWeatherAdapter = new WeatherAdapter(HomeActivity.this, mWeatherData);
        mListViewDays.setAdapter(mWeatherAdapter);

    }

    private String getCurrentLocation() {
        String currentAddress = "";
        GPSTracker gps = new GPSTracker(HomeActivity.this);
        // get location if gps is enabled
        if(gps.canGetLocation()) {
            Double latitude = gps.getLatitude();
            Double longitude = gps.getLongitude();
            mGeocoder = new Geocoder(HomeActivity.this, Locale.getDefault());
            try {
                String city = "", country = "";
                List<Address> addresses = mGeocoder.getFromLocation(latitude, longitude, 1);
                if(addresses.size() > 0) {
                    city = addresses.get(0).getAddressLine(2);
                    city = city.substring(0, city.indexOf(","));
                    currentAddress = currentAddress + city;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return currentAddress;
    }

    private void getCityWeather(String cityName) {
        String finalUrl = Constants.WEATHER_API_URL_PREFIX + cityName + Constants.WEATHER_API_URL_SUFFIX;

        JsonObjectRequest cityWeatherRequest = new JsonObjectRequest(Request.Method.GET, finalUrl, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        System.out.println("weather response: " + response);
                        extractWeatherData(response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });

        WeatherApplication.getInstance().addToRequestQueue(cityWeatherRequest);
    }

    private void extractWeatherData(JSONObject response) {
        mWeatherData.clear();
        try {
            JSONObject cityJson = response.getJSONObject("city");
            mTextViewCityName.setText(cityJson.getString("name") + " Weather");

            JSONArray daysList = response.getJSONArray("list");
            int length = daysList.length();
            for(int i=0; i<length; i++) {
                WeatherEntity entity = new WeatherEntity();
                JSONObject dayJson = daysList.getJSONObject(i);
                entity.setTimestamp(dayJson.getLong("dt"));
                entity.setTemperature(dayJson.getJSONObject("temp"));
                entity.setPressure(dayJson.getDouble("pressure"));
                entity.setHumidity(dayJson.getInt("humidity"));
                entity.setWeatherInfo(dayJson.getJSONArray("weather").getJSONObject(0));
                entity.setWindSpeed(dayJson.getDouble("speed"));
                entity.setWindDirDegrees(dayJson.getInt("deg"));
                if(dayJson.has("clouds")) {
                    entity.setCloudPercentage(dayJson.getInt("clouds"));
                } else {
                    entity.setCloudPercentage(0);
                }
                if(dayJson.has("rain")) {
                    entity.setRain(dayJson.getDouble("rain"));
                } else {
                    entity.setRain(0);
                }
                if(dayJson.has("snow")) {
                    entity.setSnow(dayJson.getDouble("snow"));
                } else {
                    entity.setSnow(0);
                }

                mWeatherData.add(entity);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        mWeatherAdapter.notifyDataSetChanged();
    }
}
