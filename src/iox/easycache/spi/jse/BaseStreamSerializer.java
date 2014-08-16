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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import iox.easycache.spi.ISerializer;

public abstract class BaseStreamSerializer implements ISerializer {
	
	@Override
	public byte[] serialize(Object obj) {
		try {
			ByteArrayOutputStream dest = new ByteArrayOutputStream();
			serialize(dest,obj);
			return dest.toByteArray();
		} catch (IOException e) {
		}
		return null;
	}

	@Override
	public Object deserialize(byte[] data) {
		try{
			ByteArrayInputStream src = new ByteArrayInputStream(data);
			return deserialize(src);
		} catch (IOException e) {
		}
		return null;
	}

	protected abstract void serialize(OutputStream dest,Object obj) throws IOException;
	protected abstract Object deserialize(InputStream src) throws IOException;
}
