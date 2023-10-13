package com.zerok.slackintegration.utils;

import com.slack.api.model.block.DividerBlock;
import com.slack.api.model.block.LayoutBlock;
import com.slack.api.model.block.SectionBlock;
import com.slack.api.model.block.composition.MarkdownTextObject;
import com.slack.api.model.block.composition.TextObject;
import com.zerok.slackintegration.model.ZeroKIssueInference;
import com.zerok.slackintegration.model.slack.SlackMessage;

import java.util.ArrayList;
import java.util.List;

public class SlackMessageBuilder {

    public static SlackMessage createSlackInferenceMessage(ZeroKIssueInference zeroKIssueInference) {
        SlackMessage slackMessage = new SlackMessage();

        List<LayoutBlock> blocks = new ArrayList<>();

        // Section 1
        SectionBlock section1 = new SectionBlock();
        MarkdownTextObject section1TextObject = MarkdownTextObject.builder()
                .text("Zerok has found new issue :\n*<fakeLink.toEmployeeProfile.com|ZeroK Dashboard Issue Link>*")
                .build();
        section1.setText(section1TextObject);
        blocks.add(section1);
        // Divider
        DividerBlock divider = DividerBlock.builder().build();
        blocks.add(divider);

        // Section 2
        List<TextObject> section2FeildObjectList = new ArrayList<>();
        section2FeildObjectList.add(MarkdownTextObject.builder()
                        .text(String.format("*Issue Id:*\n %s",zeroKIssueInference.getIssueId()))
                .build());
        section2FeildObjectList.add(MarkdownTextObject.builder()
                .text(String.format("*Issue Occurred TimeStamp:*\n %s",zeroKIssueInference.getIssueTimestamp()))
                .build());
        section2FeildObjectList.add(MarkdownTextObject.builder()
                .text(String.format("*Incident Id:*\n %s)",zeroKIssueInference.getIncidentId()))
                .build());
        section2FeildObjectList.add(MarkdownTextObject.builder()
                .text(String.format("*Inference:*\n %s",zeroKIssueInference.getInference()))
                .build());

        SectionBlock section2 = SectionBlock.builder()
                .fields(section2FeildObjectList)
                .build();
        blocks.add(section2);

        // Section 3
//        List<BlockElement> elements = new ArrayList<>();
//        elements.add(RichTextSectionElement.builder()
//                        .elements(Collections.singletonList(RichTextSectionElement.Text.builder()
//                                .text("Zerok Observations : \n")
//                                .style(RichTextSectionElement.TextStyle.builder()
//                                        .bold(true)
//                                        .build())
//                                .build()))
//                .build());
//        elements.add(RichTextListElement.builder()
//                .build());
//        RichTextBlock section3 = RichTextBlock.builder()
//                .elements(elements)
//                .build();
//
//        blocks.add(section3);

//        elements.add(new RichTextSection());
//        elements.add(new RichTextList(List.of(
//                new RichTextSection("item 1: "),
//                new RichTextEmoji("basketball")
//        )));
//        elements.add(new RichTextList(List.of(
//                new RichTextSection("item 2: "),
//                new RichTextText("this is a list item")
//        )));
//        elements.add(new RichTextList(List.of(
//                new RichTextSection("item 3: "),
//                new RichTextLink("with a link", "https://slack.com/", true)
//        )));
//        elements.add(new RichTextList(List.of(
//                new RichTextSection("item 4: "),
//                new RichTextText("we are near the end")
//        )));
//        elements.add(new RichTextList(List.of(
//                new RichTextSection("item 5: "),
//                new RichTextText("this is the end")
//        )));

//        RichTextElementList richTextElementList = new RichTextElementList("rich_text_list", "bullet", elements);
//        elements = new ArrayList<>();
//        elements.add(richTextElementList);
//        RichTextSection richTextSection = new RichTextSection("Zerok Observations : \n", true);
//        richTextSection.setElements(elements);
//        elements = new ArrayList<>();
//        elements.add(richTextSection);
//
//        RichText richText = new RichText("rich_text", elements);
//        section3.setElements(List.of(richText));
//        blocks.add(section3);

        slackMessage.setBlocks(blocks);

        return slackMessage;
    }
}

