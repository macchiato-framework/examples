## Welcome to dirac-example

This example illustrates how to setup debugging

### Prequisites

* [Node.js](https://nodejs.org/en/) needs to be installed to run the application.
* [Dirac Dev Tools](https://chrome.google.com/webstore/detail/dirac-devtools/kbkdngfljkchidcjpnfcgcokkbhlkogi)
* [Allow-Control-Allow-Origin](https://chrome.google.com/webstore/detail/allow-control-allow-origi/nlfbmbojpeacfghkpbjhddihlkkiljbi)

### running in development mode

run the following command in the terminal to install NPM modules and start Figwheel:

```
lein build
```

run `node` in another terminal:

```
node --inspect target/out/dirac-example.js
```

You'll see a URL like the following once the app starts:

```
chrome-devtools://devtools/bundled/inspector.html?experiments=true&v8only=true&ws=127.0.0.1:9229/7d80fd24-c7b3-463b-83b9-7d2869554d7a
```

Open this URL in Chrome and you should have the debugger available.

#### configuring the REPL

Once Figwheel and node are running, you can connect to the remote REPL at `localhost:7000`.
Type `(cljs)` in the REPL to connect to Figwheel ClojureSrcipt REPL.


### building the release version

```
lein package
```

Run the release version:

```
node target/release/dirac-example.js
```
