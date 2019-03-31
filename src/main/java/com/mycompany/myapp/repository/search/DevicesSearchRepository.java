package com.mycompany.myapp.repository.search;

import com.mycompany.myapp.domain.Devices;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Devices entity.
 */
public interface DevicesSearchRepository extends ElasticsearchRepository<Devices, Long> {
}
