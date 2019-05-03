package com.babbangona.evoucherapp;


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


public class SyncedRecyclerAdapter extends RecyclerView.Adapter<SyncedRecyclerAdapter.ProductViewHolder> {


    private Context mCtx;
    private List<SyncedContractClass> productList;

    SessionManagement sessionManagement;
    private SyncedContractClass testClass;

    Map<String,String> PeriodWeek;
    String module;
    Map<String,String> PaymentStructure;

    public SyncedRecyclerAdapter(Context mCtx, List<SyncedContractClass> productList) {
        this.mCtx = mCtx;
        this.productList = productList;
    }

 //this method consists of view usages

    @Override
    public ProductViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.sync_card, null);
        sessionManagement = new SessionManagement(view.getContext());



        return new ProductViewHolder(view);

    }


    @SuppressLint("SetTextI18n")
    // the method below consists of the declaration of the various views in in the recycler card

    public void onBindViewHolder(final ProductViewHolder holder, final int position) {
        testClass= productList.get(position);

        int x = position+1;
        holder.tvMemberName.setText("");
        holder.tvIkNumber.setText("");
        holder.tvLoanFieldSize.setText("");
        holder.tvSynced.setText("");

        holder.tvMemberName.setText("Picked up  by "+testClass.getMember_name());
        holder.tvIkNumber.setText(testClass.getIk_number());
        holder.tvLoanFieldSize.setText(testClass.getLoan_fieldsize()+" Hectare(s)");
        String xx = "";

        if(testClass.getSync_flag().matches("1")){
            xx = "synced";
            holder.tvSynced.setText(xx);
            holder.tvSynced.setTextColor(mCtx.getResources().getColor(R.color.view_green));

        }
        else if(testClass.getSync_flag().matches("0")) {
            xx = "unsynced";

            holder.tvSynced.setText(xx);
            holder.tvSynced.setTextColor(mCtx.getResources().getColor(R.color.view_red));
        }

    }

    @Override
    public int getItemCount() {
        Log.d("product", String.valueOf(productList.size()));
        return productList.size();
    }


    class ProductViewHolder extends RecyclerView.ViewHolder {

        TextView tvMemberName, tvIkNumber,tvLoanFieldSize, tvSynced;

        public ProductViewHolder(View itemView) {
            super(itemView);

            tvMemberName=itemView.findViewById(R.id.tvMemName);
            tvIkNumber=itemView.findViewById(R.id.tvIkNumber);
            tvLoanFieldSize=itemView.findViewById(R.id.tvLoanFieldSize);
            tvSynced=itemView.findViewById(R.id.tvSyncStatus);

        }



    }




}