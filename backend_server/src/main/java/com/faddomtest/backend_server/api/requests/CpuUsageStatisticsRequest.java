package com.faddomtest.backend_server.api.requests;
import java.time.LocalDateTime;

public record CpuUsageStatisticsRequest(
        String instanceIp,
        LocalDateTime startTime,
        LocalDateTime endTime,
        Integer sampleInterval
) {
    public boolean noNullFields(){
        return instanceIp != null && startTime != null && endTime != null && sampleInterval != null;
    }
}
