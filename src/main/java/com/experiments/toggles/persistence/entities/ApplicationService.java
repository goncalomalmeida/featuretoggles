package com.experiments.toggles.persistence.entities;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.persistence.Version;
import java.util.List;
import java.util.UUID;

@Entity
@DynamicUpdate
@Access(AccessType.FIELD)
@Table(name = ApplicationService.TABLE_NAME, uniqueConstraints = {
        @UniqueConstraint(name = "service_name_unique_idx", columnNames = {"name"})})
@Getter
@Setter
public class ApplicationService {

    static final String TABLE_NAME = "services";

    @Id
    @Access(AccessType.PROPERTY)
    private UUID id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description")
    private String description;

    @OneToMany(mappedBy = "service", fetch = FetchType.LAZY)
    private List<ApplicationServiceToggle> serviceToggles;

    @Version
    @Getter(AccessLevel.NONE)
    private int version;
}
