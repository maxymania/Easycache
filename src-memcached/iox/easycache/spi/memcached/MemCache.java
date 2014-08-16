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
package iox.easycache.spi.memcached;

import net.spy.memcached.MemcachedClient;
import iox.easycache.spi.ICache;
import iox.easycache.spi.jse.Util;

public class MemCache implements ICache {
	MemcachedClient memclient;
	BytesTranscoder transcoder = new BytesTranscoder();
	
	public MemCache(MemcachedClient memclient) {
		super();
		this.memclient = memclient;
	}
	@Override
	public void put(byte[] key, byte[] value, long expireAfter) {
		if(expireAfter<=0){
			expireAfter=60000;
		}
		expireAfter = Math.min(expireAfter, Integer.MAX_VALUE);
		memclient.set(Util.hex(key), (int)expireAfter, value, transcoder);
	}
	@Override
	public byte[] get(byte[] key) {
		try{
			return memclient.get(Util.hex(key), transcoder);
		}catch(Throwable t){
			t.printStackTrace();
			return null;
		}
	}
	@Override
	public void expire(byte[] key) {
		memclient.delete(Util.hex(key));
	}
}
