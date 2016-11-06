package id.arizalsaputro.tepe9savedata;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.util.Log;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import id.arizalsaputro.tepe9savedata.activity.DetailActivity;
import id.arizalsaputro.tepe9savedata.adapter.ListHewanAdapter;
import id.arizalsaputro.tepe9savedata.db.DBController;
import id.arizalsaputro.tepe9savedata.model.Hewan;
import id.arizalsaputro.tepe9savedata.rest.ApiInterface;
import id.arizalsaputro.tepe9savedata.rest.RestClient;
import id.arizalsaputro.tepe9savedata.util.DividerItemDecoration;
import id.arizalsaputro.tepe9savedata.util.RecyclerTouchListener;
import id.arizalsaputro.tepe9savedata.util.UserUtil;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    private ListHewanAdapter lisadapter;
    private List<Hewan> listhewan = new ArrayList<>();
    private RecyclerView recyclerView;
    ActionMode acm;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if(UserUtil.with(this).isUserFirstTime()){
            getDataFromApi();
        }else{
            listhewan = DBController.with(this).getRealm().where(Hewan.class).findAll();
        }


        lisadapter = new ListHewanAdapter(this,null,true,listhewan);

        recyclerView = (RecyclerView)findViewById(R.id.itemsRecyclerView);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(this,LinearLayoutManager.VERTICAL));

        recyclerView.setAdapter(lisadapter);

        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(),recyclerView,new MainActivity.ClickListener(){

            @Override
            public void onClick(View view, int position) {
                Intent in= new Intent(MainActivity.this, DetailActivity.class);
                in.putExtra("detail",position);
                startActivity(in);
            }

            @Override
            public void onLongClick(View view, final int position) {
                  acm = startActionMode(new ActionMode.Callback() {
                    @Override
                    public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                        MenuInflater inflater = mode.getMenuInflater();
                        inflater.inflate(R.menu.menu_action_mode, menu);
                        return true;
                    }

                    @Override
                    public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                        return false;
                    }

                    @Override
                    public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.menu_edit:
                                openDialogEdit(lisadapter.get(position).getNama(),position);
                                return true;

                            case R.id.menu_delete:
                                hapusItem(position);
                                return true;
                            default:
                                return false;
                        }
                    }

                    @Override
                    public void onDestroyActionMode(ActionMode mode) {

                    }
                });
            }
        }));

    }


    private void hapusItem(int position){
        final Hewan tmp =  DBController.with(this).getRealm().where(Hewan.class).findAll().get(position);
        DBController.with(this).deleteData(position);
        lisadapter.notifyDataSetChanged();
        final CoordinatorLayout coordinatorLayout = (CoordinatorLayout)findViewById(R.id.activity_main);
        acm.finish();
        Snackbar.make(coordinatorLayout, getString(R.string.success_deleted), Snackbar.LENGTH_LONG)
                .setAction(getString(R.string.undo), new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        DBController.with(MainActivity.this).saveData(tmp);
                        Snackbar.make(coordinatorLayout,getString(R.string.data_restored),Snackbar.LENGTH_LONG).show();
                        lisadapter.notifyDataSetChanged();
                    }
                }).show();
    }

    private void getDataFromApi(){
        Log.d("api","from api");
        ApiInterface apiInterface = RestClient.getClient().create(ApiInterface.class);

        Call<ArrayList<Hewan>> hewanFromApi = apiInterface.getHewan("hewan.json");

        hewanFromApi.enqueue(new Callback<ArrayList<Hewan>>() {
            @Override
            public void onResponse(Call<ArrayList<Hewan>> call, Response<ArrayList<Hewan>> response) {
                Log.d("api","berhasil");
                DBController.with(MainActivity.this).saveData(response.body());
                for (Hewan h:response.body()){
                    listhewan.add(h);
                }
                lisadapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<ArrayList<Hewan>> call, Throwable t) {
                Toast.makeText(MainActivity.this,getString(R.string.api_failed),Toast.LENGTH_SHORT).show();
                Log.d("api","gagal");
            }
        });


        UserUtil.with(this).setNotFirstTime();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.main_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){

        if(item.getItemId() == R.id.menu_add){
            openDialogAdd();
        }

        return super.onOptionsItemSelected(item);
    }

    private void openDialogEdit(String txt, final int position){
        AlertDialog.Builder builder = new AlertDialog.Builder(this)
                .setTitle(getString(R.string.dialog_title_edit));

        View v= getLayoutInflater().inflate(R.layout.layout_new,null);

        final EditText input = (EditText)v.findViewById(R.id.text_input);

        input.setText(txt);

        builder.setView(v);
        builder.setNegativeButton(getString(R.string.dialog_cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        }).setPositiveButton(getString(R.string.dialog_save), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(input.getText().toString().trim().isEmpty()){
                    Toast.makeText(MainActivity.this,getString(R.string.fill_name),Toast.LENGTH_SHORT).show();
                }else{
                    DBController.with(MainActivity.this).updateData(input.getText().toString(),position);
                    lisadapter.notifyDataSetChanged();
                    Toast.makeText(MainActivity.this,getString(R.string.success_update),Toast.LENGTH_SHORT).show();
                    acm.finish();
                }
            }
        });

        builder.show();
    }


    private void openDialogAdd(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this)
                .setTitle(getString(R.string.dialog_title));



        View v= getLayoutInflater().inflate(R.layout.layout_new,null);

        final EditText input = (EditText)v.findViewById(R.id.text_input);
        input.setHint(getString(R.string.placeholder));

        builder.setView(v);
        builder.setNegativeButton(getString(R.string.dialog_cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        }).setPositiveButton(getString(R.string.dialog_save), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(input.getText().toString().trim().isEmpty()){
                    Toast.makeText(MainActivity.this,getString(R.string.fill_name),Toast.LENGTH_SHORT).show();
                }else{
                    saveNewHewan(input.getText().toString());
                }
            }
        });

        builder.show();

    }



    private void saveNewHewan(String name){
        DBController.with(this).saveData(new Hewan(name,null));
        lisadapter.notifyDataSetChanged();
        Toast.makeText(MainActivity.this,getString(R.string.success_new),Toast.LENGTH_SHORT).show();
    }

    public interface ClickListener {
        void onClick(View view, int position);
        void onLongClick(View view, int position);
    }

    @Override
    protected void onDestroy() {
        finish();
        Runtime.getRuntime().gc();
        System.gc();
        super.onDestroy();
    }

}
