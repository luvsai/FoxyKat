package com.example.foxykat;

import android.content.Context;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Objects;

import static java.util.Objects.*;

public class MessageListAdapter extends RecyclerView.Adapter {
    private static final int SENT = 1;
    private static final int RECEIVED = 0;
    ViewGroup vg;
    int token=0;

    onLongItemClickListener mOnLongItemClickListener;

    public void setOnLongItemClickListener(onLongItemClickListener onLongItemClickListener) {
        mOnLongItemClickListener = onLongItemClickListener;
    }

    public interface onLongItemClickListener {
        void ItemLongClicked(View v, int position);
    }












    ItemClicked context;

    ArrayList<message> people;
    public interface ItemClicked {
    void onItemClicked(int index, ArrayList<message> people) ;
      //  void onImgClicked(int index) ;

    }
    private Context mContext;


    public MessageListAdapter(ArrayList<message> people , Context context) {
        this.people = people;
        this.context = (MessageListActivity) context;
    }




    // Determines the appropriate ViewType according to the sender of the message.
    @Override
    public int getItemViewType(int position) {
        message message = (message) people.get(position);

        if (message.getToken() == 0) {
            // If the current user is the sender of the message
            return SENT;
        } else {
            // If some other user sent the message
            return RECEIVED;
        }
    }


    //===-------  only once

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;

        if (viewType == SENT) {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_message_sent, parent, false);
            return new SentMessageHolder(view);
        } else if (viewType == RECEIVED) {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_message_received, parent, false);
            return new ReceivedMessageHolder(view);
        }
        return null;
    }




    ////--------------------------------


    @NonNull
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        token = i;
        View v;
        viewHolder.itemView.setTag(people.get(i));
        viewHolder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (mOnLongItemClickListener != null) {
                    mOnLongItemClickListener.ItemLongClicked(v, token);
                }

                return true;
            }
        });

        message msg = people.get(i);
        switch (viewHolder.getItemViewType()) {
            case SENT:
                ((SentMessageHolder) viewHolder).bind(msg);
                break;
            case RECEIVED:
                ((ReceivedMessageHolder) viewHolder).bind(msg);
        }





    }

    @Override
    public int getItemCount() {
        return people.size();
    }



    private class SentMessageHolder extends RecyclerView.ViewHolder  implements View.OnCreateContextMenuListener{
        TextView messageText, timeText,nameText;
        ImageView profileImage ,seen;
        View itemView;

        SentMessageHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
            messageText = (TextView) itemView.findViewById(R.id.text_message_body);
            timeText = (TextView) itemView.findViewById(R.id.text_message_time);
            nameText = (TextView) itemView.findViewById(R.id.text_message_name);
            profileImage = (ImageView) itemView.findViewById(R.id.image_message_profile);
            seen = (ImageView) itemView.findViewById(R.id.seen);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                }
            });

        }

        void bind(message message) {
            messageText.setText(message.getMsg());

            // Format the stored timestamp into a readable String using method.
            timeText.setText(message.getTime());

            nameText.setText(message.getName());

            // Insert the profile image from the URL into the ImageView.

            if(message.getSeen() == 0 ) {
                seen.setImageResource(R.drawable.msgsent);

            }else {
                seen.setImageResource(R.drawable.mesgread);
            }
        }
        @Override
        public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) {
            contextMenu.setHeaderTitle("Select The Action!");
            contextMenu.add(0, view.getId(), 0, "City");
            contextMenu.add(0, view.getId(), 0, "Lga");
        }

    }

    private class ReceivedMessageHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener {
        TextView messageText, timeText, nameText;
        ImageView profileImage ,seen;

        ReceivedMessageHolder(View itemView) {
            super(itemView);

            messageText = (TextView) itemView.findViewById(R.id.text_message_body);
            timeText = (TextView) itemView.findViewById(R.id.text_message_time);
            nameText = (TextView) itemView.findViewById(R.id.text_message_name);
            profileImage = (ImageView) itemView.findViewById(R.id.image_message_profile);
            seen = (ImageView) itemView.findViewById(R.id.seen);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    context.onItemClicked(people.indexOf((message) view.getTag()),people);
                }
            });

        }

        void bind(message message) {
            messageText.setText(message.getMsg());

            // Format the stored timestamp into a readable String using method.
            timeText.setText(message.getTime());

            nameText.setText(message.getName());

            // Insert the profile image from the URL into the ImageView.

            if(message.getSeen() == 0 ) {
                seen.setImageResource(R.drawable.msgsent);
            }else {
                seen.setImageResource(R.drawable.mesgread);
            }
     /*
            String url = message.getImg();

          /*  Picasso.get().load(url)
                    .memoryPolicy(MemoryPolicy.NO_CACHE)
                    .networkPolicy(NetworkPolicy.NO_CACHE)
                    .resize(150, 150)
                    .centerCrop()
                    .error(R.drawable.face)
                    .into(viewHolder.img);

      */


        }
        @Override
        public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) {
            contextMenu.setHeaderTitle("Select The Action!");
            contextMenu.add(0, view.getId(), 0, "City");
            contextMenu.add(0, view.getId(), 0, "Lga");
        }

    }

}
