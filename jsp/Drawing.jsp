<?xml version="1.0" encoding="UTF-8"?>
<%@ page language="java" contentType="text/html; charset=utf-8" %>
<%@ page pageEncoding="utf-8" %>

<html>
    <head>
        <%
        String path = request.getRequestURI();
        path = path.substring(0, 1+path.indexOf('/', 1));
        String rnd = Long.toString(Double.doubleToLongBits(Math.random()));
        %>
        <link href="<%= path %>include/style.css" type="text/css" rel="stylesheet" />
        <meta http-equiv="Pragma" content="no-cache">
        <meta http-equiv="Expires" content="-1">
        <meta http-equiv="CACHE-CONTROL" content="no-cache">
        <script type="text/javascript">
        <!--
        function getEvent(e)
        {
            return !e ? window.event : e;
        }

        function getElementStyle(elemID, IEStyleAttr, CSSStyleAttr)
        {
            var elem = document.getElementById(elemID);
            if (elem.currentStyle)
            {
                return elem.currentStyle[IEStyleAttr];
            }
            else if (window.getComputedStyle)
            {
                var compStyle = window.getComputedStyle(elem, "");
                alert(compStyle.getPropertyValue(CSSStyleAttr));
                return compStyle.getPropertyValue(CSSStyleAttr);
            }
            return "";
        }

        function randomString()
        {
            var chars = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXTZabcdefghiklmnopqrstuvwxyz";
            var string_length = 16;
            var randomstring = "";
            for (var i=0; i != string_length; i++)
            {
                var rnum = Math.floor(Math.random() * chars.length);
                randomstring += chars.substring(rnum,rnum+1);
            }
            return randomstring;
        }

        function onImageClick(obj, e)
        {
            var evt = getEvent(e);
            var coords = {x:0, y:0};

            if (evt.pageX)
            {
                coords.x = evt.pageX - obj.offsetLeft;
                coords.y = evt.pageY - obj.offsetTop;
            }
            else if (evt.clientX)
            {
                coords.x = evt.offsetX;
                coords.y = evt.offsetY;
            }
            document.getElementById('drawing').src='ImageRenderer?coordsx='+coords.x+'&coordsy='+coords.y+'&rnd='+randomString();
            // reload room list
            top.frames["RoomList"].location.href='RoomList?coordsx='+coords.x+'&coordsy='+coords.y+'&rnd='+randomString();
        }
        -->
        </script>
    </head>
    <body bgcolor="white">
        <img src="ImageRenderer" id="drawing" width="1400" height="800" onClick="onImageClick(this, event)" />
    </body>
</html>

