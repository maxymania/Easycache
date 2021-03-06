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

public class CharArray implements CharSequence {
	private final char[] chr;
	public CharArray(char[] chr) {
		super();
		this.chr = chr;
	}
	@Override
	public char charAt(int i) {
		return chr[i];
	}
	@Override
	public int length() {
		return chr.length;
	}
	@Override
	public CharSequence subSequence(int start, int end) {
		return new String(chr).subSequence(start, end);
	}
}
