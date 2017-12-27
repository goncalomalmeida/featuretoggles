package com.experiments.toggles.persistence.entities;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Version;

@Entity
@DynamicUpdate
@Access(AccessType.FIELD)
@Table(name = ApplicationServiceToggle.TABLE_NAME)
@Getter
@Setter
public class ApplicationServiceToggle {

    static final String TABLE_NAME = "service_toggles";

    @Id
    @Access(AccessType.PROPERTY)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn(name = "fk_service", foreignKey = @ForeignKey(name = "fk_service_toggles_service"), nullable = false)
    private ApplicationService service;

    @ManyToOne
    @JoinColumn(name = "fk_toggle", foreignKey = @ForeignKey(name = "fk_service_toggles_toggle"), nullable = false)
    private Toggle toggle;

    @Version
    @Getter(AccessLevel.NONE)
    private int version;
}
