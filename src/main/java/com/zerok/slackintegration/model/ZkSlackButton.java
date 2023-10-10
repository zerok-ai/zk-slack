package com.zerok.slackintegration.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ZkSlackButton {

    private String label;
    private String actionUrl;
    private boolean disable;
}
