package com.example.masquerade;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class contactAdapter extends RecyclerView.Adapter<contactAdapter.contactHoler> {
    private ArrayList<contactItem> mContactList;

    public static class contactHoler extends RecyclerView.ViewHolder{

        public ImageView mImageView;
        public TextView contactNickname;
        public TextView pairedTag;


        public contactHoler(@NonNull View itemView) {
            super(itemView);
            mImageView = itemView.findViewById(R.id.avatar);
            contactNickname = itemView.findViewById(R.id.contact_name);
            pairedTag = itemView.findViewById(R.id.contact_tag);
        }
    }

    public contactAdapter(ArrayList<contactItem> exampleList){
        mContactList = exampleList;
    }

    @NonNull
    @Override
    public contactHoler onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.contact_item, viewGroup, false);
        contactHoler vh = new contactHoler(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull contactHoler contactHoler, int i) {
        contactItem currentContact = mContactList.get(i);
        contactHoler.mImageView.setImageResource(currentContact.getAvatarSource());
        contactHoler.contactNickname.setText(currentContact.getContactNickname());
        contactHoler.pairedTag.setText(currentContact.getPairedTag());
    }

    @Override
    public int getItemCount() {
        return mContactList.size();
    }
}
