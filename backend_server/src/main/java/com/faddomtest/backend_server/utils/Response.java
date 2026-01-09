package com.faddomtest.backend_server.utils;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

public class Response {
    public final String message;
    public final boolean success;
    public final List<String> data;

    public Response(String message, boolean success, List<String> data){
        this.message = message;
        this.success = success;
        this.data = data;
    }

    public static ResponseEntity<String> getOk(List<?> data) {
        var serializedObjs = data.stream()
            .map(
            obj -> obj instanceof String str ? str : JsonUtils.serialize(obj)
            ).toList();
        String responseBody = JsonUtils.serialize(new Response(null,true,serializedObjs));
        return ResponseEntity.ok(responseBody);
    }

    public static ResponseEntity<String> getOk(){
        String responseBody = JsonUtils.serialize(new Response(null,true,null));
        return ResponseEntity.ok(responseBody);
    }

    public static ResponseEntity<String> getError(String message, HttpStatus status){
        String responseBody = JsonUtils.serialize(new Response(message,false,null));
        return new ResponseEntity<>(responseBody,status);
    }
}
