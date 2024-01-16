package ru.seits.utils.fsm;

import org.junit.Test;

import java.util.List;
import java.util.Set;

public class FiniteStateMachineImplTest {

    @Test
    public void process() throws InterruptedException {
        FiniteStateMachineImpl fsm = new FiniteStateMachineImpl(
                "start",
                Set.of("end"),
                List.of(
                        new TransitionBuilder().setName(null).setSourceState("start").setEvent((String) null).setEventHandler(this::execute).setTargetState("state1").setTargetEvent(null).setErrorState("end").setErrorEvent(null).setEventTransformer((currentEvent, nextEvent) -> nextEvent).build()
                        , new TransitionBuilder().setName(null).setSourceState("start").setEvent((String) "hi").setEventHandler(this::execute).setTargetState("state2").setTargetEvent(null).setErrorState("end").setErrorEvent(null).setEventTransformer((currentEvent, nextEvent) -> nextEvent).build()
                        , new TransitionBuilder().setName(null).setSourceState("state1").setEvent((String) null).setEventHandler(this::execute).setTargetState("state2").setTargetEvent(null).setErrorState("end").setErrorEvent(null).setEventTransformer(null).build()
                        , new TransitionBuilder().setName(null).setSourceState("state1").setEvent((String) "hi").setEventHandler(this::execute).setTargetState("end").setTargetEvent(null).setErrorState("end").setErrorEvent(null).setEventTransformer(null).build()
                        , new TransitionBuilder().setName(null).setSourceState("state2").setEvent((String) null).setEventHandler(null).setTargetState("end").setTargetEvent(null).setErrorState("end").setErrorEvent(null).setEventTransformer(null).build()
                        , new TransitionBuilder().setName(null).setSourceState("state2").setEvent((String) "hi").setEventHandler(null).setTargetState("end").setTargetEvent(null).setErrorState("end").setErrorEvent(null).setEventTransformer(null).build()
                ));

        fire(fsm, null);
        fire(fsm, null);
        fire(fsm, null);
        fire(fsm, null);

        fsm.setCurrentState("start");

        fire(fsm, "hi");
        fire(fsm, "hi");
        fire(fsm, "hi");
    }

    private void fire(FiniteStateMachine fsm, String event) {
        System.out.println(fsm.getCurrentState() + " -> " + event + " -> " + fsm.fire(event));
    }

    private boolean execute(Object event){
        System.out.println(event);
        return true;
    }

}