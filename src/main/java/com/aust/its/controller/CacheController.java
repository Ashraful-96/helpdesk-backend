package com.aust.its.controller;

import com.aust.its.dto.passwordtoken.TokenDataListView;
import com.aust.its.service.CacheService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/hazelcast")
@RequiredArgsConstructor
public class CacheController {

    private final CacheService cacheService;

    @GetMapping("/token/ttl")
    public TokenDataListView userTokensWithTTL() {
        return cacheService.getAllTokensWithTTL();
    }
}
