package com.artjom.qr_project.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@AllArgsConstructor
@Getter
public class FilterRequest {
    PagingOptions pagingOptions;
    SortOptions sortOptions;
    List<FilterOptions> filterOptions;

    public static class PagingOptions {
        public Integer page;
        public Integer pageSize;
    }

    public static class SortOptions {
        public String sortBy;
        public Integer direction;
    }

    public static class FilterOptions {
        public String filterBy;
        public String filterValue;
        public List<String> filterValues;
    }
}



