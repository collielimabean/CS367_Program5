
public class Server
{
    private String address;
    private long cacheSize;
    private long load;
    private long totalRequestsRouted; 
    
    public Server(String address, int cacheSize)
    {
        this.address = address;
        this.cacheSize = cacheSize;
        
        load = 0;
        totalRequestsRouted = 0;
    }
    
    public boolean load()
    {
        if (isFull())
            return false;
        
        load++;
        
        return true;
    }
    
    public boolean unload()
    {
        if (isEmpty())
            return false;
        
        load--;
        
        return true;
    }
    
    public boolean isFull()
    {
        return load == cacheSize;
    }
    
    public boolean isEmpty()
    {
        return load == 0;
    }
    
    public String getAddress()
    {
        return address;
    }
    
    public long getLoad()
    {
        return load;
    }
    
    public long getCacheSize()
    {
        return cacheSize;
    }
    
    public void route()
    {
        totalRequestsRouted++;
    }
    
    public long getTotalRequests()
    {
        return totalRequestsRouted;
    }
    
    public String toString()
    {
        return address + " : " + totalRequestsRouted;
    }
}
