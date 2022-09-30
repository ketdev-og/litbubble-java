package com.bitbubble.api.app.data.Response;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.bitbubble.api.app.entitiy.JsonResponse;




public class Response {

    Map <String, Object> ok =  new HashMap<>();
    Map <String, Object> error =  new HashMap<>();

    public Map<String, Object> responseOk(){
        ok.put("message", new JsonResponse("ok", "api successfull"));
        return ok;
    }

    public Map<String, Object> responseError(){
        error.put("message", new JsonResponse("bad", "api unsuccessfull"));
        return error;
    }
}

