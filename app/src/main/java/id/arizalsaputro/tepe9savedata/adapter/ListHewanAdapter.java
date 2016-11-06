package id.arizalsaputro.tepe9savedata.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import id.arizalsaputro.tepe9savedata.R;
import id.arizalsaputro.tepe9savedata.model.Hewan;
import io.realm.OrderedRealmCollection;
import io.realm.RealmRecyclerViewAdapter;

/**
 * Created by muharizals on 06/11/2016.
 */

public class ListHewanAdapter extends  RealmRecyclerViewAdapter<Hewan,ListHewanAdapter.ViewHolder> {
    private List<Hewan> listhewan ;

    public ListHewanAdapter(@NonNull Context context, @Nullable OrderedRealmCollection<Hewan> data, boolean autoUpdate,List<Hewan> list) {
        super(context, data, autoUpdate);
        this.listhewan=list;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.item_name.setText(listhewan.get(position).getNama());
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public  TextView item_name;
        public ViewHolder(View view){
            super(view);
            item_name = (TextView)view.findViewById(R.id.item_name);
        }
    }



    @Override
    public int getItemCount() {
        return listhewan.size();
    }

    public Hewan get(int position){
        return listhewan.get(position);
    }

    public void add(Hewan data){
        insert(data,listhewan.size());
    }

    public void insert(Hewan data,int position){
        listhewan.add(data);
        notifyItemInserted(position);
    }
}
