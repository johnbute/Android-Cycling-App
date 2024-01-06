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

public class AccountListAdapter extends RecyclerView.Adapter<AccountListAdapter.MyViewHolder> {


        Context context;
        ArrayList<Account> list;
        private OnClickListener onClickListener;

public AccountListAdapter(Context context, ArrayList<Account> list) {
        this.context = context;
        this.list = list;
        }

@NonNull
@Override
public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
        View v = LayoutInflater.from(context).inflate(R.layout.acountcardforaccountmoderation, parent, false);
        return new MyViewHolder(v);
        }

public void onBindViewHolder(@NonNull MyViewHolder holder, @SuppressLint("RecyclerView") int position){
        Account account = list.get(position);
        holder.AccountEmail.setText(account.getAccountEmail());
        holder.AccountUsername.setText(account.getAccountUsername());
        holder.AccountRole.setText(account.getAccountRole());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
@Override
public void onClick(View view) {
        if(onClickListener != null){
        onClickListener.onClick(position, account);
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
    void onClick(int position, Account account);
}

public static class MyViewHolder extends RecyclerView.ViewHolder{

    TextView AccountUsername, AccountEmail, AccountRole;

    public MyViewHolder(@NonNull View itemView){
        super(itemView);

        AccountUsername = itemView.findViewById(R.id.tvAccountUsername);
        AccountEmail = itemView.findViewById(R.id.tvAccountEmail);
        AccountRole = itemView.findViewById(R.id.tvAccountRole);

    }



}



}