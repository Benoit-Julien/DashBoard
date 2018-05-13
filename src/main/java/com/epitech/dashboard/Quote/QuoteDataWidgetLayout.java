package com.epitech.dashboard.Quote;

import com.vaadin.ui.Label;

public class QuoteDataWidgetLayout extends QuoteDataLayout {

    public Label getVide() {
        return vide;
    }

    public Label getSymbol() {
        return symbol;
    }

    public Label getCompanyName() {
        return companyName;
    }

    public Label getOpen() {
        return open;
    }

    public Label getClose() {
        return close;
    }

    public Label getHigh() {
        return high;
    }

    public Label getLow() {
        return low;
    }

    public Label getLatestTime() {
        return latestTime;
    }

    public Label getLatestVolume() {
        return latestVolume;
    }

    public QuoteDataWidgetLayout() {
        super();
        verticalLayout.setMargin(false);
    }
}
