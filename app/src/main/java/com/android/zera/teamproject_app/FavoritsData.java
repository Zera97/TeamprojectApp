package com.android.zera.teamproject_app;

public class FavoritsData {

    private int[] numbers;
    private boolean selected;
    private String title;

    public String getTitle() {
        return this.title;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public void  setNumbers(int[] numbers){
        this.numbers = numbers;
        String s = "";
        for (int i = 0; i< this.numbers.length;i++){
            s += this.numbers[i];
            if(i != this.numbers.length - 1){
                s += ", ";
            }
        }
        this.title = s;
    }

    public FavoritsData(int[] numbers){
        this.setNumbers(numbers);
        //this.selected = selected;
    }
}
