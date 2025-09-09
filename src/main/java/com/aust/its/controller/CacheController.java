package com.aust.its.controller;

import com.aust.its.annotation.swaggerapidoc.cachecontroller.CacheListApiDoc;
import com.aust.its.dto.passwordtoken.TokenDataListView;
import com.aust.its.service.CacheService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Cache APIs", description = "Cache related APIs")
@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/hazelcast")
@RequiredArgsConstructor
public class CacheController {

    private final CacheService cacheService;

    @CacheListApiDoc
    @GetMapping("/token/ttl")
    public ResponseEntity<TokenDataListView> userTokensWithTTL() {
        return ResponseEntity.ok(cacheService.getAllTokensWithTTL());
    }
}
