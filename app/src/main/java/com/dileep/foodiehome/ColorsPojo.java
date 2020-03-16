package com.dileep.foodiehome;

public class ColorsPojo {
    String colorName;
    boolean checked;



    public ColorsPojo(String colorName,boolean checked) {
        this.colorName = colorName;
    }

    public void setColorName(String colorName) {
        this.colorName = colorName;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public String getColorName() {
        return colorName;
    }

    public boolean isChecked() {
        return checked;
    }
}
