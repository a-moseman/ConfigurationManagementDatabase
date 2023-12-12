package org.amoseman.cmdb;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.Map;

public class AccountLoader {
    public static Map<String, String> load(String path) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            Map<String, String> map = (Map<String, String>) mapper.readValue(Paths.get(path).toFile(), Map.class);
            return map;

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
