package com.zerok.slackintegration.model.slack;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.slack.api.model.block.LayoutBlock;
import com.slack.api.model.block.SectionBlock;

import java.util.List;

public class SlackMessage {
    private List<LayoutBlock> blocks;

    @JsonProperty("blocks")
    public List<LayoutBlock> getBlocks() {
        return blocks;
    }

    public void setBlocks(List<LayoutBlock> blocks) {
        this.blocks = blocks;
    }
}



