# Java Open Service Broker

[![Build Status](https://travis-ci.org/fmui/java-osb.svg?branch=master)](https://travis-ci.org/fmui/java-osb)
[![Maven Central](https://img.shields.io/maven-central/v/de.fmui.osb.broker/java-osb-lib.svg?label=Maven%20Central)](https://search.maven.org/search?q=g:%22de.fmui.osb.broker%22%20AND%20a:%22java-osb-lib%22)
[![Javadocs](https://www.javadoc.io/badge/de.fmui.osb.broker/java-osb-lib.svg)](https://www.javadoc.io/doc/de.fmui.osb.broker/java-osb-lib)
[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://github.com/fmui/java-osb//blob/master/LICENSE)



The Java Open Service Broker is Java library for developing an [Open Service Broker][osbapi].

It supports all features of the OSBAPI v2.14 specifications, including  asynchronous Service Bindings and fetching Service Instances and Service Bindings.

_This project is an early state and has not been extensively tested. Please report [issues][github-issues]!_

## Goals

* Compliant with the Open Service Broker specification
* Lightweight
* Works with almost no external dependencies
* Can be used for stand-alone and embedded Service Brokers


## Download

Download [the latest JAR](https://search.maven.org/remote_content?g=de.fmui.osb.broker&a=java-osb-lib&v=LATEST) or grab via Maven:
```xml
<dependency>
  <groupId>de.fmui.osb.broker</groupId>
  <artifactId>java-osb-lib</artifactId>
  <version>0.0.2</version>
</dependency>
```

## Usage


### Implementing a broker handler

The business logic of your Service Broker resides in an object that implements the [`OpenServiceBrokerHandler`][javadoc-OpenServiceBrokerHandler] interface. It is recommended to extend the class `AbstractOpenServiceBrokerHandler` instead of implementing the the interface directly.
 
The interface contains a method for each operation defined in the OSBAPI specification. All methods follow the same pattern:

`OperationResponse operation(OperationRequest request) throws OpenServiceBrokerException`

* The request object contains all input parameters and the request body.
* There is builder for each response object (`OperationResponse.builder()`), setting the HTTP status code and the response body.
* The request and response bodies and all objects defined in the OSBAPI specification are glorified `Map<String, Object>` objects, which allow setting and getting vendor extension fields.
* To return an error response, throw one of the exceptions derived from [`OpenServiceBrokerException`][javadoc-OpenServiceBrokerException].

See the [example project][java-osb-examples] for code samples.

#### Authentication

Authentication can be handled inside or outside the handler. If it is handled outside, the [`authenticate()`][javadoc-authenticate] method should be empty. If it is handled inside, the credentials check should happen in this method. This method is called before each operation method called.
If the credentials are incorrect, throw an [`UnauthorizedException`][javadoc-UnauthorizedException].

```java
public void authenticate(RequestCredentials credentials) throws OpenServiceBrokerException {

  if (!credentials.isBasicAuthentication()) {
    throw new UnauthorizedException();
  }

  BasicAuthCredentials basicAuth = (BasicAuthCredentials) credentials;
  String username = basicAuth.getUsername();
  String password = basicAuth.getPassword();

  if (!check(username, password)) {
    throw new UnauthorizedException();
  }
}
```

### Creating a stand-alone broker

The easiest way to build a stand-alone broker is to extend the `OpenServiceBrokerServlet` class.

```java
@WebServlet("/my-broker/*")
public class MyBrokerServlet extends OpenServiceBrokerServlet {
  @Override
  public void init(ServletConfig config) throws ServletException {
    setOpenServiceBrokerHandler(new MyOSBHandler());
  }
}
```

### Creating an embedded broker

To embed a broker into an existing servlet, use an [`OpenServiceBroker`][javadoc-OpenServiceBroker] object. The [`processRequest()`][javadoc-processRequest] method parses the request, calls the broker handler, and sends the response.

```java
@WebServlet("/my-service/*")
public class MyServiceServlet extends HttpServlet {

  private OpenServiceBroker broker = new OpenServiceBroker();
  private OpenServiceBrokerHandler handler = new MyOSBHandler();

  @Override
  protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    if (request.getPathInfo().startsWith("/broker/")) {
      // http://<host>/<context>/my-service/broker/...
      broker.processRequest(request, response, handler);
      return;
    } 
    
    // ... other code here ...
  }
}
```

### Logging errors

The library writes error messages to `stderr`. 
This behavior can be changed by passing an object that implements the [`ErrorLogHandler`][javadoc-ErrorLogHandler] interface to the [`setErrorLogHandler()`][javadoc-setErrorLogHandler] method.

### Converting contexts

The OSBAPI specification defines a context object, which contains [platform specfic data](https://github.com/openservicebrokerapi/servicebroker/blob/master/profile.md#context-object).
The default implementation converts [Cloud Foundry context objects](https://github.com/openservicebrokerapi/servicebroker/blob/master/profile.md#cloud-foundry-context-object) and [Kubernetes context objects](https://github.com/openservicebrokerapi/servicebroker/blob/master/profile.md#kubernetes-context-object) into the respective Java objects.

This conversion can be changed and extended to other platform by providing a custom [`ContextHandler`][javadoc-ContextHandler] object to the [`setContextHandler()`][javadoc-setContextHandler] method.

## Example Service Broker

 The [example project][java-osb-examples] contains code examples for a synchronous and an asynchronous Service Broker.

[osbapi]: https://github.com/openservicebrokerapi/servicebroker/
[github-issues]: https://github.com/fmui/java-osb/issues
[java-osb-examples]: https://github.com/fmui/java-osb/tree/master/java-osb-example
[javadoc-OpenServiceBrokerHandler]: https://javadoc.io/page/de.fmui.osb.broker/java-osb-lib/latest/de/fmui/osb/broker/handler/OpenServiceBrokerHandler.html
[javadoc-authenticate]: https://javadoc.io/page/de.fmui.osb.broker/java-osb-lib/latest/de/fmui/osb/broker/handler/OpenServiceBrokerHandler.html#authenticate-de.fmui.osb.broker.RequestCredentials-
[javadoc-OpenServiceBrokerException]: https://javadoc.io/page/de.fmui.osb.broker/java-osb-lib/latest/de/fmui/osb/broker/exceptions/OpenServiceBrokerException.html
[javadoc-UnauthorizedException]: https://javadoc.io/page/de.fmui.osb.broker/java-osb-lib/latest/de/fmui/osb/broker/exceptions/UnauthorizedException.html
[javadoc-OpenServiceBroker]: https://javadoc.io/page/de.fmui.osb.broker/java-osb-lib/latest/de/fmui/osb/broker/OpenServiceBroker.html
[javadoc-processRequest]: https://javadoc.io/page/de.fmui.osb.broker/java-osb-lib/latest/de/fmui/osb/broker/OpenServiceBroker.html#processRequest-javax.servlet.http.HttpServletRequest-javax.servlet.http.HttpServletResponse-de.fmui.osb.broker.handler.OpenServiceBrokerHandler-
[javadoc-ErrorLogHandler]: https://javadoc.io/page/de.fmui.osb.broker/java-osb-lib/latest/de/fmui/osb/broker/handler/ErrorLogHandler.html
[javadoc-setErrorLogHandler]: https://javadoc.io/page/de.fmui.osb.broker/java-osb-lib/latest/de/fmui/osb/broker/OpenServiceBroker.html#setErrorLogHandler-de.fmui.osb.broker.handler.ErrorLogHandler-
[javadoc-ContextHandler]: https://javadoc.io/page/de.fmui.osb.broker/java-osb-lib/latest/de/fmui/osb/broker/handler/ContextHandler.html
[javadoc-setContextHandler]: https://javadoc.io/page/de.fmui.osb.broker/java-osb-lib/latest/de/fmui/osb/broker/OpenServiceBroker.html#setContextHandler-de.fmui.osb.broker.handler.ContextHandler-