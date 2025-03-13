package ru.seits.utils.fsm;


import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Predicate;
import java.util.regex.Pattern;

/**
 * implementation of Transition
 *
 * @author pwipo.seits@gmail.com. based on https://github.com/j-easy/easy-states, Mahmoud Ben Hassine (mahmoud.benhassine@icloud.com)
 */
public class TransitionImpl implements Transition {
    private String name;
    private Object sourceState;
    private Object targetState;
    private Object targetEvent;
    private Object errorState;
    private Object errorEvent;
    private String event;
    private Pattern eventPattern;
    private Predicate<Object> eventChecker;
    private Predicate<Object> eventHandler;
    private BiFunction<Object, Transition, TransitionResult> eventHandlerDirect;
    private BiFunction<Object, Object, Object> eventTransformer;

    // private ThreadLocal<Object> eventThreadLocal;

    TransitionImpl(
            String name,
            Object sourceState,
            String event,
            Pattern eventPattern,
            Predicate<Object> eventChecker,
            Predicate<Object> eventHandler,
            BiFunction<Object, Transition, TransitionResult> eventHandlerDirect,
            Object targetState, Object targetEvent,
            Object errorState, Object errorEvent,
            BiFunction<Object, Object, Object> eventTransformer
    ) {
        setName(name);
        setSourceState(sourceState);
        setEvent(event);
        setEventPattern(eventPattern);
        setEventChecker(eventChecker);
        setEventHandler(eventHandler);
        setEventHandlerDirect(eventHandlerDirect);
        setTargetState(targetState);
        setTargetEvent(targetEvent);
        setErrorState(errorState);
        setErrorEvent(errorEvent);
        setEventTransformer(eventTransformer);
        // eventThreadLocal = new ThreadLocal<>();
    }

    @Override
    public String getName() {
        return name;
    }

    public void setName(String name) {
        // Objects.requireNonNull(name);
        this.name = name;
    }

    @Override
    public Object getSourceState() {
        return sourceState;
    }

    public void setSourceState(Object sourceState) {
        if (sourceState == null || (sourceState instanceof String && Utils.isBlank((String) sourceState)))
            throw new IllegalArgumentException("sourceState");
        this.sourceState = sourceState;
    }

    @Override
    public Object getTargetState() {
        return targetState;
    }

    public void setTargetState(Object targetState) {
        if (/*(targetState == null && eventHandlerDirect == null) || */(targetState instanceof String && Utils.isBlank((String) targetState)))
            throw new IllegalArgumentException("targetState");
        this.targetState = targetState;
    }

    @Override
    public Object getTargetEvent() {
        /*
        if (eventTransformer != null) {
            return targetEvent != null ? eventTransformer.apply(eventThreadLocal.get(), targetEvent) : targetEvent;
        } else {
            return targetEvent;
        }
        */
        return targetEvent;
    }

    public void setTargetEvent(Object targetEvent) {
        // if (StringUtils.isBlank(targetEvent))
        //     throw new IllegalArgumentException("targetEvent");
        this.targetEvent = targetEvent;
    }

    @Override
    public Object getErrorState() {
        return errorState;
    }

    public void setErrorState(Object errorState) {
        if (errorState == null || (errorState instanceof String && Utils.isBlank((String) errorState)))
            throw new IllegalArgumentException("errorState");
        this.errorState = errorState;
    }

    @Override
    public Object getErrorEvent() {
        /*
        if (eventTransformer != null) {
            return errorEvent != null ? eventTransformer.apply(eventThreadLocal.get(), errorEvent) : errorEvent;
        } else {
            return errorEvent;
        }
        */
        return errorEvent;
    }

    public void setErrorEvent(Object errorEvent) {
        // if (StringUtils.isBlank(errorEvent))
        //     throw new IllegalArgumentException("errorEvent");
        this.errorEvent = errorEvent;
    }

    @Override
    public String getEvent() {
        return eventPattern != null ? eventPattern.pattern() : event;
    }

    public void setEvent(String event) {
        // Objects.requireNonNull(event);
        this.event = event;
        if (Utils.isBlank(getName()))
            setName(event);
    }

    public void setEventPattern(Pattern eventPattern) {
        this.eventPattern = eventPattern;
        if (eventPattern != null && Utils.isBlank(getName()))
            setName(eventPattern.pattern());
    }

    public void setEventChecker(Predicate<Object> eventChecker) {
        this.eventChecker = eventChecker;
    }

    @Override
    public boolean isOurEvent(Object event) {
        boolean result = true;
        if (eventChecker != null) {
            result = eventChecker.test(event);
        } else if (eventPattern != null) {
            result = event != null && eventPattern.matcher(event.toString()).find();
        } else if (this.event != null) {
            result = this.event.equals(event);
        }
        return result;
    }

    @Override
    public TransitionResult execute(Object event) {
        boolean result = false;
        if (eventHandlerDirect != null) {
            return eventHandlerDirect.apply(event, this);
        } else if (eventHandler != null) {
            result = eventHandler.test(event);
        } else {
            result = true;
        }
        return buildResult(
                result ? getTargetState() : getErrorState(),
                event,
                result ? getTargetEvent() : getErrorEvent());
    }

    @Override
    public TransitionResult buildResult(Object state, Object event, Object nextEvent) {
        return new TransitionResult(state, eventTransformer != null ? eventTransformer.apply(event, nextEvent) : nextEvent);
    }

    // @Override
    public Predicate<Object> getEventHandler() {
        return eventHandler;
    }

    public void setEventHandler(Predicate<Object> eventHandler) {
        // Objects.requireNonNull(eventHandler);
        this.eventHandler = eventHandler;
    }

    public BiFunction<Object, Transition, TransitionResult> getEventHandlerDirect() {
        return eventHandlerDirect;
    }

    public void setEventHandlerDirect(BiFunction<Object, Transition, TransitionResult> eventHandlerDirect) {
        this.eventHandlerDirect = eventHandlerDirect;
    }

    public BiFunction<Object, Object, Object> getEventTransformer() {
        return eventTransformer;
    }

    public void setEventTransformer(BiFunction<Object, Object, Object> eventTransformer) {
        this.eventTransformer = eventTransformer;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TransitionImpl that = (TransitionImpl) o;
        return Objects.equals(getSourceState(), that.getSourceState()) &&
                Objects.equals(getEvent(), that.getEvent());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getSourceState(), getEvent());
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("TransitionImpl{");
        sb.append("name='").append(name).append('\'');
        sb.append(", sourceState='").append(sourceState).append('\'');
        sb.append(", targetState='").append(targetState).append('\'');
        sb.append(", targetEvent='").append(targetEvent).append('\'');
        sb.append(", errorState='").append(errorState).append('\'');
        sb.append(", errorEvent='").append(errorEvent).append('\'');
        sb.append(", event='").append(getEvent()).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
