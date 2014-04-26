
public class PageHandler
{
    private Server server;
    private String pageName;
    private long numRequests;
    
    public PageHandler(Server server, String pageName)
    {
        this.server = server;
        this.pageName = pageName;
        
        numRequests = 0;
    }
    
    public Server getServer()
    {
        return server;
    }
    
    public String getPage()
    {
        return pageName;
    }
    
    public long getNumberRequests()
    {
        return numRequests;
    }
    
    public void request()
    {
        numRequests++;
        server.route();
    }
    
}
