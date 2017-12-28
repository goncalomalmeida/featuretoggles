package com.experiments.toggles.persistence.entities;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
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
import javax.persistence.UniqueConstraint;
import javax.persistence.Version;

@Entity
@DynamicUpdate
@Access(AccessType.FIELD)
@Table(name = SystemToggle.TABLE_NAME, uniqueConstraints = {
        @UniqueConstraint(name = "system_toggle_enabled_allowed_unique_idx", columnNames = {"fk_toggle", "fk_system"})})
@Getter
@Setter
@EqualsAndHashCode(of = "id")
public class SystemToggle {

    static final String TABLE_NAME = "system_toggles";

    @Id
    @Access(AccessType.PROPERTY)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn(name = "fk_system", foreignKey = @ForeignKey(name = "fk_system_toggles_system"), nullable = false)
    private System system;

    @ManyToOne
    @JoinColumn(name = "fk_toggle", foreignKey = @ForeignKey(name = "fk_system_toggles_toggle"), nullable = false)
    private Toggle toggle;

    private boolean enabled;

    private boolean allowed;

    @Version
    @Getter(AccessLevel.NONE)
    private int version;
}
