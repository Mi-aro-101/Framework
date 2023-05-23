/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package models.test;

import etu2020.framework.Modelview;
import etu2020.framework.annotation.MethodAnnotation;
import etu2020.framework.annotation.ModelAnnotation;
import java.util.*;

/**
 *
 * @author miaro
 */
@ModelAnnotation
public class Test {
    @MethodAnnotation(url="findall")
    public Modelview findall(){
        HashMap<String, Object> datas = new HashMap<>();
        datas.put("One", "Datebayo");
        datas.put("Two", 666);
        datas.put("Three", "<3");

        return new Modelview("Findall.jsp", datas);
    }

    @MethodAnnotation(url="arg", args=true)
    public Modelview insert(String a, int b){
        HashMap<String, Object> datas = new HashMap<>();
        datas.put("One", a);
        datas.put("Two", b);

        return new Modelview("Findall.jsp", datas);
    }
}
