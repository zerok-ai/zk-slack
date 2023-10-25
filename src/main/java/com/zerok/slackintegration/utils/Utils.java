package com.zerok.slackintegration.utils;

import com.zerok.slackintegration.model.request.ZeroKInferencePublishRequest;
import org.springframework.beans.factory.annotation.Autowired;

import java.lang.module.Configuration;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import java.util.Date;
import java.util.TimeZone;

public class Utils {

    public static String encodeToBase64(String input) {
        byte[] encodedBytes = Base64.getEncoder().encode(input.getBytes());
        return new String(encodedBytes);
    }

    public static String decodeFromBase64(String encodedString) {
        byte[] decodedBytes = Base64.getDecoder().decode(encodedString);
        return new String(decodedBytes);
    }

    public static String generateDashboardIssueUrl(String zeroKDashboardUrl, ZeroKInferencePublishRequest zeroKInferencePublishRequest){
        return String.format(zeroKDashboardUrl,zeroKInferencePublishRequest.getIssueId(),zeroKInferencePublishRequest.getIssueId(),zeroKInferencePublishRequest.getClusterId());
    }

    public static String getUTCDateTimeFromTimestamp(long timestamp){
        Instant instant = Instant.ofEpochMilli(timestamp);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS 'Z'")
                .withZone(ZoneOffset.UTC);
        return formatter.format(instant);
    }
}
