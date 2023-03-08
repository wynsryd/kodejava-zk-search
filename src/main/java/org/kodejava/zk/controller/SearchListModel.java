package org.kodejava.zk.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.zkoss.zk.ui.Execution;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zul.FieldComparator;
import org.zkoss.zul.ListModelList;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

public class SearchListModel<T> extends ListModelList<T> {
    public static final int PAGE_SIZE = 5;
    private final JpaSpecificationExecutor<T> repository;
    private final String cacheKey;
    private long totalElements;

    private Comparator<T> comparator;
    private boolean ascending = false;
    private final Specification<T> specification;
    private Sort.Direction defaultSortDirection = Sort.Direction.ASC;
    private String defaultSortColumn = "id";

    public SearchListModel(JpaSpecificationExecutor<T> repository, Specification<T> specification, String cacheKey) {
        this.repository = repository;
        this.specification = specification;
        this.cacheKey = cacheKey;
        this.totalElements = repository.count(specification);
    }

    @Override
    public T getElementAt(int index) {
        Map<Integer, T> cache = getCache();

        T target = cache.get(index);
        if (target == null) {
            Sort sort = Sort.by(getDefaultSortDirection(), getDefaultSortColumn());
            if (comparator != null) {
                FieldComparator fieldComparator = (FieldComparator) comparator;
                String orderBy = fieldComparator.getRawOrderBy();
                sort = Sort.by(ascending ? Sort.Direction.ASC : Sort.Direction.DESC, orderBy);
            }
            Page<T> pageResult = repository.findAll(specification, PageRequest.of(getPage(index), PAGE_SIZE, sort));
            totalElements = pageResult.getTotalElements();
            int indexKey = index;
            for (T t : pageResult.toList()) {
                cache.put(indexKey, t);
                indexKey++;
            }
        } else {
            return target;
        }

        target = cache.get(index);
        if (target == null) {
            throw new RuntimeException("element at " + index + " cannot be found in the database.");
        } else {
            return target;
        }
    }

    @Override
    public int getSize() {
        return (int) totalElements;
    }

    @Override
    public void sort(Comparator<T> comparator, boolean ascending) {
        super.sort(comparator, ascending);
        this.comparator = comparator;
        this.ascending = ascending;
    }

    @SuppressWarnings("unchecked")
    private Map<Integer, T> getCache() {
        Execution execution = Executions.getCurrent();
        Map<Integer, T> cache = (Map<Integer, T>) execution.getAttribute(cacheKey);
        if (cache == null) {
            cache = new HashMap<>();
            execution.setAttribute(cacheKey, cache);
        }
        return cache;
    }

    private int getPage(int index) {
        if (index != 0) {
            return index / PAGE_SIZE;
        }
        return index;
    }

    public Sort.Direction getDefaultSortDirection() {
        return defaultSortDirection;
    }

    public void setDefaultSortDirection(Sort.Direction defaultSortDirection) {
        this.defaultSortDirection = defaultSortDirection;
    }

    public String getDefaultSortColumn() {
        return defaultSortColumn;
    }

    public void setDefaultSortColumn(String defaultSortColumn) {
        this.defaultSortColumn = defaultSortColumn;
    }
}
