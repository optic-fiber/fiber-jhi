package com.cheroliv.fiber.repository;

import com.cheroliv.fiber.domain.Planning;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data  repository for the Planning entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PlanningRepository extends JpaRepository<Planning, Long> {

    @Query("select planning from Planning planning where planning.user.login = ?#{principal.username}")
    List<Planning> findByUserIsCurrentUser();

    @Query("select p from Planning p where lower(p.user.login) = lower(:login)")
    Optional<Planning> findByUserLoginAndOpen(@Param("login") String login);
}
