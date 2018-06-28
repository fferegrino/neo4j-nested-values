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
package org.neo4j.bolt.v1.messaging;

import java.util.Objects;

import org.neo4j.bolt.runtime.StateMachineMessage;
import org.neo4j.values.storable.MapValue;

import static java.util.Objects.requireNonNull;

public class Run implements StateMachineMessage
{
    private final String statement;
    private final MapValue params;

    public Run( String statement, MapValue params )
    {
        this.statement = requireNonNull( statement );
        this.params = requireNonNull( params );
    }

    public String statement()
    {
        return statement;
    }

    public MapValue params()
    {
        return params;
    }

    @Override
    public boolean safeToProcessInAnyState()
    {
        return false;
    }

    @Override
    public boolean equals( Object o )
    {
        if ( this == o )
        {
            return true;
        }
        if ( o == null || getClass() != o.getClass() )
        {
            return false;
        }
        Run that = (Run) o;
        return Objects.equals( statement, that.statement ) &&
               Objects.equals( params, that.params );
    }

    @Override
    public int hashCode()
    {
        return Objects.hash( statement, params );
    }

    @Override
    public String toString()
    {
        return "RUN " + statement + ' ' + params;
    }
}
