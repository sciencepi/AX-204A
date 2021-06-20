package Util;

public class Logger{
    public static String _prefix = "INFO";
    public String util_prefix = "";
    public Logger(String prefix){
        util_prefix = prefix;
    }

    public void log(String d){
        System.out.println("["+util_prefix+"]: "+d);
    }
}