//package com.zerok.slackintegration.entities;
//
//import lombok.AllArgsConstructor;
//import lombok.Data;
//import lombok.NoArgsConstructor;
//
//import javax.persistence.*;
//import java.sql.Date;
//
//@Entity(name = "slack_inference_record")
//@Data
//@AllArgsConstructor
//@NoArgsConstructor
//public class SlackInferenceRecord {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private String id;
//
//    @Lob
//    @Column(name = "inference",columnDefinition = "bytea")
//    private byte[] inference;
//
//    @Column(name = "issue_id")
//    private String issueId;
//
//    @Column(name = "incident_id")
//    private String incidentId;
//
//    @Column(name = "created_on")
//    private Date createdOn;
//
//    @Column(name = "org")
//    private String org;
//
//    @Column(name = "channel_name")
//    private String channelName;
//
//}
