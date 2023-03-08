package org.kodejava.zk.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class Label {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
}
