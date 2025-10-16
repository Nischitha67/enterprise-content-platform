package com.example.content.elasticsearch;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

import java.util.List;

@Document(indexName = "documents") // Elasticsearch index name
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SearchDocument {

    @Id
    private String id;
    private String title;
    private List<String> tags;
}
