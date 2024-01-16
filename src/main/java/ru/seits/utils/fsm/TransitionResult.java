package ru.seits.utils.fsm;

public class TransitionResult {

    // boolean result;
    Object state;
    Object event;

    public TransitionResult(/*boolean result, */Object state, Object event) {
        // this.result = result;
        this.state = state;
        this.event = event;
    }

    /*
    public boolean isResult() {
        return result;
    }

    public void setResult(boolean result) {
        this.result = result;
    }
    */

    public Object getState() {
        return state;
    }

    public void setState(Object state) {
        this.state = state;
    }

    public Object getEvent() {
        return event;
    }

    public void setEvent(Object event) {
        this.event = event;
    }
}
