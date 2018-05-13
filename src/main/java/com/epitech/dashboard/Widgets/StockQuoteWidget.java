package com.epitech.dashboard.Widgets;

import com.epitech.dashboard.Widget;
import com.epitech.dashboard.Quote.QuoteDataInfo;
import com.epitech.dashboard.Quote.QuoteDataWidgetLayout;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vaadin.ui.TextField;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class StockQuoteWidget extends AWidget {

    private TextField request_symbol = new TextField("Symbole de l'entreprise : ");

    private QuoteDataInfo info = new QuoteDataInfo();

    private QuoteDataWidgetLayout widget = new QuoteDataWidgetLayout();

    public StockQuoteWidget() {
        formContent.addComponent(request_symbol);
    }

    @Override
    public boolean refresh() {

        String symbol = null;
        String companyName = null;
        String open = null;
        String close = null;
        String high = null;
        String low = null;
        String latestTime = null;
        String latestVolume = null;

        if (info.getSymbol_request() == null)
            return false;

        try {
            URL url = new URL("https://api.iextrading.com/1.0/stock/" + info.getSymbol_request() + "/book");
            URLConnection urlconnect = url.openConnection();
            InputStreamReader inputStream = new InputStreamReader(urlconnect.getInputStream());
            BufferedReader buffer = new BufferedReader(inputStream);

            String data = buffer.readLine();

            // parse all data and quote data only
            JSONParser parser_all = new JSONParser();
            JSONObject all_quote_data = (JSONObject) parser_all.parse(data);
            String quote = all_quote_data.get("quote").toString();
            JSONParser parser_quote = new JSONParser();
            JSONObject quote_data = (JSONObject) parser_quote.parse(quote);

            /// data manager
            symbol = quote_data.get("symbol").toString();
            companyName = quote_data.get("companyName").toString();
            open = quote_data.get("open").toString();
            close = quote_data.get("close").toString();
            high = quote_data.get("high").toString();
            low = quote_data.get("low").toString();
            latestTime = quote_data.get("latestTime").toString();
            latestVolume = quote_data.get("latestVolume").toString();
        }
        catch (IOException e) {
            e.printStackTrace();
            return false;
        } catch (ParseException e) {
            e.printStackTrace();
            return false;
        }

        widget.getSymbol().setValue("Symbole de l'entreprise: " + symbol);
        widget.getCompanyName().setValue("Nom de l'entreprise: " + companyName);
        widget.getOpen().setValue("Ouverture : " + open);
        widget.getClose().setValue("Clôture : " + close);
        widget.getHigh().setValue("+Haut : " + high);
        widget.getLow().setValue("+Bas : " + low);
        widget.getLatestTime().setValue("Dernière actualisation : " + latestTime);
        widget.getLatestVolume().setValue("Volume : " + latestVolume);

        return true;
    }

    @Override
    public void loadFromData(Widget source) {
        ObjectMapper mapper = new ObjectMapper();
        info = mapper.convertValue(source.getInstance(), QuoteDataInfo.class);
        submitted();
    }

    @Override
    public Widget SaveWidget() {
        Widget save = new Widget();
        save.setInstance(info);
        save.setType(this.getClass().getName());
        return save;
    }

    @Override
    public boolean submitted() {
        if (info.getSymbol_request() == null) {
            if (request_symbol.isEmpty())
                return false;
            info.setSymbol_request(request_symbol.getValue());
        }
        mainDisplay = widget;
        return refresh();
    }
}
