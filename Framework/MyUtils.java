/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package etu2020.framework.myutils;

import etu2020.framework.Mapping;
import etu2020.framework.annotation.MethodAnnotation;
import etu2020.framework.upload.*;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
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

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;

/**
 *
 * @author miaro
 */
@MultipartConfig
public class MyUtils {

    // map that willcontains all parser method
    public static final Map<Class<?>, Function<String, ?>> PARSERS = new HashMap<>();
    static{
        PARSERS.put(int.class, Integer::parseInt);
        PARSERS.put(double.class, Double::parseDouble);
        PARSERS.put(String.class, Function.identity());
        PARSERS.put(long.class, Long::parseLong);
    }

    /**
     * Find the proper method to call in case the other one have sensitive case
     * @return the right method with the rigth case
     */
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

    /**
     * Take value from a formulaire and parse those values to the proper type of the designed class
     * @param request is the HttpServletRequest where we want to get the attribute from the formulaire
     * @param yourclass is the class we want to set attribute to the value from the formulaire
     */
    public static void setParsed(HttpServletRequest request, Object yourclass, PrintWriter out) throws Exception{
        Field[] fields = yourclass.getClass().getDeclaredFields();
        if(fields.length == 0){
            return;
        }   
        
        Object parsedValue = null;
        
        
        for(Field field : fields){
            
            Class<?> fieldType = field.getType();
            Function<String, ?> parser = PARSERS.get(fieldType);
            
            if(checkType(request, field.getName(), out)){
                FileDetails fileDetails = FileDetails.retrieveFileDetails(request.getPart(field.getName()));
                parsedValue = (FileDetails)fileDetails;
            }

            else{
                parsedValue = parser.apply(request.getParameter(field.getName()));
            }
            String method = findMethod("set"+field.getName(), yourclass);
            yourclass.getClass().getMethod(method, fieldType).invoke(yourclass, parsedValue);
        }
        
    }
    
    /**
     * Check if the input is a file upload or a simple input
     */
    public static boolean checkType(HttpServletRequest request, String parameterName, PrintWriter out)throws Exception{
        boolean isFileupload = false;
        Part filePart = request.getPart(parameterName);
        String disposition = filePart.getHeader("Content-Disposition");
        if(disposition != null && disposition.startsWith("form-data") && filePart.getSubmittedFileName() != null){
            isFileupload = true;
        }
        else{
            isFileupload = false;
        }
        return isFileupload;
    }
    
    /**
     * function that will scan all classes in a package
     * also calls get method to retrieve all the methods in that class that will present a method annotation
     * @param packageName is the package to scan
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
                for (File child : file.listFiles()) {
                    if(child.isFile()){
                        String className = packageName + "." + child.getName().split("\\.")[0];
                        classes.add(Class.forName(className));
                        getMethods(Class.forName(className), urlMapping);
                    }
                    classes.addAll(getClasses(packageName + "." + child.getName().split("\\.")[0], urlMapping));
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
    * @param urlMapping HashMap that contain all classes and method in your class
    * @param urlMethod is the url from requestURI that shall correspond to a method in urlMapping
    * @throws Exception any
    */
    public static Modelview urlModelView(HttpServletRequest request, Mapping map, HashMap<String, Mapping> urlMapping, String urlMethod, Object yourclass, PrintWriter out)
    throws Exception {
        Modelview viewPage = null;
        // The function argument, if none then set to null
        if(urlMapping.containsKey(urlMethod)){
            if(methodNoArgs(map, yourclass).getAnnotation(MethodAnnotation.class).args()){
                viewPage = setParseArgs(request, yourclass, map, out);
            }
            else{
                viewPage = (Modelview)yourclass.getClass().getMethod(map.getMethod(), (Class<?>[])null).invoke(yourclass);
            }

            HashMap<String, Object> datas = viewPage.getData();

            for(String key : datas.keySet()){
                request.setAttribute(key, datas.get(key));
            }
        }
        return viewPage;
    }

    /**
     * This function gets the proper method as Method type
     * @param map mapping that contains the class name and method name that has been called
     * @param yourclass is instance of the class in map
     */
    public static Method methodNoArgs(Mapping map, Object yourclass){
        Method argsMethod = null;
        for(Method method : yourclass.getClass().getDeclaredMethods()){
            if(method.getName().equals(map.getMethod())){
                argsMethod = method;
                break;
            }
        }

        return argsMethod;
    }

    /**
     * This function call the right method by giving him the right(s) argument
     * Also It parse the input from html to the proper type required by the method thqt will be called
     * @param request HttpServletRequest that contains the attribute sended from form
     * @param yourclass instance of the proper class that contains the method
     * @param map is Mapping that contains the class and the method 
     * @return Modelview returned by the method invoked with it's argument given
     * @throws Exception of all type
     */
    public static Modelview setParseArgs(HttpServletRequest request, Object yourclass, Mapping map, PrintWriter out)throws Exception{
        Enumeration enumeration = request.getParameterNames();
        HashMap<String, Class<?>> argInfo = new HashMap<>();
        // Find the method to call their parameters
        
        Method argsMethod = methodNoArgs(map, yourclass);

        for(Class<?> paramtypes : argsMethod.getParameterTypes()){
            String paramname = (String)enumeration.nextElement();
            argInfo.put(request.getParameter(paramname), paramtypes);
        }

        Object[] args = new Object[argInfo.size()];
        Class<?>[] argsTypes = new Class<?>[argInfo.size()];
        int i = 0;


        for(String key : argInfo.keySet()){
            argsTypes[i] = argInfo.get(key);
            Function<String ,?> parser = PARSERS.get(argsTypes[i]);
            args[i] = parser.apply(key);
            i++;
        }

        return (Modelview)yourclass.getClass().getMethod(map.getMethod(), argsTypes).invoke(yourclass, args);
    }  
}
