package io.kr.assignmentserver.extension.service;

import io.kr.assignmentserver.extension.controller.dto.InsertExtensionDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ExtensionBlockServiceTest {

    @Autowired
    ExtensionBlockService extensionBlockService;

    @Autowired
    RedisTemplate<String, String> redisTemplate;

    @BeforeEach
    void init() {
        redisTemplate.delete("test");
    }

    @Test
    void 확장자_200개_넘게_추가() {
        assertThrows(RuntimeException.class, () -> {
            for(int i = 0; i < 201; i++) {
                InsertExtensionDto dto = new InsertExtensionDto("test", "HI" + i);
                extensionBlockService.saveExtension("test:key", "test", dto);
                List<String> list = extensionBlockService.findExtension("test");
                System.out.println(list.size());
            }
        });
    }
}