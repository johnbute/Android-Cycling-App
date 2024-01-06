package com.example.cyclinggroupapp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class EventTypeListAdapter extends RecyclerView.Adapter<EventTypeListAdapter.MyViewHolder>{



    Context context;
    ArrayList<EventType> list;
    private OnClickListener onClickListener;


    public EventTypeListAdapter(Context context, ArrayList<EventType> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
        View v = LayoutInflater.from(context).inflate(R.layout.eventforeventtypelist,parent, false);
        return new MyViewHolder(v);
    }


    public void onBindViewHolder(@NonNull MyViewHolder holder, @SuppressLint("RecyclerView") int position){
        EventType event = list.get(position);
        holder.EventTypeName.setText(event.getEventType());
        holder.EventDescription.setText(event.getEventDescription());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(onClickListener != null){
                    onClickListener.onClick(position, event);
                }
            }
        });
    }

    public int getItemCount(){
        return list.size();
    }

    public void setOnClickListener(OnClickListener onClickListener){
        this.onClickListener = onClickListener;
    }

    public interface OnClickListener {
        void onClick(int position, EventType event);
    }


    public static class MyViewHolder extends RecyclerView.ViewHolder{

        TextView EventTypeName, EventDescription;

        public MyViewHolder(@NonNull View itemView){
            super(itemView);
            EventTypeName = itemView.findViewById(R.id.tvEventTypeName);
            EventDescription = itemView.findViewById(R.id.tvEventTypeDescription);
        }



    }




}
