package com.aust.its.service;

import com.aust.its.dto.passwordtoken.TokenData;
import com.aust.its.dto.passwordtoken.TokenDataListView;
import com.aust.its.dto.passwordtoken.TokenDataView;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.map.IMap;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@Getter
public class CacheService {

    private static final Logger logger = LoggerFactory.getLogger(CacheService.class);
    private static final long tokenTTLMillis = 60 * 1000; // 1 minute
    private final IMap<String, String> userTokens;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public CacheService(@Qualifier("hazelcastServerInstance") HazelcastInstance hazelcastInstance) {
        this.userTokens = hazelcastInstance.getMap("userTokens");
    }

    public void storeToken(String userId, String randomNumber) {
        String value = null;
        try {
            value = objectMapper.writeValueAsString(new TokenData(randomNumber, System.currentTimeMillis()));
            userTokens.put(userId, value);
        } catch (JsonProcessingException e) {
            logger.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }

    public TokenData getToken(String userId) {
        String value = userTokens.get(userId);
        try {
            return objectMapper.readValue(value, TokenData.class);
        }catch (JsonProcessingException e) {
            logger.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }

    public TokenDataListView getAllTokensWithTTL() {
        long now = System.currentTimeMillis();
        List<TokenDataView> tokenDataViewList = new ArrayList<>();

        for (Map.Entry<String, String> entry : userTokens.entrySet()) {
            String userId = entry.getKey();
            try {
                TokenData data = objectMapper.readValue(entry.getValue(), TokenData.class);
                long remaining = tokenTTLMillis - (now - data.timeInMillis());

                if (remaining <= 0) {
                    remaining = 0;
                }

                tokenDataViewList.add(new TokenDataView(userId, data.randomUUID(), remaining));
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
            }
        }

        return new TokenDataListView(tokenDataViewList);
    }
}
