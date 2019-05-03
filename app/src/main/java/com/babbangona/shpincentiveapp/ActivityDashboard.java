package com.babbangona.shpincentiveapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ActivityDashboard extends AppCompatActivity {

    BarChart visualization_dashboards;
    TextView viewWeek, viewMonth, viewYear;


    String module, module_id, staff_id, operation;
    SessionManagement sessionManagement;
    ArrayList<String> xLabels;
    Map<String, String> PeriodWeek;
    Map<String, String> PaymentStructure;
    TextView tvOperationPeriod;

    List data;
    private List<StatisticalData> statisticalData;
    private StatisticalData stats;

    /**
     * This class handles the visualisations for 'This Week', 'This Payment Period', 'Since Year Started'
     **/


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        // This block of code retrieves the Staff ID of the logged on user stored in shared preferences
        sessionManagement = new SessionManagement(getApplicationContext());
        HashMap<String, String> controller = sessionManagement.getUserDetails();
        staff_id = controller.get(SessionManagement.KEY_STAFF_ID);
/*
        The retrieval of string extras help the visualisation controller decide what the user is interested in seeing
        The  module ID contains the activity ID of interest
        The module contains the activity name of interest
        The operations contains the boundary of the visualisation whether weekly,periodically or yearly
  */
        module = getIntent().getStringExtra("module");
        module_id = getIntent().getStringExtra("module_id");
        operation = getIntent().getStringExtra("operation");


        // Declaration of the various views in this activity
        visualization_dashboards = findViewById(R.id.activity_dashboard_barchart);
        viewWeek = findViewById(R.id.visualisation_week);
        viewMonth = findViewById(R.id.visualisation_month);
        viewYear = findViewById(R.id.visualisation_year);
        tvOperationPeriod = findViewById(R.id.operation_period);


        /**
         *
         * The block of code below passess the necessary parameters needed by the visualisation controller
         * */


        if (operation.trim().equalsIgnoreCase("week")) {

            viewWeek.setBackgroundColor(getResources().getColor(R.color.colorAccent));
            viewWeek.setTextColor(getResources().getColor(R.color.txt_white_color));
            viewMonth.setBackgroundColor(getResources().getColor(R.color.layout_white));
            viewMonth.setTextColor(getResources().getColor(R.color.txt_black_color));
            viewYear.setBackgroundColor(getResources().getColor(R.color.layout_white));
            viewYear.setTextColor(getResources().getColor(R.color.txt_black_color));

            VisualisationController("week");


        } else if (operation.trim().equalsIgnoreCase("period")) {


            viewMonth.setBackgroundColor(getResources().getColor(R.color.colorAccent));
            viewMonth.setTextColor(getResources().getColor(R.color.txt_white_color));

            viewWeek.setBackgroundColor(getResources().getColor(R.color.layout_white));
            viewWeek.setTextColor(getResources().getColor(R.color.txt_black_color));
            viewYear.setBackgroundColor(getResources().getColor(R.color.layout_white));
            viewYear.setTextColor(getResources().getColor(R.color.txt_black_color));

            VisualisationController("period");


        } else if (operation.trim().equalsIgnoreCase("year")) {


            viewYear.setBackgroundColor(getResources().getColor(R.color.colorAccent));
            viewYear.setTextColor(getResources().getColor(R.color.txt_white_color));

            viewMonth.setBackgroundColor(getResources().getColor(R.color.layout_white));
            viewMonth.setTextColor(getResources().getColor(R.color.txt_black_color));
            viewWeek.setBackgroundColor(getResources().getColor(R.color.layout_white));
            viewWeek.setTextColor(getResources().getColor(R.color.txt_black_color));
            xLabels = new ArrayList<>();
            VisualisationController("year");

        }


        viewWeek.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewWeek.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                viewWeek.setTextColor(getResources().getColor(R.color.txt_white_color));

                viewMonth.setBackgroundColor(getResources().getColor(R.color.layout_white));
                viewMonth.setTextColor(getResources().getColor(R.color.txt_black_color));
                viewYear.setBackgroundColor(getResources().getColor(R.color.layout_white));
                viewYear.setTextColor(getResources().getColor(R.color.txt_black_color));
                xLabels = new ArrayList<>();
                VisualisationController("week");

            }
        });

        viewMonth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                viewMonth.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                viewMonth.setTextColor(getResources().getColor(R.color.txt_white_color));

                viewWeek.setBackgroundColor(getResources().getColor(R.color.layout_white));
                viewWeek.setTextColor(getResources().getColor(R.color.txt_black_color));
                viewYear.setBackgroundColor(getResources().getColor(R.color.layout_white));
                viewYear.setTextColor(getResources().getColor(R.color.txt_black_color));
                xLabels = new ArrayList<>();
                VisualisationController("period");
            }
        });
        viewYear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                viewYear.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                viewYear.setTextColor(getResources().getColor(R.color.txt_white_color));

                viewMonth.setBackgroundColor(getResources().getColor(R.color.layout_white));
                viewMonth.setTextColor(getResources().getColor(R.color.txt_black_color));
                viewWeek.setBackgroundColor(getResources().getColor(R.color.layout_white));
                viewWeek.setTextColor(getResources().getColor(R.color.txt_black_color));
                xLabels = new ArrayList<>();
                VisualisationController("year");

            }
        });


    }

    /*
     * The method below handles the analysis of the visualisation rendered on the Screen
     * This analysis is done on a 'weekly','payment period' and 'yearly' basis
     *
     * */
    private void VisualisationController(String duration) {

        PeriodWeek = new HashMap<>();
        PaymentStructure = new HashMap<>();


        NotesContentProvider.DatabaseHelper notesContentProvider = new NotesContentProvider.DatabaseHelper(getApplicationContext());
        PeriodWeek = notesContentProvider.getPeriodWeek(staff_id);
        PaymentStructure = notesContentProvider.getPaymentStructure(staff_id, module_id);
        Log.d("PaymentStructure", PaymentStructure + "");


        if (PeriodWeek == null) {
            PeriodWeek = new HashMap<>();
            PeriodWeek.put("period_start", "0000-00-00");
            PeriodWeek.put("period_end", "0000-00-00");
            PeriodWeek.put("week_start", "0000-00-00");
            PeriodWeek.put("week_end", "0000-00-00");

        }

        data = notesContentProvider.ActivitData(staff_id, module_id, duration, PeriodWeek.get("period_start"), PeriodWeek.get("period_end"), PeriodWeek.get("week_start"), PeriodWeek.get("week_end"));


        String weekStart = null, weekEnd = null, periodStart = null, periodEnd = null;
        tvOperationPeriod.setText("");
        String input_date_string = "2015-02-24";
        SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd");
        Date date1, date2, date3, date4;
        try {
            date1 = dateformat.parse(PeriodWeek.get("week_start"));
            date2 = dateformat.parse(PeriodWeek.get("week_end"));
            date3 = dateformat.parse(PeriodWeek.get("period_start"));
            date4 = dateformat.parse(PeriodWeek.get("period_end"));


            DateFormat dayFormate = new SimpleDateFormat("EE dd, MMM");
            weekStart = dayFormate.format(date1);
            weekEnd = dayFormate.format(date2);
            periodStart = dayFormate.format(date3);
            periodEnd = dayFormate.format(date4);


        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        if (duration.trim().equalsIgnoreCase("week"))
            tvOperationPeriod.setText(weekStart + " to " + weekEnd);
        else if (duration.trim().equalsIgnoreCase("period"))
            tvOperationPeriod.setText(periodStart + " to " + periodEnd);
        else if (duration.trim().equalsIgnoreCase("year")) tvOperationPeriod.setText(" 2019");


        statisticalData = new ArrayList<>();

        Log.d("__STATS__", data + "");
        try {
            JSONArray jsonArray = new JSONArray(data);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject Quiz = jsonArray.getJSONObject(i);
                statisticalData.add(new StatisticalData(
                        Quiz.getString("date"),
                        Quiz.getString("frequency")


                ));
            }
        } catch (JSONException e1) {
            Log.e("FAILED", "Json parsing error: " + e1.getMessage());
        }
        List<BarEntry> barEntries = new ArrayList<>();
        float s = 0;
        for (StatisticalData stats : statisticalData) {
            Log.d("__STATS__", stats.getFrequency() + "");

            s += 1.0;

            Log.d("__STATS__", s + "");
            barEntries.add(new BarEntry(s, Float.parseFloat(stats.getFrequency())));
        }

        BarDataSet barDataSet = new BarDataSet(barEntries, module);
        barDataSet.setColors(getResources().getColor(R.color.colour_card_4));

        BarData barData = new BarData(barDataSet);
        barData.setBarWidth(0.5f);
        visualization_dashboards.getDescription().setEnabled(false);

        xLabels = new ArrayList<>();


        if (duration.trim().equalsIgnoreCase("week")) {


            for (int i = 0; i < statisticalData.size(); i++) {
                //Log.d("__STATS__",stats.getStatistics_date()+"");
                StatisticalData stats = statisticalData.get(i);

                SimpleDateFormat dateformat1 = new SimpleDateFormat("yyyy-MM-dd");
                Date datex;
                try {
                    datex = dateformat1.parse(stats.getStatistics_date());
                    DateFormat dayFormate = new SimpleDateFormat("EE");
                    weekStart = dayFormate.format(datex);
                    xLabels.add(weekStart);

                    Log.d("__STATS__", xLabels + "");

                } catch (ParseException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }


        } else if (duration.trim().equalsIgnoreCase("period")) {

            for (int i = 0; i < statisticalData.size(); i++) {
                //Log.d("__STATS__",stats.getStatistics_date()+"");

                StatisticalData stats = statisticalData.get(i);
                SimpleDateFormat dateformat1 = new SimpleDateFormat("yyyy-MM-dd");
                Date datex;
                try {
                    datex = dateformat1.parse(stats.getStatistics_date());
                    DateFormat dayFormate = new SimpleDateFormat("dd/MMM");
                    weekStart = dayFormate.format(datex);
                    xLabels.add(weekStart);

                    Log.d("__STATS__", xLabels + "");

                } catch (ParseException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }


        } else if (duration.trim().equalsIgnoreCase("year")) {

            for (int i = 0; i < statisticalData.size(); i++) {
                //Log.d("__STATS__",stats.getStatistics_date()+"");

                StatisticalData stats = statisticalData.get(i);
                SimpleDateFormat dateformat1 = new SimpleDateFormat("yyyy-MM-dd");
                Date datex;
                try {
                    datex = dateformat1.parse(stats.getStatistics_date());
                    DateFormat dayFormate = new SimpleDateFormat("MMM");
                    weekStart = dayFormate.format(datex);
                    xLabels.add(weekStart);

                    Log.d("__STATS__", xLabels + "");

                } catch (ParseException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }


        visualization_dashboards.animateY(1200);
        visualization_dashboards.setData(barData);
        visualization_dashboards.setFitBars(true);


        XAxis xAxis = visualization_dashboards.getXAxis();
        xAxis.setGranularity(1f);

        xAxis.setLabelRotationAngle(-90);
        xAxis.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                Log.d("__STATS__", value + "");
                if ((int) value > 0 && (int) value <= xLabels.size()) {
                    Log.d("Return_Values", value + "");
                    return xLabels.get((int) value - 1);
                } else {
                    return "";
                }
            }

        });


    }


}
