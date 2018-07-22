package com.kyulix.RGTestApp.repositories;

import com.kyulix.RGTestApp.entities.Office;
import org.springframework.data.repository.CrudRepository;

public interface OfficeRepository extends CrudRepository<Office, Integer> {

    boolean existsByName(String name);

    boolean existsByAddress(String address);
}
