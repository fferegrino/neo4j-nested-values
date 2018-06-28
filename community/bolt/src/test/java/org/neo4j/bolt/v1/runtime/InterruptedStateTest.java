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
package org.neo4j.bolt.v1.runtime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import org.neo4j.bolt.runtime.BoltStateMachineState;
import org.neo4j.bolt.runtime.MutableConnectionState;
import org.neo4j.bolt.runtime.StateMachineContext;
import org.neo4j.bolt.runtime.StateMachineMessage;
import org.neo4j.bolt.v1.messaging.AckFailure;
import org.neo4j.bolt.v1.messaging.DiscardAll;
import org.neo4j.bolt.v1.messaging.Init;
import org.neo4j.bolt.v1.messaging.Interrupt;
import org.neo4j.bolt.v1.messaging.PullAll;
import org.neo4j.bolt.v1.messaging.Reset;
import org.neo4j.bolt.v1.messaging.Run;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyMap;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.neo4j.values.storable.Values.EMPTY_MAP;

class InterruptedStateTest
{
    private final InterruptedState state = new InterruptedState();

    private final BoltStateMachineState readyState = mock( BoltStateMachineState.class );
    private final BoltStateMachineState failedState = mock( BoltStateMachineState.class );

    private final StateMachineContext context = mock( StateMachineContext.class );
    private final MutableConnectionState connectionState = new MutableConnectionState();

    @BeforeEach
    void setUp()
    {
        state.setReadyState( readyState );
        state.setFailedState( failedState );

        when( context.connectionState() ).thenReturn( connectionState );
    }

    @Test
    void shouldThrowWhenNotInitialized() throws Exception
    {
        InterruptedState state = new InterruptedState();

        assertThrows( IllegalStateException.class, () -> state.process( Reset.INSTANCE, context ) );

        state.setReadyState( readyState );
        assertThrows( IllegalStateException.class, () -> state.process( Reset.INSTANCE, context ) );

        state.setReadyState( null );
        state.setFailedState( failedState );
        assertThrows( IllegalStateException.class, () -> state.process( Reset.INSTANCE, context ) );
    }

    @Test
    void shouldProcessInterruptMessage() throws Exception
    {
        BoltStateMachineState newState = state.process( Interrupt.INSTANCE, context );

        assertEquals( state, newState ); // remains in interrupted state
    }

    @Test
    void shouldProcessResetMessageWhenInterrupted() throws Exception
    {
        connectionState.incrementInterruptCounter();
        connectionState.incrementInterruptCounter();
        assertTrue( connectionState.isInterrupted() );
        assertFalse( connectionState.hasPendingIgnore() );

        BoltStateMachineState newState = state.process( Reset.INSTANCE, context );

        assertEquals( state, newState ); // remains in interrupted state
        assertTrue( connectionState.hasPendingIgnore() );
    }

    @Test
    void shouldProcessResetMessage() throws Exception
    {
        when( context.resetMachine() ).thenReturn( true ); // reset successful
        BoltStateMachineState newState = state.process( Reset.INSTANCE, context );

        assertEquals( readyState, newState );
    }

    @Test
    void shouldHandleFailureDuringResetMessageProcessing() throws Exception
    {
        when( context.resetMachine() ).thenReturn( false ); // reset failed
        BoltStateMachineState newState = state.process( Reset.INSTANCE, context );

        assertEquals( failedState, newState );
    }

    @Test
    void shouldIgnoreMessagesOtherThanInterruptAndReset() throws Exception
    {
        List<StateMachineMessage> messages = asList( AckFailure.INSTANCE, PullAll.INSTANCE, DiscardAll.INSTANCE,
                new Run( "RETURN 1", EMPTY_MAP ), new Init( "Driver", emptyMap() ) );

        for ( StateMachineMessage message : messages )
        {
            connectionState.resetPendingFailedAndIgnored();

            BoltStateMachineState newState = state.process( message, context );

            assertEquals( state, newState ); // remains in interrupted state
            assertTrue( connectionState.hasPendingIgnore() );
        }
    }
}
