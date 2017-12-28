package com.experiments.toggles.persistence.repositories;

import com.experiments.toggles.persistence.entities.System;
import com.experiments.toggles.persistence.entities.SystemToggle;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface SystemToggleRepository extends CrudRepository<SystemToggle, Long> {

    @Query("SELECT st FROM SystemToggle st LEFT JOIN FETCH st.system INNER JOIN fetch st.toggle " +
            "WHERE st.allowed = TRUE " +
            "AND ((st.system IS NULL) OR st.system = ?1)")
    List<SystemToggle> findMyToggles(System system);
}
