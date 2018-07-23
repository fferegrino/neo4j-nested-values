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

import org.junit.jupiter.api.Test;

import java.util.HashMap;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

public class MapUtilTest
{
    @Test
    void shouldHandleEqualArrays()
    {
        HashMap<String, Object> map1 = new HashMap<String, Object>()
        {
            {
                put("a", "a");
                put("a1", new int[]{1,2,3});
                put("a2", new float[]{1,2,3});
                put("sss", new String[]{"A", "B", "C"});
            }
        };
        HashMap<String, Object> map2 = new HashMap<String, Object>()
        {
            {
                put("a", "a");
                put("a1", new int[]{1,2,3});
                put("a2", new float[]{1,2,3});
                put("sss", new String[]{"A", "B", "C"});
            }
        };

        assertTrue(MapTestUtil.customComparison(map1, map2));
    }

    @Test
    void shouldHandleEqualNestedValues()
    {
        HashMap<String, Object> map1 = new HashMap<String, Object>();

        map1.put("a", "a");
        map1.put("a1", new int[]{1,2,3});
        map1.put("a2", new float[]{1,2,3});
        map1.put("sss", new String[]{"A", "B", "C"});
        HashMap<String, Object> map1Nested = new HashMap<String, Object>();
        map1Nested.put("a1", new int[]{1,2,3});
        map1Nested.put("a2", new float[]{1,2,3});
        map1Nested.put("sss", new String[]{"A", "B", "C"});
        map1.put("ss", map1Nested);

        HashMap<String, Object> map2 = new HashMap<String, Object>();

        map2.put("a", "a");
        map2.put("a1", new int[]{1,2,3});
        map2.put("a2", new float[]{1,2,3});
        map2.put("sss", new String[]{"A", "B", "C"});
        HashMap<String, Object> map2Nested = new HashMap<String, Object>();
        map2Nested.put("a1", new int[]{1,2,3});
        map2Nested.put("a2", new float[]{1,2,3});
        map2Nested.put("sss", new String[]{"A", "B", "C"});
        map2.put("ss", map2Nested);

        assertTrue(MapTestUtil.customComparison(map1, map2));
    }

    @Test
    void shouldHandleNotEqualNestedValues()
    {
        HashMap<String, Object> map1 = new HashMap<String, Object>();

        map1.put("a", "a");
        map1.put("a1", new int[]{1,2,3});
        map1.put("a2", new float[]{1,2,3});
        map1.put("sss", new String[]{"A", "B", "C"});
        HashMap<String, Object> map1Nested = new HashMap<String, Object>();
        map1Nested.put("a1", new int[]{1,2,3});
        map1Nested.put("a2", new float[]{1,2,3});
        map1Nested.put("sss", new String[]{"A", "B", "C"});
        map1.put("ss", map1Nested);

        HashMap<String, Object> map2 = new HashMap<String, Object>();

        map2.put("a", "a");
        map2.put("a1", new int[]{1,2,3});
        map2.put("a2", new float[]{1,2,3});
        map2.put("sss", new String[]{"A", "B", "C"});
        HashMap<String, Object> map2Nested = new HashMap<String, Object>();
        map2Nested.put("a1", new int[]{1,2,3});
        map2Nested.put("a2", new float[]{1,2,3});
        map2Nested.put("sss", new String[]{"a", "B", "C"}); // this contains lowercase a
        map2.put("ss", map2Nested);

        assertFalse(MapTestUtil.customComparison(map1, map2));
    }
}
