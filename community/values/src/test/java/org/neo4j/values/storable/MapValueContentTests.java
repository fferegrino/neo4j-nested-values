package org.neo4j.values.storable;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;



class MapValueContentTests
{

    @Test
    void shouldReturnMixed()
    {
        MapValueContent storable = MapValueContent.STORABLE;
        MapValueContent virtual = MapValueContent.VIRTUAL;

        MapValueContent actual1 = storable.Combine(MapValueContent.VIRTUAL);
        MapValueContent actual2 = virtual.Combine(MapValueContent.STORABLE);

        assertEquals(MapValueContent.MIXED, actual1);
        assertEquals(MapValueContent.MIXED, actual2);
    }

    @Test
    void shouldReturnNone()
    {
        MapValueContent empty = MapValueContent.EMPTY;

        MapValueContent actual = empty.Combine(MapValueContent.EMPTY);

        assertEquals(MapValueContent.EMPTY, actual);
    }

    @Test
    void shouldReturnSame()
    {
        MapValueContent storable = MapValueContent.STORABLE;

        MapValueContent actual = storable.Combine(MapValueContent.STORABLE);
        assertEquals(MapValueContent.STORABLE, actual);

        actual = storable.Combine(MapValueContent.EMPTY);
        assertEquals(MapValueContent.STORABLE, actual);
    }
}
