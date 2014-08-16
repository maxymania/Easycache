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
package iox.easycache.internal.prox;

import java.lang.reflect.Method;

import com.google.common.hash.HashFunction;

import iox.easycache.Cached;
import iox.easycache.ExpiresAfter;
import iox.easycache.internal.Util;
import iox.easycache.spi.ICache;
import iox.easycache.spi.ISerializer;

public class CachingWrapper extends BaseWrapper {
	private ISerializer serializer;
	private ICache cache;
	private HashFunction hash;
	public CachingWrapper(Object base, ISerializer serializer, ICache cache,
			HashFunction hash) {
		super(base);
		this.serializer = serializer;
		this.cache = cache;
		this.hash = hash;
	}
	@Override
	public Object invoke(Object proxy, Method method, Object[] args)
			throws Throwable {
		Cached cached = method.getAnnotation(Cached.class);
		long expire = 10000;
		if(cached==null)
			return super.invoke(proxy, method, args);
		ExpiresAfter expiresAfter = method.getAnnotation(ExpiresAfter.class);
		if(expiresAfter!=null)
			expire = expiresAfter.value();
		byte[] key = Util.hash(hash, cached.value(), args);
		byte[] value = cache.get(key);
		if(value!=null)return serializer.deserialize(value);
		Object result = super.invoke(proxy, method, args);
		value = serializer.serialize(result);
		if(value!=null)cache.put(key, value, expire);
		return result;
	}
}
