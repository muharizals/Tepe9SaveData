package id.arizalsaputro.tepe9savedata.activity;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import id.arizalsaputro.tepe9savedata.R;
import id.arizalsaputro.tepe9savedata.db.DBController;
import id.arizalsaputro.tepe9savedata.model.Hewan;

public class DetailActivity extends AppCompatActivity {
    private TextView lihat,sumber;
    private ImageView gambar;
    Hewan hewan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        lihat=(TextView)findViewById(R.id.lihat);
        sumber=(TextView)findViewById(R.id.sumber);
        gambar = (ImageView) findViewById(R.id.gambar);

        int position  = getIntent().getIntExtra("detail",0);

        hewan = DBController.with(this).getData(position);

        String txt = getString(R.string.detail_lihat) + " " + hewan.getNama() +" "+ (hewan.getLihat()+ 1) + "x";
        lihat.setText(txt);

        DBController.with(this).updateData(hewan.getLihat()+1,position);

        getSupportActionBar().setTitle(hewan.getNama());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if(hewan.getFoto() != null){
            sumber.setText(hewan.getFoto());
            Picasso.with(this)
                    .load(hewan.getFoto())
                    .resize(500, 500)
                    .centerCrop()
                    .into(gambar);
        }else{
            sumber.setVisibility(View.GONE);
            gambar.setVisibility(View.GONE);
        }



    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == android.R.id.home) {
            onBackPressed();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        finish();
        Runtime.getRuntime().gc();
        System.gc();
        super.onDestroy();
    }

    public void openWeb(View v){
        if(hewan.getFoto() != null){
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(hewan.getFoto())));
        }
    }
}
