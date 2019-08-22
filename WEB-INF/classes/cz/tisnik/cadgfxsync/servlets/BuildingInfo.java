package cz.tisnik.cadgfxsync.servlets;

import java.io.IOException;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cz.tisnik.cadgfxsync.sap.SapInterface;
import cz.tisnik.cadgfxsync.sap.SapObject;

public class BuildingInfo extends CustomHttpServlet
{
    /**
     * Generated serial version ID.
     */
    private static final long serialVersionUID = -5316525198807234124L;

    public void doProcess(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException
    {
        this.log.logBegin("doProcess()");
        ServletContext servletContext = this.getServletContext();

        setAllAttributes(request, request.getParameter("id"));
        this.redirectTo(servletContext, request, response, "ok");
        this.log.logEnd("doProcess()");
    }

    private void setAllAttributes(HttpServletRequest request, String intreno)
    {
        if (intreno == null)
        {
            setAttributes(request, null);
        }
        SapObject sapObject = new SapInterface().getBuilding(intreno);
        setAttributes(request, sapObject);
    }

    private void setAttributes(HttpServletRequest request, SapObject sapObject)
    {
        if (sapObject == null)
        {
            sapObject = new SapObject();
        }
        setAttribute(request, "intreno", sapObject.getIntreno());
        setAttribute(request, "aoid", sapObject.getAoid());
        setAttribute(request, "aotype", sapObject.getAoType());
        setAttribute(request, "aotypetext", sapObject.getAoTypeText());
        setAttribute(request, "aonr", sapObject.getAoNr());

        setAttribute(request, "validFrom", sapObject.getValidFrom());
        setAttribute(request, "validTo", sapObject.getValidTo());

        setAttribute(request, "xao", sapObject.getXao());
        setAttribute(request, "city", sapObject.getCity());
        setAttribute(request, "street", sapObject.getStreet());
        setAttribute(request, "houseNum", sapObject.getHouseNum());
        setAttribute(request, "postCode", sapObject.getPostCode());
    }

    private void setAttribute(HttpServletRequest request, String attributeName, String attributeValue)
    {
        request.setAttribute(attributeName, attributeValue == null ? "" : attributeValue);
    }

}
