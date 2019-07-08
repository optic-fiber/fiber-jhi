package com.cheroliv.fiber.repository;

import com.cheroliv.fiber.domain.Planning;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data  repository for the Planning entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PlanningRepository extends JpaRepository<Planning, Long> {

    @Query("select planning from Planning planning where planning.user.login = ?#{principal.username}")
    List<Planning> findByUserIsCurrentUser();

}
