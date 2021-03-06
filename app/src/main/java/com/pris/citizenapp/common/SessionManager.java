package com.pris.citizenapp.common;

/**
 * Created by manav on 5/4/17.
 */

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.pris.citizenapp.entrolab.SplashScreen;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

public class SessionManager {
    // Shared Preferences
    SharedPreferences pref;

    // Editor for Shared preferences
    Editor editor;
    // Shared pref mode
    int PRIVATE_MODE = 0;
    // Context
    Context _context;



    // Sharedpref file name
    public static final String PREF_NAME = "com.entrolabs.prisap.SessionFile";

    // All Shared Preferences Keys
    private static final String IS_LOGIN = "IsLoggedIn";


    // User name (make variable public to access from outside)
    public static final String USER_NAME = "username";

    // Email address (make variable public to access from outside)
    public static final String USER_EMAIL = "email";


    // User mobile (make variable public to access from outside)
    public static final String USER_MOBILE = "mobile";

    // Email address (make variable public to access from outside)
    public static final String USER_LEVEL = "userlevel";

    public static final String USER_DIVISION = "division";
    public static final String USER_PREFIX = "prefix";

    // User Full name (make variable public to access from outside)
    public static final String USER_FULL_NAME = "name";

    // Last active timestamp address (make variable public to access from outside)
    public static final String USER_TIME = "Last Active time";
    private static final int RC_INTERNET_STATE = 111;




    // Constructor
    public SessionManager(Context context){

        try {
            this._context = context;
            pref = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
            editor = pref.edit();
        }
        catch (NullPointerException e){
            e.printStackTrace();
        }
    }

    /**
     * Create login session
     * */


    public void createLoginSession(JSONObject user){
        // Storing login value as TRUE
        editor.putBoolean(IS_LOGIN, true);

        Log.d("CreateLogin",user.toString());

        // Storing Standard params in pref
        try {
            editor.putString(USER_NAME, user.getString("username"));
            editor.putString(USER_EMAIL, user.getString("email"));
            if(user.has("mobile")) {
                editor.putString(USER_MOBILE, user.getString("mobile"));
            }
            editor.putString(USER_FULL_NAME, user.getString("name"));
            if(user.has("userlevel")) {
                editor.putString(USER_LEVEL, user.getString("userlevel"));
            }
            editor.putString("district", user.getString("district"));
            editor.putString("division", user.getString("division"));
            editor.putString("mandal", user.getString("mandal"));
            editor.putString("panchayat", user.getString("panchayat"));
            if(user.has("panchayat_name")) {
                editor.putString("panchayat_name", user.getString("panchayat_name"));
            }
            editor.putString("gender", user.getString("gender"));
            editor.putString("dob", user.getString("dob"));
            editor.putString("aadhar", user.getString("aadhar"));
/*
            if(user.has("profilepic")){
                editor.putString("profilepic", user.getString("profilepic"));
            }
            */

        } catch (JSONException e) {
            e.printStackTrace();
        }
        // commit changes
        editor.commit();
    }




    public Boolean hasVal(String key){

        return pref.contains(key);

    }

    public String getStrVal(String key){
        return pref.getString(key,"na");
    }

    public void removeVal(String key){

        editor.remove(key);
        editor.commit();

    }

    public void createFakeSession(){
        // Storing login value as TRUE
        editor.putBoolean(IS_LOGIN, true);


        // Storing Standard params in pref
        editor.putString(USER_NAME, "testuser");
        editor.putString(USER_EMAIL, "email");
        editor.putString(USER_MOBILE, "mobile");
        editor.putString(USER_FULL_NAME, "name");
        editor.putString(USER_LEVEL,"1");
        editor.putString(USER_TIME, "time");



        // commit changes
        editor.commit();
    }


    public String getImei(){
        TelephonyManager mTelephonyMgr;
        mTelephonyMgr = (TelephonyManager) _context.getSystemService(_context.TELEPHONY_SERVICE);
        return mTelephonyMgr.getDeviceId();


    }
    /**
     * Check login method wil check user login status
     * If false it will redirect user to login page
     * Else won't do anything
     * */
    public void checkLogin(){
        // Check login status

        Log.d("session","checkLogin: "+IS_LOGIN.toString());
        if(!this.isLoggedIn()){
            Log.d("session","checkLogin return false");
            // user is not logged in redirect him to Login Activity
            Intent i = new Intent(_context, SplashScreen.class);
            // Closing all the Activities
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            // Add new Flag to start new Activity
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            // Staring Login Activity
            _context.startActivity(i);
        }
        else{
            Log.d("session","checkLogin return true");
        }

    }



    /**
     * Get stored session data
     * */
    public HashMap<String, String> getUserDetails(){
        HashMap<String, String> user = new HashMap<String, String>();
        // user name
        user.put(USER_NAME, pref.getString(USER_NAME, null));
        // user email id
        user.put(USER_EMAIL, pref.getString(USER_EMAIL, null));
        // user full name
        user.put(USER_FULL_NAME, pref.getString(USER_FULL_NAME, null));
        // user level
        user.put(USER_LEVEL, pref.getString(USER_LEVEL, null));
        // user mobile
        user.put(USER_MOBILE, pref.getString(USER_MOBILE, null));




        // return user
        return user;
    }

    /**
     * Clear session Details
     * */
    public void logoutUser(){
        // Clearing all data from Shared Preferences
        editor.clear();
        editor.commit();


        SharedPreferences ratePrefs = _context.getSharedPreferences("com.entrolabs.prisap.routines",0);

        SharedPreferences.Editor redit = ratePrefs.edit();
        redit.clear();
        redit.commit();


        SharedPreferences ratPrefs = _context.getSharedPreferences(PREF_NAME,0);

        SharedPreferences.Editor rdit = ratePrefs.edit();
        rdit.clear();
        rdit.commit();



    }

    public void storeVal(String key,String value){
        editor.putString(key,value);
        editor.commit();
    }

    /**
     * Quick check for login
     * **/
    // Get Login State
    public boolean isLoggedIn(){
        return pref.getBoolean(IS_LOGIN, false);
    }



    public boolean methodRequiresPermission(String[] perms,int permission) {

        if (EasyPermissions.hasPermissions(_context, perms)) {
            // Already have permission, do the thing
            // ...
            return true;
        } else {
            // Do not have permissions, request them now
            EasyPermissions.requestPermissions(this, "Need these permissions",
                    permission, perms);
        }

        return false;
    }

    public boolean haveNetworkConnection() {





        ConnectivityManager connectivity = (ConnectivityManager) _context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null)
        {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null)
                for (int i = 0; i < info.length; i++)
                    if (info[i].getState() == NetworkInfo.State.CONNECTED)
                    {
                        return true;
                    }

        }
        return false;
    }


}
