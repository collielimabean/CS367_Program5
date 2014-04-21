import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

public class LoadBalancerMain
{

    public static void main(String[] args) throws IOException
    {
        // TODO: Check the number of arguments

        int maxServers = Integer.parseInt(args[0]);
        int cacheSize = Integer.parseInt(args[1]);
        BufferedReader br = new BufferedReader(new FileReader(args[2]));
        boolean isVerbose = (args.length == 4) && (args[3].equals("-v"));

        String line;
        SimpleHashMap<K, V> map = new SimpleHashMap<K, V>(); // TODO: Set the
                                                             // types for the
                                                             // HashMap

        while ((line = br.readLine()) != null)
        {
            // TODO: Handle each page request
        }
        br.close();

        // TODO: Output the number of requests routed to each server
        // TODO: Output the total number of evictions
    }

}
