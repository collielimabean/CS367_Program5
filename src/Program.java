import java.util.List;


public class Program
{
    public static void main(String[] args)
    {
        SimpleHashMap<String, String> map = new SimpleHashMap<String, String>();
        
        for(int i = 0; i < 15; i++)
            map.put(new Integer(i).toString(), new Integer(i + 4).toString());
        
        List<SimpleHashMap.Entry<String, String>> list = map.entries();
        
        for (SimpleHashMap.Entry<String, String> e : list)
            System.out.println(e.getKey() + ", " + e.getValue());
        
    }
}
