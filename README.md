## About this project

This is a sample project that implements a small REST service with [Spring Boot](https://github.com/spring-projects/spring-boot).

## How to run

To build and run a local server that will listen on port 8080 by default, run:

```
 $ ./gradlew bootRun
```

## Endpoints

The main endpoint is '/measurements' that will produce a JSON reply of the form:

```
http://localhost:8080/measurements

{"id":10,"data":[{"timestamp":"2018-11-24T11:17:40","value":561,"status":"Critical","node":"Node9"}]}
```

The key 'data' will contain a randomly generated list, each list element represent a measurement of a fictional 'value'.

Key 'value' will be in the range 0-1000 and when greater than 500 will trigger the 'status':'Critical'.

Key 'node' represent the fictional appliance that reports a value, a node is in the range Node0-Node9.

A request to '/measurements' without any parameter will produce new random data.

The endpoint '/measurements' accepts four optional parameters to filter the results:

Parameter 'status' should be either Normal or Critical:

```
/measurements?status=Normal

/measurements?status=Critical
```

Parameter 'node' in the range Node0 - Node9:

```
/measurements?node=Node0
```


Parameters 'since' and 'until' are Strings representing dates that will be used to filter an interval:

```
/measurements?since=2018-11-24T11:17:40

/measurements?until=2018-11-24T11:17:40

/measurements?since=2018-11-24T11:17:40&until=2018-11-25T13:10:10
```


The endpoint '/avg-by-status' returns averages for values grouped by the 'status':

```
/avg-by-status

{"id":11,"data":[{"groupBy":"status","groupId":"Critical","avgValue":753.7692307692307},{"groupBy":"status","groupId":"Normal","avgValue":218.5}]}
```


Analogously '/avg-by-node' returns averages grouped by the 'node' field:

```
/avg-by-node

{"id":13,"data":[{"groupBy":"node","groupId":"Node1","avgValue":813.0},{"groupBy":"node","groupId":"Node0","avgValue":270.0},... ]}
```
## Contacts

[Giuseppe Zerbo](https://github.com/pepzer), [giuseppe (dot) zerbo (at) gmail (dot) com](mailto:giuseppe.zerbo@gmail.com).
