package com.example.masquerade;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;


public class contactAdapter extends RecyclerView.Adapter<contactAdapter.contactHolder> {
    Context context;
    private ArrayList<contactItem> mContactList;



    public class contactHolder extends RecyclerView.ViewHolder{

        public ImageView mImageView;
        public TextView contactNickname;
        public TextView pairedTag;
        public Button chat;
        public Button checkProfile;


        public contactHolder(@NonNull View itemView) {
            super(itemView);
            mImageView = (ImageView) itemView.findViewById(R.id.avatar);
            contactNickname = (TextView) itemView.findViewById(R.id.contact_name);
            pairedTag = (TextView) itemView.findViewById(R.id.contact_tag);
            chat = (Button)itemView.findViewById(R.id.start_chat);
            checkProfile = (Button)itemView.findViewById(R.id.check_profile);
        }

        public void onClick(final String id) {
            chat.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Toast.makeText(context,id+"button click",Toast.LENGTH_SHORT).show();


                        Intent intent = new Intent(context , MessageActivity.class);
                        intent.putExtra("userid",id);
                        context.startActivity(intent);

                }
            });
            checkProfile.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Toast.makeText(context,id+"button click",Toast.LENGTH_SHORT).show();

                        Intent intent = new Intent(context,ProfileActivity.class);
                        intent.putExtra("contactid",id);
                        Toast.makeText(context,"check details",Toast.LENGTH_SHORT).show();
                        context.startActivity(intent);


                }
            });
        }

    }

    public contactAdapter(Context c,ArrayList<contactItem> exampleList){
        context = c;
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
        contactHolder.mImageView.setImageResource(context.getResources().getIdentifier(currentContact.getAvatarSource(), "drawable", context.getPackageName()));
        contactHolder.contactNickname.setText(currentContact.getContactNickname());
        contactHolder.pairedTag.setText(currentContact.getPairedTag());

        if(currentContact.getFriend()){
            if(!currentContact.getContactId().equals("")){
                contactHolder.checkProfile.setVisibility(View.VISIBLE);
                contactHolder.chat.setVisibility(View.GONE);
                contactHolder.onClick(currentContact.getContactId());
            }
            else{
                contactHolder.chat.setVisibility(View.GONE);
                contactHolder.checkProfile.setVisibility(View.GONE);
            }
        }
        else{

            if(!currentContact.getContactId().equals("")) {
                contactHolder.chat.setVisibility(View.VISIBLE);
                contactHolder.onClick(currentContact.getContactId());
                contactHolder.checkProfile.setVisibility(View.VISIBLE);
            }
            else{
                contactHolder.chat.setVisibility(View.GONE);
                contactHolder.checkProfile.setVisibility(View.GONE);
            }
        }


    }



    @Override
    public int getItemCount() {
        return mContactList.size();
    }
}
