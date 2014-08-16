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
package iox.easycache.spi.jse;

import java.util.HashMap;
import java.util.Map;

import iox.easycache.spi.ICache;

public class MapCache implements ICache {
	private Map<String,byte[]> index = new HashMap<String,byte[]>();
	@Override
	public void put(byte[] key, byte[] value, long expireAfter) {
		index.put(Util.hex(key), value);
	}
	@Override
	public byte[] get(byte[] key) {
		return index.get(Util.hex(key));
	}
	@Override
	public void expire(byte[] key) {
		index.remove(Util.hex(key));
	}
}
