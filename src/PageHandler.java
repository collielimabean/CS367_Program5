/**
 * This class contains a mapped page and its accompanying server. It also keeps
 * track of how many requests the page has received.
 */
public class PageHandler
{
    private Server server;
    private String pageName;
    private long lastAccessTime;

    /**
     * Constructs a new PageHandler object with the specified server and page
     * names.
     * 
     * @param server
     *            Server to host the page
     * @param pageName
     *            Name of the page
     */
    public PageHandler(Server server, String pageName)
    {
        this.server = server;
        this.pageName = pageName;
        
        lastAccessTime = System.nanoTime();
    }

    /**
     * Gets the hosting server.
     * 
     * @return the Server object that hosts the page.
     */
    public Server getServer()
    {
        return server;
    }

    /**
     * Gets the hosted page name.
     * 
     * @return a String containing the page name.
     */
    public String getPage()
    {
        return pageName;
    }

    /**
     * Gets the number of requests for this page.
     * 
     * @return a long representing the number of requests for this page.
     */
    public long getLastAccessTime()
    {
        return lastAccessTime;
    }

    /**
     * Logs a request to a page.
     */
    public void request()
    {
        lastAccessTime = System.nanoTime();
        server.route();
    }

}
