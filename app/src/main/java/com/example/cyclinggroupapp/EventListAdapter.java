package com.example.cyclinggroupapp;

import static androidx.core.content.ContextCompat.startActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Locale;

public class EventListAdapter extends RecyclerView.Adapter<EventListAdapter.MyViewHolder> {


    Context context;
    ArrayList<Event> list;
    ArrayList<Event> eventsFull;
    private OnClickListener onClickListener;

    public EventListAdapter(Context context, ArrayList<Event> list) {
        this.context = context;
        this.list = list;
        this.eventsFull = new ArrayList<>(list);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
        View v = LayoutInflater.from(context).inflate(R.layout.eventforeventlist,parent, false);
        return new MyViewHolder(v);
    }

    public void onBindViewHolder(@NonNull MyViewHolder holder, @SuppressLint("RecyclerView") int position){
        Event event = list.get(position);
        holder.EventName.setText(event.getEventName());
        holder.EventRegion.setText(event.getEventRegion());
        holder.EventType.setText(event.getEventType());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(onClickListener != null){
                    onClickListener.onClick(position, event);
                }
            }
        });
    }
    @Override
    public int getItemCount(){
        return list.size();
    }

    public void setOnClickListener(OnClickListener onClickListener){
        this.onClickListener = onClickListener;
    }

    public interface OnClickListener {
        void onClick(int position, Event event);
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        TextView EventName, EventRegion, EventType;

        public MyViewHolder(@NonNull View itemView){
            super(itemView);

            EventName = itemView.findViewById(R.id.tvEventName);
            EventRegion = itemView.findViewById(R.id.tvEventRegion);
            EventType = itemView.findViewById(R.id.tvEventType);

        }
    }
    public void resetList() {
        list.clear();
        list.addAll(eventsFull);
        notifyDataSetChanged();
    }

    public void filter(String text) {
        list.clear();
        if(text.isEmpty()){
            list.addAll(eventsFull);
        } else{
            text = text.toLowerCase();
            for(Event event : eventsFull){
                if(event.getEventName().toLowerCase().contains(text)){
                    list.add(event);
                }
                else if(event.getEventType().toLowerCase().contains(text)){
                    list.add(event);
                }
                else if(event.getEventRegion().toLowerCase().contains(text)){
                    list.add(event);
                }
            }
        }
        notifyDataSetChanged();
    }

}
