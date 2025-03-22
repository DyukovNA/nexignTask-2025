package org.example.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
public class UDR {
    private String msisdn;
    private CallDetail incomingCall;
    private CallDetail outcomingCall;

    @Data
    @AllArgsConstructor
    public static class CallDetail {
        private String totalTime;
    }
}
