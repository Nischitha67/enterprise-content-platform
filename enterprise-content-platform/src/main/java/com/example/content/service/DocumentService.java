package com.example.content.service;

import com.example.content.model.Document;
import com.example.content.model.DocumentVersion;
import com.example.content.repository.DocumentRepository;
import com.example.content.repository.DocumentVersionRepository;
import com.example.content.mapper.DocumentMapper;
import com.example.content.elasticsearch.SearchDocument;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class DocumentService {

    private final DocumentVersionRepository versionRepository;

    @Autowired
    private DocumentRepository documentRepository;

    @Autowired
    private SearchService searchService;

    public DocumentService(DocumentRepository documentRepository, DocumentVersionRepository versionRepository) {
        this.documentRepository = documentRepository;
        this.versionRepository = versionRepository;
    }

    public Document create(Document doc) {
        return documentRepository.saveAndFlush(doc);
    }

    public Document update(Long id, Document update) {
        Document existing = documentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Not found"));
        existing.setTitle(update.getTitle());
        existing.setContentUrl(update.getContentUrl());
        existing.setTags(update.getTags());
        existing.setUpdatedAt(java.time.Instant.now());
        return documentRepository.save(existing);
    }

    @Transactional
    public DocumentVersion addVersion(Long documentId, String fileUrl) {
        List<DocumentVersion> versions = versionRepository.findByDocumentIdOrderByVersionNumberDesc(documentId);
        int nextVersion = versions.isEmpty() ? 1 : versions.get(0).getVersionNumber() + 1;
        DocumentVersion v = new DocumentVersion();
        v.setDocumentId(documentId);
        v.setVersionNumber(nextVersion);
        v.setFileUrl(fileUrl);
        return versionRepository.save(v);
    }

    public Document uploadDocument(Document document) {
        Document saved = documentRepository.save(document);

        // Convert JPA Document -> SearchDocument before indexing
        SearchDocument searchDoc = DocumentMapper.toSearchDocument(saved);
        searchService.indexDocument(searchDoc);

        return saved;
    }

    public List<Document> getAllDocuments() {
        return documentRepository.findAll();
    }

    public Optional<Document> getDocumentById(Long id) {
        return documentRepository.findById(id);
    }

    public void deleteDocument(Long id) {
        documentRepository.deleteById(id);
        searchService.deleteDocument(String.valueOf(id));
    }

    public List<Document> search(String keyword) {
        // Convert Elasticsearch result -> JPA Document list
        return DocumentMapper.toDocumentList(searchService.searchByTitleOrTags(keyword));
    }
}
