<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN"
        "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<%@ page language="java" contentType="text/html; charset=utf-8" %>
<%@ page pageEncoding="utf-8" %>
<%
    String path = request.getRequestURI();
    path = path.substring(0, 1+path.indexOf('/', 1));
    String rnd = Long.toString(Double.doubleToLongBits(Math.random()));
%>

<html xmlns="http://www.w3.org/1999/xhtml" lang="en" xml:lang="en">
    <head>
        <link href="<%= path %>include/style.css" type="text/css" rel="stylesheet" />
        <link href="<%= path %>include/calendar.css" type="text/css" rel="stylesheet" >
        <meta http-equiv="Pragma" content="no-cache">
        <meta http-equiv="Expires" content="-1">
        <meta http-equiv="CACHE-CONTROL" content="no-cache">
        <script language="JavaScript" src="<%= path %>include/calendar_eu.js"></script>
        <script type="text/javascript">
        </script>
    </head>
    <body>
        <h1>CAD-RE-FX sync</h1>
        <br />
        <h2>Nová varianta podlaží krok 4/4: výsledek</h2>
        <table border="2" frame="border" rules="all" cellspacing="1" cellpadding="1" class="formular" style="width:900px" summary="" bordercolorlight="black">
            <tr class='key'><td>Operace</td><td>Výsledek</td></tr>
            <%= request.getAttribute("log") %>
            <tr><td colspan='2'><a href="Index"><img src="img/1leftarrow.gif" border="0" />&nbsp;Zpět na index</a></td></tr>
        </table>
        </form>
    </body>
</html>
