package com.zerok.slackintegration.entities;

import com.zerok.slackintegration.model.enums.SlackIntegrationStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.sql.Timestamp;



@Entity(name = "slack_client_integration")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SlackClientIntegration {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "org",unique = true)
    private String org;

    @Column(name = "created_by")
    private String createdBy;

    @Column(name = "updated_by")
    private String updatedBy;

    @Column(name = "client_access_token")
    private String clientAccessToken;

    @Column(name = "slack_workspace")
    private String slackWorkspace;

    @Column(name = "slack_channel")
    private String slackChannel;

    @Column(name = "slack_user_id")
    private String slackUserId;

    @Column(name = "slack_team_id")
    private String slackTeamId;

    @Column(name = "token_type")
    private String tokenType;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private SlackIntegrationStatus status;

    @CreationTimestamp
    @Column(name = "created_on")
    private Timestamp createdOn;

    @UpdateTimestamp
    @Column(name = "updated_on")
    private Timestamp updatedOn;
}