# 🚀 아키텍쳐

![전체 아키텍쳐](https://github.com/elyudwo/extension-block-server/assets/97587573/63450d38-d4ea-45ae-a894-0b7c0eef7c35)

# 📋 API 흐름도

### 확장자 추가 API
![확장자 추가 API](https://github.com/elyudwo/extension-block-server/assets/97587573/f9b6b971-1b14-4443-b4ce-9cf19aa24e0d)

### 확장자 조회 API
![확장자 조회](https://github.com/elyudwo/extension-block-server/assets/97587573/a94dc549-7dcd-4213-9adf-7902aca393f8)

### 확장자 삭제 API
![확장자 삭제](https://github.com/elyudwo/extension-block-server/assets/97587573/b01ff3ca-7a22-4a7e-86fa-a4560bcef660)


# 📜 예외 처리

## 동시성 문제
**문제 상황**
- ex) 두명의 클라이언트가 동시에 확장자를 추가할 경우 중복 추가 문제가 발생할 수 있습니다.

**해결**
- Redisson 분산락을 적용해 Lock을 획득했을 때만 확장자를 추가할 수 있도록 구현해 해결했습니다.
- Lock을 획득하는 코드를 AOP를 이용해 어노테이션화 시켜 코드의 유지보수성을 향상시켰습니다.

## 고정 확장자에 존재하는 확장자를 커스텀확장자에 추가할 경우
**문제 상황**
- ex) 고정 확장자에 존재하는 com을 사용자가 임의로 커스텀 확장자에서 추가하려고 할 수 있습니다.

**해결**
- 커스텀 확장자를 추가하기 전, 고정 확장자에 존재하는 확장자인지 확인하고 존재할 경우 알림 창을 띄워 해결했습니다.
  ![image](https://github.com/elyudwo/extension-block-server/assets/97587573/48676f4e-58d6-4113-a247-0bd3938906a6)

## 커스텀 확장자에 동일한 확장자를 추가할 경우
**문제 상황**
- ex) 커스텀 확장자에 존재하는 jpg를 사용자가 중복으로 추가하려고 할 수 있습니다.

**해결**
- 커스텀 확장자를 추가하기 전, 현재 커스텀 확장자에 존재하는 확장자인지 확인하고 존재할 경우 알림 창을 띄워 해결했습니다.
![image](https://github.com/elyudwo/extension-block-server/assets/97587573/4e2b0fdf-0c8b-401e-95bb-452d1d866cda)

## 커스텀 확장자를 200개 초과해서 추가하는 경우

**해결**
- 커스텀 확장자를 추가하기 전, (전체 확장자 - 고정 확장자) >= 200 일 경우 예외 발생

```java
private void validateExtensionCount(InsertExtensionDto dto) {
    List<String> list = findExtension(dto.getKey());
    List<String> customExtensions = list.stream()
            .filter(ext -> !ExtensionBlockService.FIXED_ITEMS.contains(ext))
            .toList();

    if (customExtensions.size() >= 200) {
        throw new RuntimeException("커스텀 확장자는 200개 까지만 추가할 수 있어요.");
    }
}
```