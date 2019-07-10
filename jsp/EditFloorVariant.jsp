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

<html>
    <head>
        <link href="<%= path %>include/style.css" type="text/css" rel="stylesheet" />
        <link href="<%= path %>include/mktree.css" type="text/css" rel="stylesheet" />
        <script type="text/javascript" language="JavaScript" src="<%= path %>include/mktree.js"></script>
        <meta http-equiv="Pragma" content="no-cache">
        <meta http-equiv="Expires" content="-1">
        <meta http-equiv="CACHE-CONTROL" content="no-cache">
    </head>

<frameset cols="300px,*" border="2">
    <frameset rows="20%,*" border="2">
        <frame src="<%= path %>Commands?rnd=<%= rnd %>" name="Commands" scrolling="auto" />
        <frame src="<%= path %>RoomList?rnd=<%= rnd %>" id="RoomList" name="RoomList" scrolling="auto" />
        <%--
        <frame src="<%= path %>RoomInfo?rnd=<%= rnd %>" name="RoomInfo" scrolling="auto" />
        --%>
    </frameset>
    <frame src="<%= path %>Drawing?rnd=<%= rnd %>"  id="Drawing"  name="Drawing" scrolling="auto" />
</frameset>

<noframes>
<body>
</body>
</noframes>
</html>
