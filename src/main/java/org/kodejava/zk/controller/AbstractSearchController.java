package org.kodejava.zk.controller;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Listbox;

@VariableResolver(org.zkoss.zkplus.spring.DelegatingVariableResolver.class)
public abstract class AbstractSearchController<T> extends SelectorComposer<Component> {
    @Wire
    protected Listbox listbox;

    public abstract JpaSpecificationExecutor<T> getRepository();

    public abstract Specification<T> getSpecification();

    public abstract String getCacheKey();

    protected String getDefaultSortColumn() {
        return "id";
    }

    protected Sort.Direction getDefaultSortDirection() {
        return Sort.Direction.ASC;
    }

    protected boolean getMultiple() {
        return false;
    }

    @Override
    public void doAfterCompose(Component comp) throws Exception {
        super.doAfterCompose(comp);
        search();
    }

    @Listen("onClick=#searchButton")
    public void search() {
        listbox.setVisible(true);
        SearchListModel<T> model = new SearchListModel<>(getRepository(), getSpecification(), getCacheKey());
        model.setMultiple(getMultiple());
        model.setDefaultSortColumn(getDefaultSortColumn());
        model.setDefaultSortDirection(getDefaultSortDirection());
        listbox.setModel(model);
        listbox.setActivePage(0);
    }

    @Listen("onOK=#searchForm")
    public void onEnterPressed(Event event) {
        search();
    }

    public int getPageSize() {
        return SearchListModel.PAGE_SIZE;
    }
}
