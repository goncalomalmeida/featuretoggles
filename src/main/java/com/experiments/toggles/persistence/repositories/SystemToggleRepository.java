package com.experiments.toggles.persistence.repositories;

import com.experiments.toggles.persistence.entities.System;
import com.experiments.toggles.persistence.entities.SystemToggle;
import org.springframework.core.annotation.Order;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

@Order(1)
public interface SystemToggleRepository extends GenericRepository<SystemToggle, Long> {

    @Query("SELECT st FROM SystemToggle st LEFT JOIN FETCH st.system INNER JOIN fetch st.toggle " +
            "WHERE st.allowed = TRUE " +
            "AND ((st.system IS NULL) OR st.system = ?1)")
    List<SystemToggle> findMyToggles(System system);

    SystemToggle findBySystemIdAndToggleId(UUID systemId, UUID toggleId);
}
