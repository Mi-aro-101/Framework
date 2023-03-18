/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import etu2020.framework.annotation.MethodAnnotation;
import etu2020.framework.annotation.ModelAnnotation;

/**
 *
 * @author miaro
 */
@ModelAnnotation
public class People {
    String anarana;
    int age;
    
    @MethodAnnotation(url="sayhello")
    public String sayHello(){
        return "Hello guys";
    }
    
    //Constructor
    public People() {
    }

    public People(String anarana, int age) {
        this.anarana = anarana;
        this.age = age;
    }
    
    //Getters

    public String getAnarana() {
        return anarana;
    }

    public int getAge() {
        return age;
    }
    
    //Setters

    public void setAnarana(String anarana) {
        this.anarana = anarana;
    }

    public void setAge(int age) {
        this.age = age;
    }
    
}
