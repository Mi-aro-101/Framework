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
import java.io.*;

@ModelAnnotation
@WebServlet("/upload")
@MultipartConfig
public class FileDetails {
    Part filePart;
    String fileName;
    long fileSize;
    String contentType;
    byte[] filebytes;

    public byte[] getBytesPart()throws Exception{
        InputStream content = this.getFilePart().getInputStream();
        byte[] buffer = new byte[1024];
        int read = 0;
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        
        while ((read = content.read(buffer)) != -1) {
            output.write(buffer, 0, read);
        }

        return output.toByteArray();
    }

    @MethodAnnotation(url = "upload")
    public Modelview getFileabout() throws Exception{
        HashMap<String, Object> datas = new HashMap<>();
        datas.put("Nom", getFileName());
        datas.put("Content", getContentType());
        datas.put("bytes", getFileBytes());

        return new Modelview("Display.jsp", datas, true);
    }

    public static FileDetails retrieveFileDetails(Part filePart)throws Exception{
        String fileName = filePart.getSubmittedFileName();
        long fileSize = filePart.getSize();
        String contentType = filePart.getContentType();

        return new FileDetails(filePart, fileName, fileSize, contentType);
    }
    
    // Constructor
    public FileDetails(Part filePart, String fileName, long fileSize, String contentType) throws Exception{
        this.setFilePart(filePart);
        this.setFileName(fileName);
        this.setFileSize(fileSize);
        this.setContentType(contentType); 
        this.setFileBytes(this.getBytesPart());   
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

    public byte[] getFileBytes(){
        return this.filebytes;
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

    public void setFileBytes(byte[] fileBytes) {
        this.filebytes = fileBytes;
    }
}
