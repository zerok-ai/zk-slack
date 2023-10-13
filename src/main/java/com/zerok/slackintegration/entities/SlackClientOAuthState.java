package com.zerok.slackintegration.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.sql.Date;

@Entity(name = "slack_client_oauth_state_key")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SlackClientOAuthState {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "org")
    private String org;

    @Column(name = "created_by")
    private String createdBy;

    @Column(name = "state_oauth_key")
    private String stateOAuthKey;

    @CreationTimestamp
    @Column(name = "created_on")
    private Date createdOn;

}
