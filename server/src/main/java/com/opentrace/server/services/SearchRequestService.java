package com.opentrace.server.services;

import com.opentrace.server.models.dto.SearchRequestDTO;
import com.opentrace.server.models.entities.SearchRequestEntity;
import com.opentrace.server.repositories.SearchRequestRepository;
import org.springframework.stereotype.Service;


@Service
public class SearchRequestService {

    private final SearchRequestRepository searchRequestRepository;

    public SearchRequestService(SearchRequestRepository searchRequestRepository) {
        this.searchRequestRepository = searchRequestRepository;
    }


    public String CreateSearchRequest(SearchRequestDTO req) {
        System.out.println("CreateSearchRequest RUN");

        SearchRequestEntity entity = new SearchRequestEntity(req.getQuery());
//        SearchRequestEntity savedEntity = searchRequestRepository.save(entity);


        return "";
    }
}
