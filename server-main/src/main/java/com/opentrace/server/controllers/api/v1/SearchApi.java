package com.opentrace.server.controllers.api.v1;

import com.opentrace.server.models.api.request.SearchRequest;
import com.opentrace.shared.models.api.response.ApiResponseModel;
import com.opentrace.shared.models.api.response.SearchStatusResponse;
import com.opentrace.server.services.SearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/search")
public class SearchApi {

    private final SearchService searchService;

    @PostMapping
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ApiResponseModel<SearchStatusResponse> createSearch(@RequestBody SearchRequest request) {

        SearchStatusResponse response = searchService.createSearch(request);

        return ApiResponseModel.ok(response);
    }

    @GetMapping("/{id}/status")
    public ApiResponseModel<SearchStatusResponse> getStatus(@PathVariable UUID id) {

        SearchStatusResponse response = searchService.getSearchStatus(id);

        return ApiResponseModel.ok(response);
    }
}
