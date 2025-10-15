package com.example.content.controller;

import com.example.content.model.Document;
import com.example.content.model.DocumentVersion;
import com.example.content.service.DocumentService;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/documents")
public class DocumentController {
    private final DocumentService documentService;

    public DocumentController(DocumentService documentService) {
        this.documentService = documentService;
    }

    @PostMapping
    public ResponseEntity<Document> create(@RequestBody Document doc) {
        return ResponseEntity.ok(documentService.create(doc));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Document> update(@PathVariable Long id, @RequestBody Document doc) {
        return ResponseEntity.ok(documentService.update(id, doc));
    }

    @PostMapping("/{id}/versions")
    public ResponseEntity<DocumentVersion> addVersion(@PathVariable Long id, @RequestParam String fileUrl) {
        return ResponseEntity.ok(documentService.addVersion(id, fileUrl));
    }
    
    @GetMapping("/search")
    public List<Document> search(@RequestParam String keyword) {
        return documentService.search(keyword);
    }
}