package com.zerok.slackintegration.repository;

import com.zerok.slackintegration.entities.SlackInferenceRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SlackInferenceRecordRepository extends JpaRepository<SlackInferenceRecord, Long> {

    SlackInferenceRecord findSlackInferenceRecordByIssueId(String issueId);

    List<SlackInferenceRecord> findSlackInferenceRecordByOrg(String org);

}
