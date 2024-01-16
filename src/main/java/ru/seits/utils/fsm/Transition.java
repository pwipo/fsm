package ru.seits.utils.fsm;

/**
 * Abstraction for a FSM transition.
 * <strong>Transitions are unique according to source state and triggering event type.</strong>
 *
 * @author pwipo.seits@gmail.com. based on https://github.com/j-easy/easy-states, Mahmoud Ben Hassine (mahmoud.benhassine@icloud.com)
 */
public interface Transition {

    /**
     * Return transition name.
     *
     * @return transition name
     */
    String getName();

    /**
     * Return transition source state.
     *
     * @return transition source state
     */
    Object getSourceState();

    /**
     * Return transition target state.
     *
     * @return transition target state
     */
    Object getTargetState();

    /**
     * return event, that will be fired if current action will be target state
     * may be empty
     * realization of fsm with transactions
     *
     * @return target event
     */
    Object getTargetEvent();

    /**
     * Return transition error target state.
     *
     * @return transition error target state
     */
    Object getErrorState();

    /**
     * return event, that will be fired if current action will be error state
     * may be empty
     * realization of fsm with transactions
     *
     * @return error event
     */
    Object getErrorEvent();

    /**
     * Return fired event type upon which the transition should be made.
     *
     * @return Event type
     */
    Object getEvent();

    /**
     * compare events
     *
     * @param event input event
     * @return check is our event
     */
    boolean isOurEvent(Object event);

    /**
     * Return result of apply event handler to execute when an event is fired.
     * handler return value: true - then all right and then set target state, or set error state (if not exist - throw exception)
     *
     * @return transition event handler
     */
    TransitionResult execute(Object event);

    /**
     * Build result
     *
     * @param state     next state. may be null. If not existed, when state not changed.
     * @param event     input event
     * @param nextEvent next event. may be null. If existed, when fire this new event.
     * @return result. may be null. if null, when state not changed and new event not fired.
     */
    TransitionResult buildResult(Object state, Object event, Object nextEvent);
}
