<%-- 
    Document   : Display
    Created on : Mar 28, 2023, 4:51:17 PM
    Author     : miaro
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
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
            <li><%=request.getAttribute("Nom")%></li>
            <li><%=request.getAttribute("Compagnon")%></li>
        </ul>
    </body>
</html>
