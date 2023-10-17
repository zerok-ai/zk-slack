package com.zerok.slackintegration.repository;

import com.zerok.slackintegration.entities.SlackClientOAuthState;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SlackClientOAuthStateRepository extends JpaRepository<SlackClientOAuthState, Long> {

    Optional<SlackClientOAuthState> findSlackClientOAuthStatesByStateOAuthKey(String stateOAuthKey);
}
