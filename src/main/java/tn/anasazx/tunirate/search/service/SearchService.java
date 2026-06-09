package tn.anasazx.tunirate.search.service;

import tn.anasazx.tunirate.search.searchDTO.SearchResponseDTO;

public interface SearchService {
    SearchResponseDTO search(String query);
}
