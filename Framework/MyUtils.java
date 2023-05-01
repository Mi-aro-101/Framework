/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package etu2020.framework.myutils;

import etu2020.framework.Mapping;
import etu2020.framework.annotation.MethodAnnotation;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.*;
import java.util.function.*;
import java.lang.reflect.*;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import etu2020.framework.Mapping;
import etu2020.framework.Modelview;

/**
 *
 * @author miaro
 */
public class MyUtils {

    // map that willcontains all parser method
    public static final Map<Class<?>, Function<String, ?>> PARSERS = new HashMap<>();
    static{
        PARSERS.put(int.class, Integer::parseInt);
        PARSERS.put(double.class, Double::parseDouble);
        PARSERS.put(String.class, Function.identity());
    }

    public static String findMethod(String methodName, Object yourclass)throws Exception{
        String rightMethod = "";
        for(Method method : yourclass.getClass().getDeclaredMethods()){
            if(methodName.equalsIgnoreCase(method.getName())){
                rightMethod = method.getName();
                break;
            }
        }

        return rightMethod;
    }

    public static void setParsed(HttpServletRequest request, Object yourclass, PrintWriter out) throws Exception{
        Field[] fields = yourclass.getClass().getDeclaredFields();
        int i = 0;
        for(Field field : fields){
            Class<?> fieldType = field.getType();
            Function<String, ?> parser = PARSERS.get(fieldType);
            if(parser == null){
                throw new Exception("Unsupported field type: "+fieldType);
            }
            Object parsedValue = parser.apply(request.getParameter(field.getName()));
            String method = findMethod("set"+field.getName(), yourclass);
            yourclass.getClass().getMethod(method, fieldType).invoke(yourclass, parsedValue);
        }
        out.println(yourclass.getClass().getMethod("getNom").invoke(yourclass));
        out.println(yourclass.getClass().getMethod("getNbrCompagnon").invoke(yourclass));

    }
    
    /**
     * function that will scan all classes in a package
     * also calls get method to retrieve all the methods in that class that will present a method annotation
     *@param packageName is the package to scan
     * @param urlMapping is the HashMap to fill with the url and Mapping (sprint2)
     * @return an Arraylist of all the classes found
     * @throws java.lang.ClassNotFoundException in case there is non e such class
     * @throws java.io.IOException
     * @throws java.net.URISyntaxException
     */
    public static ArrayList<Class<?>> getClasses(String packageName, HashMap<String, Mapping> urlMapping) throws ClassNotFoundException, IOException, URISyntaxException {
        ArrayList<Class<?>> classes = new ArrayList<>();
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        String path = packageName.replace('.', '/');
        path = path.replace("%20", " ");
        Enumeration<URL> resources = classLoader.getResources(path);
        while (resources.hasMoreElements()) {
            URL resource = resources.nextElement();
            File file = new File(resource.toURI());
//            if (file.isDirectory()) {
                for (File child : file.listFiles()) {
                    if(child.isFile()){
                        String className = packageName + "." + child.getName().split("\\.")[0];
                        classes.add(Class.forName(className));
                        getMethods(Class.forName(className), urlMapping);
                    }
                    classes.addAll(getClasses(packageName + "." + child.getName().split("\\.")[0], urlMapping));
//                }
            } 
        }
        return classes;
    }
    
    
    /**
     * also this function will be called above in order to : 
     * scan the classes and in the meantime scan the methods in it 
     * @param klass is the class that will have the methods to retrieve
     * @param urlMapping is the HashMap object that will be filled as requested by Mr Naina
     */
    public static void getMethods(Class<?> klass, HashMap<String, Mapping> urlMapping){
        for(Method method : klass.getDeclaredMethods()){
            if(method.isAnnotationPresent(MethodAnnotation.class)){
                MethodAnnotation annotation = method.getAnnotation(MethodAnnotation.class);
                String url  = annotation.url();
                Mapping map = new Mapping(klass.getName(), method.getName());
                urlMapping.put(url, map);
            }
        }        
    }

    /**
    * @return Modelview(String jspPage, HashMap<> datas) 
    * @param request is needed to setAttribute the data in the Modelview that will be return
    * @param map Mapping that corresponds a method contained in your class
    * @param urlMapping HasMap that contain all classes and method in your class
    * @param urlMethod is the url from requestURI that shall correspond to a method in urlMapping
    * @throws Exception any
     */
    public static Modelview urlModelView(HttpServletRequest request, Mapping map, HashMap<String, Mapping> urlMapping, String urlMethod, Object yourclass)
    throws Exception {
        Modelview viewPage = null;
        if(urlMapping.containsKey(urlMethod)){
            viewPage = (Modelview)yourclass.getClass().getMethod(map.getMethod(), (Class<?>[])null).invoke(yourclass);
            HashMap<String, Object> datas = viewPage.getData();
            for(String key : datas.keySet()){
                request.setAttribute(key, datas.get(key));
            }
        }
        return viewPage;
    }
    
}
