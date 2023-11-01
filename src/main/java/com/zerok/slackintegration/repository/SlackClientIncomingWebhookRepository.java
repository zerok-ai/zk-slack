package com.zerok.slackintegration.repository;

import com.zerok.slackintegration.entities.SlackClientIncomingWebhookEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SlackClientIncomingWebhookRepository extends JpaRepository<SlackClientIncomingWebhookEntity, Long> {
    List<SlackClientIncomingWebhookEntity> findSlackClientIncomingWebhookEntitiesByOrg(String org);

    void deleteSlackClientIncomingWebhookEntitiesByOrg(String org);
}
