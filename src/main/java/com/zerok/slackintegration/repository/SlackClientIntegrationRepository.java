package com.zerok.slackintegration.repository;

import com.zerok.slackintegration.entities.SlackClientIntegration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SlackClientIntegrationRepository extends JpaRepository<SlackClientIntegration, Long> {
    SlackClientIntegration findByOrg(String org);
}
