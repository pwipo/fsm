package ru.seits.utils.fsm;

import java.util.function.BiFunction;
import java.util.function.Predicate;
import java.util.regex.Pattern;

public class TransitionBuilder {
    private String name;
    private Object sourceState;
    private String event = null;
    private Pattern eventPattern = null;
    private Predicate<Object> eventChecker = null;
    private Predicate<Object> eventHandler = null;
    private BiFunction<Object, Transition, TransitionResult> eventHandlerDirect = null;
    private Object targetState = null;
    private Object targetEvent = null;
    private Object errorState;
    private Object errorEvent;
    private BiFunction<Object, Object, Object> eventTransformer;

    public TransitionBuilder setName(String name) {
        this.name = name;
        return this;
    }

    public TransitionBuilder setSourceState(Object sourceState) {
        this.sourceState = sourceState;
        return this;
    }

    public TransitionBuilder setEvent(String event) {
        this.event = event;
        return this;
    }

    public TransitionBuilder setEventPattern(Pattern eventPattern) {
        this.eventPattern = eventPattern;
        return this;
    }

    public TransitionBuilder setEventChecker(Predicate<Object> eventChecker) {
        this.eventChecker = eventChecker;
        return this;
    }

    public TransitionBuilder setEventHandler(Predicate<Object> eventHandler) {
        this.eventHandler = eventHandler;
        return this;
    }

    public TransitionBuilder setEventHandlerDirect(BiFunction<Object, Transition, TransitionResult> eventHandlerDirect) {
        this.eventHandlerDirect = eventHandlerDirect;
        return this;
    }

    public TransitionBuilder setTargetState(Object targetState) {
        this.targetState = targetState;
        return this;
    }

    public TransitionBuilder setTargetEvent(Object targetEvent) {
        this.targetEvent = targetEvent;
        return this;
    }

    public TransitionBuilder setErrorState(Object errorState) {
        this.errorState = errorState;
        return this;
    }

    public TransitionBuilder setErrorEvent(Object errorEvent) {
        this.errorEvent = errorEvent;
        return this;
    }

    public TransitionBuilder setEventTransformer(BiFunction<Object, Object, Object> eventTransformer) {
        this.eventTransformer = eventTransformer;
        return this;
    }

    public TransitionImpl build() {
        return new TransitionImpl(name, sourceState, event, eventPattern, eventChecker, eventHandler, eventHandlerDirect, targetState, targetEvent, errorState, errorEvent, eventTransformer);
    }
}