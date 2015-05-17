package edu.sjsu.cmpe.cache.client;

public class Client {

	public static void main(String[] args) throws Exception {
		System.out.println("Starting Cache Client...");
		CacheServiceInterface cache = new DistributedCacheService();

		cache.put(1, "a");
		cache.put(2, "b");
		cache.put(3, "c");
		cache.put(4, "d");
		cache.put(5, "e");
		cache.put(6, "f");
		cache.put(7, "g");
		cache.put(8, "h");
		cache.put(9, "i");
		cache.put(10, "j");
		for (int i = 1; i <= 10; i++) {
			System.out.println("get(" + i + ") => " + cache.get(i));
		}
		System.out.println("Stopping C Cache Client");

		/*System.out.println("Starting R Cache Client");

		CacheServiceInterface r_Cache = new RendezvousCacheService();

		r_Cache.put(1, "a");
		r_Cache.put(2, "b");
		r_Cache.put(3, "c");
		r_Cache.put(4, "d");
		r_Cache.put(5, "e");
		r_Cache.put(6, "f");
		r_Cache.put(7, "g");
		r_Cache.put(8, "h");
		r_Cache.put(9, "i");
		r_Cache.put(10, "j");
		for (int i = 1; i <= 10; i++) {
			System.out.println("get(" + i + ") => " + r_Cache.get(i));
		}
		System.out.println("Stopping R Cache Client");
*/
	}

}
