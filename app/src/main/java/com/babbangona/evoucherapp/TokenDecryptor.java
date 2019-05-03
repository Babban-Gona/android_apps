package com.babbangona.evoucherapp;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.util.Log;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TokenDecryptor {


    /**
     * THIS CLASS RETURNS
     * D: When season is wrong
     * L: When the token entered in the right LMD's phone
     * C: When the token format is corrupted
     * R: When the random characters have been changed
     * I: When a token cant be decrypted.
     * U: When token has been used
     * */

    String tknStaffId ="";
    String tknIkNumber = "";
    String tknHectSum = "";
    String tknSeason = "";
    String tknSeedType = "";
    String gracePeriod = "7";
    String tknValidateDate= "";
    String tknValidateStaffID= "";
    String tknRandomChars = "";
    String tknStatus = "";
    Context mCtx;

    String tknValidateRandomChars;

    String response="";

    String[] tknList = {};

    String TAG = "TokenDecryptor";
    String calSeedType = "";

    public String TokenDecryptor(String Token, Context context){
       this.mCtx = context;

       SessionManagement sessionManagement = new SessionManagement(context);
       sessionManagement.saveToken(Token);
       response = getDecryptionMethod(Token);

        return response;
    }

    private String getDecryptionMethod(String token){


       List<String> Token1 = new ArrayList<String>();

        char[] letter = token.toCharArray();
        Log.d(TAG,letter.toString());
        for (int i = 0; i < letter.length; i++) {
            Token1.add(letter[i] + "");

        }

        String x = Token1.toString();

        tknList = Token1.toArray(new String[0]);

        String response = "";

        Log.d(TAG,token);
        Log.d(TAG,tknList[0]+tknList[0]);

        //this determines the decryption algorithm to apply
        String decryptionModule = tknList[3];
        Log.d(TAG,decryptionModule);

        switch(decryptionModule){
            case "Q":
                response =  decryption1(tknList);
                break;

            case "X":
                response =   decryption2(tknList);

                break;

            case "N":
                response = decryption3(tknList);

                break;

            case "H":
                response = decryption4(tknList);

                break;

            case "K":
                response = decryption5(tknList);

                break;

            default:
                response = "I";

                break;


        }

      return response;
    }

    //The block below extracts the necessary parameters from the token entered

    private String decryption1(String[] arr){
        String a,b,c,d,e,f,g,h,i,j,k,l,m,n,o,p,q,r,s,t;
        a = arr[5]; b= arr[4]; c = arr[6];
        d = arr[7]; e = arr[16]; f = arr[10];
        g = arr[9]; h = arr[8];  i = arr[15];
        j  = arr[14]; k = arr[12]; l = arr[12];
        m = arr[3]; n = arr[2]; o = arr[19];
        p = arr[13]; q = arr[11]; r = arr[1];
        s = arr[18]; t= arr[0];

        tknIkNumber = "IK"+f+e+d+c+b+a;
        tknHectSum= h+"."+g;
        tknStaffId = "T-100000000000"+i+j+k+l;
        tknRandomChars = p+q+r+s+t;



        if(o.matches("1") || o.matches("3")||o.matches("5")||o.matches("W")||o.matches("D")||o.matches("V")){
            tknSeedType= "SM15-C";
        }else if(o.matches("2") || o.matches("4")||o.matches("6")||o.matches("R")||o.matches("B")||o.matches("X")){
            tknSeedType= "PVA8-Grain";
        }else{
            tknSeedType= "UnClassified";
        }

        if(n.matches("A") || n.matches("B") || n.matches("0")){
            tknSeason = "R19";
        }else{
            tknSeason="WrongSeason";
        }



        Log.d(TAG,tknIkNumber+" "+tknHectSum+" "+tknStaffId+" "+tknSeason+" "+tknSeedType);

        //tknValidateDate = validateDate(tknDate);
        //Log.d(TAG,tknValidateDate);

       response =  processDecryptedToken(tknIkNumber,tknHectSum,tknStaffId,tknSeason,tknSeedType,tknRandomChars);

        return response;
    }

    private String decryption2(String[] arr){

        String a,b,c,d,e,f,g,h,i,j,k,l,m,n,o,p,q,r,s,t;
        a = arr[10]; b= arr[5]; c = arr[14];
        d = arr[12]; e = arr[0]; f = arr[1];
        g = arr[15]; h = arr[16];  i = arr[9];
        j  = arr[7]; k = arr[6]; l = arr[19];
        m = arr[3]; n = arr[11]; o = arr[4];
        p = arr[18]; q = arr[8]; r = arr[17];
        s = arr[13]; t= arr[2];

        tknIkNumber = "IK"+f+e+d+c+b+a;
        tknHectSum= h+"."+g;
        tknStaffId = "T-100000000000"+i+j+k+l;
        tknRandomChars = p+q+r+s+t;

        if(o.matches("1") || o.matches("3")||o.matches("5")||o.matches("W")||o.matches("D")||o.matches("V")){
            tknSeedType= "SM15-C";
        }else if(o.matches("2") || o.matches("4")||o.matches("6")||o.matches("R")||o.matches("B")||o.matches("X")){
            tknSeedType= "PVA8-Grain";
        }else{
            tknSeedType= "UnClassified";
        }

        if(n.matches("A") || n.matches("B") || n.matches("0")){
            tknSeason = "R19";
        }else{
            tknSeason="WrongSeason";
        }



        Log.d(TAG,tknIkNumber+" "+tknHectSum+" "+tknStaffId+" "+tknSeason+" "+tknSeedType);

        //tknValidateDate = validateDate(tknDate);
        //Log.d(TAG,tknValidateDate);


        response =  processDecryptedToken(tknIkNumber,tknHectSum,tknStaffId,tknSeason,tknSeedType,tknRandomChars);

        return response;
    }

    private String decryption3(String[] arr){

        String a,b,c,d,e,f,g,h,i,j,k,l,m,n,o,p,q,r,s,t;
        a = arr[2]; b= arr[4]; c = arr[6];
        d = arr[8]; e = arr[10]; f = arr[15];
        g = arr[0]; h = arr[19];  i = arr[12];
        j  = arr[14]; k = arr[17]; l = arr[5];
        m = arr[3]; n = arr[18]; o = arr[7];
        p = arr[11]; q = arr[9]; r = arr[1];
        s = arr[16]; t= arr[13];

        tknIkNumber = "IK"+f+e+d+c+b+a;
        tknHectSum= h+"."+g;
        tknStaffId = "T-100000000000"+i+j+k+l;
        tknRandomChars = p+q+r+s+t;

        if(o.matches("1") || o.matches("3")||o.matches("5")||o.matches("W")||o.matches("D")||o.matches("V")){
            tknSeedType= "SM15-C";
        }else if(o.matches("2") || o.matches("4")||o.matches("6")||o.matches("R")||o.matches("B")||o.matches("X")){
            tknSeedType= "PVA8-Grain";
        }else{
            tknSeedType= "UnClassified";
        }

        if(n.matches("A") || n.matches("B") || n.matches("0")){
            tknSeason = "R19";
        }else{
            tknSeason="WrongSeason";
        }


        Log.d(TAG,tknIkNumber+" "+tknHectSum+" "+tknStaffId+" "+tknSeason+" "+tknSeedType);

        //tknValidateDate = validateDate(tknDate);
        //Log.d(TAG,tknValidateDate);


        response =  processDecryptedToken(tknIkNumber,tknHectSum,tknStaffId,tknSeason,tknSeedType,tknRandomChars);

        return response;
    }

    private String decryption4(String[] arr){
        String a,b,c,d,e,f,g,h,i,j,k,l,m,n,o,p,q,r,s,t;
        a = arr[13]; b= arr[11]; c = arr[5];
        d = arr[7]; e = arr[17]; f = arr[15];
        g = arr[14]; h = arr[9];  i = arr[8];
        j  = arr[10]; k = arr[18]; l = arr[16];
        m = arr[3]; n = arr[19]; o = arr[4];
        p = arr[2]; q = arr[1]; r = arr[0];
        s = arr[12]; t= arr[6];


        tknIkNumber = "IK"+f+e+d+c+b+a;
        tknHectSum= h+"."+g;
        tknStaffId = "T-100000000000"+i+j+k+l;
        tknRandomChars = p+q+r+s+t;

        if(o.matches("1") || o.matches("3")||o.matches("5")||o.matches("W")||o.matches("D")||o.matches("V")){
            tknSeedType= "SM15-C";
        }else if(o.matches("2") || o.matches("4")||o.matches("6")||o.matches("R")||o.matches("B")||o.matches("X")){
            tknSeedType= "PVA8-Grain";
        }else{
            tknSeedType= "UnClassified";
        }

        if(n.matches("A") || n.matches("B") || n.matches("0")){
            tknSeason = "R19";
        }else{
            tknSeason="WrongSeason";
        }



        Log.d(TAG,tknIkNumber+" "+tknHectSum+" "+tknStaffId+" "+tknSeason+" "+tknSeedType);

        //tknValidateDate = validateDate(tknDate);
        //Log.d(TAG,tknValidateDate);


        response =  processDecryptedToken(tknIkNumber,tknHectSum,tknStaffId,tknSeason,tknSeedType,tknRandomChars);

        return response;
    }

    private String decryption5(String[] arr){
        String a,b,c,d,e,f,g,h,i,j,k,l,m,n,o,p,q,r,s,t;
        a = arr[15]; b= arr[5]; c = arr[10];
        d = arr[2]; e = arr[4]; f = arr[1];
        g = arr[0]; h = arr[6];  i = arr[12];
        j  = arr[8]; k = arr[16]; l = arr[14];
        m = arr[3]; n = arr[7]; o = arr[9];
        p = arr[17]; q = arr[18]; r = arr[19];
        s = arr[11]; t= arr[13];

        tknIkNumber = "IK"+f+e+d+c+b+a;
        tknHectSum= h+"."+g;
        tknStaffId = "T-100000000000"+i+j+k+l;
        tknRandomChars = p+q+r+s+t;

        if(o.matches("1") || o.matches("3")||o.matches("5")||o.matches("W")||o.matches("D")||o.matches("V")){
            tknSeedType= "SM15-C";
        }else if(o.matches("2") || o.matches("4")||o.matches("6")||o.matches("R")||o.matches("B")||o.matches("X")){
            tknSeedType= "PVA8-Grain";
        }else{
            tknSeedType= "UnClassified";
        }

        if(n.matches("A") || n.matches("B") || n.matches("0")){
            tknSeason = "R19";
        }else{
            tknSeason="WrongSeason";
        }


        Log.d(TAG,tknIkNumber+" "+tknHectSum+" "+tknStaffId+" "+tknSeason+" "+tknSeedType);

        //tknValidateDate = validateDate(tknDate);
        //Log.d(TAG,tknValidateDate);

        response =  processDecryptedToken(tknIkNumber,tknHectSum,tknStaffId,tknSeason,tknSeedType,tknRandomChars);

        return response;
    }


    private String processDecryptedToken(String iknumber,String hectsum,String staffid, String season,String seedtype, String randChars){
        // Check if the token staff ID is the staff ID of the logged on user from access control
        tknValidateDate = validateDate(season);
        Log.d(TAG,tknValidateDate);

        tknValidateStaffID = validateStaffID(staffid);
        tknValidateRandomChars = validateRandomCharacters(randChars);
        tknStatus = validateTokenStatus();
        Log.d(TAG,"rand char "+tknValidateRandomChars);
        Log.d(TAG,tknValidateStaffID);
        Log.d(TAG,tknStatus);

        if (tknValidateStaffID.trim().matches("L")){
            return "L";
        }
        else if(tknValidateDate.trim().matches("D")){
            return "D";
        }else if (tknRandomChars.trim().matches("R")){
            return "R";
        }else if (tknStatus.trim().matches("U")){
            return "U";
        }

        else if(!tknValidateStaffID.trim().matches("L") && !tknValidateDate.trim().matches("D") && !tknRandomChars.trim().matches("R") && !tknStatus.trim().matches("U")){
            return iknumber+"//"+hectsum+"//"+staffid+"//"+season+"//"+seedtype;
        }
        else{
            return "I";
        }


    }

    private String validateDate(String season){

       if(season.matches("R19")){
           return "valid";
       }else{
           return "D";
       }

    }

    private String validateStaffID(String STAFFID){
        SessionManagement sessionManagement = new SessionManagement(mCtx);
        HashMap<String,String> user = sessionManagement.getUserDetails();
        String sessionRole = user.get(SessionManagement.KEY_STAFF_ROLE);
        String sessionStaffId = user.get(SessionManagement.KEY_STAFF_ID);
        if(sessionStaffId.trim().matches(STAFFID)){
            return sessionRole;
        }
        else return "L";
    }

    private String validateRandomCharacters(String concatRandomChar){
       String[] a =   {"ARY8S","BA6K7S","C2AKS","D4CAS","EA6IS","6AKLS","G6CXN","HAOLS","IZZKA","JLPAS","KQWLA","LUIAS","NLASA","NVSJA","LSKAS","PYBSHA","Q112AS","RNAPW7","SAJS6","TNAJK8","UKAS6","VQWE2","WJAK3","XLASK","11Y99","012Z33"};
        Log.d(TAG,"rand char "+concatRandomChar);
           int val=0;
           for(int i = 0; i<a.length; i++){
               if(concatRandomChar.trim().matches(a[i])) val+= 1;
           }

       if(val >0) return "valid";
       else  return "R";


    }

    private String validateTokenStatus(){

        String token = "";

        SessionManagement sessionManagement  = new SessionManagement(mCtx);
        HashMap<String,String> token1 = sessionManagement.getTokenDetails();
        token = token1.get(sessionManagement.TKN_TOKEN_ENTERED);

      DBOpenHelper dbOpenHelper = new DBOpenHelper(mCtx);
      if(dbOpenHelper.validateToken(token) && dbOpenHelper.validateTokenFrmCompleteTransactions(token)) return "good";
      else return "U";

    }



}
