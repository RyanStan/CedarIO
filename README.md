# Cedar IO

### Summary
This is a Java NIO-based implementation of the Reactor Pattern, which is an OOP pattern for handling asynchronous IO.
I'm still actively (ish) working on this project.  It's just for educational purposes, to see how the pattern works.

The Reactor Pattern was created by Douglas C. Shmidt from Washington University, St. Louis, MO.
[Here's a link to the paper describing the pattern](https://www.dre.vanderbilt.edu/~schmidt/PDF/reactor-siemens.pdf).

The pattern allows for the implementation of event-driven architectures by demultiplexing 
incoming service requests to callbacks that you register with the dispatcher thread.

### Packages
- cedario: this is the main implementation of the framework
- web-server-example: this is an example I'm working on that shows how to use the framework in the context of a web server.

### Todos
- Implement the `InititationDispatcher.removeHandler` method.
- Rethink the data structures I'm using in `InitiationDispatcher`. Having to handle `registerEventHandler` accepting
an `eventHandler` and `eventType` makes things a bit messy, and I think I'm going to run into problems when I go to implement
the `removeHandler` function.
- Flush out the web-server-example to be an actual Rest HTTP service.
- Allow an EventHandler to be registered on multiple EventTypes.
- Currently, the dispatcher executes and handles events as a single thread. 
Instead, the dispatcher should create a threadpool and submit jobs (`EventHandler`'s `handleEvent`) to the thread pool.
- Use log4j instead of print statements.

### My general comments on the pattern
I've always been curious about how event loops are implemented, so this was a cool project to throw together.  One thing about the
pattern that feels a bit odd to me is the tight coupling in the `EventHandle` class of the `Handle` 
(a `SelectableChannel` in this case) with the `handleEvent` application code.  Ideally, I think that the 
application callback handler should be independent of the handle that it is registered on wait on.  I need to go read up on
some OOP patterns to decide how I would reimplement that.

### Installation
This is a maven project.  You can build from the source code and install it locally.
1. `git clone https://github.com/RyanStan/CedarIO`
2. `mvn install`