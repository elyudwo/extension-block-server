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

    @Operation(summary = "findExtension")
    @GetMapping("/v1/find/extension")
    public ResponseEntity<List<String>> findExtension(@RequestHeader(name = "key") String key) {
        return ResponseEntity.ok(extensionBlockService.findExtension(key));
    }

    @Operation(summary = "insertExtension")
    @PutMapping("/v1/insert/extension")
    public ResponseEntity<Void> insertExtension(@RequestBody InsertExtensionDto dto) {
        extensionBlockService.saveExtension(dto);
        return ResponseEntity.ok().build();
    }
}
