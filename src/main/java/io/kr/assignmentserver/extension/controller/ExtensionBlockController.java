package io.kr.assignmentserver.extension.controller;

import io.kr.assignmentserver.extension.controller.dto.DeleteExtensionDto;
import io.kr.assignmentserver.extension.controller.dto.InsertExtensionDto;
import io.kr.assignmentserver.extension.service.ExtensionBlockService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController("/api")
@Slf4j
public class ExtensionBlockController {

    private final ExtensionBlockService extensionBlockService;

    @Operation(summary = "find-extension")
    @GetMapping("/v1/find/extension")
    public ResponseEntity<List<String>> findExtension(@RequestHeader(name = "key") String key) {
        return ResponseEntity.ok(extensionBlockService.findExtension(key));
    }

    @Operation(summary = "find-fixed-extension")
    @GetMapping("/v1/find/fixed-extension")
    public ResponseEntity<List<String>> findFixedExtension() {
        return ResponseEntity.ok(extensionBlockService.findFixedExtension());
    }

    @Operation(summary = "insertExtension")
    @PutMapping("/v1/insert/extension")
    public ResponseEntity<Void> insertExtension(@RequestBody InsertExtensionDto dto) {
        extensionBlockService.saveExtension(dto.getKey() + ":key", dto.getKey(), dto);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/v1/delete/extension")
    public ResponseEntity<Void> deleteExtension(@RequestBody DeleteExtensionDto dto) {
        extensionBlockService.deleteExtension(dto.getKey() + ":key", dto.getKey(), dto);
        return ResponseEntity.ok().build();
    }
}
