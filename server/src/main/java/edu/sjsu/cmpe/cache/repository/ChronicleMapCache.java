package edu.sjsu.cmpe.cache.repository;
import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import edu.sjsu.cmpe.cache.domain.Entry;
import net.openhft.chronicle.map.*;



public class ChronicleMapCache implements CacheInterface  {
	
	private final ChronicleMap<Long, Entry> persistedMap;
	
	public ChronicleMapCache(ChronicleMap<Long, Entry> entries) {
		persistedMap=entries;
		
    }
	
	@Override
	public Entry save(Entry newEntry) {
		// TODO Auto-generated method stub
		checkNotNull(newEntry, "newEntry instance must not be null");
		persistedMap.putIfAbsent(newEntry.getKey(), newEntry);
		return null;
	}
	@Override
	public Entry get(Long key) {
		// TODO Auto-generated method stub
		checkArgument(key > 0,
                "Key was %s but expected greater than zero value", key);
		return persistedMap.get(key);
	}
	@Override
	public List<Entry> getAll() {
		// TODO Auto-generated method stub
		return new ArrayList<Entry>(persistedMap.values());
	}
}
