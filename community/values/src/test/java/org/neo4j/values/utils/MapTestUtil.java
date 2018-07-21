package org.neo4j.values.utils;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Map;
import java.util.Set;

/**
 * Class that provides utilities to work with java.util.Map
 */
public class MapTestUtil
{
    public static boolean customComparison(Map<String, Object> first, Map<String, Object> second)
    {
        boolean equal = first.equals(second);
        if(equal)
            return equal;

        Set<String> firstKeys = first.keySet();
        Set<String> secondKeys = second.keySet();

        if(!firstKeys.equals(secondKeys))
            return false;

        for(String keyInFirst: firstKeys)
        {
            Object firstObject = first.get(keyInFirst);
            Object secondObject = second.get(keyInFirst);

            equal = firstObject.equals(secondObject);
            if(equal) // Objects are equal
                continue;
            else
            {
                Class<?> firstClass = firstObject.getClass();
                equal = firstClass.equals(secondObject.getClass());
                if(equal) // Same type
                {
                    if (firstClass.isArray())
                    {
                        Class<?> arrayType = firstClass.getComponentType();
                        if(arrayType == int.class)
                        {
                            equal =  Arrays.equals((int[])firstObject, (int[])secondObject);
                        }
                        else if(arrayType == float.class)
                        {
                            equal =  Arrays.equals((float[])firstObject, (float[])secondObject);
                        }
                        else if(arrayType == double.class)
                        {
                            equal =  Arrays.equals((float[])firstObject, (float[])secondObject);
                        }
                        else if(arrayType == char.class)
                        {
                            equal =  Arrays.equals((char[])firstObject, (char[])secondObject);
                        }
                        else if(arrayType == byte.class)
                        {
                            equal =  Arrays.equals((byte[])firstObject, (byte[])secondObject);
                        }
                        else if(arrayType == boolean.class)
                        {
                            equal =  Arrays.equals((boolean[])firstObject, (boolean[])secondObject);
                        }
                        else if (arrayType == short.class)
                        {
                            equal = Arrays.equals((short[]) firstObject, (short[])secondObject);
                        }
                        else
                        {
                            equal = Arrays.equals((Object[]) firstObject, (Object[])secondObject);
                        }
                    }
                    else if (firstObject instanceof Map)
                    {
                        equal = customComparison((Map<String, Object>) firstObject, (Map<String, Object>) secondObject);
                    }
                }
            }
            if(!equal)
                return false;
        }

        return true;
    }
}
