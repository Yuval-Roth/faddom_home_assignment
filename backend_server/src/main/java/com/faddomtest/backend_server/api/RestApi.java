package com.faddomtest.backend_server.api;

import com.faddomtest.backend_server.api.requests.CpuUsageStatisticsRequest;
import com.faddomtest.backend_server.business.AwsService;
import com.faddomtest.backend_server.utils.JsonUtils;
import com.faddomtest.backend_server.utils.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
public class RestApi {

    private final AwsService awsService;
    private final ResponseEntity<String> badRequest = Response.getError("Bad request", HttpStatus.BAD_REQUEST);

    public RestApi(AwsService awsService){
        this.awsService = awsService;
    }

    @PostMapping("/cpu-usage-statistics")
    public ResponseEntity<String> cpuUsageStatistics(@RequestBody String body){
        CpuUsageStatisticsRequest request;

        // deserialize and validate request
        try{
            request = JsonUtils.deserialize(body, CpuUsageStatisticsRequest.class);
        } catch (Exception ignored){
            return badRequest;
        }
        if(! request.noNullFields()){
            return badRequest;
        }

        // business call
        var result = awsService.getCpuUsageStatistics(
                request.instanceIp(),
                request.startTime(),
                request.endTime(),
                request.sampleInterval()
        );

        // if result is null it means that the ip does not match an instance and thus no data could be found
        // still we return status code 200 because the http request itself succeeded
        if (result == null){
            return Response.getError("Failed to find an instance with the requested IP", HttpStatus.OK);
        }

        return Response.getOk(result);
    }
}
