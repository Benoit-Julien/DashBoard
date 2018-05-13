package com.epitech.dashboard.Widgets;

import com.epitech.dashboard.GoogleMap.GoogleMapInfos;
import com.epitech.dashboard.GoogleMap.GoogleMapWidgetLayout;
import com.epitech.dashboard.Widget;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.maps.DirectionsApi;
import com.google.maps.GeoApiContext;
import com.google.maps.model.DirectionsResult;
import com.google.maps.model.DirectionsStep;
import com.google.maps.model.Unit;
import com.vaadin.server.ExternalResource;
import com.vaadin.ui.TextField;

import java.net.MalformedURLException;
import java.net.URL;

public class GoogleMapWidget extends AWidget {
    private static final String GOOGLE_APPID = "AIzaSyDNNiVF69c_TfUPTBk598pu_E-q4RsZn4g";
    private GeoApiContext context = new GeoApiContext.Builder()
            .apiKey(GOOGLE_APPID)
            .build();

    private TextField originField = new TextField("Origine : ");
    private TextField destinationField = new TextField("Destination : ");

    private GoogleMapInfos infos = new GoogleMapInfos();

    private GoogleMapWidgetLayout widget = new GoogleMapWidgetLayout();

    public GoogleMapWidget() {
        formContent.addComponent(originField);
        formContent.addComponent(destinationField);
    }

    @Override
    public boolean refresh() {
        DirectionsResult directionsResult = null;
        try {
            directionsResult = DirectionsApi.getDirections(context, infos.getOrigin(), infos.getDestination()).units(Unit.METRIC).await();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        String url = "https://maps.googleapis.com/maps/api/staticmap?size=600x300";
        url += "&key=" + GOOGLE_APPID;
        url += "&path=color:0x0000ff";//|weight:5
        for (DirectionsStep step : directionsResult.routes[0].legs[0].steps) {
            url += "|" + step.endLocation.lat + "," + step.endLocation.lng;
        }
        url += "|" + directionsResult.routes[0].legs[0].endLocation.lat + "," + directionsResult.routes[0].legs[0].endLocation.lng;
        url += "&markers=color:red%7Clabel:A%7C" + directionsResult.routes[0].legs[0].startLocation.lat + "," + directionsResult.routes[0].legs[0].startLocation.lng;
        url += "&markers=color:red%7Clabel:B%7C" + directionsResult.routes[0].legs[0].endLocation.lat + "," + directionsResult.routes[0].legs[0].endLocation.lng;
        URL mapurl = null;
        try {
            mapurl = new URL(url);
        } catch (MalformedURLException e) {
            e.printStackTrace();
            System.out.println("ok");
            return false;
        }
        widget.getImage().setSource(new ExternalResource(mapurl));
        widget.getDistance().setValue("Distance : " + directionsResult.routes[0].legs[0].distance.humanReadable);
        widget.getDuration().setValue("Temps de trajet : " + directionsResult.routes[0].legs[0].duration.humanReadable);
        return true;
    }

    @Override
    public void loadFromData(Widget source) {
        ObjectMapper mapper = new ObjectMapper();
        infos = mapper.convertValue(source.getInstance(), GoogleMapInfos.class);
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
        if (infos.getOrigin() == null || infos.getDestination() == null) {
            if (originField.isEmpty() || destinationField.isEmpty())
                return false;
            infos.setOrigin(originField.getValue());
            infos.setDestination(destinationField.getValue());
        }
        mainDisplay = widget;
        return refresh();
    }
}
