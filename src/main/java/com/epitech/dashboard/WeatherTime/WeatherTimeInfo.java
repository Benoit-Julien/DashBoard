package com.epitech.dashboard.WeatherTime;

import net.aksingh.owmjapis.core.OWM;

public class WeatherTimeInfo {
    private OWM.Country country;
    private String cityName;

    public WeatherTimeInfo() {}

    public WeatherTimeInfo(OWM.Country country, String cityName) {
        this.country = country;
        this.cityName = cityName;
    }

    public OWM.Country getCountry() {
        return country;
    }

    public void setCountry(OWM.Country country) {
        this.country = country;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    @Override
    public String toString() {
        return this.country + "/" + this.cityName;
    }
}
