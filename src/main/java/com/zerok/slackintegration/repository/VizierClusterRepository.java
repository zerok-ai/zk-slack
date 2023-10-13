package com.zerok.slackintegration.repository;

import com.zerok.slackintegration.entities.VizierCluster;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface VizierClusterRepository extends JpaRepository<VizierCluster, Long> {

    Optional<VizierCluster> findVizierClusterById(UUID clusterId);
}
