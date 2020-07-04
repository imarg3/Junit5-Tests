package org.model;

import java.util.ArrayList;
import java.util.List;

public class CompositeFilter implements BookFilter {
    private List<BookFilter> filters;

    public CompositeFilter() {
        filters = new ArrayList<>();
    }

    @Override
    public boolean apply(final Book book) {
        return filters.stream()
                .map(bookFilter -> bookFilter.apply(book))
                .reduce(true, (b1, b2) -> b1 && b2);
    }

    public void addFilter(final BookFilter bookFilter){
        filters.add(bookFilter);
    }
}
