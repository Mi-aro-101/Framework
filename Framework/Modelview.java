/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package etu2020.framework;

import java.util.*;

/**
 *
 * @author miaro
 */
public class Modelview {
    String view;
    HashMap<String, Object> data;

    //Constructor
    public Modelview(){}
    
    public Modelview(String view, HashMap<String, Object> data) {
        setView(view);
        setData(data);
    }
    
    public HashMap<String, Object> getData(){
        return this.data;
    }

    //Getters
    public String getView() {
        return view;
    }

    //Setters
    public void setView(String view) {
        this.view = view;
    }

    public void setData(HashMap<String, Object> data){
        this.data = data;
    }

    public void addItem(String key, Object value){
        this.getData().put(key, value);
    }
    
    
}
