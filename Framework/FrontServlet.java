/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package etu2020.framework.servlet;

import etu2020.framework.Mapping;
import etu2020.framework.Modelview;
import etu2020.framework.myutils.MyUtils;
import jakarta.servlet.ServletConfig;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import jakarta.servlet.http.Part;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;

/**
 *
 * @author miaro
 */
@MultipartConfig
public class FrontServlet extends HttpServlet {
    
    HashMap<String, Mapping> urlMapping = new HashMap<>();

    @Override
    public void init(ServletConfig config) throws ServletException {
        String packageName = config.getInitParameter("root");
        
        try{
            MyUtils.getClasses(packageName, this.getUrlMapping());
            
//            Mapping map =  getUrlMapping().get(url);
//            out.println(map.getMethod());
        }catch(IOException | ClassNotFoundException | URISyntaxException e){
            Logger.getLogger(FrontServlet.class.getName()).log(Level.SEVERE, null, e);
        }
        
    }
    
    

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     * @throws java.lang.ClassNotFoundException
     * @throws java.net.URISyntaxException
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, ClassNotFoundException, URISyntaxException, NoSuchMethodException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        response.setContentType("text/html;charset=UTF-8");
        response.setContentType("text/plain");
        
        PrintWriter out = response.getWriter();
        
        String methodUri = request.getRequestURI();
        String[] urlMethods = methodUri.split("/");
        String urlMethod = urlMethods[urlMethods.length-1];

        // out.println(getUrlMapping().get("findall").getClassName());

        Mapping map = getUrlMapping().get(urlMethod);
        out.println(map.getClassName());
        out.println(map.getMethod());

        // If the user choose the results to be in json, this variable will be set to
        String datas = "";
        
        // Checking if the url match a method and if so dispatch it, otherwise do nothing
        try{
            Object o = Class.forName(map.getClassName()).getConstructor().newInstance();
            MyUtils.setParsed(request, o, out);
            Modelview mv =  MyUtils.urlModelView(request, map, getUrlMapping(), urlMethod, o, out);
            out.println(mv.getIsJson());
            if(mv.getIsJson()){
                datas = MyUtils.hashMaptoJson(mv.getData());
                mv.setDataJson(datas);
                request.setAttribute("json", mv.getDataJson());
            }
            if(mv != null){
                request.getRequestDispatcher(mv.getView()).forward(request, response);
            }
        }catch(Exception e){
            out.println(e);
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            processRequest(request, response);
        } catch (Exception ex) {
            Logger.getLogger(FrontServlet.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            processRequest(request, response);
        } catch (Exception ex) {
            Logger.getLogger(FrontServlet.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

    public HashMap<String, Mapping> getUrlMapping() {
        return urlMapping;
    }

    public void setUrlMapping(HashMap<String, Mapping> urlMapping) {
        this.urlMapping = urlMapping;
    }

}
