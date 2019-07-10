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
        <link href="<%= path %>include/mktree.css" type="text/css" rel="stylesheet" />
        <script type="text/javascript" language="JavaScript" src="<%= path %>include/mktree.js"></script>
        <meta http-equiv="Pragma" content="no-cache">
        <meta http-equiv="Expires" content="-1">
        <meta http-equiv="CACHE-CONTROL" content="no-cache">
    </head>
    <body>
        <h1>CAD-RE-FX sync</h1>
        <h2>Uložení změn</h2>
        <table border="2" frame="border" rules="all" cellspacing="1" cellpadding="1" class="formular" style="width:600px" summary="" bordercolorlight="black">
            <tr>
                <td>
                    <img src="img/filesave.png" />Změny byly trvale uloženy do databáze.
                </td>
            </tr>
            <tr>
                <td>
                    <a href="Index">Zpět na hlavní menu aplikace</a>
                </td>
            </tr>
        </table>
    </body>
</html>
