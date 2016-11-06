package id.arizalsaputro.tepe9savedata.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import id.arizalsaputro.tepe9savedata.MainActivity;

public class SplashScreenActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }
}
