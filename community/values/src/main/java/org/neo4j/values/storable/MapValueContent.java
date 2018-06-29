package org.neo4j.values.storable;

public enum MapValueContent {
    EMPTY,
    STORABLE,
    VIRTUAL,
    MIXED;

    public MapValueContent Combine(MapValueContent other)
    {
        if(other == STORABLE && this == VIRTUAL)
        {
            return MIXED;
        }
        if(other == VIRTUAL && this == STORABLE)
        {
            return MIXED;
        }
        if(other == EMPTY)
            return this;

        return other;
    }
}
