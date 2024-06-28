package io.kr.assignmentserver.extension.service;

import io.kr.assignmentserver.extension.controller.dto.DeleteExtensionDto;
import io.kr.assignmentserver.extension.controller.dto.InsertExtensionDto;
import io.kr.assignmentserver.extension.service.common.DistributedLock;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class ExtensionBlockService {

    private final RedisTemplate<String, String> redisTemplate;

    public List<String> findExtension(String key) {
        return redisTemplate.opsForList().range(key, 0, -1);
    }

    @DistributedLock(hashKey = "#hashKey", field = "#field")
    public void saveExtension(String hashKey, String field, InsertExtensionDto dto) {
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
}
