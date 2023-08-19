package com.kurt.gym.gym.audit.conroller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.kurt.gym.gym.audit.service.AuditTrailService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("audit")
public class AuditTrailController {

    private final AuditTrailService auditTrailService;

    @GetMapping
    public ResponseEntity<?> getAudit(
            @RequestParam("page") int page,
            @RequestParam("size") int size,
            @RequestParam("search") String search,
            @RequestParam(required = false) String role) {

      

        return auditTrailService.data(search, size, page - 1);
    }

}
