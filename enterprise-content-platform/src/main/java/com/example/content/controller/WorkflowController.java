package com.example.content.controller;

import com.example.content.model.Workflow;
import com.example.content.service.WorkflowService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/workflows")
public class WorkflowController {
    private final WorkflowService workflowService;

    public WorkflowController(WorkflowService workflowService) {
        this.workflowService = workflowService;
    }

    @PostMapping("/submit")
    public ResponseEntity<Workflow> submit(@RequestParam Long documentId, @RequestParam Long approverId) {
        return ResponseEntity.ok(workflowService.submitForApproval(documentId, approverId));
    }

    @PostMapping("/{id}/approve")
    public ResponseEntity<Workflow> approve(@PathVariable Long id, @RequestParam Long approverId, @RequestParam(required = false) String comment) {
        return ResponseEntity.ok(workflowService.approve(id, approverId, comment));
    }

    @PostMapping("/{id}/reject")
    public ResponseEntity<Workflow> reject(@PathVariable Long id, @RequestParam Long approverId, @RequestParam(required = false) String comment) {
        return ResponseEntity.ok(workflowService.reject(id, approverId, comment));
    }

    @GetMapping("/document/{documentId}")
    public ResponseEntity<List<Workflow>> getByDocument(@PathVariable Long documentId) {
        return ResponseEntity.ok(workflowService.getByDocument(documentId));
    }
}
