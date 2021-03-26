# EventWaiter-JDA
an EventWaiter for JDA discord bot library in java

#24/03/2021 by Astri_

**How to use it:**

First you'll have to register the EventWaiter class listener : 
```java
<jda>.addEventListeners(
  new EventWaiter() //,
  //your other listeners
);
```

Then you'll have to create a Waiter variable :

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

And finnally register/unregister it : 

  to register a new Waiter, use EventWaiter.register(Waiter waiter);
  to unregister a Waiter, use EventWaiter.unregister(Waiter waiter) or EventWaiter.unregister(WaiterAction action) if you are inside the waiter action block

**Tip :**

the IDE will probably have difficulties to understand the type of your eventWaiter. It may lead to auto-completion failures.
I advise you to first make a template of your constructor while putting all your lambda arguments to null, and then replace the null by your lambdas 

example of waiter template :

```java
EventWaiter.register(new Waiter<>(
  null,
  null,
  null,
  true
));
```

**Examples :**

A waiter that will repeat everything you say until you say "stop" or after 5 minutes :

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
