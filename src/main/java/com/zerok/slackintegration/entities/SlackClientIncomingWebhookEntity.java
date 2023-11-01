package com.zerok.slackintegration.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.sql.Timestamp;

@Entity(name = "slack_client_incoming_webhook_entity")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SlackClientIncomingWebhookEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "org")
    private String org;

    @Column(name = "slack_webhook_url")
    private String slackWebhookUrl;

    @Column(name = "slack_webhook_config_url")
    private String slackWebhookConfigurationUrl;

    @Column(name = "slack_channel")
    private String slackChannel;

    @Column(name = "created_by")
    private String createdBy;

    @Column(name = "slack_channel_id")
    private String slackChannelId;

    @Column(name = "slack_team_id")
    private String slackTeamId;

    @CreationTimestamp
    @Column(name = "created_on")
    private Timestamp createdOn;

    @UpdateTimestamp
    @Column(name = "updated_on")
    private Timestamp updatedOn;
}

