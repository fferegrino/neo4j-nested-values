/*
 * Copyright (c) 2002-2018 "Neo4j,"
 * Neo4j Sweden AB [http://neo4j.com]
 *
 * This file is part of Neo4j Enterprise Edition. The included source
 * code can be redistributed and/or modified under the terms of the
 * GNU AFFERO GENERAL PUBLIC LICENSE Version 3
 * (http://www.fsf.org/licensing/licenses/agpl-3.0.html) with the
 * Commons Clause, as found in the associated LICENSE.txt file.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * Neo4j object code can be licensed independently from the source
 * under separate terms from the AGPL. Inquiries can be directed to:
 * licensing@neo4j.com
 *
 * More information is also available at:
 * https://neo4j.com/licensing/
 */
package org.neo4j.cypher.internal.runtime.vectorized

import org.neo4j.cypher.result.QueryResult.QueryResultVisitor
import org.neo4j.values.storable.MapValue
import org.neo4j.values.storable.Values

object QueryState {
  val EMPTY = QueryState(Values.EMPTY_MAP, null, 10000, singeThreaded = true)
}

/**
  * The query state of the morsel runtime
  */
case class QueryState(params: MapValue,
                      visitor: QueryResultVisitor[_],
                      morselSize: Int,
                      singeThreaded: Boolean, // hack until we solve [Transaction 1 - * Threads] problem
                      reduceCollector: Option[ReduceCollector] = None)
