package com.kyulix.RGTestApp.repositories;

import com.kyulix.RGTestApp.entities.Office;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OfficeRepository extends JpaRepository<Office, Integer> {

    boolean existsByName(String name);

    boolean existsByAddress(String address);
}
