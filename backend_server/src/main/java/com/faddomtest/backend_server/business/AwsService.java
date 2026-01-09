package com.faddomtest.backend_server.business;

import com.faddomtest.backend_server.exceptions.AwsFileCredentialsProviderException;
import com.faddomtest.backend_server.exceptions.AwsServiceException;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.cloudwatch.CloudWatchClient;
import software.amazon.awssdk.services.cloudwatch.model.*;
import software.amazon.awssdk.services.ec2.Ec2Client;
import software.amazon.awssdk.services.ec2.model.DescribeInstancesRequest;
import software.amazon.awssdk.services.ec2.model.DescribeInstancesResponse;
import software.amazon.awssdk.services.ec2.model.Filter;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Comparator;
import java.util.List;

@Service
public class AwsService {

    private static final ZoneId JERUSALEM_ZONE_ID = ZoneId.of("Asia/Jerusalem");
    private final Ec2Client ec2;
    private final CloudWatchClient cloudWatch;

    public AwsService(AwsConfig awsConfig) {
        // setup credentials provider
        var credentialsProvider = new AwsFileCredentialsProvider();
        try {
            credentialsProvider.init(awsConfig.getCredentialsPath());
        } catch (AwsFileCredentialsProviderException e) {
            throw new RuntimeException(e);
        }

        // optionally, validate the credentials if runtime args requested so
        if(awsConfig.getValidateCredentials() && ! credentialsProvider.testCredentials()){
            throw new RuntimeException("AWS credentials were rejected");
        }

        // initialize clients
        ec2 = Ec2Client.builder()
                .credentialsProvider(credentialsProvider)
                .region(Region.US_EAST_1)
                .build();
        cloudWatch = CloudWatchClient.builder()
                .credentialsProvider(credentialsProvider)
                .region(Region.US_EAST_1)
                .build();
    }

    /**
     * @return a list of datapoints (that may be empty) or null if the instance id could not be resolved,
     * which means that there is no instance associated with the ip
     */
    public List<Datapoint> getCpuUsageStatistics(String instanceIp, LocalDateTime startTime, LocalDateTime endTime, int sampleInterval) throws AwsServiceException {
        String instanceId = resolveInstanceId(instanceIp);
        if(instanceId == null) return null;

        var request = GetMetricStatisticsRequest.builder()
            .namespace("AWS/EC2")
            .metricName("CPUUtilization")
            .dimensions(
                Dimension.builder()
                    .name("InstanceId")
                    .value(instanceId)
                    .build()
            )
            .startTime(startTime.atZone(JERUSALEM_ZONE_ID).toInstant())
            .endTime(endTime.atZone(JERUSALEM_ZONE_ID).toInstant())
            .period(sampleInterval)
            .statistics(Statistic.AVERAGE)
            .build();
        try{
            var response = cloudWatch.getMetricStatistics(request);
            return response.datapoints().stream().sorted(Comparator.comparing(Datapoint::timestamp)).toList();
        } catch(Exception e){
            throw new AwsServiceException(e.getMessage());
        }
    }

    /**
     * @return the instance id or null if not found a matching instance with the private ip provided
     */
    private String resolveInstanceId(String privateIp) throws AwsServiceException {
        var request = DescribeInstancesRequest.builder().filters(
            Filter.builder()
                    .name("private-ip-address")
                    .values(privateIp)
                    .build()
        ).build();
        DescribeInstancesResponse response;
        try{
            response = ec2.describeInstances(request);
        } catch(Exception e){
            throw new AwsServiceException(e.getMessage());
        }
        // at most one instance is expected to match
        for (var reservation : response.reservations()) {
            for (var instance : reservation.instances()) {
                return instance.instanceId();
            }
        }
        return null; // none matching
    }
}
