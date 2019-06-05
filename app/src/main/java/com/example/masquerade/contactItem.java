package com.example.masquerade;

public class contactItem {
    private String avatarSource;
    private String contactNickname;
    private String pairedTag;
    private String contactId;
    private Boolean friend;

    public contactItem(String picSource, String contactName, String tag, String id, Boolean isfriend){
        avatarSource = picSource;
        contactNickname = contactName;
        pairedTag = tag;
        contactId = id;
        friend = isfriend;

    }

    public String getAvatarSource(){
        return avatarSource;
    }

    public String getContactNickname() {
        return contactNickname;
    }

    public String getPairedTag() {
        return pairedTag;
    }

    public String getContactId() {
        return contactId;
    }

    public Boolean getFriend() {
        return friend;
    }
}
