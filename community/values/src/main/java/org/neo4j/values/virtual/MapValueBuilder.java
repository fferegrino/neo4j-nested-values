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
package org.neo4j.values.virtual;

import java.util.HashMap;
import java.util.Map;

import org.neo4j.values.AnyValue;
import org.neo4j.values.storable.MapValue;

public class MapValueBuilder
{
    private final HashMap<String, AnyValue> map;

    public MapValueBuilder()
    {
        this.map = new HashMap<>(  );
    }

    public MapValueBuilder( int size )
    {
        this.map = new HashMap<>( size );
    }

    public AnyValue add( String key, AnyValue value )
    {
        return map.put( key, value );
    }

    public void clear()
    {
        map.clear();
    }

    public MapValue build()
    {
        return new MapValue.MapWrappingMapValue( map );
    }

}
