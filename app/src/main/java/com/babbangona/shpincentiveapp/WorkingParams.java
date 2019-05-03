package com.babbangona.shpincentiveapp;

/***
 *
 * This class handles the json array response from the server
 *
 *
 *
 *
 *
 *
 * **/

public class WorkingParams {


    private String staffid_activitycode_time;
    private String staff_id;
    private String activity_id;
    private String activity_name;
    private String process_completed;
    private String target;
    private String time_completed;
    private String time_created;
    private String time_updated;
    private String activity_code;



   public WorkingParams(String staffid_activitycode_time, String staff_id, String activity_id, String activity_name, String process_completed, String target,String time_completed
   ,String time_created, String time_updated,String activity_code){

       this.staffid_activitycode_time = staffid_activitycode_time;
       this.activity_id = activity_id;
       this.activity_name = activity_name;
       this.staff_id = staff_id;
       this.process_completed = process_completed;
       this.target = target;
       this.time_completed = time_completed;
       this.time_created = time_created;
       this.time_updated = time_updated;
       this.activity_code = activity_code;



   }

   public String getStaffid_activitycode_time(){ return staffid_activitycode_time; }
   public String getActivity_id(){return activity_id;}
   public String getActivity_name(){return activity_name;}
   public String getStaff_id(){return staff_id;}
   public String getProcess_completed(){return process_completed;}
   public String getTarget(){return target;}
   public String getTime_completed(){return  time_completed;}
   public String getTime_created(){return time_created;}
   public String getTime_updated(){return  time_updated;}
   public String getActivity_code(){return  activity_code;}




}
