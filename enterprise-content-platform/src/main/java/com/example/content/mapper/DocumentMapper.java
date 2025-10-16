package com.example.content.mapper;

import com.example.content.model.Document;

import java.util.List;
import java.util.stream.Collectors;

import com.example.content.elasticsearch.SearchDocument;

public class DocumentMapper {

    public static SearchDocument toSearchDocument(Document document) {
        return SearchDocument.builder()
                .id(String.valueOf(document.getId()))
                .title(document.getTitle())
                .tags(document.getTags())
                .build();
    }

    public static Document toEntity(SearchDocument sd) {
        return Document.builder()
                .id(Long.valueOf(sd.getId()))
                .title(sd.getTitle())
                .tags(sd.getTags())
                .build();
    }

    // Add this method
    public static List<Document> toDocumentList(List<SearchDocument> searchDocuments) {
        return searchDocuments.stream()
                .map(DocumentMapper::toEntity)   // use existing toEntity
                .collect(Collectors.toList());
    }
}