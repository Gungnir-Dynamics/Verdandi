package com.example.verdandi.exception;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test-ex")
public class ExceptionTestController {

    @GetMapping("/notfound")
    public void throwNotFound() {
        throw new ResourceNotFoundException("Not found!");
    }

    @GetMapping("/db")
    public void throwDb() {
        throw new DatabaseOperationException("DB exploded", null);
    }
}

