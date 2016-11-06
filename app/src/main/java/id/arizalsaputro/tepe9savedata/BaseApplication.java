package id.arizalsaputro.tepe9savedata;

import android.app.Application;

import io.realm.Realm;

/**
 * Created by muharizals on 06/11/2016.
 */

public class BaseApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Realm.init(this);
    }
}
