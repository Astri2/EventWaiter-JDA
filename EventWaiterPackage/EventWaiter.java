package EventWaiterPack;

import net.dv8tion.jda.api.events.GenericEvent;
import net.dv8tion.jda.api.hooks.EventListener;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class EventWaiter implements EventListener {

    private static final HashMap<Class<GenericEvent>, ArrayList<Waiter<GenericEvent>>> waiterMap = new HashMap<>();
    private static final ArrayList<Waiter<GenericEvent>> toDelete = new ArrayList<>();

    @Override
    public void onEvent(@NotNull GenericEvent e) {
            if (waiterMap.containsKey(e.getClass())) {
                waiterMap.get(e.getClass()).forEach(waiter -> {
                    if(waiter.getConditions().test(e)) {
                        waiter.getAction().accept(new WaiterAction<>(e,waiter.getId()));
                        if (waiter.autoRemove)
                            toDelete.add(waiter);
                    }
                });
                waiterMap.get(e.getClass()).removeAll(toDelete);
            }
    }

    public static void register(Waiter<? extends GenericEvent> waiterToRegister) {
        @SuppressWarnings("unchecked")
        Waiter<GenericEvent> waiter = (Waiter<GenericEvent>) waiterToRegister; //casting from template to GenericEvent

        waiter.setId(Math.random());

        waiterMap.compute(
                waiter.getEventType(), (k,v) -> {
                    if(v == null)
                        return new ArrayList<>() {{add(waiter);}};
                    v.add(waiter);
                    return v;
                });

        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if(waiterMap.get(waiter.getEventType()).remove(waiter) && waiter.getTimeoutAction() != null) { //if waiter was in the list && there is a timeoutAction
                   waiter.getTimeoutAction().run();
                   unregister(waiter);
                }
            }
        }, TimeUnit.MILLISECONDS.convert(waiter.getExpirationTime(),waiter.getTimeUnit()));
    }

    public static void unregister(Waiter<GenericEvent> waiter) {
        toDelete.add(waiter);
    }

    public static void unregister(WaiterAction<? extends GenericEvent> action) {
        toDelete.addAll(waiterMap.get(action.getEvent().getClass()).stream().filter(waiter -> waiter.getId().equals(action.getId())).collect(Collectors.toList()));
    }
}
