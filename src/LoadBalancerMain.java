import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class LoadBalancerMain
{
    public static final String IP_ADDRESS_TEMPLATE = "192.168.0.";
    
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
        
        Server[] servers = new Server[maxServers];
        
        for (int i = 0; i < maxServers; i++)
            servers[i] = new Server(IP_ADDRESS_TEMPLATE + i, cacheSize);

        SimpleHashMap<String, PageHandler> map = new SimpleHashMap<String, PageHandler>();

        BufferedReader reader = new BufferedReader(new FileReader(input));
        
        int next = 0;
        
        int evictions = 0;
        
        int lineNumber = 1; // debugging tool - remove when done
        
        String line;
        while ((line = reader.readLine()) != null)
        {
            lineNumber++;
            
            if (lineNumber >= 999222)
            {
                int lel = 3; // debugging tool - remove when done
            }
            
            if (next >= maxServers)
                next = 0;
            
            // check for existence of request
            PageHandler handler = map.get(line);
            
            Server server;
            
            if (handler != null)
            {
                server = handler.getServer();
                handler.request();
                
                if (isVerbose)
                    System.out.println(server.getAddress());
                
                continue;
            }
            
            else
            {
                server = servers[next];
                next++;
            }
            
            if (server.isFull())
            {
                PageHandler leastUsed = null;
                
                for (SimpleHashMap.Entry<String, PageHandler> entry : map.entries())
                {
                    if (!entry.getValue().getServer().equals(server))
                        continue;
                    
                    if ((leastUsed == null || leastUsed.getNumberRequests() < entry.getValue().getNumberRequests()))
                        leastUsed = entry.getValue();
                }
                
                String evicted_page = leastUsed.getPage();
                map.remove(evicted_page);
                server.unload();
                evictions++;
                
                if (isVerbose)
                    System.out.println("Page " + evicted_page + " has been evicted.");

            }
            
            if (isVerbose)
                System.out.println(server.getAddress());
            
            
            map.put(line, new PageHandler(server, line));
            server.load();
            server.route();
        }
        
        reader.close();
        
        for (Server s : servers)
            System.out.println(s);
        
        System.out.println("Evictions : " + evictions);
    }

}
