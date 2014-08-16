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
package iox.easycache.internal;

import java.nio.charset.Charset;

import com.google.common.hash.HashFunction;
import com.google.common.hash.Hasher;

public class Util {
	static final Charset UTF = Charset.forName("utf8");
	static final AnyFunnel funnel = new AnyFunnel();
	public static byte[] hash(HashFunction hf,String catname,Object[] obj){
		Hasher digest = hf.newHasher();
		digest.putString(catname, UTF);
		digest.putObject(obj, funnel);
		return digest.hash().asBytes();
	}
}
