package com.example.masquerade;

public class contactItem {
    private int avatarSource;
    private String contactNickname;
    private String pairedTag;

    public contactItem(int picSource, String contactName, String tag){
        avatarSource = picSource;
        contactNickname = contactName;
        pairedTag = tag;
    }

    public int getAvatarSource(){
        return avatarSource;
    }

    public String getContactNickname() {
        return contactNickname;
    }

    public String getPairedTag() {
        return pairedTag;
    }
}
