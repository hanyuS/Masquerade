package com.example.masquerade;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class contactAdapter extends RecyclerView.Adapter<contactAdapter.contactHolder> {
    private ArrayList<contactItem> mContactList;

    public static class contactHolder extends RecyclerView.ViewHolder{

        public ImageView mImageView;
        public TextView contactNickname;
        public TextView pairedTag;


        public contactHolder(@NonNull View itemView) {
            super(itemView);
            mImageView = (ImageView) itemView.findViewById(R.id.avatar);
            contactNickname = (TextView) itemView.findViewById(R.id.contact_name);
            pairedTag = (TextView) itemView.findViewById(R.id.contact_tag);
        }
    }

    public contactAdapter(ArrayList<contactItem> exampleList){
        mContactList = exampleList;
    }

    @NonNull
    @Override
    public contactHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.contact_item, viewGroup, false);
        contactHolder vh = new contactHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull contactHolder contactHolder, int i) {
        contactItem currentContact = mContactList.get(i);
        contactHolder.mImageView.setImageResource(currentContact.getAvatarSource());
        contactHolder.contactNickname.setText(currentContact.getContactNickname());
        contactHolder.pairedTag.setText(currentContact.getPairedTag());

    }

    @Override
    public int getItemCount() {
        return mContactList.size();
    }
}
