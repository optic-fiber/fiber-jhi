package com.cheroliv.fiber.repository;

import com.cheroliv.fiber.domain.Inter;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the Inter entity.
 */
@SuppressWarnings("unused")
@Repository
public interface InterRepository extends JpaRepository<Inter, Long> {

}
