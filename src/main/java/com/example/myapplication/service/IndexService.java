package com.example.myapplication.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class IndexService {

    public String getMessage() {
        log.info("getMessage was called"); // ログ出力例
        return "Hello World!";
    }
}
