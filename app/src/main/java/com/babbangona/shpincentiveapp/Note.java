package com.babbangona.shpincentiveapp;


import android.net.Uri;
import android.provider.BaseColumns;

public class Note {

    /*
    * This class contains all the column names used in the database tables for this application
    *
    * */


        public Note() {
        }

        public static final class Notes implements BaseColumns {
            private Notes() {
            }

            public static final Uri CONTENT_URI = Uri.parse("content://"
                    + NotesContentProvider.AUTHORITY + "/operations");

            public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.jwei512.operations";

            public static final String OPERATION_ID = "_id";

            public static final String ACTIVITY_NAME = "activity_name";

            public static final String STAFF_ID = "staff_id";

            public static final String UPLOAD_STATUS = "upload_status";

            public static final String STATISTICS_DATE = "statistics_date";

            public static final String COMPLETION_DATE = "activity_date";

            public static final String UNIQUE_ID = "unique_id";

            public static final String ACTIVITY_FLAG = "activity_flag";



            public static final String STAFFID_ACTIVITYCODE_TIME = "staffid_activitycode_time";

           public static final String ACTIVITY_ID = "activity_id";

           public static final String PROCESS_COMPLETED = "process_completed";

           public static final String TARGET = "target";

           public static final String TIME_COMPLETED = "time_completed";

           public static final String TIME_CREATED = "time_created";

           public static final String TIME_UPDATED = "time_updated";


           public static final String PERIOD_START = "period_start";

           public static final String PERIOD_END ="period_end";

           public static final String WEEK_START = "week_start";

           public static final String WEEK_END  = "week_end";

           public static final String BASE_PAY = "base_pay";

           public static final String SUCCESSFUL_PAY = "successful_pay";

           public static final String STAFFID_ACTIVITYID = "staffid_activityid";

           public static final String MISCELLANEOUS_BAL = "miscellaneous_balance";

           public static final String OPENING_BAL = "opening_balance";

           public static final String MONEY_EARNED = "money_earned";

           public static final String MONEY_PAID = "money_paid";

           public static final String PAYMENT_PERIOD = "payment_period";

           public static final String PAYMENT_DATE = "payment_date";

           public static final String PAYMENT_ID = "payment_id";

           public static final String BALANCE_BROUGHT_FORWARD = "balance_brought_forward";














        }

    }

