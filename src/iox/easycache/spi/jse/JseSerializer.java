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

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;

public class JseSerializer extends BaseStreamSerializer {

	@Override
	protected void serialize(OutputStream dest, Object obj) throws IOException {
		ObjectOutputStream ser = new ObjectOutputStream(dest);
		ser.writeObject(obj);
		ser.close();
	}

	@Override
	protected Object deserialize(InputStream src) throws IOException {
		ObjectInputStream ser = new ObjectInputStream(src);
		try {
			return ser.readObject();
		} catch (ClassNotFoundException e) {
			throw new IOException(e);
		}
	}

}
