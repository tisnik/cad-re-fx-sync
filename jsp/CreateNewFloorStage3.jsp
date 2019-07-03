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
        <!--
        function floorInfoClick(buildingId, floorId)
        {
            var url = "FloorInfo?building="+buildingId+"&floor="+floorId;
            var w=window.open(url, "informace", 'width=420,height=550,toolbar=no,status=no,titlebar=no,menubar=no,toolbar=no,resizable=yes,dependent=yes,scrollbars=yes');
            w.focus();
            return false;
        }
        function floorVariantInfoClick(buildingId, floorId, floorVariantId)
        {
            var url = "FloorVariantInfo?building="+buildingId+"&floor="+floorId+"&variant="+floorVariantId;
            var w=window.open(url, "informace", 'width=420,height=550,toolbar=no,status=no,titlebar=no,menubar=no,toolbar=no,resizable=yes,dependent=yes,scrollbars=yes');
            w.focus();
            return false;
        }
        function checkForm(doc)
        {
            var name = document.getElementById("name").value;
            var id = document.getElementById("id").value;
            var valid = document.getElementById("valid").value;
            var existingVariants =
[
<%= request.getAttribute("existingVariants") %>
];
            if (name == "")
            {
                window.alert("Není vyplněn název patra");
                return false;
            }
            if (id == "")
            {
                window.alert("Není vyplněn identifikátor patra");
                return false;
            }
            var variantExist = false;
            for (var i in existingVariants)
            {
                var iId = existingVariants[i][0];
                var iValid = existingVariants[i][1];
                if (iId === id && iValid === valid)
                {
                    variantExist = true;
                    break;
                }
            }
            if (variantExist)
            {
                window.alert("Vámi vybraná varianta patra již existuje");
                return false;
            }
            return true;
        }
        -->
        </script>
    </head>
    <body>
        <h1>CAD-RE-FX sync</h1>
        <br />
        <table border="2" frame="border" rules="all" cellspacing="1" cellpadding="1" class="formular" style="width:900px" summary="" bordercolorlight="black">
            <tr class='key'><td>Vybraná hospodářská jednotka:</td><td><%= request.getAttribute("selectedManagementUnit") %></td></tr>
            <tr class='key'><td>Vybraná budova:</td><td><%= request.getAttribute("selectedBuilding") %></td></tr>
        </table>
        <h2>Existující patra ve vybrané budově</h2>
        <table border="2" frame="border" rules="all" cellspacing="1" cellpadding="1" class="formular" style="width:900px" summary="" bordercolorlight="black">
            <tr class='key'><td>Název</td><td>Identifikace</td><td>Platnost</td><td>Poznámky</td></tr>
            <%= request.getAttribute("existingFloors") %>
        </table>
        <h2>Nová varianta podlaží krok 3/4: zadání informací o novém patře</h2>
        <form action="CreateNewFloorStage4" method="get" name="inputForm" onsubmit="return checkForm(this);">
        <table border="2" frame="border" rules="all" cellspacing="1" cellpadding="1" class="formular" style="width:900px" summary="" bordercolorlight="black">
            <tr><td>Název:</td><td><input type='text' size='20' id="name" name='name'></td></tr>
            <tr><td>Identifikace:</td><td><input type='text' size='20' id="id" name='id'></td></tr>
            <tr><td>Platnost:</td><td><input type='text' size='10' id="valid" readonly="readonly" name='valid'>
			<script language="JavaScript">
			new tcal ({
				// form name
				'formname': 'inputForm',
				// input name
				'controlname': 'valid'
			});
			</script>
			</td></tr>
            <tr><td valign="top">Výkres:</td><td><select id="drawing" name="drawing" size="1"><%= request.getAttribute("drawingList") %></select></td></tr>
            <tr><td>&nbsp;</td><td><input type='submit' value='Vytvořit nové patro'></td></tr>
            <tr><td colspan='2'>&nbsp;</td></tr>
            <tr><td colspan='2'><a href="CreateNewFloorStage2"><img src="img/1leftarrow.gif" border="0" />&nbsp;Zpět</a></td></tr>
        </table>
        </form>
    </body>
</html>
