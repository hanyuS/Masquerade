package com.example.masquerade;

public class Tag {
    boolean isSelected;
    String tagName;

    //now create constructor and getter setter method using shortcut like command+n for mac & Alt+Insert for window.


    public Tag(boolean isSelected, String tagName) {
        this.isSelected = isSelected;
        this.tagName = tagName;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public String getTagName() {
        return tagName;
    }

    public void setTagName(String tagName) {
        this.tagName = tagName;
    }
}
