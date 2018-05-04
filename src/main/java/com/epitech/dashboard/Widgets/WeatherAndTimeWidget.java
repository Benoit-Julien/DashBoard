package com.epitech.dashboard.Widgets;

import com.epitech.dashboard.Widget;
import net.aksingh.owmjapis.api.APIException;
import net.aksingh.owmjapis.core.OWM;
import net.aksingh.owmjapis.model.CurrentWeather;

public class WeatherAndTimeWidget extends AWidget {

    public WeatherAndTimeWidget()
    {
        super("Weather and Time");
        OWM owm = new OWM("760df4191f118ae3e6155d29c549ab90");

        // getting current weather data for the "London" city
        CurrentWeather cwd = null;
        try {
            cwd = owm.currentWeatherByCityName("London");
        } catch (APIException e) {
            e.printStackTrace();
        }

        //printing city name from the retrieved data
        System.out.println("City: " + cwd.getCityName());

        // printing the max./min. temperature
        System.out.println("Temperature: " + cwd.getMainData().getTempMax()
                + "/" + cwd.getMainData().getTempMin() + "\'K");
    }

    public WeatherAndTimeWidget(AWidget source)
    {
        super(source);
        if (source instanceof RSSFeedWidget) {

        }
        else
            throw new IllegalArgumentException(CLONE_ERR);
    }

    @Override
    public void refresh() {

    }

    @Override
    public AWidget clone() {
        return new WeatherAndTimeWidget(this);
    }

    @Override
    public void loadFromData(Widget source) {

    }

    @Override
    public Widget SaveWidget() {
        Widget save = new Widget();
        save.setType(this.getClass().getName());
        return save;
    }

    @Override
    public boolean submitted() {
        return true;
    }
}
