package com.zerok.slackintegration.entities;

import com.zerok.slackintegration.model.enums.SlackIntegrationStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.sql.Date;

import javax.persistence.*;

@Entity(name = "slack_client_integration")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SlackClientIntegration {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "org",unique = true)
    private String org;

    @Column(name = "created_by")
    private String createdBy;

    @Column(name = "updated_by")
    private String updatedBy;

    @Column(name = "client_access_token")
    private String clientAccessToken;

    @Column(name = "slack_channel")
    private String slackChannel;

    @Column(name = "token_type")
    private String tokenType;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private SlackIntegrationStatus status;

    @CreationTimestamp
    @Column(name = "created_on")
    private Date createdOn;

    @UpdateTimestamp
    @Column(name = "updated_on")
    private Date updatedOn;
}