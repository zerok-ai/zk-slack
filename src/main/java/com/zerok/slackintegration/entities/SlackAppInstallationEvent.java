//package com.zerok.slackintegration.entities;
//
//import com.fasterxml.jackson.databind.JsonNode;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import jakarta.persistence.Entity;
//import jakarta.persistence.*;
//import jakarta.persistence.Table;
//import org.hibernate.annotations.TypeDef;
//import org.hibernate.annotations.TypeDefs;
//import org.hibernate.annotations.Type;
//
//import java.io.IOException;
//import java.time.LocalDateTime;
//
//@Entity
//@Table(name = "slack_app_installation_event")
//public class SlackAppInstallationEvent {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//
//    private String token;
//    private String teamId;
//    private String teamDomain;
//    private String enterpriseId;
//    private String enterpriseName;
//
//    @Type(type = "jsonb")
//    @TypeDefs({
//            @TypeDef(name = "json", typeClass = JsonStringType.class),
//            @TypeDef(name = "jsonb", typeClass = JsonBinaryType.class)
//    })
//    @Column(columnDefinition = "jsonb")
//    private JsonNode channelList;
//
//    private String userId;
//    private String userName;
//    private String responseUrl;
//    private String triggerId;
//
//    private LocalDateTime timestamp;
//
//    // Getters and setters omitted for brevity
//
//    @PrePersist
//    public void prePersist() {
//        this.timestamp = LocalDateTime.now();
//    }
//
//    @Transient
//    public void setChannelJson(String channelJson) {
//        ObjectMapper objectMapper = new ObjectMapper();
//        try {
//            this.channel = objectMapper.readTree(channelJson);
//        } catch (IOException e) {
//            // Handle JSON parsing exception
//        }
//    }
//}
//
