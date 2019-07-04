<?xml version="1.0" encoding="UTF-8"?>
<%@ page language="java" contentType="text/html; charset=utf-8" %>
<%@ page pageEncoding="utf-8" %>

<%@ include file="../include/page_head_popup.jsp" %>

<h1 style='left:0px'>Informace o podlaží</h1>

<br />
<table border="2" frame="border" rules="all" cellspacing="1" cellpadding="1" class="formular" style="width:100%" summary="" bordercolorlight="black">
    <tr><td class='group' colspan='2'>Budova</td></tr>
    <tr><td class='key'>identifikace:  </td><td><%= request.getAttribute("building") %></td></tr>
    <tr><td class='key'>jméno:         </td><td><%= request.getAttribute("buildingName") %></td></tr>
    <tr><td class='group' colspan='2'>Podlaží</td></tr>
    <tr><td class='key'>identifikace:  </td><td><%= request.getAttribute("floor") %></td></tr>
    <tr><td class='key'>jméno:         </td><td><%= request.getAttribute("floorName") %></td></tr>
    <tr><td class='group' colspan='2'>Varianty</td></tr>
    <%= request.getAttribute("variants") %>
</table>

<hr noshade />

<%@ include file="../include/page_tail_popup.jsp" %>

