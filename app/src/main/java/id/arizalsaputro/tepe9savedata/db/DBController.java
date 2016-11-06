package id.arizalsaputro.tepe9savedata.db;

import android.app.Activity;
import android.app.Application;
import android.support.v4.app.Fragment;

import java.util.ArrayList;

import id.arizalsaputro.tepe9savedata.model.Hewan;
import io.realm.Realm;

/**
 * Created by muharizals on 06/11/2016.
 */

public class DBController {

    private static DBController instance;
    private  Realm realm;

    public DBController(Application application){
        realm = Realm.getDefaultInstance();
    }

    public static DBController with(Fragment fragment) {

        if (instance == null) {
            instance = new DBController(fragment.getActivity().getApplication());
        }
        return instance;
    }

    public static DBController with(Activity activity) {

        if (instance == null) {
            instance = new DBController(activity.getApplication());
        }
        return instance;
    }

    public static DBController with(Application application) {

        if (instance == null) {
            instance = new DBController(application);
        }
        return instance;
    }

    public static DBController getInstance() {

        return instance;
    }

    public Realm getRealm() {
        return realm;
    }

    public void saveData(ArrayList<Hewan> hewan){
        realm.beginTransaction();
        for (Hewan ihewan:hewan){
            realm.copyToRealm(ihewan);
        }
        realm.commitTransaction();
    }

    public void saveByName(String name){
        realm.beginTransaction();
        realm.copyToRealm(new Hewan(name,null));
        realm.commitTransaction();
    }

    public Hewan getData(int position){
        return realm.where(Hewan.class).findAll().get(position);
    }

    public void saveData(Hewan h){
        realm.beginTransaction();
        realm.copyToRealm(h);
        realm.commitTransaction();
    }

    public void updateData(String baru,int position){
        realm.beginTransaction();
        realm.where(Hewan.class).findAll().get(position).setNama(baru);
        realm.commitTransaction();
    }

    public void updateData(int lihat,int position){
        realm.beginTransaction();
        realm.where(Hewan.class).findAll().get(position).setLihat(lihat);
        realm.commitTransaction();
    }

    public void deleteData(int position){
        realm.beginTransaction();
        realm.where(Hewan.class).findAll().get(position).deleteFromRealm();
        realm.commitTransaction();
    }

}
