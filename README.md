# Simple Calculator

Yet another simple client-server based calculator implementation 

## Platforms to be Used

Application consists of 2 layers, client and server. There is a simple <b>Java</b> application on the server side that setups  websockets connections and executes various I/O operations.

On the client side, there is a <b>React.js</b> application to show the data taken from the server side and send equations & operations to the server.

## Dependencies

Here is the list of dependencies & libraries used in this.project:

* Java-WebSocket@1.4.1
* json@20190722
* rebass@4.0.7
* @rebass/forms@4.0.6
* @rebass/preset@4.0.5
* emotion-theming@10.0.27

Detailed explanation is in the project document.

## How to Use

In order to use application, both server and client side servers should be running. 

To start client side:

```
cd client
chmod +x ./startup.sh
./startup.sh
```

To start server side:

```
cd server
chmod +x ./startup.sh
./startup.sh
```