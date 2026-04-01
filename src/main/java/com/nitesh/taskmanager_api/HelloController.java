package com.nitesh.taskmanager_api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {
    @GetMapping("/hello")
    public ResponseEntity<String> sayHello() {
        String str = "You need to be authenticated to see this";
        return ResponseEntity.ok(str);
    }

}
