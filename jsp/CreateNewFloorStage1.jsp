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
        <meta http-equiv="Pragma" content="no-cache">
        <meta http-equiv="Expires" content="-1">
        <meta http-equiv="CACHE-CONTROL" content="no-cache">
        <script type="text/javascript">
        <!--
        function managementUnitInfoClick(objectId)
        {
            var url = "ManagementUnitInfo?id="+objectId;
            var w=window.open(url, "informace", 'width=420,height=550,toolbar=no,status=no,titlebar=no,menubar=no,toolbar=no,resizable=yes,dependent=yes,scrollbars=yes');
            w.focus();
        }
        -->
        </script>
    </head>
    <body>
        <h1>CAD-RE-FX sync</h1>
        <h2>Nová varianta podlaží krok 1/4: výběr hospodářské jednotky</h2>
        <table border="2" frame="border" rules="all" cellspacing="1" cellpadding="1" class="formular" style="width:900px" summary="" bordercolorlight="black">
            <tr class='key'><td>Identifikace</td><td>Typ objektu</td><td>Popis</td><td>Platnost od</td><td>Platnost do</td><td>Lokace</td><td>Poznámky</td></tr>
            <%= request.getAttribute("out") %>
            <tr><td colspan='7'>&nbsp;</td></tr>
            <tr><td colspan='7'><a href="Index"><img src="img/1leftarrow.gif" border="0" />&nbsp;Zpět</a></td></tr>
        </table>
    </body>
</html>
