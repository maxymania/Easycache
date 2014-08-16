Easycache
=========

A easy Caching API for Java

Why another caching api
-----------------------

Well, there are many cache solutions out there, for example spymemcached and JCache(JSR 107).

But these APIs expose a key-value API (some kind of key value store). You have to mess up your code with a lot of boilerplate code for cache checks and cache-exception handling.

Easycache will do all the cache checks for you, so you can focus on your Code.

Example
-------

Unlike other cache solutions, Easycache wraps functions (more precisely, methods). All you have to do is, write your Data retrieval code into a DAO-like class. Make sure that you use an interface for your DAO.

```java
import iox.easycache.Cached;
import iox.easycache.ExpiresAfter;
public interface ICalc {
	@Cached("calculate") // this must be an unique name for each method
	@ExpiresAfter(10*60*1000) // expires after 10 minutes
	int calculate(int a,int b);
}

public class CCalc implements ICalc {
	@Override
	public int calculate(int a, int b) {
		System.out.println("calculate ("+a+","+b+")");
		return a+b;
	}
}
```

Then, the usage of the cache is straightforward.

```java
import iox.easycache.CachingApplier;
// create the DAO object...
ICalc calc = new CCalc();

// create the cache Applier
CachingApplier amp = new CachingApplier();

// wrap your object into a caching Proxy
calc = amp.apply(calc, ICalc.class);

// and use the methods as regular
System.out.println("call calculate ("+a+","+b+")");
int result = calc.calculate(1,2);
System.out.println("result = "+result);

// and again!
System.out.println("call calculate ("+a+","+b+")");
int result = calc.calculate(1,2);
System.out.println("result = "+result);

```

