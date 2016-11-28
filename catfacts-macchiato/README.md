## Welcome to appjs

### Prequisites

[Node.js](https://nodejs.org/en/) needs to be installed to run the application.

### running in development mode

```
  cd catfacts-macchiato
  lein build
```

then in another terminal

```
  cd catfacts-macchiato
  node target/out/catfacts.js
```

#### configuring the REPL

Once Figwheel and node are running, you can connect to the remote REPL at `localhost:7000`.
Type `(cljs)` in the REPL to connect to Figwheel ClojureSrcipt REPL.


### building the release version

```
lein release
```

Run the release version:

```
node target/release/catfacts.js
```
