package etu2020.framework.upload;

import etu2020.framework.Modelview;
import etu2020.framework.annotation.MethodAnnotation;
import etu2020.framework.annotation.ModelAnnotation;
import java.util.*;
import etu2020.framework.Mapping;
import etu2020.framework.Modelview;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.Part;
import java.io.IOException;

@ModelAnnotation
@WebServlet("/upload")
@MultipartConfig
public class FileDetails {
    Part filePart;
    String fileName;
    long fileSize;
    String contentType;

    @MethodAnnotation(url = "upload")
    public Modelview getFileabout(){
        HashMap<String, Object> datas = new HashMap<>();
        datas.put("Nom", getFileName());
        datas.put("Content", getContentType());

        return new Modelview("Display.jsp", datas);
    }

    public static FileDetails retrieveFileDetails(Part filePart)throws Exception{
        String fileName = filePart.getSubmittedFileName();
        long fileSize = filePart.getSize();
        String contentType = filePart.getContentType();

        return new FileDetails(filePart, fileName, fileSize, contentType);
    }
    
    // Constructor
    public FileDetails(Part filePart, String fileName, long fileSize, String contentType) {
        this.filePart = filePart;
        this.fileName = fileName;
        this.fileSize = fileSize;
        this.contentType = contentType;
    }

    public FileDetails(){}

    // Getters
    public Part getFilePart() {
        return filePart;
    }

    public String getFileName() {
        return fileName;
    }

    public long getFileSize() {
        return fileSize;
    }

    public String getContentType() {
        return contentType;
    }

    // Setters
    public void setFilePart(Part filePart) {
        this.filePart = filePart;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public void setFileSize(long fileSize) {
        this.fileSize = fileSize;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }
}
