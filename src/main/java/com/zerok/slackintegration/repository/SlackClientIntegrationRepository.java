package com.zerok.slackintegration.repository;

import com.zerok.slackintegration.entities.SlackClientIntegration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SlackClientIntegrationRepository extends JpaRepository<SlackClientIntegration, Long> {
    Optional<SlackClientIntegration> findSlackClientIntegrationByOrg(String org);
}
