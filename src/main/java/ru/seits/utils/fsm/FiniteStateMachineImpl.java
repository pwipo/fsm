package ru.seits.utils.fsm;


import org.apache.commons.lang3.StringUtils;

import java.util.*;

/**
 * implementation of FiniteStateMachine
 *
 * @author pwipo.seits@gmail.com. based on https://github.com/j-easy/easy-states, Mahmoud Ben Hassine (mahmoud.benhassine@icloud.com)
 */
public class FiniteStateMachineImpl implements FiniteStateMachine {

    // private static final Logger logger = LoggerFactory.getLogger(FiniteStateMachineImpl.class);

    private volatile Object currentState;
    private final Object initialState;
    private final Set<Object> finalStates;
    private final List<Transition> transitions;
    private Transition lastTransition;
    private Throwable lastException;
    // private final List<Object> currentExecuteEvents;

    public FiniteStateMachineImpl(final Object initialState, final Set<Object> finalStates, final List<Transition> transitions) {
        if (initialState == null || (initialState instanceof String && StringUtils.isBlank((String) initialState)))
            throw new IllegalArgumentException("initialState");
        Objects.requireNonNull(finalStates);
        Objects.requireNonNull(transitions);

        this.initialState = initialState;
        this.currentState = initialState;
        this.transitions = new ArrayList<>();
        this.finalStates = new HashSet<>();
        lastException = null;

        finalStates.forEach(finalState -> {
            if (finalState == null || (finalState instanceof String && StringUtils.isBlank((String) finalState)))
                throw new IllegalArgumentException("finalState");
            this.finalStates.add(finalState);
        });

        transitions.forEach(transition -> {
            Objects.requireNonNull(transition);
            this.transitions.add(transition);
        });
        // currentExecuteEvents = isMultithread ? Collections.singletonList(new ArrayList<>()) : null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object getCurrentState() {
        return currentState;
    }

    @Override
    public void setCurrentState(Object currentState) {
        this.currentState = currentState;
    }

    @Override
    public Object getInitialState() {
        return initialState;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Set<Object> getFinalStates() {
        return finalStates;
    }

    @Override
    public boolean isInFinaleState() {
        return !finalStates.isEmpty() && finalStates.contains(currentState);
    }

    @Override
    public boolean isInWorkState() {
        return !isInFinaleState() && !getInitialState().equals(currentState);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Set<Transition> getTransitions() {
        return new HashSet<>(transitions);
    }

    @Override
    public Transition getLastTransition() {
        return lastTransition;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final /*synchronized*/ Object fire(final Object event) {
        /*
        if (StringUtils.isBlank(event))
            throw new IllegalArgumentException("event");
        */

        if (isInFinaleState()) {
            // logger.warn("FSM is in final state '{}', event '{}' is ignored.", currentState, event);
            return currentState;
        }

        Optional<Transition> oTransition = transitions.stream()
                .filter(t -> currentState.equals(t.getSourceState()) && t.isOurEvent(event))
                .findAny();
        if (oTransition.isEmpty()) {
            // logger.warn("for state '{}' no actions for event '{}'", currentState, event);
            return currentState;
        }

        //perform action, if any
        oTransition.ifPresent(transition -> {
            // Object eventTmp=event;
            // try {
                /*
                if (event != null && currentExecuteEvents!=null)
                    currentExecuteEvents.add(event);
                */

            TransitionResult result = null;
            try {
                result = transition.execute(event);
            } catch (Exception e) {
                // logger.error("during handling event '{}' of transition '{}', an exception occurred: ", event, transition, e);
                // e.printStackTrace();
                lastException = e;
                // result = false;
                result = transition.buildResult(transition.getErrorState(), event, transition.getErrorEvent());
            }

            // currentState = result ? transition.getTargetState() : transition.getErrorState();
            if (isInFinaleState())
                return;

            currentState = result != null && result.getState() != null ? result.getState() : currentState;
            lastTransition = transition;

            // realization fsm with transaction
                /*if (result) {
                    Object targetEvent = transition.getTargetEvent();
                    if (targetEvent != null) {
                        if (event != null)
                            currentExecuteEvents.remove(event);
                        fire(targetEvent);
                    }
                } else {
                    Object errorEvent = transition.getErrorEvent();
                    if (errorEvent != null) {
                        if (event != null)
                            currentExecuteEvents.remove(event);
                        fire(transition.getErrorEvent());
                    }
                }*/
            Object targetEvent = result != null ? result.getEvent() : null;
            if (targetEvent != null) {
                    /*
                    if (eventTmp != null && currentExecuteEvents!=null) {
                        currentExecuteEvents.remove(eventTmp);
                        eventTmp=null;
                    }
                    */
                fire(targetEvent);
            }
            /*
            } finally {
                if (eventTmp != null && currentExecuteEvents!=null)
                    currentExecuteEvents.remove(eventTmp);
            }
            */
        });

        return currentState;
    }

    /*
    @Override
    public List<Object> getCurrentExecuteEvents() {
        return currentExecuteEvents;
    }
    */

    @Override
    public Throwable getLastException() {
        return lastException;
    }

    @Override
    public void setLastException(Throwable lastException) {
        this.lastException = lastException;
    }
}
