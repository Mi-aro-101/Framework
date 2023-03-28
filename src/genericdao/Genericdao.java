/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package genericdao;

import etu2020.framework.Modelview;
import etu2020.framework.annotation.MethodAnnotation;
import etu2020.framework.annotation.ModelAnnotation;

/**
 *
 * @author miaro
 */
@ModelAnnotation
public class Genericdao {
    @MethodAnnotation(url="findall")
    public static Modelview findall(){
        return new Modelview("Display.jsp");
    }
}
