package com.babbangona.shpincentiveapp;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class YearActivityRecycler extends RecyclerView.Adapter<YearActivityRecycler.ProductViewHolder> {


    private Context mCtx;
    private List<ActivityClass> productList;

    SessionManagement sessionManagement;
    private ActivityClass testClass;
    NotesContentProvider.DatabaseHelper databaseHelper;
    Map<String,String> PeriodWeek;
    String module;
    Map<String,String> PaymentStructure;

    public YearActivityRecycler(Context mCtx, List<ActivityClass> productList) {
        this.mCtx = mCtx;
        this.productList = productList;
    }

 //this method consists of view usages

    @Override
    public ProductViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.activity_year_recycler, null);
        sessionManagement = new SessionManagement(view.getContext());

        return new ProductViewHolder(view);

    }


    @SuppressLint("SetTextI18n")
    // the method below consists of the declaration of the various views in in the recycler card

    public void onBindViewHolder(final ProductViewHolder holder, final int position) {
        testClass= productList.get(position);

        int x = position+1;

        holder.activity_title.setText("");
        holder.activity_status.setText("");
        holder.hidden.setText("");

        Double Coefficient  = Double.valueOf(testClass.getProcess_completed())/Double.valueOf(testClass.getTarget());


        SessionManagement sessionManagement =  new SessionManagement(mCtx);
        HashMap<String,String> user = sessionManagement.getUserDetails();
        String staff_id = user.get(SessionManagement.KEY_STAFF_ID);




        holder.activity_title.setText(holder.activity_title.getText()+testClass.activity_name());
        holder.activity_status.setText(holder.activity_status.getText()+testClass.getProcess_completed());
        holder.activity_i_am_working.setText("");
        holder.hidden.setText(testClass.getActivity_id());

        databaseHelper = new NotesContentProvider.DatabaseHelper(mCtx);
        PeriodWeek = new HashMap<>();
        PeriodWeek = databaseHelper.getPeriodWeek(staff_id);
        if (PeriodWeek == null){
            PeriodWeek = new HashMap<>();
            PeriodWeek.put("period_start","0000-00-00");
            PeriodWeek.put("period_end","0000-00-00");
            PeriodWeek.put("week_start","0000-00-00");
            PeriodWeek.put("week_end","0000-00-00");

        }


        String TargetMetDays = "";
              TargetMetDays =   databaseHelper.getCompletedTarget(staff_id,testClass.getActivity_id(),testClass.getTime_completed(),testClass.getModule(),PeriodWeek.get("period_start"),PeriodWeek.get("period_end"),PeriodWeek.get("week_start"),PeriodWeek.get("week_end"));

        String StartDate = "";
        StartDate = databaseHelper.getActivityStartDate(testClass.getModule(),testClass.getActivity_id(),PeriodWeek.get("week_start"),PeriodWeek.get("week_end"),PeriodWeek.get("period_start"),PeriodWeek.get("period_end"));
        SimpleDateFormat dateformat=new SimpleDateFormat("yyyy-MM-dd");
        Date date1 = null;
        Date date2 = null;
        try {
            date1 = dateformat.parse(PeriodWeek.get("week_start"));
            date2=  dateformat.parse(PeriodWeek.get("week_end"));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        long diff = date2.getTime() - date1.getTime();
        long seconds = diff / 1000;
        long minutes = seconds / 60;
        long hours = minutes / 60;
        long days = hours / 24;

        NotesContentProvider.DatabaseHelper notesContentProvider = new NotesContentProvider.DatabaseHelper(mCtx);
        PaymentStructure = new HashMap<>();
        PaymentStructure = notesContentProvider.getPaymentStructure(staff_id,holder.hidden.getText().toString().trim());
        Log.d("PaymentStructure",PaymentStructure+"");



        Log.d("START",StartDate+" 00");
        holder.start_date.setText("");
        holder.start_date.setText(StartDate);
       if(testClass.getTarget().trim().equalsIgnoreCase("0")){
           holder.activity_i_am_working.setText(holder.activity_i_am_working.getText()+mCtx.getResources().getString(R.string.activity_dashboard_dailyrate));
       }
       else{
           holder.activity_i_am_working.setText(holder.activity_i_am_working.getText()+TargetMetDays+" x Daily Rate (₦"+PaymentStructure.get("base_pay")+")");
       }


            holder.activity_rate.setText(holder.activity_rate.getText()+mCtx.getResources().getString(R.string.year_recycler_body)+" (₦"+PaymentStructure.get("successful_pay")+")");
       // holder.activity_i_am_working.setText(holder.activity_i_am_working.getText()+TargetMetDays+" of "+days+"\n" +TargetMetDays+" x Base Pay");

        if(!testClass.getTarget().trim().matches("0")) {
            holder.yearActivty.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mCtx, ActivityDashboard.class);
                    intent.putExtra("module", holder.activity_title.getText().toString().trim());
                    intent.putExtra("module_id", holder.hidden.getText().toString().trim());
                    intent.putExtra("operation", testClass.getModule());
                    mCtx.startActivity(intent);
                }
            });
        }
        if(testClass.getActivity_id().trim().matches("1")){
            holder.activity_colour.setBackgroundColor(mCtx.getResources().getColor(R.color.colour_card_2));
        }
        else if(testClass.getActivity_id().trim().matches("2")){
            holder.activity_colour.setBackgroundColor(mCtx.getResources().getColor(R.color.colour_card_1));
        }
        else if(testClass.getActivity_id().trim().matches("3")){
            holder.activity_colour.setBackgroundColor(mCtx.getResources().getColor(R.color.colour_card_3));
        }
        else  if(testClass.getActivity_id().trim().matches("4")){
            holder.activity_colour.setBackgroundColor(mCtx.getResources().getColor(R.color.colour_card_3));
        }

    }


    @Override
    public int getItemCount() {
        Log.d("product", String.valueOf(productList.size()));
        return productList.size();
    }

    class ProductViewHolder extends RecyclerView.ViewHolder {

        TextView activity_title,activity_status,start_date,activity_i_am_working,hidden,activity_rate;
        LinearLayout yearActivty;
        ImageView activity_colour;


        public ProductViewHolder(View itemView) {
            super(itemView);

            activity_title = itemView.findViewById(R.id.year_recycler_title);
            activity_status = itemView.findViewById(R.id.year_recycler_count);
            start_date = itemView.findViewById(R.id.year_recycler_date);
            yearActivty = itemView.findViewById(R.id.year_recycler_layout);

            activity_i_am_working = itemView.findViewById(R.id.activity_working);
            activity_colour = itemView.findViewById(R.id.activity_colour);
            hidden = itemView.findViewById(R.id.widthController);
            activity_rate = itemView.findViewById(R.id.year_recycler_rate);
        }



    }




}