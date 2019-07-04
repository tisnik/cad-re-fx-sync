<?xml version="1.0" encoding="UTF-8"?>
<%@ page language="java" contentType="text/html; charset=utf-8" %>
<%@ page pageEncoding="utf-8" %>

<%@ include file="../include/page_head_popup.jsp" %>

<%
    String rnd = Long.toString(Double.doubleToLongBits(Math.random()));
%>

<script type="text/javascript">
<!--

function onRoomClick(id)
{
    top.frames["Drawing"].location.href="Drawing?rnd="+<%= rnd %>+"&id="+id;
}

function fillArea(computedArea)
{
    document.getElementById("area").value = computedArea;
    return false;
}
-->
</script>

<h2 style='left:0px;margin-top: 5px; margin-bottom: 5px'>Byty</h2>
<table border="2" frame="border" rules="all" cellspacing="1" cellpadding="1" class="formular" style="width:100%" summary="" bordercolorlight="black">
    <form method='get' action='CreateFlat'>
    <%= request.getAttribute("flatList") %>
    <tr><td><input type='text' name='flatName' size='10' /></td><td><input type='submit' value='Nový' /></td></tr>
    </form>
</table>

<h2 style='left:0px;margin-top: 5px; margin-bottom: 5px'>Místnosti</h2>
<span style='color:#00cc00'>&nbsp;je v SAP, s atributy</span><br />
<span style='color:#00cccc'>&nbsp;je v SAP, bez atributů</span><br />
<span style='color:#cccc00'>&nbsp;není v SAP, s atributy</span><br />
<span style='color:#cc00cc'>&nbsp;není v SAP, bez atributů</span><br />

<table border="2" frame="border" rules="all" cellspacing="1" cellpadding="1" class="formular" style="width:100%" summary="" bordercolorlight="black">
    <%= request.getAttribute("roomList") %>
</table>

<% if (request.getAttribute("aoid") != null) { %>
<h2 style='left:0px;margin-bottom: 0px'>Vybraná místnost</h2>

<table border="2" frame="border" rules="all" cellspacing="1" cellpadding="1" class="formular" style="width:100%" summary="" bordercolorlight="black">
    <form method='get' action='SaveRoomInfo'><input type='hidden' name='roomId' value='<%= request.getAttribute("aoid") %>' />
    <% if (request.getAttribute("message")!=null) { %>
    <tr><td><%= request.getAttribute("message") %></td></tr>
    <% } else { %>
    <tr><td>AOID          </td><td><input type='text' name="aoid"     size='10' value='<%= request.getAttribute("aoid") %>' disabled='disabled'    /></td></tr>
    <tr><td>Název         </td><td><input type='text' name="name"     size='10' value='<%= request.getAttribute("name") %>'     /></td></tr>
    <tr><td>Plocha (<a href='javascript:void(0)' onClick='fillArea(<%= request.getAttribute("computedArea") %>)'><%= request.getAttribute("computedArea") %></a>)</td><td><input type='text' id='area' name="area"     size='10' value='<%= request.getAttribute("area") %>'     /></td></tr>
    <tr><td>Kapacita      </td><td><input type='text' name="capacity" size='10' value='<%= request.getAttribute("capacity") %>' /></td></tr>
    <tr><td>Volná kapacita</td><td><input type='text' name="free"     size='10' value='<%= request.getAttribute("free") %>'     /></td></tr>
    <tr><td>Typ místnosti </td><td><select name='roomType'><%= request.getAttribute("type") %></select></td></tr>
    <tr><td>Byt           </td><td><select name='flat'><%= request.getAttribute("flats") %></select></td></tr>
    <tr><td>&nbsp;        </td><td><input type='submit' value='Uložit změny' /></td></tr>
    <% } %>
    </form>
</table>

<% } %>

</body>
</html>
