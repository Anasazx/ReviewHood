package tn.anasazx.tunirate.search.service;

import tn.anasazx.tunirate.search.dto.SearchResponseDTO;

public interface SearchService {
    SearchResponseDTO search(String query);
}
