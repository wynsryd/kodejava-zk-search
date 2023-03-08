package org.kodejava.zk.repository;

import org.kodejava.zk.entity.Label;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface LabelRepository extends JpaRepository<Label, Long>, JpaSpecificationExecutor<Label> {
}
