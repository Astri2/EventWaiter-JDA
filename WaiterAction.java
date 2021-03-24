package EventWaiterPack;

import net.dv8tion.jda.api.events.GenericEvent;

/**
 *
 * The object that is sent when the action is performed
 *
 */

public class WaiterAction<T extends GenericEvent> {
    private final T e;
    private final Double id;

    Double getId() {
        return id;
    }

    public T getEvent() {
        return e;
    }

    WaiterAction(T e, Double id) {
        this.e = e;
        this.id = id;
    }
}
