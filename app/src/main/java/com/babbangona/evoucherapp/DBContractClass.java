package com.babbangona.evoucherapp;

public class DBContractClass {

    public static final String DATABASE_NAME = "evoucher.db";
    public static final int DATABASE_VERSION = 1;


    public static final String TABLE_LAST_SYNCED = "evoucher_lastsynced";
    public static final String COL_LAST_SYNC_UP = "last_sync_up";
    public static final String COL_LAST_SYNC_DOWN = "last_sync_down";



    //generic colunms
    public static final String COL_STAFF_ID = "staff_id";
    public static final String COL_LAST_SYNCED_TIME = "last_synced_time";
    public static final String COL_IKNUMBER = "ik_number";
    public static final String COL_MEMBER_ROLE = "member_role";
    public static final String COL_SEED_TYPE = "seed_type";
    public static final String COL_FIELD_SIZE = "field_size";
    public static final String COL_MEMBER_ID = "member_id";


    // Table containing all member info on a field level
    public static final String TABLE_FIELD_T = "evoucher_field_t";
    public static final String COL_FIELD_ID = "field_id";
    public static final String COL_TGHECTARES = "tg_hectares";
    public static final String COL_FIELD_PERCENTAGE_OWNERSHIP = "percentage_ownership";

    public static final String COL_LOAN_FIELDSIZE = "loan_fieldsize";



//Table for PRODUCT QUANTITY T containing all package types
    public static final String TABLE_PACKAGE_TABLE = "product_quantity_t";
    public static final String COL_LOANFIELDSIZE_SEEDTYPE_CROPTYPE = "loanfieldsize_seedtype_croptype";
    public static final String COL_ID1_SEEDTYPE = "id1_seed_type";
    public static final String COL_ID1_FIELDSIZE = "id1_field_size";
    public static final String COL_ID1_SEED = "id1_seed";
    public static final String COL_ID1_ATRAZIN = "id1_atrazin";
    public static final String COL_ID1_UREA = "id1_urea";
    public static final String COL_ID1_DAP = "id1_dap";
    public static final String COL_ID2_UREA = "id2_urea";
    public static final String COL_ID2_ALFASAFE = "id2_alfasafe";
   // public static final String COL_ID2_MOP = "id2_mop";

    //Table for output on the eVoucher Table
    public static final String TABLE_EVOUCHER = "evoucher_data";
    public static final String COL_TXN_ID = "transaction_id";
    public static final String  COL_PACKAGE_DISTRIBUTED= "package_distributed";
    public static final String COL_STAFF_ROLE = "staff_role";
    public static final String COL_TOKEN_SEASON = "token_season";
    public static final String COL_TOKEN = "token";
    public static final String COL_COLLECTION_TIME = "collection_time";
    public static final String COL_SYNC_FLAG = "sync_flag";
    public static final String COL_APP_VERSION = "app_version";

    public static final String COL_RECEIPT_ID= "receipt_id";


    //Table for LMD data
    public static final String TABLE_LMDDATA = "lmd_data";
    public static final String COL_LMD_ID = "lmd_id";
    public static final String COL_LMD_NAME = "lmd_name";
    public static final String COL_LMD_VILLAGE = "lmd_village";
    public static final String COL_LMD_PHONENUMBER = "lmd_phonenumber";

    //Table for incomplete data
    public static final String TABLE_INCOMPLETE_TRANSACTIONS = "evoucher_incomplete_transactions";
    public static final String COL_COMPLAINT_ID = "complaint_id";
    public static final String COL_TOKEN_ISSUED = "token_issued";
    public static final String COL_COMPLAINT_TYPE = "complaint_type";
    public static final String COL_COMPLAINT_DESC = "complaint_description";
    public static final String COL_DATE_LOGGED = "date_logged";

    //Table for member Template
    public static final String TABLE_MEMBER_DETAILS = "member_details";
    public static final String COL_MEMBER_NAME = "member_name";
    public static final String COL_MEMBER_TEMPLATE = "member_template";


    //Table for used tokens
    public static final String TABLE_USED_TOKENS = "evoucher_used_tokens";
    public static final String COL_USED_TOKENS = "used_tokens";

    //Table for ACCO
    public static final String TABLE_ACCO_BINDING = "evoucher_acco_binding";
    public static final String COL_ACCO_ID = "acco_staff_id";
    public static final String COL_ACCO_NAME = "acco_staff_name";
    public static final String COL_ACCO_TEMPLATE = "acco_template";





}
