package com.opentrace.server.services;

import com.opentrace.server.models.dto.SearchRequestDto;
import com.opentrace.server.models.entities.SearchRequestEntity;
import com.opentrace.server.repositories.SearchRequestRepository;
import org.springframework.stereotype.Service;


@Service
public class SearchRequestService {

    private final SearchRequestRepository searchRequestRepository;

    public SearchRequestService(SearchRequestRepository searchRequestRepository) {
        this.searchRequestRepository = searchRequestRepository;
    }


    public String CreateSearchRequest(SearchRequestDto req) {
        System.out.println("CreateSearchRequest RUN");

        SearchRequestEntity entity = new SearchRequestEntity(req.getQuery());


        return "";
    }
}
