<%-- 
    Document   : Display
    Created on : Mar 28, 2023, 4:51:17 PM
    Author     : miaro
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="etu2020.framework.upload.FileDetails"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Just a test</title>
    </head>
    <body>
        <h1>La liste de vos objets : 
        </h1>
        <ul>
            <% try { %>
            <li><%=request.getAttribute("Nom")%></li>
            <li><%=request.getAttribute("Compagnon")%></li>
            <li><%=((FileDetails)(request.getAttribute("File"))).getFileSize()%></li>
            <% } catch(Exception e){out.println(e);}%>
        </ul>
    </body>
</html>
