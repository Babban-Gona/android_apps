package com.babbangona.shpincentiveapp;

public class ActivityClass {
    /*
     * Declarations of the variables for the Recycler views
     * */

        private String staffid_activitycode_time;
        private String activity_name;
        private String activity_id;
        private String process_completed;
        private String target;
        private String time_completed;
        private String count;


        private String module;


        // the method below handles the JSON for 'This Week', 'This Payment Period' and 'Since Year Started'
        public ActivityClass(String staffid_activitycode_time,String target, String activity_id, String process_completed, String time_completed,String activity_name,  String module) {

          this.staffid_activitycode_time = staffid_activitycode_time;
          this.target = target;
          this.activity_id = activity_id;
          this.activity_name = activity_name;
          this.process_completed = process_completed;
          this.time_completed = time_completed;
          this.module = module;


        }

        // the method below handles JSON for the 'Today' view
        public ActivityClass(String ActivityName,String Count, String Module){
            this.activity_name = ActivityName;
            this.count = Count;
            this.module = Module;

        }


        //The Method below consists of the getters for all the variables declared in this class
        public String getStaffid_activitycode_time(){return staffid_activitycode_time;}
        public String getTarget(){return target;}
        public String getActivity_id(){return activity_id;}
        public String getProcess_completed(){return process_completed;}
        public String getTime_completed(){return time_completed;}
        public String activity_name() {
            return activity_name;
        }
        public String getCount(){return count;}

        public String getModule() {
        return module;
    }






}
