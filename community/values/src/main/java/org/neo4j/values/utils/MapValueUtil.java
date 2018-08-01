/*
 * Copyright (c) 2002-2018 "Neo4j,"
 * Neo4j Sweden AB [http://neo4j.com]
 *
 * This file is part of Neo4j.
 *
 * Neo4j is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.neo4j.values.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.neo4j.values.AnyValue;
import org.neo4j.values.storable.Values;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static java.lang.String.format;

public class MapValueUtil
{
    /**
     * Turn a string representation of a Map<String, Object> into a Map<String, AnyValue>.
     * @param mapRepresentation
     * @return
     */
    public static Map<String, Object> parseMap( String mapRepresentation )
    {
        ObjectMapper mapper = new ObjectMapper();
        mapper.enable( DeserializationFeature.USE_JAVA_ARRAY_FOR_JSON_ARRAY );
        try
        {
            HashMap<String, Object> hashMap = (HashMap<String, Object>) mapper.readValue( mapRepresentation, HashMap.class );
            replaceArrays( hashMap );
            return hashMap;
        }
        catch ( IOException e )
        {
            // TODO: Indicate error while parsing
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Using ObjectMapper arrays are being received as Object[] containing... Object, the purpose of this method is to
     * cast each array into the original values it contains, i.e. if the data type of the elementis in the array is
     * Integer, it will get transformed to an array of type Integer[]
     * @param map
     */
    private static void replaceArrays( HashMap<String, Object> map )
    {
        for ( String key : map.keySet() )
        {
            Object value = map.get(key);
            if ( value instanceof Object[] )
            {
                Object[] objects = (Object[]) value;
                if ( objects.length > 0 )
                {
                    Object first =  objects[0];
                    if ( first instanceof String )
                    {
                        map.put( key, Arrays.copyOf( objects, objects.length, String[].class ) );
                    }
                    else if ( first instanceof Byte )
                    {
                        map.put( key, Arrays.copyOf( objects, objects.length, Byte[].class ) );
                    }
                    else if ( first instanceof Long )
                    {
                        map.put( key, Arrays.copyOf( objects, objects.length, Long[].class ) );
                    }
                    else if ( first instanceof Integer )
                    {
                        map.put( key, Arrays.copyOf( objects, objects.length, Integer[].class ) );
                    }
                    else if ( first instanceof Double )
                    {
                        map.put( key, Arrays.copyOf( objects, objects.length, Double[].class ) );
                    }
                    else if ( first instanceof Float )
                    {
                        map.put( key, Arrays.copyOf( objects, objects.length, String[].class ) );
                    }
                    else if ( first instanceof Boolean )
                    {
                        map.put( key, Arrays.copyOf( objects, objects.length, Boolean[].class ) );
                    }
                    else if ( first instanceof Character )
                    {
                        map.put( key, Arrays.copyOf( objects, objects.length, Character[].class ) );
                    }
                    else if ( first instanceof Short )
                    {
                        map.put( key, Arrays.copyOf( objects, objects.length, Short[].class ) );
                    }
                    else
                    {
                        throw new IllegalArgumentException(
                                format( "[%s:%s] is not a supported value for arrays inside maps", first, first.getClass().getName() ) );
                    }
                }
                else
                {
                    // I've seen that empty arrays are treated as empty string arrays
                    map.put( key, new String[0]);
                }
            }
            else if ( value instanceof HashMap<?,?> )
            {
                HashMap<String, Object> innerMap = (HashMap<String, Object>) value;
                replaceArrays( innerMap );
            }
        }

    }

    /**
     * Serialize a Map into a string
     * @param map
     * @return
     */
    public static String stringifyMap( Map<String, Object> map )
    {
        ObjectMapper mapper = new ObjectMapper();
        // Using a HashMap as backing storage...
        String mapRepresentation = null;
        try
        {
            mapRepresentation = mapper.writeValueAsString(map);
        }
        catch ( JsonProcessingException e )
        {
            throw new IllegalArgumentException( "There was an error converting your map to a string", e );
        }
        return mapRepresentation;
    }
}
