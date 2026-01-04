package com.opentrace.server.controllers.api.v1;

import com.opentrace.server.models.dto.SearchRequestDTO;
import com.opentrace.server.services.SearchRequestService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class SearchRequestAPI {

    private SearchRequestService searchRequestService;

    @PostMapping("/request")
    public SearchRequestDTO SearchRequest(@RequestBody SearchRequestDTO req) {
        System.out.println(req.getName());
        searchRequestService.CreateSearchRequest(req);

        return req;
    }
}
