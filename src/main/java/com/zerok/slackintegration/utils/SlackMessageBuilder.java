package com.zerok.slackintegration.utils;

import com.slack.api.model.block.*;
import com.slack.api.model.block.composition.MarkdownTextObject;
import com.slack.api.model.block.composition.PlainTextObject;
import com.slack.api.model.block.element.ButtonElement;
import com.zerok.slackintegration.model.request.ZeroKInferencePublishRequest;
import com.zerok.slackintegration.model.slack.SlackMessage;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SlackMessageBuilder {


    public static SlackMessage generateSlackIntegrationMessage(ZeroKInferencePublishRequest zeroKInferencePublishRequest, String zeroKInferenceUrl){
        SlackMessage slackMessage = new SlackMessage();

        List<LayoutBlock> blocks = new ArrayList<>();

        // block 1
        // Divider
        DividerBlock block1 = DividerBlock.builder().build();
        blocks.add(block1);

        // block 2
        //Hi message
        SectionBlock helloMessageBlock = new SectionBlock();
        MarkdownTextObject hiMessageSection = MarkdownTextObject.builder()
                .text("Hi Team  :wave:")
                .build();
        helloMessageBlock.setText(hiMessageSection);
        blocks.add(helloMessageBlock);

        // block 3
        SectionBlock zeroKIntroMessageBlock = new SectionBlock();
        String issueTitleWithLink = String.format("<%s|%s>",zeroKInferenceUrl, zeroKInferencePublishRequest.getIssueTitle());
        MarkdownTextObject zeroKIntroMessage = MarkdownTextObject.builder()
                .text(String.format("ZeroK has conducted a preliminary investigation for the reported issue : `%s` :loud_sound:",issueTitleWithLink))
                .build();
        zeroKIntroMessageBlock.setText(zeroKIntroMessage);
        blocks.add(zeroKIntroMessageBlock);

        // block 4
//        SectionBlock investigationTitleBlock = new SectionBlock();
//        MarkdownTextObject investigationTitle = MarkdownTextObject.builder()
//                .text("*Investigation report :*")
//                .build();
//        investigationTitleBlock.setText(investigationTitle);
//        blocks.add(investigationTitleBlock);

        HeaderBlock investigationTitleBlock = new HeaderBlock();
        PlainTextObject investigationTitle = PlainTextObject.builder()
                .text("Investigation report :")
                .build();
        investigationTitleBlock.setText(investigationTitle);
        blocks.add(investigationTitleBlock);

        SectionBlock section1 = new SectionBlock();
        MarkdownTextObject section1TextObject = MarkdownTextObject.builder()
                .text(String.format("*Issue Title*: %s",zeroKInferencePublishRequest.getIssueTitle()))

                .build();
        section1.setText(section1TextObject);
        blocks.add(section1);


        // block 5
        // Inference
        SectionBlock inferenceBlock = new SectionBlock();
        MarkdownTextObject zeroKInference = MarkdownTextObject.builder()
                .text(zeroKInferencePublishRequest.getInference())
                .build();
        inferenceBlock.setText(zeroKInference);
        blocks.add(inferenceBlock);

        // block 6
        ActionsBlock actionsBlock = new ActionsBlock();
        ButtonElement viewInvestigationButtonElement = ButtonElement.builder()
                .text(PlainTextObject.builder()
                        .emoji(true)
                        .text("View Investigation")
                        .build())
                .style("primary")
                .value("click_me")
                .url(zeroKInferenceUrl)
                .build();
        actionsBlock.setElements(Collections.singletonList(viewInvestigationButtonElement));
        blocks.add(actionsBlock);

        // block 7
        // Divider
        DividerBlock block7 = DividerBlock.builder().build();
        blocks.add(block7);

        slackMessage.setBlocks(blocks);
        return slackMessage;
    }
}

