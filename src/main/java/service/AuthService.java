package service;

import java.util.HashMap;
import java.util.Map;

public class AuthService {
    public static Map<String, String > getVoterRegistry(){
        Map<String ,String> registry = new HashMap<>();
        registry.put("dilen", "dilen123");
        registry.put("sam", "sam123");
        registry.put("sayuri", "sayuri123");
        registry.put("imasha", "imasha123");
        return registry;
    }
}