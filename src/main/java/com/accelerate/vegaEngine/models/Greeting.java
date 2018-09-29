package com.accelerate.vegaEngine.models;

import com.accelerate.vegaEngine.CouchDBRestClient;
import org.json.JSONTokener;
import org.json.JSONObject;

public class Greeting {

    private final long id;
    private final String content;

    public Greeting(long id, String content) {
        this.id = id;
        this.content = content;
    }

    public long getId() {
        return id;
    }

    public String getContent() throws Exception {
    	
    	String DatabaseName = "";
    	
    	CouchDBRestClient ServerRequest = new CouchDBRestClient();
    	String ServerResponse = ServerRequest.get("/zaitoon_settings");
    	
    	JSONObject ServerJSONResponse = new JSONObject(new JSONTokener(ServerResponse));

        return ServerJSONResponse.getString("db_name");
    }
}