package com.kurt.gym.gym.audit.conroller;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.kurt.gym.gym.audit.service.AuditTrailService;
import com.kurt.gym.helper.service.ApiMessage;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("audit")
public class AuditTrailController {

    private final AuditTrailService auditTrailService;

    @GetMapping(value = {
        "",
        "/{startDate}/{endDate}"
    })
    public ResponseEntity<?> getAudit(
            @RequestParam("page") int page,
            @RequestParam("size") int size,
            @RequestParam("search") String search,
            @RequestParam(required = false) String role,
            @PathVariable(required = false, value = "startDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
            @PathVariable(required = false, value = "endDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate) {

        if (startDate == null && endDate == null) {
            return auditTrailService.data(search, size, page - 1);
        }

        System.out.println(startDate);
        System.out.println(endDate);

        if (startDate.after(endDate)) {
            return ApiMessage.successResponse("End Date should be after Start Date");
        }

        return auditTrailService.data(search, size, page - 1, startDate, endDate);
    }

}
