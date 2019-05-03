package com.babbangona.shpincentiveapp;

import android.content.Context;
import android.content.OperationApplicationException;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.github.mikephil.charting.charts.CandleStickChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.CandleData;
import com.github.mikephil.charting.data.CandleDataSet;
import com.github.mikephil.charting.data.CandleEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.utils.MPPointF;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

//implements OnChartValueSelectedListener
public class FullDashboard extends AppCompatActivity  {
    private  CandleStickChart candleStickChart;
    private  CandleStickChart candleStickChartPrevious;
    private YAxis yAxis;
    private YAxis rightAxis;
    private XAxis xAxis;
    private  ArrayList<String> xAxisLabel;
    SessionManagement sessionManagement;
    String staff_id;
    ArrayList<Map<String,String>> fullRecords;
    ArrayList<Float> Values;
    Map<String,String> PeriodWeek;
    ArrayList<Map<String,String>> PaymentStructure;
    TextView tvDate,tvPreviousPayment, tvCurrentPayment;
    NotesContentProvider.DatabaseHelper databaseHelper;
    ArrayList<Float> myNumbers ;

    /*
    * This class handles the visualisations for current payment period and this payment period
    * It makes use of the candle stick chart provided by the MP android chart library
    * this activity makes use of two candle stick chart views in its XML one for the previous payment period and the other for the current payment period
    * and the user easily transitions between the two with visibility adjustments
    * */


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_dashboard);

        // Declaration of candle stick chart views for both previous and current payment period
        candleStickChart = findViewById(R.id.activity_dashboard_barchart);
        candleStickChartPrevious = findViewById(R.id.activity_dashboard_barchart_previous);

        candleStickChart.setVisibility(View.VISIBLE);
        candleStickChartPrevious.setVisibility(View.GONE);

        candleStickChart.getDescription().setEnabled(false);
        candleStickChartPrevious.getDescription().setEnabled(false);



        myNumbers = new ArrayList<Float>();
        databaseHelper = new NotesContentProvider.DatabaseHelper(getApplicationContext());

        //Block of code to retrieve staff ID from shared Preferences
        sessionManagement = new SessionManagement(getApplicationContext());
        HashMap<String,String> user = sessionManagement.getUserDetails();
        staff_id = user.get(SessionManagement.KEY_STAFF_ID);

        tvDate = findViewById(R.id.date_format);
        tvPreviousPayment = findViewById(R.id.fulldashboard_previous);
        tvCurrentPayment = findViewById(R.id.fulldashboard_current);

        tvCurrentPayment.setBackgroundColor(getResources().getColor(R.color.colorAccent));
        tvCurrentPayment.setTextColor(getResources().getColor(R.color.txt_white_color));
        cursorController(staff_id,"current");


        tvPreviousPayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvPreviousPayment.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                tvPreviousPayment.setTextColor(getResources().getColor(R.color.txt_white_color));
                tvCurrentPayment.setBackgroundColor(getResources().getColor(R.color.layout_white));
                tvCurrentPayment.setTextColor(getResources().getColor(R.color.txt_black_color));

                cursorController(staff_id,"previous");


            }
        });

        tvCurrentPayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvCurrentPayment.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                tvCurrentPayment.setTextColor(getResources().getColor(R.color.txt_white_color));

                tvPreviousPayment.setBackgroundColor(getResources().getColor(R.color.layout_white));
                tvPreviousPayment.setTextColor(getResources().getColor(R.color.txt_black_color));
                cursorController(staff_id,"current");


            }
        });


    }

    /*
    * The method below retrieves data from the database based on the parameters passed into it
    * the parameters can either be for the current payment period or the previous payment period
    * */
    public void cursorController(String staff_id, String module){





if(module.trim().equalsIgnoreCase("current")) {
    candleStickChartPrevious.setVisibility(View.GONE);
    candleStickChart.setVisibility(View.VISIBLE);


    setUpCharts("current");
    candleStickChart.setHighlightPerDragEnabled(true);


    NotesContentProvider.DatabaseHelper notesContentProvider = new NotesContentProvider.DatabaseHelper(getApplicationContext());
    PeriodWeek = notesContentProvider.getPeriodWeek(staff_id);
    myNumbers = new ArrayList<Float>();
    fullRecords = new ArrayList<>();
    PeriodWeek = new HashMap<>();
    PeriodWeek = databaseHelper.getPeriodWeek(staff_id);
    if (PeriodWeek == null) {
        PeriodWeek = new HashMap<>();
        PeriodWeek.put("period_start", "0000-00-00");
        PeriodWeek.put("period_end", "0000-00-00");
        PeriodWeek.put("week_start", "0000-00-00");
        PeriodWeek.put("week_end", "0000-00-00");

    }

    String periodStart = null,periodEnd = null;
    String input_date_string="2015-02-24";
    SimpleDateFormat dateformat=new SimpleDateFormat("yyyy-MM-dd");
    Date date1,date2,date3,date4;
    try {

        date3 = dateformat.parse(PeriodWeek.get("period_start"));
        date4 = dateformat.parse(PeriodWeek.get("period_end"));



        DateFormat dayFormate=new SimpleDateFormat("EE dd, MMM");

        periodStart =dayFormate.format(date3);
        periodEnd =dayFormate.format(date4);



    } catch (ParseException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
    }
    tvDate.setText("");
    tvDate.setText(tvDate.getText()+periodStart+"  To  "+periodEnd);

    fullRecords = databaseHelper.FullActivityData(staff_id, PeriodWeek.get("period_start"), PeriodWeek.get("period_end"));
    Log.d("LLLLL",fullRecords+"");

    myNumbers = new ArrayList<Float>();

    JSONArray jsonArray = new JSONArray(fullRecords);
    if(fullRecords.size()>0){
    for (int i = 0; i < jsonArray.length(); i++)
    {
        JSONObject Quiz = null;
        try {
            Quiz = jsonArray.getJSONObject(i);
            if( !Quiz.getString("frequency").trim().matches("null")) {
                myNumbers.add(Float.valueOf(Quiz.getString("frequency")));
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }}

    if(myNumbers.size() > 0) {

        CandleDataSet set1 = new CandleDataSet(setDataList(myNumbers), "Payment Due");
        set1.setBarSpace(0);
        set1.setColor(Color.rgb(80, 80, 80));
        set1.setShadowColor(getResources().getColor(R.color.colour_card_3));
        set1.setShadowWidth(0.8f);
        set1.setDecreasingColor(getResources().getColor(R.color.colour_card_3));
        set1.setDecreasingPaintStyle(Paint.Style.FILL);
        set1.setIncreasingColor(getResources().getColor(R.color.colour_card_4));
        set1.setIncreasingPaintStyle(Paint.Style.FILL);
        set1.setNeutralColor(R.color.colour_card_3);
        set1.setDrawValues(true);


// create a data object with the datasets
        CandleData data = new CandleData(set1);


// set data
        candleStickChart.setData(data);
        candleStickChart.invalidate();
        candleStickChart.setClickable(true);
        CustomMarkerView mv = new CustomMarkerView(this, R.layout.tvcontent);
        candleStickChart.setMarker(mv);

        candleStickChart.animateY(1200);

    }
    xAxisLabel = new ArrayList<>();
    setXlabels(xAxisLabel,fullRecords);
    xAxisFormatter("current");
}
else if (module.trim().equalsIgnoreCase("previous")){
    candleStickChart.setVisibility(View.GONE);
    candleStickChartPrevious.setVisibility(View.VISIBLE);
    PaymentStructure = new ArrayList<>();
    fullRecords = new ArrayList<>();

    candleStickChartPrevious.setHighlightPerDragEnabled(true);
    setUpCharts("previous");
    tvDate.setText("");
    NotesContentProvider.DatabaseHelper notesContentProvider = new NotesContentProvider.DatabaseHelper(getApplicationContext());
    PeriodWeek = notesContentProvider.getPeriodWeek(staff_id);
    PaymentStructure = notesContentProvider.getPaymentStructureFullDashboard(staff_id);



    try {

        String previousPaymentPeriod = notesContentProvider.getLastPeriodDate();
        String[] date = previousPaymentPeriod.split("_");
        String previous = date[0];
        String current = date[1];
        String periodStart = null,periodEnd = null;
        String input_date_string="2015-02-24";
        SimpleDateFormat dateformat=new SimpleDateFormat("yyyy-MM-dd");
        Date date1,date2,date3,date4;

        date3 = dateformat.parse(previous);
        date4 = dateformat.parse(current);



        DateFormat dayFormate=new SimpleDateFormat("EE dd, MMM");

        periodStart =dayFormate.format(date3);
        periodEnd =dayFormate.format(date4);

        tvDate.setText("");
        tvDate.setText(tvDate.getText()+periodStart+"  To  "+periodEnd);


    } catch (ParseException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
    }


    try {
        fullRecords = databaseHelper.getPaymentHistory(staff_id);
        Log.d("LLLLL",fullRecords+"");
        myNumbers = new ArrayList<Float>();

        JSONArray jsonArray = new JSONArray(fullRecords);
        for (int i = 0; i < jsonArray.length(); i++)
        {
            JSONObject Quiz = null;
            try {
                Quiz = jsonArray.getJSONObject(i);
                myNumbers.add(Float.valueOf(Quiz.getString("frequency")));


            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

        if(myNumbers.size() > 0) {

            CandleDataSet set1 = new CandleDataSet(setDataListPrevious(myNumbers), "Payment Made");
            set1.setBarSpace(0);
            set1.setColor(Color.rgb(80, 80, 80));
            set1.setShadowColor(getResources().getColor(R.color.colour_card_3));
            set1.setShadowWidth(0.8f);
            set1.setDecreasingColor(getResources().getColor(R.color.colour_card_3));
            set1.setDecreasingPaintStyle(Paint.Style.FILL);
            set1.setIncreasingColor(getResources().getColor(R.color.colour_card_4));
            set1.setIncreasingPaintStyle(Paint.Style.FILL);
            set1.setNeutralColor(R.color.colour_card_3);
            set1.setDrawValues(true);


// create a data object with the datasets
            CandleData data = new CandleData(set1);


// set data
            candleStickChartPrevious.setData(data);
            candleStickChartPrevious.invalidate();
            candleStickChartPrevious.setClickable(true);
            CustomMarkerView mv = new CustomMarkerView(this, R.layout.tvcontent);
            candleStickChartPrevious.setMarker(mv);

            candleStickChartPrevious.animateY(1200);

        }


        xAxisLabel = new ArrayList<>();
        setXlabels(xAxisLabel,fullRecords);
        xAxisFormatter("previous");

    }catch (Exception e){
        Log.d("LLLLL",e+"");
    }
}


    }


    /*
    * This method prepares the chart views to represent the data of interest
    * */
    public void setUpCharts(String module){

        if(module.trim().equalsIgnoreCase("current")) {
            candleStickChart.setDrawBorders(true);
            candleStickChart.setBorderColor(getResources().getColor(R.color.layout_light_grey));

            yAxis = candleStickChart.getAxisLeft();
            rightAxis = candleStickChart.getAxisRight();
            yAxis.setDrawGridLines(true);
            rightAxis.setDrawGridLines(true);
            candleStickChart.requestDisallowInterceptTouchEvent(true);

            xAxis = candleStickChart.getXAxis();
        }
        else if (module.trim().equalsIgnoreCase("previous")){
            candleStickChartPrevious.setDrawBorders(true);
            candleStickChartPrevious.setBorderColor(getResources().getColor(R.color.layout_light_grey));

            yAxis = candleStickChartPrevious.getAxisLeft();
            rightAxis = candleStickChartPrevious.getAxisRight();
            yAxis.setDrawGridLines(true);
            rightAxis.setDrawGridLines(true);
            candleStickChartPrevious.requestDisallowInterceptTouchEvent(true);

            xAxis = candleStickChartPrevious.getXAxis();
        }
    }

    // this method processes the labels for the X axis
    public void setXlabels(final ArrayList<String> xAxisLabel, ArrayList<Map<String, String>> arrayList){


        JSONArray jsonArray = new JSONArray(arrayList);
        for (int i = 0;i< jsonArray.length();i++){
            try {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                if(jsonObject.getString("date").trim().equalsIgnoreCase("Trust Group Formation")) xAxisLabel.add("TFM");
                else if(jsonObject.getString("date").trim().equalsIgnoreCase("Field Mapping")) xAxisLabel.add("FM");
                else xAxisLabel.add(jsonObject.getString("date"));


            } catch (JSONException e) {
                e.printStackTrace();
            }

        }



        xAxis.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return xAxisLabel.get((int) value);
            }
        });


    }

    public void xAxisFormatter(String module){
        xAxis.setDrawGridLines(true);// disable x axis grid lines
        xAxis.setDrawLabels(true);
        rightAxis.setTextColor(Color.WHITE);
        yAxis.setDrawLabels(true);
        xAxis.setGranularity(1f);
        xAxis.setGranularityEnabled(true);
        xAxis.setAvoidFirstLastClipping(true);
        xAxis.setLabelRotationAngle(-90);

        if(module.trim().equalsIgnoreCase("current")){
            Legend l = candleStickChart.getLegend();
            l.setEnabled(true);
        }else if (module.trim().equalsIgnoreCase("previous")){

            Legend l = candleStickChartPrevious.getLegend();
            l.setEnabled(true);
        }


    }


    /*
    * This method processes the data to be represented in the candle stick chart and ensures they are in the right format
    * */
    public ArrayList<CandleEntry> setDataList(ArrayList<Float> dataSet){
        ArrayList<CandleEntry> yValsCandleStick = new ArrayList<CandleEntry>();
        yValsCandleStick.add(new CandleEntry(0,dataSet.get(0),0f, 0f, dataSet.get(0)));
        for(int i = 1; i < dataSet.size(); i++){

            //================================
            float Open = dataSet.get(i-1);
            float Close = dataSet.get(i);
            //===============================
            yValsCandleStick.add(new CandleEntry((float)i,dataSet.get(i), Math.min(Open, Close), Open, dataSet.get(i)));
        }

        return yValsCandleStick;
    }

    public ArrayList<CandleEntry> setDataListPrevious(ArrayList<Float> dataSet){
        ArrayList<CandleEntry> yValsCandleStick = new ArrayList<CandleEntry>();
        yValsCandleStick.add(new CandleEntry(0,dataSet.get(0),0f, 0f, dataSet.get(0)));
        for(int i = 1; i < dataSet.size(); i++){

            //================================
            float Open = dataSet.get(i-1);
            float Close = dataSet.get(i);
            //===============================
            yValsCandleStick.add(new CandleEntry((float)i,dataSet.get(i), Math.min(Open, Close), Open, dataSet.get(i)));

         /*   if(i == dataSet.size()-2){
                yValsCandleStick.add(new CandleEntry((float)i,dataSet.get(i), Math.min(0, Close), Open, dataSet.get(i)));
            }*/
        }

        return yValsCandleStick;
    }

    public class CustomMarkerView extends MarkerView {

        private TextView tvContent;
        public CustomMarkerView (Context context, int layoutResource) {
            super(context, layoutResource);
            // this markerview only displays a textview
            tvContent = (TextView) findViewById(R.id.tvContent);
        }

        // callbacks every time the MarkerView is redrawn, can be used to update the
        // content (user-interface)
        @Override
        public void refreshContent(Entry e, Highlight highlight) {
            tvContent.setText(" " + e.toString()); // set the entry-value as the display text
        }

        @Override
        public MPPointF getOffset() {
            return super.getOffset();
        }
    }


}
