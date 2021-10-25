package com.github.kolegran.grape.search;

import java.util.List;
import java.util.Objects;

public class PageDto {

    private final int numberOfDocs;
    private final List<PageItemDto> pageItems;

    public PageDto(int numberOfDocs, List<PageItemDto> pageItems) {
        this.numberOfDocs = numberOfDocs;
        this.pageItems = pageItems;
    }

    public int getNumberOfDocs() {
        return numberOfDocs;
    }

    public List<PageItemDto> getPageItems() {
        return pageItems;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PageDto pageDto = (PageDto) o;
        return numberOfDocs == pageDto.numberOfDocs &&
            Objects.equals(pageItems, pageDto.pageItems);
    }

    @Override
    public int hashCode() {
        return Objects.hash(numberOfDocs, pageItems);
    }

    @Override
    public String toString() {
        return "PageDto{" +
            "numberOfDocs=" + numberOfDocs +
            ", pageItems=" + pageItems +
            '}';
    }
}
