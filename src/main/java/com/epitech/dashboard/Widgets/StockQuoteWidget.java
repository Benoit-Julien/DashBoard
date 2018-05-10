package com.epitech.dashboard.Widgets;

import com.epitech.dashboard.Widget;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

public class StockQuoteWidget extends AWidget {

    public StockQuoteWidget()
    {
        super("Stock Quote");

        final String STW = "KO";
        try {
            URL url = new URL("https://api.iextrading.com/1.0/stock/aapl/book");
            URLConnection urlconnect = url.openConnection();
            InputStreamReader inputStream = new InputStreamReader(urlconnect.getInputStream());
            BufferedReader buffer = new BufferedReader(inputStream);

            String data = buffer.readLine();
            System.out.println(data);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public StockQuoteWidget(AWidget source)
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
