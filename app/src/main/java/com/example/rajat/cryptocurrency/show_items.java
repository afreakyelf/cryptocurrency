package com.example.rajat.cryptocurrency;


public class show_items {
        private String TIME,HIGHDAY,LOWDAY,NAME,PRICE,DATE;  //put this name same as Database Fields

        public show_items(String times, String names , String highs,String lows, String prices , String dates) {
            TIME=times;
            NAME = names;
            HIGHDAY = highs;
            LOWDAY = lows;
            PRICE = prices;
            DATE = dates;


        }
    public show_items()
    {
        //Empty Constructor Needed
    }

    public String getDATE() {
        return DATE;
    }

    public void setDATE(String DATE) {
        this.DATE = DATE;
    }

    public String getTIME() {
        return TIME;
    }

    public void setTIME(String TIME) {
        this.TIME = TIME;
    }

    public String getHIGHDAY() {
        return HIGHDAY;
    }

    public void setHIGHDAY(String HIGHDAY) {
        this.HIGHDAY = HIGHDAY;
    }

    public String getLOWDAY() {
        return LOWDAY;
    }

    public void setLOWDAY(String LOWDAY) {
        this.LOWDAY = LOWDAY;
    }

    public String getNAME() {
        return NAME;
    }

    public void setNAME(String NAME) {
        this.NAME = NAME;
    }

    public String getPRICE() {
        return PRICE;
    }

    public void setPRICE(String PRICE) {
        this.PRICE = PRICE;
    }
}

