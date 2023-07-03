package models.olona;

import etu2020.framework.annotation.MethodAnnotation;
import etu2020.framework.annotation.ModelAnnotation;
import etu2020.framework.Modelview;
import etu2020.framework.upload.*;
import java.util.*;

@ModelAnnotation
public class Olona{
    String nom;
    int nbrCompagnon;
    FileDetails myFile;

    public Olona(){}

    public Olona(String nom, int nbrCompagnon){
        setNom(nom);
        setNbrCompagnon(nbrCompagnon);
    }

    @MethodAnnotation(url="fillup.do")
    @RestAPI
    public Modelview fillup(){
        HashMap<String, Object> datas = new HashMap<>();
        datas.put("Nom", getNom());
        datas.put("Compagnon", getNbrCompagnon());
        datas.put("File", getMyFile());

        return new Modelview("Display.jsp", datas, false);
    }

    //Getters
    public String getNom(){
        return this.nom;
    }

    public int getNbrCompagnon(){
        return this.nbrCompagnon;
    }

    public FileDetails getMyFile() {
        return myFile;
    }

    //Setters
    public void setNom(String nom){
        this.nom = nom;
    }

    public void setNbrCompagnon(int nbrCompagnon){
        this.nbrCompagnon = nbrCompagnon;
    }

    public void setMyFile(FileDetails myFile) {
        this.myFile = myFile;
    }
}