package com.example.khatri.weather.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.khatri.weather.R;
import com.example.khatri.weather.entity.WeatherEntity;
import com.example.khatri.weather.utils.Constants;
import com.example.khatri.weather.utils.Utility;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;

/**
 * Created by Khatri on 8/2/2015.
 */
public class WeatherAdapter extends BaseAdapter {
    ArrayList<WeatherEntity> mWeatherData;
    Context mContext;
    LayoutInflater inflater;

    public WeatherAdapter(Context context,ArrayList<WeatherEntity> weatherData){
        mContext = context;
        mWeatherData = weatherData;
        inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return mWeatherData.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        ViewHolder holder;

        if (view == null) {
            holder = new ViewHolder();
            view = inflater.inflate(R.layout.list_weather_item, null);
            holder.ivDay = (ImageView) view.findViewById(R.id.imageview_day);
            holder.tvDay = (TextView) view.findViewById(R.id.textview_day);
            holder.tvMaxTemp = (TextView) view.findViewById(R.id.textview_max_temp);
            holder.tvMinTemp = (TextView) view.findViewById(R.id.textview_min_temp);
            holder.tvDayTemp = (TextView) view.findViewById(R.id.textview_day_temp);
            holder.tvMornTemp = (TextView) view.findViewById(R.id.textview_morn_temp);
            holder.tvEveTemp = (TextView) view.findViewById(R.id.textview_eve_temp);
            holder.tvNightTemp = (TextView) view.findViewById(R.id.textview_night_temp);
            holder.tvPressure = (TextView) view.findViewById(R.id.textview_pressure);
            holder.tvHumidity = (TextView) view.findViewById(R.id.textview_humidity);
            holder.tvWindSpeed = (TextView) view.findViewById(R.id.textview_windspeed);
            holder.tvWindDir = (TextView) view.findViewById(R.id.textview_winddir);
            holder.tvCloudiness = (TextView) view.findViewById(R.id.textview_cloudiness);
            holder.tvRain = (TextView) view.findViewById(R.id.textview_rain);
            holder.tvSnow = (TextView) view.findViewById(R.id.textview_snow);
            view.setTag(holder);
        } else {
            holder = (ViewHolder)view.getTag();
        }

        try {
            WeatherEntity entity = mWeatherData.get(position);

            JSONObject temperature = entity.getTemperature();
            Double maxTemp = temperature.getDouble("max") - 273.5;
            String maxTempString = new DecimalFormat("##.##").format(maxTemp);
            Double minTemp = temperature.getDouble("min") - 273.5;
            String minTempString = new DecimalFormat("##.##").format(minTemp);
            Double dayTemp = temperature.getDouble("day") - 273.5;
            String dayTempString = new DecimalFormat("##.##").format(dayTemp);
            Double mornTemp = temperature.getDouble("morn") - 273.5;
            String mornTempString = new DecimalFormat("##.##").format(mornTemp);
            Double eveTemp = temperature.getDouble("eve") - 273.5;
            String eveTempString = new DecimalFormat("##.##").format(eveTemp);
            Double nightTemp = temperature.getDouble("night") - 273.5;
            String nightTempString = new DecimalFormat("##.##").format(nightTemp);

            JSONObject weatherInfo = entity.getWeatherInfo();
            String description = weatherInfo.getString("description");
            String iconId = weatherInfo.getString("icon");

            holder.tvDay.setText(Utility.getDate(entity.getTimestamp()) + " (" + description + ")");
            holder.tvMaxTemp.setText("Maximum: " + maxTempString);
            holder.tvMinTemp.setText("Minimum: " + minTempString);
            holder.tvDayTemp.setText("Day: " + dayTempString);
            holder.tvMornTemp.setText("Minimum: " + mornTempString);
            holder.tvEveTemp.setText("Evening: " + eveTempString);
            holder.tvNightTemp.setText("Night: " + nightTempString);
            holder.tvPressure.setText("Pressure: " + entity.getPressure() + " hPa");
            holder.tvHumidity.setText("Humidity: " + entity.getHumidity() + " %");
            holder.tvWindSpeed.setText("Wind Speed: " + entity.getWindSpeed() + " mps");
            holder.tvWindDir.setText("Wind Direction: " + entity.getWindDirDegrees() + " degrees");
            holder.tvCloudiness.setText("Cloudiness: " + entity.getCloudPercentage() + " %");

            Double rain = entity.getRain();
            if(rain == 0) {
                holder.tvRain.setVisibility(View.GONE);
            } else {
                holder.tvRain.setText("Precipation (last 3h): " + rain);
            }
            Double snow = entity.getSnow();
            if(snow == 0) {
                holder.tvSnow.setVisibility(View.GONE);
            } else {
                holder.tvSnow.setText("Snow (last 3h): " + snow);
            }

            String weatherIconUrl = Constants.WEATHER_ICON_URL + iconId + ".png";
            ImageLoader.getInstance().displayImage(weatherIconUrl, holder.ivDay);

        } catch (JSONException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return view;
    }

    private static class ViewHolder {
        ImageView ivDay;
        TextView tvDay;
        TextView tvMaxTemp;
        TextView tvMinTemp;
        TextView tvDayTemp;
        TextView tvMornTemp;
        TextView tvEveTemp;
        TextView tvNightTemp;
        TextView tvPressure;
        TextView tvHumidity;
        TextView tvWindSpeed;
        TextView tvWindDir;
        TextView tvCloudiness;
        TextView tvRain;
        TextView tvSnow;
    }
}
