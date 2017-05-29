# cljsbin - HTTP Request & Response Service

[original implementation facundoolano](https://github.com/facundoolano/cljsbin)

cljsbin is a ClojureScript clone of [httpbin](https://httpbin.org/) that
runs on Node.js. It provides an API to test common HTTP features and operations
(request methods, headers, redirects, etc.).

cljsbin is implemented using the [Macchiato web framework](https://github.com/macchiato-framework/)
for ClojureScript.

## Endpoints

* `/` home page.
* `/ip` Returns Origin IP.
* `/user-agent` Returns user-agent.
* `/headers` Returns header dict.
* `/get` Returns GET data.
* `/post` Returns POST data.
* `/put` Returns PUT data.
* `/patch` Returns PATCH data.
* `/delete` Returns DELETE data.
* `/forms/post` HTML form that submits to /post
* `/status/:status` Returns given HTTP Status code.
* `/response-headers` Returns given response headers.
* `/cookies Return` cookie data.
* `/cookies/set?name=value` Sets one or more simple cookies.
* `/cookies/delete?name` Deletes one or more simple cookies.
* `/cache` Returns 200 unless an If-Modified-Since or If-None-Match header is provided, when it returns a 304.
* `/cache/:n` Sets a Cache-Control header for n seconds.
* `/delay/:n` Delays responding for min(n, 10) seconds.
* `/redirect/:n` 302 relative redirects n times.
* `/absolute-redirect/:n` 302 absolyte redirects n times.
* `/redirect-to?url=foo` 302 Redirects to the given URL.
* `/basic-auth/:user/:pass` Challenges HTTPBasic Auth.
* `/hidden-basic-auth/:user/:pass` 404'd BasicAuth.
* `/digest-auth/:user/:pass` Challenges HTTP Digest Auth.
* `/links/:n` Returns page containing n HTML links.
* `/encoding/utf8` Returns page containing UTF-8 data.
* `/xml` Returns some XML.
* `/html` Renders an HTML Page.
* `/robots.txt` Returns some robots.txt rules.
* `/deny` Denied by robots.txt file.
* `/image` Returns page containing an image based on sent Accept header.
* `/image/png` Returns page containing a PNG image.
* `/image/webp` Returns page containing a WEBP image.
* `/image/svg` Returns page containing a SVG image.
* `/image/jpeg` Returns page containing a JPEG image.
* `/compress` Returns gzip or deflate enconded data, based on the Accept-encoding header.

## Examples

### GET /ip

``` json
{"origin": "24.127.96.129"}
```

### GET /user-agent

``` json
{"user-agent": "curl/7.19.7 (universal-apple-darwin10.0) libcurl/7.19.7 OpenSSL/0.9.8l zlib/1.2.3"}
```

### GET /get

``` json
{
   "args": {},
   "headers": {
      "Accept": "*/*",
      "Connection": "close",
      "Content-Length": "",
      "Content-Type": "",
      "Host": "cljsbin.org",
      "User-Agent": "curl/7.19.7 (universal-apple-darwin10.0) libcurl/7.19.7 OpenSSL/0.9.8l zlib/1.2.3"
   },
   "origin": "24.127.96.129",
   "url": "http://cljsbin.org/get"
}
```

### GET /status/418

``` http
HTTP/1.1 418 I'M A TEAPOT
Server: nginx/0.7.67
Date: Mon, 13 Jun 2011 04:25:38 GMT
Connection: close
x-more-info: http://tools.ietf.org/html/rfc2324
Content-Length: 135
```

## Building and running

### Prequisites

[Node.js](https://nodejs.org/) and [leiningen](http://leiningen.org/)
need to be installed to run the application.

### Running in development mode

run the following command in the terminal to install NPM modules and start Figwheel:

```
lein build
```

run `node` in another terminal:

```
npm start
```

### Building the release version

```
lein package
```

Run the release version:

```
npm start
```

### Build from the Dockerfile

From a folder containing the [Dockerfile](Dockerfile) build the container:

``` shell
docker build -t cljsbin .
```

And the run it:

``` shell
docker run -p 3000:3000 cljsbin
```

### Deploy using [now](https://zeit.co/now/)

From a folder containing the [Dockerfile](Dockerfile) build the container:

``` shell
now
```

Currently live [here](https://cljsbin-bkhgroqzwe.now.sh/).
