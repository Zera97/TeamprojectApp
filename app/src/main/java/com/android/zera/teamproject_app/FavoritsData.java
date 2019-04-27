package com.android.zera.teamproject_app;

import java.util.ArrayList;

public class FavoritsData {

    private ArrayList<Integer> numbers;
    private boolean selected;
    private String title;

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String t){
        this.title = t;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public void  setNumbers(ArrayList<Integer> numbers){
        this.numbers = numbers;
        if(numbers == null){
            this.title= "Meine Favoriten";
        }
        else{
            String s = "";
            for (int i = 0; i< this.numbers.size();i++){
                s += this.numbers.get(i);
                if(i != this.numbers.size() - 1){
                    s += ", ";
                }
            }
            this.title = s;
        }
    }

    public ArrayList<Integer> getNumbers(){
        return this.numbers;
    }

    public FavoritsData(ArrayList<Integer> numbers){
        this.setNumbers(numbers);
        //this.selected = selected;
    }
}
