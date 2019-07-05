package cz.tisnik.cadgfxsync.utils;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;

import cz.tisnik.cadgfxsync.Configuration;
import cz.tisnik.cadgfxsync.data.DataModel;

public class HttpUtils
{
    public static Configuration getConfiguration(HttpServletRequest request)
    {
        Configuration configuration = (Configuration)request.getSession().getAttribute("configuration");
        if (configuration == null)
        {
            System.out.println("new configuration created in HttpUtils.getConfiguration()");
            configuration = new Configuration();
        }
        return configuration;
    }

    public static void storeConfiguration(HttpServletRequest request, Configuration configuration)
    {
        request.getSession().setAttribute("configuration", configuration);
    }

    public static DataModel getDataModel(HttpServlet httpServlet)
    {
        ServletContext servletContext = httpServlet.getServletContext();
        return (DataModel) servletContext.getAttribute("data");
    }

    public static DataModel getDrawings(HttpServlet httpServlet)
    {
        ServletContext servletContext = httpServlet.getServletContext();
        return (DataModel) servletContext.getAttribute("drawings");
    }
}
