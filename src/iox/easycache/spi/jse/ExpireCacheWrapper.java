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

import java.util.Date;
import java.util.HashMap;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.concurrent.Executor;

import iox.easycache.spi.ICache;

public class ExpireCacheWrapper implements ICache {
	private ICache base;
	private SortedMap<Date,byte[]> mexpire = new TreeMap<Date,byte[]>();
	private HashMap<String,Date> rexpire = new HashMap<String,Date>();
	private Runnable runnable = new Runnable(){
		public void run(){
			while(waitExpires());
			stuck=true;
		}
	};
	private boolean stuck;
	private Executor exec;
	
	public ExpireCacheWrapper(ICache base, Executor exec) {
		super();
		this.base = base;
		this.exec = exec;
		stuck=true;
	}

	private void wipe(String key){
		rexpire.remove(key);
	}
	@Override
	public void put(byte[] key, byte[] value, long expireAfter) {
		String skey = Util.hex(key);
		wipe(skey);
		base.put(key, value, expireAfter);
		Date date = new Date();
		date.setTime(date.getTime()+expireAfter);
		mexpire.put(date, key);
		rexpire.put(skey, date);
		synchronized(this){
			if(stuck){
				stuck=false;
				exec.execute(runnable);
			}
		}
	}
	@Override
	public byte[] get(byte[] key) {
		return base.get(key);
	}
	@Override
	public void expire(byte[] key) {
		base.expire(key);
	}
	private boolean getExpires(){
		Date old = mexpire.firstKey();
		if(old.before(new Date())){
			byte[] key = mexpire.remove(old);
			expire(key);
			return true;
		}
		return false;
	}
	private boolean waitExpires(){
		long time = (new Date()).getTime();
		long time2 = mexpire.firstKey().getTime();
		if(time2>time) try {
			Thread.sleep(10+time2-time);
		} catch (InterruptedException e) {
		}
		return getExpires();
	}
}
