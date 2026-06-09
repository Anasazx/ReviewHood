package tn.anasazx.tunirate.search.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import tn.anasazx.tunirate.search.searchDTO.SearchResponseDTO;
import tn.anasazx.tunirate.search.service.SearchService;

@RestController
@RequestMapping("/search")
@RequiredArgsConstructor
public class SearchController {

    private final SearchService searchService;

    @GetMapping
    public SearchResponseDTO search(@RequestParam String query) {
        return searchService.search(query);
    }

}
