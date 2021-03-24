# EventWaiter-JDA
an EventWaiter for JDA discord bot library in java

#24/03/2021 by Astri_


to register a new Waiter, use EventWaiter.register(Waiter waiter);
if you want to unregister a waiter, use EventWaiter.unregister(Waiter waiter) or  EventWaiter.unregister(WaiterAction action) if you are inside the waiter action block

```java
public Waiter(Class<T> eventType, Predicate<T> conditions, Consumer<WaiterAction<T>> action, boolean autoRemove, Long expirationTime, TimeUnit timeUnit, Runnable timeoutAction) {
  //the class of your event type, used in privates methods and to make java understand your template type
  this.eventType = eventType; 
  
  //a predicate of the type of your event, used to test if an event match with the events wanted in the waiter
  this.conditions = conditions; 
  
  //the action that will be performed once the conditions are fullfilled
  this.action = action; 
  
  //will the waiter get removed once the action is done ? (if no, you'll have to unregister it manually or wait for it to expire)
  this.autoRemove = autoRemove; 
  
  //the duration of your waiter, it is a long (ex: "5**L**)
  this.expirationTime = expirationTime; 
  
  //the unit of your duration
  this.timeUnit = timeUnit; 
  
  //can be null, the action that is performed once the waiter expire (won't be triggered if the waiter is removed by another way)
  this.timeoutAction = timeoutAction; 
}
```

**Examples :**

A waiter that will repeat everything you say until you say "stop" or after 5 minutes :
_please note that your IDE may have difficulties to understand the type of your event. That's why I recommand to put the event type between the <> of `new Waiter<>` at first, and then remove it when you're done_
```java
EventWaiter.register(new Waiter<>(
  GuildMessageReceivedEvent.class,
  e -> (!e.getMessage().equals(event.getMessage()) && e.getAuthor().equals(event.getAuthor())),
  action -> {
    if(action.getEvent().getMessage().getContentRaw().equals("stop")) {
      EventWaiter.unregister(action);
      return;
    }
    action.getEvent().getChannel().sendMessage("you said : " 
      + action.getEvent().getMessage().getContentRaw()).queue();
  },
  false,
  5L, TimeUnit.MINUTES,
  () -> event.getChannel().sendMessage("waiter expired!").queue()
));
```
