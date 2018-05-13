package com.epitech.dashboard.Quote;

public class QuoteDataInfo {
    private String symbol_request;

    public String getSymbol_request() {
        return symbol_request;
    }

    public void setSymbol_request(String _symbol_request) {
        this.symbol_request = _symbol_request;
    }

    @Override
    public String toString() {
        return this.symbol_request;
    }
}
