package com.zerok.slackintegration.repository;

import com.zerok.slackintegration.entities.SlackClientOAuthState;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SlackClientOAuthStateRepository extends JpaRepository<SlackClientOAuthState, Long> {

    SlackClientOAuthState findSlackClientOAuthStatesByStateOAuthKey(String stateOAuthKey);
}
