### Building

This exmaple has both the frontend and backend portions to work with the WebSocket connection.
Both the server and the client must be compiled.

#### Backend

start the compiler

```
lein build
```

run Node

```
node target/out/guestbook.js
```

##### Frontend

start the compiler

```
lein build-client
```
