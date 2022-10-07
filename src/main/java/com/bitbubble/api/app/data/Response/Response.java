package com.bitbubble.api.app.data.Response;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;




public class Response {

    static Map <String, Object> ok =  new HashMap<>();
    static Map <String, Object> error =  new HashMap<>();

    public static Map<String, Object> responseOk(){
        ok.put("message", new JsonResponse("ok", "api successfull"));
        return ok;
    }

    public static Map<String, Object> responseError(){
        error.put("message", new JsonResponse("bad", "api unsuccessfull"));
        return error;
    }
}

