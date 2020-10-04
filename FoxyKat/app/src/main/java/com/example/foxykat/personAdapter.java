package com.example.foxykat;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

//import com.squareup.picasso.MemoryPolicy;
//import com.squareup.picasso.NetworkPolicy;
//import com.squareup.picasso.Picasso;

import com.example.foxykat.ui.home.HomeFragment;

import java.util.ArrayList;

public class personAdapter extends RecyclerView.Adapter<personAdapter.ViewHolder> {
    ItemClicked context;

    ArrayList<person> people;
    public interface ItemClicked {
        void onItemClicked(int index, ArrayList<person> people) ;
        void onImgClicked(int index, ArrayList<person> people) ;

    }
    public personAdapter(ArrayList<person> people , Context context) {
        this.people = people;
        this.context = (botnav) context;
    }


    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView tvName , tvMsg, tvTime;
        ImageView img;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
             tvName  =  itemView.findViewById(R.id.tvName);
             tvMsg = itemView.findViewById(R.id.tvMsg);
             tvTime  = itemView.findViewById(R.id.tvTime);
             img = itemView.findViewById(R.id.img);
             itemView.setOnClickListener(new View.OnClickListener() {
                 @Override
                 public void onClick(View view) {
                    context.onItemClicked(people.indexOf((person) view.getTag()),people);
                 }
             });
             img.setOnClickListener(new View.OnClickListener() {
                 @Override
                 public void onClick(View view) {
                     context.onImgClicked(people.indexOf((person) view.getTag()),people);
                 }
             });


        }
    }

    @NonNull
    @Override
    public personAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_items, viewGroup, false );
        return new ViewHolder(v);
    }

    @NonNull
    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        viewHolder.itemView.setTag(people.get(i));
        viewHolder.tvName.setText(people.get(i).getName());
        viewHolder.tvMsg.setText(people.get(i).getMsg());
        viewHolder.tvTime.setText(people.get(i).getTime());
        if ( people.get(i).getImg().equals("logo")) {
            viewHolder.img.setImageResource(R.mipmap.test_round);
        }
        else if ( people.get(i).getImg().equals("")){}else {
            String url = people.get(i).getImg();


          /*  Picasso.get().load(url)
                    .memoryPolicy(MemoryPolicy.NO_CACHE)
                    .networkPolicy(NetworkPolicy.NO_CACHE)
                    .resize(150, 150)
                    .centerCrop()
                    .error(R.drawable.face)
                    .into(viewHolder.img);
        */
        }






    }

    @Override
    public int getItemCount() {
        return people.size();
    }
}
