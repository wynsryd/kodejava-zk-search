package org.kodejava.zk.controller;

import jakarta.persistence.criteria.Predicate;
import org.kodejava.zk.entity.Label;
import org.kodejava.zk.repository.LabelRepository;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zul.Textbox;

import java.util.ArrayList;
import java.util.List;

@VariableResolver(org.zkoss.zkplus.spring.DelegatingVariableResolver.class)
public class LabelSearchController extends AbstractSearchController<Label> {
    @WireVariable
    private LabelRepository labelRepository;

    @Wire
    private Textbox labelNameTextbox;

    @Override
    public JpaSpecificationExecutor<Label> getRepository() {
        return labelRepository;
    }

    @Override
    public Specification<Label> getSpecification() {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            String labelName = labelNameTextbox.getValue();
            if (!labelName.isBlank()) {
                predicates.add(criteriaBuilder.like(root.get("name"), "%" + labelName + "%"));
            }
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }

    @Override
    public String getCacheKey() {
        return "LABEL_CACHE_KEY";
    }
}
