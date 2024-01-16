package ru.seits.utils.fsm;

import java.util.Set;

/**
 * FSM interface. This is the main abstraction for a finite state machine.
 *
 * @author pwipo.seits@gmail.com. based on https://github.com/j-easy/easy-states, Mahmoud Ben Hassine (mahmoud.benhassine@icloud.com)
 */
public interface FiniteStateMachine {

    /**
     * Return current FSM state.
     *
     * @return current FSM state
     */
    Object getCurrentState();

    void setCurrentState(Object currentState);

    /**
     * Return FSM initial state.
     *
     * @return FSM initial state
     */
    Object getInitialState();

    /**
     * Return FSM final states.
     *
     * @return FSM final states
     */
    Set<Object> getFinalStates();

    boolean isInFinaleState();

    /**
     * is in work state
     * @return true if current state not initial state and not in final states
     */
    boolean isInWorkState();

    /**
     * Return FSM registered transitions.
     *
     * @return FSM registered transitions
     */
    Set<Transition> getTransitions();

    /**
     * Return the last transition made.
     *
     * @return the last transition made
     */
    Transition getLastTransition();

    /**
     * Fire an event. According to event type, the FSM will make the right transition.
     *
     * @param event to fire
     * @return The next FSM state defined by the transition to make
     */
    Object fire(Object event);

    Throwable getLastException();

    void setLastException(Throwable lastException);

    // List<Object> getCurrentExecuteEvents();
}
