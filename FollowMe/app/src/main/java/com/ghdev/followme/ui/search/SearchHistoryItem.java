package com.ghdev.followme.ui.search;

import android.graphics.drawable.Drawable;
import android.media.Image;

public class SearchHistoryItem {
    private String titleStr ;
    private String dateStr ;
    private Drawable iconDrawable ;

    public void setTitle(String title) {
        titleStr = title ;
    }
    public void setDate(String date) {  dateStr = date ; }
    public void setIcon(Drawable icon) {
        iconDrawable = icon ;
    }

    public String getTitle() {
        return this.titleStr ;
    }
    public String getDate() {
        return this.dateStr ;
    }
    public Drawable getIcon() { return this.iconDrawable ; }
}
