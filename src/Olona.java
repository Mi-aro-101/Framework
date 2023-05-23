package models.olona;

import etu2020.framework.annotation.MethodAnnotation;
import etu2020.framework.annotation.ModelAnnotation;
import etu2020.framework.Modelview;
import java.util.*;

@ModelAnnotation
public class Olona{
    String nom;
    int nbrCompagnon;

    public Olona(){}

    public Olona(String nom, int nbrCompagnon){
        setNom(nom);
        setNbrCompagnon(nbrCompagnon);
    }

    @MethodAnnotation(url="fillup")
    public Modelview fillup(){
        HashMap<String, Object> datas = new HashMap<>();
        datas.put("Nom", getNom());
        datas.put("Compagnon", getNbrCompagnon());

        return new Modelview("Display.jsp", datas);
    }

    //Getters
    public String getNom(){
        return this.nom;
    }

    public int getNbrCompagnon(){
        return this.nbrCompagnon;
    }

    //Setters
    public void setNom(String nom){
        this.nom = nom;
    }

    public void setNbrCompagnon(int nbrCompagnon){
        this.nbrCompagnon = nbrCompagnon;
    }
}