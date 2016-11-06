package id.arizalsaputro.tepe9savedata.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by muharizals on 06/11/2016.
 */

public class UserUtil {
    private SharedPreferences preferences;
    private UserUtil(Context c){
        preferences = PreferenceManager.getDefaultSharedPreferences(c);
    }

    public static UserUtil with(Context c){
        return new UserUtil(c);
    }

    public void setBoleanProperty(String key,boolean value){
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(key,value);
        editor.apply();
    }
    public boolean getBoleanProperty(String key){
        return preferences.getBoolean(key,false);
    }

    public void setNotFirstTime(){
        this.setBoleanProperty(Constant.USER_FIRST_TIME,false);
    }

    public boolean isUserFirstTime(){
        return preferences.getBoolean(Constant.USER_FIRST_TIME,true);
    }
}
