/**
 * @author Pavel Tišnovský
 */
package cz.tisnik.cadgfxsync.listeners;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import cz.tisnik.cadgfxsync.data.DataModel;
import cz.tisnik.cadgfxsync.data.DataModelInFiles;
import cz.tisnik.cadgfxsync.utils.Log;

/**
 * Objekt, který je vytvořený ve chvíli inicializace servlet kontextu (nasazení
 * aplikace na aplikační server) a naopak i zrušení servlet kontextu (odstranění
 * aplikace z aplikačního serveru). Právě v okamžiku inicializace může dojít
 * k načtení konfigurace i celého datového modelu použitého v informačním systému.
 *
 * @author Pavel Tišnovský
 */
public final class ContextListener implements ServletContextListener
{
    private ServletContext servletContext = null;

    /**
     * Instance objektu použitého pro logování do logovacího souboru či na
     * standardní výstup servlet kontejneru.
     */
    private Log log = new Log(this.getClass().getName());

  /**
   * Tato metoda je zavolána ihned po inicializaci servlet kontextu, tj. po
   * nasazení aplikace na aplikační server či servlet kontejner.
   */
    public void contextInitialized(ServletContextEvent event)
    {
        this.log.logBegin("initializing");
        this.servletContext = event.getServletContext();
        DataModelInFiles dmif = new DataModelInFiles();
        DataModel dataModel = dmif.readDataModel(this.servletContext, true, true);
        DataModel drawings = dmif.readDrawings(servletContext, true, true);
        this.servletContext.removeAttribute("drawings");
        this.servletContext.removeAttribute("data");
        this.servletContext.removeAttribute("initialized");
        this.servletContext.setAttribute("data", dataModel);
        this.servletContext.setAttribute("drawings", drawings);
        this.servletContext.setAttribute("initialized", true);
        this.log.logEnd("initializing");
    }

  /**
   * Tato metoda je zavolána těsně před zrušením servlet kontextu, tj. před
   * odstraněním aplikace z aplikačního serveru či servlet kontejneru.
   */
    public void contextDestroyed(ServletContextEvent event)
    {
        this.log.logBegin("destroying");
        this.servletContext = event.getServletContext();
        this.servletContext.removeAttribute("initialized");
        this.log.logEnd("destroying");
    }

  /**
   * Funkce, kterou lze použít pro otestování základní funkčnosti chování tohoto
   * objektu.
   *
   * @param args nepoužito
   */
    public static void main(String[] args)
    {
        System.out.println("test start");
        System.out.println("test end");
    }

}



//-----------------------------------------------------------------------------
//finito
//-----------------------------------------------------------------------------

//vim: foldmethod=marker
