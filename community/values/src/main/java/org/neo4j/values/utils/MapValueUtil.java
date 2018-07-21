package org.neo4j.values.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.neo4j.values.AnyValue;
import org.neo4j.values.storable.Values;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class MapValueUtil
{

    /**
     * Turn a string representation of a Map<String, Object> into a Map<String, AnyValue>.
     * @param mapRepresentation
     * @return
     */
    public static Map<String, AnyValue> parseMap(String mapRepresentation) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            HashMap<String, Object> hashMap = (HashMap<String, Object>) mapper.readValue(mapRepresentation, HashMap.class);
            HashMap<String, AnyValue> avMap = new HashMap<>();
            for (Map.Entry<String, Object> entry : hashMap.entrySet()) {
                avMap.put(entry.getKey(), Values.of(entry.getValue()));
            }
            return avMap;
        } catch (IOException e) {
            // I'm eating this exception, this is NOT good
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Serialize a Map into a string
     * @param map
     * @return
     */
    public static String stringifyMap(Map<String, Object> map) {
        ObjectMapper mapper = new ObjectMapper();
        // Using a HashMap as backing storage...
        String mapRepresentation = null;
        try {
            mapRepresentation = mapper.writeValueAsString(map);
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("There was an error converting your map to a string", e);
        }
        return mapRepresentation;
    }
}
