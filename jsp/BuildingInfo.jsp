<?xml version="1.0" encoding="UTF-8"?>
<%@ page language="java" contentType="text/html; charset=utf-8" %>
<%@ page pageEncoding="utf-8" %>

<%@ include file="../include/page_head_popup.jsp" %>

<h1 style='left:0px'>Informace o budově</h1>

<br />
<table border="2" frame="border" rules="all" cellspacing="1" cellpadding="1" class="formular" style="width:100%" summary="" bordercolorlight="black">
    <tr><td class='group' colspan='2'>SAP objekt</td></tr>
    <tr><td class='key'>INTRENO:       </td><td><%= request.getAttribute("intreno") %></td></tr>
    <tr><td class='key'>identifikace:  </td><td><%= request.getAttribute("aoid") %></td></tr>
    <tr><td class='key'>typ (číselník):</td><td><%= request.getAttribute("aotype") %></td></tr>
    <tr><td class='key'>typ (text):    </td><td><%= request.getAttribute("aotypetext") %></td></tr>
    <tr><td class='key'>číslo:         </td><td><%= request.getAttribute("aonr") %></td></tr>
    <tr><td class='group' colspan='2'>Platnost</td></tr>
    <tr><td class='key'>od:            </td><td><%= request.getAttribute("validFrom") %></td></tr>
    <tr><td class='key'>do:            </td><td><%= request.getAttribute("validTo") %></td></tr>
    <tr><td class='group' colspan='2'>Adresa</td></tr>
    <tr><td class='key'>Oblast:        </td><td><%= request.getAttribute("xao") %></td></tr>
    <tr><td class='key'>Město:         </td><td><%= request.getAttribute("city") %></td></tr>
    <tr><td class='key'>Ulice:         </td><td><%= request.getAttribute("street") %></td></tr>
    <tr><td class='key'>Číslo domu:    </td><td><%= request.getAttribute("houseNum") %></td></tr>
    <tr><td class='key'>PSČ:           </td><td><%= request.getAttribute("postCode") %></td></tr>
</table>

<hr noshade />

<%@ include file="../include/page_tail_popup.jsp" %>

