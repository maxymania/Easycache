/*
   Copyright 2014 Simon Schmidt

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
 */
package iox.easycache;

import iox.easycache.internal.prox.CachingWrapper;
import iox.easycache.spi.ICache;
import iox.easycache.spi.ISerializer;
import iox.easycache.spi.jse.JseSerializer;
import iox.easycache.spi.jse.MapCache;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.Map;

import com.google.common.hash.HashFunction;
import com.google.common.hash.Hashing;

public class CachingApplier {
	private Map<Class<?>,Class<?>> proxies = new HashMap<Class<?>,Class<?>>();
	private boolean isinit = false;
	private ICache cache;
	private ISerializer seralizer;
	private HashFunction hashfunc;
	private synchronized void init(){
		if(isinit)return;
		cache = createCache();
		seralizer = createSerializer();
		hashfunc = createHash();
		isinit=true;
	}
	private Class<?> getProxy(Class<?> cls){
		Class<?> pcls = proxies.get(cls);
		if(pcls!=null)return pcls;
		pcls = Proxy.getProxyClass(cls.getClassLoader(), cls);
		proxies.put(cls, pcls);
		return pcls;
	}
	private Object instanciate(Class<?> pcls,InvocationHandler ih) {
		try {
			return pcls.getConstructor(InvocationHandler.class).newInstance(ih);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	protected ICache createCache(){
		return new MapCache();
	}
	protected ISerializer createSerializer(){
		return new JseSerializer();
	}
	protected HashFunction createHash(){
		return Hashing.md5();
	}
	@SuppressWarnings("unchecked")
	public<T> T apply(T object,Class<T> cls){
		init();
		Class<?> pcls = getProxy(cls);
		return (T) instanciate(pcls, new CachingWrapper(object, seralizer, cache, hashfunc));
	}
}
