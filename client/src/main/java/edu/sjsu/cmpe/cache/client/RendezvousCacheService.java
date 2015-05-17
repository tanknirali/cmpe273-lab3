package edu.sjsu.cmpe.cache.client;

import com.google.common.hash.Funnel;
import com.google.common.hash.Funnels;
import com.google.common.hash.HashFunction;
import com.google.common.hash.Hashing;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

import java.nio.charset.Charset;
import java.util.ArrayList;

/**
 * Distributed cache service
 *
 */
public class RendezvousCacheService implements CacheServiceInterface {
    private String cache_server_url;
    private ArrayList<String> serverArr;
    private static final HashFunction hfunc = Hashing.murmur3_128();
    private static final Funnel<CharSequence> strfunnel = Funnels.stringFunnel(Charset.defaultCharset());
    private static final Funnel<Long> keyfunnel = Funnels.longFunnel();



    public RendezvousCacheService() {
    	serverArr = new ArrayList<String>();
    	serverArr.add("http://localhost:3000");
    	serverArr.add("http://localhost:3001");
    	serverArr.add("http://localhost:3002");
    }


    /**
     * @see CacheServiceInterface#get(long)
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
     * @see CacheServiceInterface#put(long,
     *      String)
     */
    @Override
    public void put(long key, String value) {

        HttpResponse<JsonNode> response = null;
        System.out.println("put("+key+" => "+value+")");

        try {
        	cache_server_url = getBucket(key);
            System.out.println("--------->" + cache_server_url);
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


    public String getBucket(Long key){
        RendezvousHash<Long, String> r_hash = new RendezvousHash(hfunc, keyfunnel, strfunnel, serverArr);
         return r_hash.get(key);

    }


}
