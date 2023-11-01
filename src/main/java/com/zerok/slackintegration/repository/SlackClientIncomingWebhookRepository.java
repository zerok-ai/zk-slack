package com.zerok.slackintegration.repository;

import com.zerok.slackintegration.entities.SlackClientIncomingWebhookEntity;
import com.zerok.slackintegration.entities.SlackClientIntegration;
import com.zerok.slackintegration.model.enums.SlackIntegrationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SlackClientIncomingWebhookRepository extends JpaRepository<SlackClientIncomingWebhookEntity, Long> {
    List<SlackClientIncomingWebhookEntity> findSlackClientIncomingWebhookEntitiesBy(String org);

    void deleteSlackClientIncomingWebhookEntitiesByOrg(String org);
}
