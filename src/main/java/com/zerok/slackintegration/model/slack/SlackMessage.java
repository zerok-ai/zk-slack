package com.zerok.slackintegration.model.slack;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.slack.api.model.block.LayoutBlock;
import lombok.*;

import java.util.List;

@Data
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SlackMessage {
    @JsonProperty("blocks")
    private List<LayoutBlock> blocks;
}



