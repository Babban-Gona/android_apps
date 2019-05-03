package com.babbangona.shpincentiveapp;


import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.List;


public class ActivityRecycler extends RecyclerView.Adapter<ActivityRecycler.ProductViewHolder> {


    private Context mCtx;
    private List<ActivityClass> productList;

    SessionManagement sessionManagement;
    private ActivityClass testClass;


    public ActivityRecycler(Context mCtx, List<ActivityClass> productList) {
        this.mCtx = mCtx;
        this.productList = productList;
    }



    @Override
    public ProductViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.activity_recycler, null);
        sessionManagement = new SessionManagement(view.getContext());

        return new ProductViewHolder(view);

    }


    @SuppressLint("SetTextI18n")

    /*
    * The method below consists of various view usage
    * */

    public void onBindViewHolder(final ProductViewHolder holder, final int position) {


        testClass= productList.get(position);

        int x = position+1;

       holder.activity_title.setText("");
       holder.activity_status.setText("");



       holder.activity_title.setText(holder.activity_title.getText()+testClass.activity_name());
       holder.activity_status.setText(holder.activity_status.getText()+testClass.getCount()+" "+mCtx.getResources().getString(R.string.recycler_concat1)+" 5 "+mCtx.getResources().getString(R.string.recycler_concat2));



       ProgressBarAnimation animation = new ProgressBarAnimation(holder.progressBar,0,Float.parseFloat(testClass.getCount()));
       animation.setDuration(1000);
        Double progressCalculated = Double.valueOf((Double.valueOf(testClass.getCount())/Double.valueOf(5))*100);


        long progressCalculated_2dp = Math.round(progressCalculated);

        Log.d("__NOW__",progressCalculated_2dp+"");

       holder.progressBar.startAnimation(animation);
        holder.progressBar.setProgress(Integer.parseInt(String.valueOf(progressCalculated_2dp)));
       holder.progressBar.animate();

       if(testClass.activity_name().trim().equalsIgnoreCase("Trust Group Formation")){
           holder.activity_colour.setBackgroundColor(mCtx.getResources().getColor(R.color.colour_card_2));
       }
        else if(testClass.activity_name().trim().equalsIgnoreCase("Field Mapping")){
            holder.activity_colour.setBackgroundColor(mCtx.getResources().getColor(R.color.colour_card_1));
        }


    }

    @Override
    public int getItemCount() {
        Log.d("product", String.valueOf(productList.size()));
        return productList.size();
    }

    // the method below consists of the declaration of the various views in in the recycler card
    class ProductViewHolder extends RecyclerView.ViewHolder {

        ImageView activity_colour;
        TextView activity_title,activity_status;
        TextView activity_date;
       ProgressBar progressBar;

        public ProductViewHolder(View itemView) {
            super(itemView);

            activity_title = itemView.findViewById(R.id.activity_title);
            activity_status = itemView.findViewById(R.id.activity_status);
            progressBar = itemView.findViewById(R.id.activity_progress);
            activity_colour = itemView.findViewById(R.id.activity_colour_code);


        }



    }




}