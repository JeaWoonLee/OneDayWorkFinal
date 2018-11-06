package com.edu.lx.onedayworkfinal.util.session;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.edu.lx.onedayworkfinal.login.LoginActivity;

import java.util.HashMap;

public class SessionManager {

    SharedPreferences preferences;

    // Editor for shared preferences
    Editor editor;

    //Context
    Context context;

    //Shared pref mode
    int PRIVATE_MODE = 0;

    //SharedPref file name
    private static final String PREF_NAME = "AndroidHivePref";

    //All shared Preferences Keys
    private static final String IS_LOGIN = "IsLoggedIn";

    //User name (make variable public to access from outside)
    public static final String KEY_NAME = "name";

    //User id (make variable public to access from outside)
    public static final String KEY_ID = "id";

    //User Index
    public static final String KEY_INDEX = "userIndex";
    public static final String IS_SEEKER = "0";
    public static final String IS_OFFER = "1";
    //Constructor
    public SessionManager (Context context) {
        this.context = context;
        preferences = context.getSharedPreferences(PREF_NAME,PRIVATE_MODE);
        editor = preferences.edit();
    }

    //Create login session
    public void createLoginSession(String name, String id, String userIndex) {
        //Storing login value as TRUE
        editor.putBoolean(IS_LOGIN,true);

        //Storing name in pref
        editor.putString("name",name);

        //Storing id in pref
        editor.putString("id",id);

        //Storing userIndex in pref
        editor.putString("userIndex",userIndex);

        //commit changes
        editor.commit();
    }

    /**
     * check login method will check user login status
     * if false it will redirect user to login page
     * Else won't do anything
     */
    public void checkLogin() {
        //Check login status
        if (!this.isLoggedIn()) {
            // user is not logged in redirect him to Login Activity
            Intent intent = new Intent(context,LoginActivity.class);
            // Closing all the Activities
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            //Add new Flag to start new Activity
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            //Starting Login Activity
            context.startActivity(intent);

        }
    }

    /**
     * Get stored session data
     * */
    public HashMap<String, String> getUserDetails(){
        HashMap<String, String> user = new HashMap<String, String>();
        // user name
        user.put(KEY_NAME, preferences.getString(KEY_NAME, null));

        // user id
        user.put(KEY_ID, preferences.getString(KEY_ID, null));

        user.put(KEY_INDEX,preferences.getString(KEY_INDEX,null));

        // return user
        return user;
    }

    /**
     * Clear session details
     * */
    public void logoutUser(){
        // Clearing all data from Shared Preferences
        editor.clear();
        editor.commit();

        // After logout redirect user to Loing Activity
        Intent intent = new Intent(context, LoginActivity.class);
        // Closing all the Activities
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        // Add new Flag to start new Activity
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        // Staring Login Activity
        context.startActivity(intent);
    }
    /**
     * Quick check for login
     * **/
    // Get Login State
    public boolean isLoggedIn(){
        return preferences.getBoolean(IS_LOGIN, false);
    }
}
