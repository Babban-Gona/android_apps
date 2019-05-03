package com.babbangona.evoucherapp;

/***
 *
 * This class handles the json array response from the server
 *
 ***/

public class SyncedContractClass {


    private String txn_id;
    private String member_name;
    private String loan_fieldsize;
    private String sync_flag;
    private String ik_number;


   public SyncedContractClass(String txn_id, String member_name, String ik_number,String loan_fieldsize, String sync_flag){
     this.txn_id = txn_id;
     this.member_name = member_name;
     this.loan_fieldsize = loan_fieldsize;
     this.sync_flag = sync_flag;
     this.ik_number = ik_number;


   }

   public String getTxn_id(){ return txn_id; }
   public String getMember_name(){return member_name;}
   public String getLoan_fieldsize(){return loan_fieldsize;}
   public String getSync_flag(){return sync_flag;}
   public String getIk_number(){return ik_number;}




}
