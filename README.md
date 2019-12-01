# REST with Jersey2 and Guice

Bridge Jersey's HK2 to Guice Dependency Injection Framework
----------------------------

This project implements a RESTful Webservice using Jersey and Guice.

[Jersey](https://jersey.java.net) is a very popular Java toolkit that abstracts away the low level details
of the client-server communication, while [Guice](https://github.com/google/guice/wiki/Motivation)
is one of the most popular dependency injection frameworks.

Jersey uses its own depencency injection framework [HK2](https://hk2.java.net).
There is a bridge between Jersey and Guice, but it is not easy to elegantly configure REST endpoints that use Guice dependency injection.
This is where this project comes in. It defines a RestServletModule, that allows you to configure your REST application
in the same way as all the other parts within it.
This RestServletModule adds a ```rest()``` and a ```packages()``` function to configure Jersey2. 
This pays off by giving us a clear and elegant application structure.
A typical configuration would be:
```java
public class MyServletContextListener extends GuiceServletContextListener {
    @Override
    protected Injector getInjector() {
        return Guice.createInjector(new RestServletModule() {
            @Override
            protected void configureServlets() {
                bind(WelcomeTexter.class);
                bind(HelloWorldServlet.class).in(Scopes.SINGLETON);
                serve("/hello").with(HelloWorldServlet.class);
                
                rest("/app/*").packages("com.example.jerseyguice.app");
                
                bind(User.class);
                bind(UserDao.class).to(InMemoryUserDao.class);
                bind(InMemoryUserDatabase.class);
            }
        });
    }
}
```

The solution is made for the google app engine but it should work on any application server.

For details see my
blog about [RESTful Webservices with Jersey 2 and Guice](http://www.aberger.at/en/blog/implementation/java/2016/11/12/jersey-guice.html).
For more information about the design principles behind it see my other [blogs](https://caberger.github.io).

The source code for the tutorials can be found in the [tutorials](./tutorials) subfolder.

