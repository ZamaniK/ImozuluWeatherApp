package com.example.imozuluweatherapp;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class weatherData {

    private String mTemperature,micon,mcity,mWeatherType,updatedAtText,temp,tempMin,tempMax,pressure,humidity,windSpeed,weatherDescription,address;
    private int mCondition;
    private Long sunrise,sunset;

    public static weatherData fromJson(JSONObject jsonObject)
    {

        try
        {

            JSONObject main = jsonObject.getJSONObject("main");
            JSONObject sys = jsonObject.getJSONObject("sys");
            JSONObject wind = jsonObject.getJSONObject("wind");

            weatherData weatherD = new weatherData();
            weatherD.mcity=jsonObject.getString("name");
            weatherD.mCondition=jsonObject.getJSONArray("weather").getJSONObject(0).getInt("id");
            weatherD.mWeatherType=jsonObject.getJSONArray("weather").getJSONObject(0).getString("main");
            weatherD.micon=updateWeatherIcon(weatherD.mCondition);
            double tempResult=jsonObject.getJSONObject("main").getDouble("temp")-273.15;
            int roundedValue=(int)Math.rint(tempResult);
            weatherD.mTemperature=Integer.toString(roundedValue);
            Long updatedAt = jsonObject.getLong("dt");

//            weatherD.updatedAtText = "Updated at: " + new SimpleDateFormat("dd/MM/yyyy hh:mm a", Locale.ENGLISH).format(new Date(updatedAt * 1000));
//            weatherD.temp = jsonObject.getString("temp") + "°C";
//            weatherD.tempMin = "Min Temp: " + jsonObject.getString("temp_min") + "°C";
//
            double tempMaxResult=jsonObject.getJSONObject("main").getDouble("temp_max")-273.15;
            int roundedMaxValue=(int)Math.rint(tempMaxResult);
            weatherD.tempMax = Integer.toString(roundedMaxValue);

            double tempMinResult=jsonObject.getJSONObject("main").getDouble("temp_min")-273.15;
            int roundedMinValue=(int)Math.rint(tempMinResult);
            weatherD.tempMin = Integer.toString(roundedMinValue);
//           weatherD.pressure = jsonObject.getJSONArray("weather").getString(Integer.parseInt("pressure")).toString();
//            weatherD.humidity = jsonObject.getString("humidity");
            weatherD.sunrise = sys.getLong("sunrise");
            weatherD.sunset = sys.getLong("sunset");
            weatherD.windSpeed = wind.getString("speed");
//            weatherD.weatherDescription = jsonObject.getString("description");
//            weatherD.address = jsonObject.getString("name") + ", " + sys.getString("country");

            return weatherD;
        }


        catch (JSONException e) {
            e.printStackTrace();
            return null;
        }


    }


    private static String updateWeatherIcon(int condition)
    {
        if(condition>=0 && condition<=300)
        {
            return "thunderstrom1";
        }
        else if(condition>=300 && condition<=500)
        {
            return "lightrain";
        }
        else if(condition>=500 && condition<=600)
        {
            return "shower";
        }
        else  if(condition>=600 && condition<=700)
        {
            return "snow2";
        }
        else if(condition>=701 && condition<=771)
        {
            return "fog";
        }

        else if(condition>=772 && condition<=800)
        {
            return "overcast";
        }
        else if(condition==800)
        {
            return "sunny";
        }
        else if(condition>=801 && condition<=804)
        {
            return "cloudy";
        }
        else  if(condition>=900 && condition<=902)
        {
            return "thunderstrom1";
        }
        if(condition==903)
        {
            return "snow1";
        }
        if(condition==904)
        {
            return "sunny";
        }
        if(condition>=905 && condition<=1000)
        {
            return "thunderstrom2";
        }

        return "dunno";


    }

    public String getmTemperature() {
        return mTemperature+"°C";
    }

    public String getMicon() {
        return micon;
    }

    public String getMcity() {
        return mcity;
    }

    public String getmWeatherType() {
        return mWeatherType;
    }


    public String getUpdatedAtText() {
        return updatedAtText;
    }


    public String getTemp() {
        return temp;
    }



    public String getTempMin() {
        return tempMin;
    }



    public String getTempMax() {
        return tempMax;
    }



    public String getPressure() {
        return pressure;
    }



    public String getHumidity() {
        return humidity;
    }


    public String getWindSpeed() {
        return windSpeed;
    }



    public String getWeatherDescription() {
        return weatherDescription;
    }


    public String getAddress() {
        return address;
    }



    public int getmCondition() {
        return mCondition;
    }



    public Long getSunrise() {
        return sunrise;
    }


    public Long getSunset() {
        return sunset;
    }


}
