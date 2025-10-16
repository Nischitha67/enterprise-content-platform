package com.example.content.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.Criteria;
import org.springframework.data.elasticsearch.core.query.CriteriaQuery;
import org.springframework.stereotype.Service;

import com.example.content.elasticsearch.SearchDocument;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SearchService {

    private final ElasticsearchTemplate elasticsearchTemplate;

    @Autowired
    public SearchService(ElasticsearchTemplate elasticsearchTemplate) {
        this.elasticsearchTemplate = elasticsearchTemplate;
    }

    public List<SearchDocument> searchByTitleOrTags(String keyword) {
        Criteria criteria = new Criteria("title").matches(keyword)
                .or(new Criteria("tags").matches(keyword));

        CriteriaQuery query = new CriteriaQuery(criteria);
        SearchHits<SearchDocument> hits = elasticsearchTemplate.search(query, SearchDocument.class);

        return hits.getSearchHits()
                .stream()
                .map(hit -> hit.getContent())
                .collect(Collectors.toList());
    }

    public void indexDocument(SearchDocument document) {
        elasticsearchTemplate.save(document);
    }

    public void deleteDocument(String documentId) {
        elasticsearchTemplate.delete(documentId, SearchDocument.class);
    }
}
