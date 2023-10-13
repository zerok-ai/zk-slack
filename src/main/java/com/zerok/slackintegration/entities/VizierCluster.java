package com.zerok.slackintegration.entities;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.UUID;

@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "vizier_cluster")
public class VizierCluster {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private UUID id;

    @Column(name = "org_id", nullable = false)
    private UUID orgId;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Timestamp createdAt;

    @Column(name = "project_name", nullable = false, length = 50)
    private String projectName;

    @Column(name = "updated_at", nullable = false)
    private Timestamp updatedAt;

    @Column(name = "cluster_name", length = 1000)
    private String clusterName;

    @Column(name = "cluster_uid", length = 1000)
    private String clusterUid;

}
