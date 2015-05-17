package edu.sjsu.cmpe.cache.client;

import java.util.ArrayList;

import com.google.common.hash.Hashing;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

/**
 * Distributed cache service
 * 
 */
public class DistributedCacheService implements CacheServiceInterface {
    private String cache_server_url;
    private ArrayList<String> serverArr;

    public DistributedCacheService() {
    	serverArr = new ArrayList<String>();
    	serverArr.add("http://localhost:3000");
    	serverArr.add("http://localhost:3001");
    	serverArr.add("http://localhost:3002");
    }


    /**
     * @see edu.sjsu.cmpe.cache.client.CacheServiceInterface#get(long)
     */
    @Override
    public String get(long key) {
        HttpResponse<JsonNode> response = null;
        try {

        	cache_server_url = getBucket(key);
            response = Unirest.get(this.cache_server_url + "/cache/{key}")
                    .header("accept", "application/json")
                    .routeParam("key", Long.toString(key)).asJson();
        } catch (UnirestException e) {
            System.err.println(e);
        }
        String str_val = response.getBody().getObject().getString("value");

        return str_val;
    }

    /**
     * @see edu.sjsu.cmpe.cache.client.CacheServiceInterface#put(long,
     *      java.lang.String)
     */
    @Override
    public void put(long key, String value) {

        HttpResponse<JsonNode> response = null;
        System.out.println("put("+key+" => "+value+")");

        try {
        	cache_server_url = getBucket(key);
            System.out.println("---------->" + cache_server_url);
            response = Unirest
                    .put(this.cache_server_url + "/cache/{key}/{value}")
                    .header("accept", "application/json")
                    .routeParam("key", Long.toString(key))
                    .routeParam("value", value).asJson();
        } catch (UnirestException e) {
            System.err.println(e);
        }

        if (response.getCode() != 200) {
            System.out.println("cache add fail");
        }

    }


    public String getBucket(long key){

        int bukt = Hashing.consistentHash(Hashing.md5().hashLong(key), serverArr.size());
        String c_url = "";

        if(bukt == 0){
        	c_url = "http://localhost:3000";
        }else if(bukt == 1){
        	c_url = "http://localhost:3001";
        }else{
        	c_url = "http://localhost:3002";
        }

        return c_url;

    }
}
