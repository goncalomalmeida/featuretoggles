package com.experiments.toggles.persistence.entities;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.UUID;

@Entity
@DynamicUpdate
@Access(AccessType.FIELD)
@Table(name = Toggle.TABLE_NAME)
@Getter
@Setter
public class Toggle {

    static final String TABLE_NAME = "toggles";

    @Id
    @Access(AccessType.PROPERTY)
    private UUID id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description")
    private String description;
}
