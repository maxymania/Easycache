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
package iox.easycache.spi;

public interface ICache {
	/**
	 * put(key,value,expireAfter)
	 * @param key not null
	 * @param value not null
	 * @param expireAfter in Milliseconds; if -1 then no expire
	 */
	void put(byte[] key,byte[] value,long expireAfter);
	/**
	 * get(key) -> data
	 * @param key not null
	 * @return the data or null
	 */
	byte[] get(byte[] key);
	/**
	 * makes a key to expire manually
	 * @param key not null
	 */
	void expire(byte[] key);
}