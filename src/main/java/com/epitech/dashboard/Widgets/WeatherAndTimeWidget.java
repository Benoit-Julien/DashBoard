package com.epitech.dashboard.Widgets;

import com.epitech.dashboard.WeatherTime.WeatherTimeInfo;
import com.epitech.dashboard.WeatherTime.WeatherTimeWidgetLayout;
import com.epitech.dashboard.Widget;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.maps.GeoApiContext;
import com.google.maps.TimeZoneApi;
import com.google.maps.model.LatLng;
import com.vaadin.data.provider.ListDataProvider;
import com.vaadin.server.ExternalResource;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.TextField;
import net.aksingh.owmjapis.api.APIException;
import net.aksingh.owmjapis.core.OWM;
import net.aksingh.owmjapis.model.CurrentWeather;

import java.util.Arrays;
import java.util.Calendar;
import java.util.TimeZone;

public class WeatherAndTimeWidget extends AWidget {
    private static final String GOOGLE_APPID = "AIzaSyDNNiVF69c_TfUPTBk598pu_E-q4RsZn4g";
    private static final String OWM_APPID = "760df4191f118ae3e6155d29c549ab90";

    private OWM owm = new OWM(OWM_APPID);
    private GeoApiContext context = new GeoApiContext.Builder()
            .apiKey(GOOGLE_APPID)
            .build();

    private ComboBox<OWM.Country> countrySelect = new ComboBox<>("Sélectionner le pays : ");
    private TextField cityNameField = new TextField("Entrer le nom de la ville : ");

    private WeatherTimeWidgetLayout widget = new WeatherTimeWidgetLayout();

    private WeatherTimeInfo infos = new WeatherTimeInfo();

    public WeatherAndTimeWidget() {
        owm.setUnit(OWM.Unit.METRIC);

        countrySelect.setDataProvider(new ListDataProvider<>(Arrays.asList(OWM.Country.values())));

        formContent.addComponent(countrySelect);
        formContent.addComponent(cityNameField);
    }

    @Override
    public boolean refresh() {
        CurrentWeather cwd = null;
        try {
            cwd = owm.currentWeatherByCityName(infos.getCityName(), infos.getCountry());
        } catch (APIException e) {
            e.printStackTrace();
            return false;
        }

        widget.getCity().setValue(cwd.getCityName());
        widget.getImage().setSource(new ExternalResource(cwd.getWeatherList().get(0).getIconLink()));
        widget.getTemperature().setValue(cwd.getMainData().getTemp() + " °C");
        widget.getWind().setValue((int) (cwd.getWindData().getSpeed() * 3.6) + " km/h");

        TimeZone timeZone = null;
        try {
            timeZone = TimeZoneApi.getTimeZone(context,
                    new LatLng(cwd.getCoordData().component1(),
                            cwd.getCoordData().component2())).await();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        Calendar time = Calendar.getInstance(timeZone);
        widget.getClock().setValue(time.get(Calendar.HOUR_OF_DAY) + "h" + time.get(Calendar.MINUTE));
        return true;
    }

    @Override
    public void loadFromData(Widget source) {
        ObjectMapper mapper = new ObjectMapper();
        infos = mapper.convertValue(source.getInstance(), WeatherTimeInfo.class);
        submitted();
    }

    @Override
    public Widget SaveWidget() {
        Widget save = new Widget();
        save.setInstance(infos);
        save.setType(this.getClass().getName());
        return save;
    }

    @Override
    public boolean submitted() {
        if (infos.getCityName() == null || infos.getCountry() == null) {
            if (cityNameField.isEmpty() || countrySelect.isEmpty())
                return false;
            infos.setCountry(countrySelect.getValue());
            infos.setCityName(cityNameField.getValue());
        }

        mainDisplay = widget;
        return refresh();
    }
}
