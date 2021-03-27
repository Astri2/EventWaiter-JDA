# 👋 Hello!

Here is a small projet that allows you create an event waiter with the discord jda library!
If you find a bug or have any suggestion, please contact me!

# ❓ What is it for?

This projet will allow you to easily create waiters.
For instance, making a reaction role or a bingo game will now be child's play!

# 📂 How to install ?

You have to download the 3 following .java files and add them in a __**single**__ package:

   * [EventWaiter.java](https://github.com/Astri2/EventWaiter-JDA/blob/main/EventWaiterPackage/EventWaiter.java)
   * [Waiter.java](https://github.com/Astri2/EventWaiter-JDA/blob/main/EventWaiterPackage/Waiter.java)
   * [WaiterAction.java](https://github.com/Astri2/EventWaiter-JDA/blob/main/EventWaiterPackage/WaiterAction.java)

# 📚 How to setup ?

The only setup step is to register the [EventWaiter](https://github.com/Astri2/EventWaiter-JDA/blob/main/EventWaiterPackage/EventWaiter.java) in your JDABuilder object:
```java
public class Bot {
    public static void main(String[] args) {
        JDA jda = JDABuilder.createDefault("token")
            .addEventListerners(new EventWaiter())
            .build();
    }
}
```

# 🖥 How to create a waiter ?

Now you have to create a [Waiter](https://github.com/Astri2/EventWaiter-JDA/blob/main/EventWaiterPackage/Waiter.java) object:

_note that T is a template extending from [GenericEvent](https://github.com/DV8FromTheWorld/JDA/blob/master/src/main/java/net/dv8tion/jda/api/events/GenericEvent.java)_

   * First comes the constructors : 

| Constructor | Description |
|-|:-:|
| Waiter(Class<T> eventType, Predicate<T> conditions, Consumer<WaiterAction<T>> action, boolean autoRemove, Long expirationTime, TimeUnit timeUnit) | The full constructor. |
| Waiter(Class eventType, Predicate conditions, Consumer> action, boolean autoRemove) | The constructor without expiration handling. by default the expiration time will be set to 1 minute. |
| Waiter() | The empty constructor.  You'll have to set the attributes by yourself.  |

   * Then, here is the list of all the Waiter attributes that you can acces to :

| Attribute | Type | Description |
|-|:-:|:-:|
| eventType | Class<T> | the class of the event you'll be waiting for |
| conditions | Predicate<T> | all the conditions that an event must meet to execute the waiter's action |
| action | Consumer<WaiterAction<T>> | the action that will be executed if the conditions are fulfilled |
| autoRemove | boolean | will the waiter be unregistered once the action is executed ? if false, you may want to [unregister the waiter](#-how-to-register-and-unregister-the-waiter) by yourself |
| expirationTime | long | the time after which the waiter will be unregistered automatically |
| timeUnit | TimeUnit | the unit of the previous time |
| timeoutAction | Runnable | the action that will be executed once the waiter expires (null if no action) |
    
You can access to all of them using getter / setters.
To access to the event inside of the waiter action, use action.getEvent()


While creating the waiter, the IDE may be lost and erroring/failing at auto-completion.
That's why I recommand you to first make a "template" of your constructor by setting all your lambda arguments to null, and then replace the null values by your lambdas:
```java
Waiter<GuildMessageReceivedEvent> waiter = new Waiter<>(
    GuildMessageReceivedEvent.class,
    null,
    null,
    true
);
```

# ⚒ How to register and unregister the waiter?

To register your just created waiter:
```java
EventWaiter.register(waiter);
```
To unregister the waiter while you still have acces to it:
```java
EventWaiter.unregister(waiter);
```
To unregister the waiter from the performed action:
```java
EventWaiter.unregister(action);
```

# 🎲 Example time!

In the examples, I will use these two variables:
| Variable | Description |
|-|:-:|
| event | the waiter creation event |
| e | the event that will trigger the waiter |

* A waiter that will repeat your next message (within 1 minute)
```java
EventWaiter.register(new Waiter<>(
    GuildMessageReceivedEvent.class,
    e -> (!e.getMessage().equals(event.getMessage()) && e.getAuthor().equals(event.getAuthor())),
    action -> action.getEvent().getChannel().sendMessage(action.getEvent().getMessage().getContentRaw()).queue(),
    true
));
```

* A bingo game 
```java
int range = 20;
String number = Integer.toString ((int) (Math.random() * range));
System.out.println(number);

Waiter<GuildMessageReceivedEvent> waiter = new Waiter<>();
waiter
    .setEventType(GuildMessageReceivedEvent.class)
    .setConditions(e -> !e.getMessage().equals(event.getMessage()) && e.getChannel().equals(event.getChannel()))
    .setAutoRemove(false)
    .setExpirationTime(2L, TimeUnit.MINUTES)
    .setTimeoutAction(() -> event.getChannel().sendMessage("no one found :sob: The number was " + number).queue())
    .setAction(action -> {
        if(action.getEvent().getMessage().getContentRaw().equals(number)) {
            action.getEvent().getMessage().addReaction("✅").queue();
            action.getEvent().getChannel().sendMessage("GG " + action.getEvent().getAuthor().getAsMention() + "! You found the number! It was " + number).queue();
            EventWaiter.unregister(action);
        }
        else {
            action.getEvent().getMessage().addReaction("❌").queue();
            action.getEvent().getMessage().delete().queueAfter(5,TimeUnit.SECONDS);
        }
    });
EventWaiter.register(waiter);
```

* A reaction role
```java
TODO create a reaction role
```
