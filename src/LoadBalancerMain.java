///////////////////////////////////////////////////////////////////////////////
// Title:            LoadBalancerMain
// Files:            LoadBalancerMain.java, Server.java, SimpleHashMap.java,
//                   PageHandler.java, FullHashMapException.java
//
// Semester:         CS367 Spring 2014
//
// Author:           William Jen
// Email:            wjen@wisc.edu
// CS Login:         jen
// Lecturer's Name:  Professor Jim Skrentny
//////////////////// PAIR PROGRAMMERS COMPLETE THIS SECTION ////////////////////
// Pair Partner:     Allen Hung
// Email:            athung2@wisc.edu
// CS Login:         ahung
// Lecturer's Name:  Professor Jim Skrentny
///////////////////////////////////////////////////////////////////////////////

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

/**
 * The LoadBalancerMain class simulates routing page requests to servers.
 */
public class LoadBalancerMain
{
    public static final String IP_ADDRESS_TEMPLATE = "192.168.0.";

    /**
     * Simulates loading pages onto multiple servers and prints out a list of
     * requests to each ip server address
     * 
     * @param args
     *            the number of available hosting servers the number of pages
     *            that each hosting server can cache the path to an input file
     *            optional: the String "-v" indicating that the program should
     *            operate in verbose mode
     * @throws IOException
     *             If the file cannot be read due to a system lock.
     */
    public static void main(String[] args) throws IOException
    {
        if (args.length != 3 && args.length != 4)
        {
            System.out.println("Usage: java LoadBalancerMain <max servers> "
                    + "<cache size> <input file> <Optional: -v verbose>");
            return;
        }

        int maxServers = Integer.parseInt(args[0]);
        int cacheSize = Integer.parseInt(args[1]);
        File input = new File(args[2]);

        if (!input.exists() || !input.canRead())
        {
            System.out.println("Error: Cannot read from input file!");
            return;
        }

        boolean isVerbose = (args.length == 4) && (args[3].equals("-v"));

        // initialize servers
        Server[] servers = new Server[maxServers];

        for (int i = 0; i < maxServers; i++)
            servers[i] = new Server(IP_ADDRESS_TEMPLATE + i, cacheSize);

        SimpleHashMap<String, PageHandler> map
                                    = new SimpleHashMap<String, PageHandler>();

        BufferedReader reader = new BufferedReader(new FileReader(input));

        int next = 0;
        int evictions = 0;

        String line;
        while ((line = reader.readLine()) != null)
        {
            if (next >= maxServers)
                next = 0;

            // check for existence of request
            PageHandler handler = map.get(line);

            Server server;

            // if page already exists, then grab it and increment
            if (handler != null)
            {
                handler.request();

                if (isVerbose)
                    System.out.println(handler.getServer().getAddress());

                continue;
            }

            else
            {
                // otherwise grab next server from the array
                server = servers[next];
                next++;
            }

            // if the server is full, evict a page
            if (server.isFull())
            {
                PageHandler leastUsed = null;

                /*
                 * iterate through all entries and grab the page that is least
                 * used
                 */
                for (SimpleHashMap.Entry<String, PageHandler> entry 
                                                                : map.entries())
                {
                    // ignore all entries from a different server
                    if (!entry.getValue().getServer().equals(server))
                        continue;
                    
                    long small_time = leastUsed.getLastAccessTime();
                    long entry_time = entry.getValue().getLastAccessTime();
                    
                    /*
                     * if the reference is not set or the entry has smaller
                     * values, set the ref to the smaller entry's value
                     */
                    if ((leastUsed == null || (small_time > entry_time)))
                        leastUsed = entry.getValue();
                }

                // get the evicted page name [key in the map]
                String evicted_page = leastUsed.getPage();

                /*
                 * remove it from the map as well as decrease # of pages on
                 * server
                 */
                map.remove(evicted_page);
                server.unload();

                // increment number of evictions
                evictions++;

                if (isVerbose)
                    System.out.println("Page " + evicted_page + " has been "
                            + "evicted.");

            }

            if (isVerbose)
                System.out.println(server.getAddress());

            // load the page into the map
            map.put(line, new PageHandler(server, line));
            server.load();
            server.route();
        }

        reader.close();

        // print out the number of requests per server
        for (Server s : servers)
            System.out.println(s);

        // print out the number of evictions
        System.out.println("Evictions : " + evictions);
    }

}
