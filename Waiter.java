package EventWaiterPack;

import net.dv8tion.jda.api.events.GenericEvent;

import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.function.Predicate;

@SuppressWarnings("unused")
public class Waiter<T extends GenericEvent> {
    private Class<T> eventType;
    private Predicate<T> conditions;
    private Consumer<WaiterAction<T>> action;
    boolean autoRemove;
    private Long expirationTime;
    private TimeUnit timeUnit;
    private Runnable timeoutAction;
    private Double id;

    public Class<T> getEventType() { return this.eventType; }
    public Consumer<WaiterAction<T>> getAction() { return this.action; }
    public Predicate<T> getConditions() { return this.conditions; }
    public boolean getAutoRemove() { return this.autoRemove; }
    public long getExpirationTime() { return this.expirationTime; }
    public TimeUnit getTimeUnit() { return this.timeUnit; }
    public Runnable getTimeoutAction() { return this.timeoutAction; }
    public Double getId() { return id; }

    public Waiter<T> setEventType(Class<T> eventType) { this.eventType = eventType; return this; }
    public Waiter<T> setAction(Consumer<WaiterAction<T>> action) { this.action = action; return this; }
    public Waiter<T> setConditions(Predicate<T> conditions) { this.conditions = conditions; return this; }
    public Waiter<T> setAutoRemove(boolean autoRemove) { this.autoRemove = autoRemove; return this; }
    public Waiter<T> setExpirationTime(long expirationTime, TimeUnit timeUnit) { this.expirationTime = expirationTime ; this.timeUnit = timeUnit; return this; }
    public Waiter<T> setTimeoutAction(Runnable timeoutAction) { this.timeoutAction = timeoutAction; return this; }
    Waiter<T> setId(Double eventId) { this.id = eventId; return this; }

    public Waiter(Class<T> eventType, Predicate<T> conditions, Consumer<WaiterAction<T>> action, boolean autoRemove, Long expirationTime, TimeUnit timeUnit, Runnable timeoutAction) {
        this.eventType = eventType;
        this.conditions = conditions;
        this.action = action;
        this.autoRemove = autoRemove;
        this.expirationTime = expirationTime;
        this.timeUnit = timeUnit;
        this.timeoutAction = timeoutAction;
    }

    public Waiter(Class<T> eventType, Predicate<T> conditions, Consumer<WaiterAction<T>> action, boolean autoRemove, Long expirationTime, TimeUnit timeUnit) {
        this(eventType,conditions,action,autoRemove,expirationTime,timeUnit,() -> {});
    }

    public Waiter(Class<T> eventType, Predicate<T> conditions, Consumer<WaiterAction<T>> action, boolean autoRemove) {
        this(eventType,conditions,action,autoRemove,1L,TimeUnit.MINUTES);
    }

    public Waiter() {
    }

}
