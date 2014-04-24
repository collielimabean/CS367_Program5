import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

public class LoadBalancerMain
{

    public static void main(String[] args) throws IOException
    {
        if (args.length != 3 || args.length != 4)
        {
            System.out.println("Usage: java LoadBalancerMain <max servers> "
                    + "<cache size> <input file> <Optional: -v verbose");
            return;
        }
        
        int maxServers = Integer.parseInt(args[0]);
        int cacheSize = Integer.parseInt(args[1]);
        File input = new File(args[2]);
        
        if(!input.exists() || !input.canRead())
        {
            System.out.println("Error: Cannot read from input file!");
            return;
        }
        
        boolean isVerbose = (args.length == 4) && (args[3].equals("-v"));
        
        SimpleHashMap<K, V> map = new SimpleHashMap<K, V>();
        
        Scanner scan = new Scanner(input);
        
        while (scan.hasNextLine())
        {
            
            // TODO: Handle each page request
        }
        
        scan.close();

        // TODO: Output the number of requests routed to each server
        // TODO: Output the total number of evictions
    }

}
