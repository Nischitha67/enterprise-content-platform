package com.example.content.service;

import com.example.content.model.Document;
import com.example.content.model.Workflow;
import com.example.content.repository.DocumentRepository;
import com.example.content.repository.WorkflowRepository;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
public class WorkflowService {
    private final WorkflowRepository workflowRepository;
    private final DocumentRepository documentRepository;

    public WorkflowService(WorkflowRepository workflowRepository, DocumentRepository documentRepository) {
        this.workflowRepository = workflowRepository;
        this.documentRepository = documentRepository;
    }

    public Workflow submitForApproval(Long documentId, Long approverId) {
        Workflow wf = new Workflow();
        wf.setDocumentId(documentId);
        wf.setApproverId(approverId);
        wf.setStatus(Workflow.Status.PENDING);
        wf.setCreatedAt(Instant.now());
        wf.setUpdatedAt(Instant.now());
        return workflowRepository.save(wf);
    }

    public Workflow approve(Long workflowId, Long approverId, String comment) {
        Workflow wf = workflowRepository.findById(workflowId).orElseThrow(() -> new RuntimeException("Not found"));
        if (!wf.getApproverId().equals(approverId)) throw new RuntimeException("Not authorized");
        wf.setStatus(Workflow.Status.APPROVED);
        wf.setComment(comment);
        wf.setUpdatedAt(Instant.now());
        Workflow saved = workflowRepository.save(wf);

        // update document status as well
        Document doc = documentRepository.findById(wf.getDocumentId()).orElseThrow(() -> new RuntimeException("Doc not found"));
        doc.setStatus(Document.Status.APPROVED);
        doc.setUpdatedAt(Instant.now());
        documentRepository.save(doc);

        return saved;
    }

    public Workflow reject(Long workflowId, Long approverId, String comment) {
        Workflow wf = workflowRepository.findById(workflowId).orElseThrow(() -> new RuntimeException("Not found"));
        if (!wf.getApproverId().equals(approverId)) throw new RuntimeException("Not authorized");
        wf.setStatus(Workflow.Status.REJECTED);
        wf.setComment(comment);
        wf.setUpdatedAt(Instant.now());
        Workflow saved = workflowRepository.save(wf);

        Document doc = documentRepository.findById(wf.getDocumentId()).orElseThrow(() -> new RuntimeException("Doc not found"));
        doc.setStatus(Document.Status.REJECTED);
        doc.setUpdatedAt(Instant.now());
        documentRepository.save(doc);

        return saved;
    }

    public List<Workflow> getByDocument(Long documentId) {
        return workflowRepository.findByDocumentId(documentId);
    }
}
