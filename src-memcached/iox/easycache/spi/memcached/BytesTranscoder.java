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

import net.spy.memcached.CachedData;
import net.spy.memcached.transcoders.Transcoder;

public class BytesTranscoder implements Transcoder<byte[]> {

	@Override
	public boolean asyncDecode(CachedData arg0) {
		return false;
	}

	@Override
	public byte[] decode(CachedData arg0) {
		return arg0.getData().clone();
	}

	@Override
	public CachedData encode(byte[] arg) {
		return new CachedData(0,arg,getMaxSize());
	}

	@Override
	public int getMaxSize() {
		return CachedData.MAX_SIZE;
	}
}
