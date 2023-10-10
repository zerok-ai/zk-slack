//package com.zerok.slackintegration.entities;
//
//import lombok.AllArgsConstructor;
//import lombok.Data;
//import lombok.NoArgsConstructor;
//import org.hibernate.annotations.CreationTimestamp;
//
//import javax.persistence.*;
//import java.sql.Date;
//
//@Entity(name = "slack_inference_impressions")
//@Data
//@AllArgsConstructor
//@NoArgsConstructor
//public class SlackInferenceImpressions {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//
//    @Column(name = "org")
//    private String org;
//
//    @Column(name = "created_by")
//    private String createdBy;
//
//    @CreationTimestamp
//    @Column(name = "created_on")
//    private Date createdOn;
//}
