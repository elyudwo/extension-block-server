package io.kr.assignmentserver.extension.service;

import io.kr.assignmentserver.extension.controller.dto.DeleteExtensionDto;
import io.kr.assignmentserver.extension.controller.dto.InsertExtensionDto;
import io.kr.assignmentserver.extension.service.common.DistributedLock;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class ExtensionBlockService {

    private final RedisTemplate<String, String> redisTemplate;

    private static final List<String> FIXED_ITEMS = Arrays.asList(
            "bat", "cmd", "com", "cpl", "exe", "scr", "js"
    );

    public List<String> findExtension(String key) {
        return redisTemplate.opsForList().range(key, 0, -1);
    }

    public List<String> findFixedExtension() {
        return new ArrayList<>(FIXED_ITEMS);
    }

    @DistributedLock(hashKey = "#hashKey", field = "#field")
    public void saveExtension(String hashKey, String field, InsertExtensionDto dto) {
        validateExtensionCount(dto);
        validateDuplicatedExtension(dto);
        redisTemplate.opsForList().rightPush(dto.getKey(), dto.getExtension());
    }

    public void deleteExtension(String hashKey, String field, DeleteExtensionDto dto) {
        redisTemplate.opsForList().remove(dto.getKey(), 1, dto.getExtension());
    }

    private void validateDuplicatedExtension(InsertExtensionDto dto) {
        List<String> list = findExtension(dto.getKey());
        if(list.contains(dto.getExtension())) {
            throw new RuntimeException("이미 존재하는 확장자예요.");
        }
    }

    private void validateExtensionCount(InsertExtensionDto dto) {
        List<String> list = findExtension(dto.getKey());
        if(list.size() >= 200) {
            throw new RuntimeException("커스텀 확장자는 200개 까지만 추가할 수 있어요.");
        }
    }
}
