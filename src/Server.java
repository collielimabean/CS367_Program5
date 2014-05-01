///////////////////////////////////////////////////////////////////////////////
// Main Class File:  LoadBalancerMain.java
// File:             LoadBalancerMain.java
// Semester:         CS367 Spring 2014
//
// Author:           Allen Hung <athung2@wisc.edu>
// CS Login:         ahung
// Lecturer's Name:  Professor Jim Skrentny
//////////////////// PAIR PROGRAMMERS COMPLETE THIS SECTION ////////////////////
// Pair Partner:     William Jen <wjen@wisc.edu>
// CS Login:         jen
// Lecturer's Name:  Professor Jim Skrentny
//////////////////////////// 80 columns wide ///////////////////////////////////
/**
 * This class simulates a Server with a load, cache size, and an IP address.
 * This class also keeps track of the total number of requests routed through
 * the server.
 */
public class Server
{
    private String address;
    private long cacheSize;
    private long load;
    private long totalRequestsRouted;

    /**
     * Constructs a new Server object with the IP address and cache size.
     * 
     * @param address
     *            A String containing the Server's IP Address
     * @param cacheSize
     *            An integer containing the maximum number of pages to cache.
     */
    public Server(String address, int cacheSize)
    {
        this.address = address;
        this.cacheSize = cacheSize;

        load = 0;
        totalRequestsRouted = 0;
    }

    /**
     * Loads a page onto the server, unless it is full.
     * 
     * @return true if was able to load another page onto the server.
     */
    public void load()
    {
        if (isFull())
            return;

        load++;
    }

    /**
     * Unloads or removes a page from the server, unless it is empty.
     * 
     * @return true if able to remove a page from the server.
     */
    public void unload()
    {
        if (isEmpty())
            return;

        load--;
    }

    /**
     * Checks whether the server is at max capacity.
     * 
     * @return true if at max capacity, false otherwise
     */
    public boolean isFull()
    {
        return load >= cacheSize;
    }

    /**
     * Checks whether the server is empty or has no elements.
     * 
     * @return true if empty, false otherwise
     */
    public boolean isEmpty()
    {
        return load <= 0;
    }

    /**
     * Gets the IP address of the server.
     * 
     * @return A String containing the server's IP address.
     */
    public String getAddress()
    {
        return address;
    }

    /**
     * Returns the number of pages currently loaded onto the server.
     * 
     * @return A long containing the current load of the server.
     */
    public long getLoad()
    {
        return load;
    }

    /**
     * Returns the maximum number of pages "cache size" that can be loaded onto
     * the server.
     * 
     * @return A long containing the cache size of the server.
     */
    public long getCacheSize()
    {
        return cacheSize;
    }

    /**
     * Routes a request through the simulated server.
     */
    public void route()
    {
        totalRequestsRouted++;
    }

    /**
     * Returns the total number of requests that have been routed through the
     * server.
     * 
     * @return the number of requests that have come through the server.
     */
    public long getTotalRequests()
    {
        return totalRequestsRouted;
    }

    /**
     * Returns a String detailing the IP Address and the total number of routed
     * requests.
     */
    public String toString()
    {
        return address + " : " + totalRequestsRouted;
    }
}
