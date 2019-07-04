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
    </head>
    <body>
        <h1>Synchronizace se SAP</h1>
        <h2>Krok 3/3: synchronizace se SAP</h2>

        <table border="2" frame="border" rules="all" cellspacing="1" cellpadding="1" class="formular" style="width:900px" summary="" bordercolorlight="black">
            <tr><td class='key'>I_AOID</td><td><%= request.getAttribute("i_aoid") %></td></tr>
            <tr><td class='key'>I_VAL_FROM</td><td><%= request.getAttribute("i_val_from") %></td></tr>
            <tr><td class='key'>I_TEST</td><td><%= request.getAttribute("i_test") %></td></tr>
        </table>
        <p>Zprávy ze SAPu:</p>
        <%= request.getAttribute("message") %>
        <br />
        <table border="2" frame="border" rules="all" cellspacing="1" cellpadding="1" class="formular" style="width:900px" summary="" bordercolorlight="black">
            <tr class='key'><td>AOLEVEL</td><td>AOTYPE_EXT</td><td>AONR</td><td>AOID</td><td>AOFUNCTION</td><td>XAO</td><td>FL_AREA</td><td>CHNGIND</td><td>PARENT</td></tr>
            <%= request.getAttribute("table") %>
            <tr><td colspan='9'>&nbsp;</td></tr>
            <tr><td colspan='9'>
                <%--<a href="jsp/EditFloorVariant.jsp"><img src="img/1leftarrow.gif" border="0" />&nbsp;Zpět</a>&nbsp;&nbsp;--%>
                <a href="jsp/EditFloorVariant.jsp"><img src="img/1leftarrow.gif" border="0" />&nbsp;Pokračovat</a>&nbsp;&nbsp;
            </td></tr>
        </table>
    </body>
</html>
