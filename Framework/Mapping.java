/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package etu2020.framework;

/**
 *
 * @author miaro
 */
public class Mapping {
    String className;
    String method;

    //Constructor

    public Mapping() {
    }
    
    
    public Mapping(String className, String method) {
        this.className = className;
        this.method = method;
    }

    //Getters
    public String getClassName() {
        return className;
    }

    public String getMethod() {
        return method;
    }

    //Setters
    public void setClassName(String className) {
        this.className = className;
    }

    public void setMethod(String method) {
        this.method = method;
    }
    
    
    
    
    
    
}
