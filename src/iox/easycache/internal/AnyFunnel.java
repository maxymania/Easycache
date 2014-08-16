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

import iox.easycache.Acyclic;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import com.google.common.hash.Funnel;
import com.google.common.hash.PrimitiveSink;

public class AnyFunnel implements Funnel<Object> {
	private static final long serialVersionUID = -6606932468537196126L;
	static final byte NUL=0;
	static final byte ONE=-1;
	static final byte MARK=74; // #74!!!!
	@Override
	public void funnel(Object data, PrimitiveSink sink) {
		sink.putByte(MARK);
		if(data==null)
			sink.putByte(NUL);
		else if(data instanceof byte[])
			sink.putBytes((byte[])data);
		else if(data instanceof char[])
			sink.putString(new CharArray((char[])data), Util.UTF);
		else if(data instanceof String)
			sink.putString((String)data, Util.UTF);
		else if(data instanceof boolean[])
		{for(boolean e:(boolean[])data)sink.putBoolean(e);}
		else if(data instanceof short[])
		{for(short e:(short[])data)sink.putShort(e);}
		else if(data instanceof int[])
		{for(int e:(int[])data)sink.putInt(e);}
		else if(data instanceof long[])
		{for(long e:(long[])data)sink.putLong(e);}
		else if(data instanceof float[])
		{for(float e:(float[])data)sink.putFloat(e);}
		else if(data instanceof double[])
		{for(double e:(double[])data)sink.putDouble(e);}
		else if(data instanceof Object[])
		{for(Object e:(Object[])data)funnel(e,sink);}
		else if(data instanceof Boolean)
			sink.putBoolean((Boolean)data);
		else if(data instanceof Byte)
			sink.putByte((Byte)data);
		else if(data instanceof Character)
			sink.putChar((Character)data);
		else if(data instanceof Short)
			sink.putShort((Short)data);
		else if(data instanceof Integer)
			sink.putInt((Integer)data);
		else if(data instanceof Long)
			sink.putLong((Long)data);
		else if(data instanceof Float)
			sink.putFloat((Float)data);
		else if(data instanceof Double)
			sink.putDouble((Double)data);
		else{
			putObject(data,sink);
		}
	}
	private void putObject(Object data, PrimitiveSink sink) {
		if(!(data instanceof Acyclic)){
			sink.putByte(ONE);
			return;
		}
		for(Field fld:data.getClass().getFields()){
			if(Modifier.isStatic(fld.getModifiers()))continue;
			sink.putString(fld.getName(),Util.UTF);
			try {
				Object obj = fld.get(data);
				funnel(obj,sink);
			} catch (IllegalArgumentException e) {
			} catch (IllegalAccessException e) {
			}
		}
	}
}
