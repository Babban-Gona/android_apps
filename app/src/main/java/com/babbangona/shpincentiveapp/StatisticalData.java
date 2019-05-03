package com.babbangona.shpincentiveapp;

public class StatisticalData {


    private String statistics_date;
    private String frequency;

// this method created a list of statistical data fed into the payment dashboards
    public StatisticalData(String statistical_date, String frequency) {

        this.statistics_date = statistical_date;
        this.frequency = frequency;


    }

    // statistical date getters
    public String getStatistics_date() {
        return statistics_date;
    }

    // frequency getters
    public String getFrequency() {
        return frequency;
    }




}
