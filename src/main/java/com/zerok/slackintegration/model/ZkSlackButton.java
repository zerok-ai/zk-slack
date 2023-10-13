package com.zerok.slackintegration.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpMethod;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ZkSlackButton {

    private HttpMethod httpMethod;
    private String label;
    private String actionUrl;
    private boolean disable;
}
