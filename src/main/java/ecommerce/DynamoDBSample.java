package ecommerce;


import com.amazonaws.auth.ClasspathPropertiesFileCredentialsProvider;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBAsyncClient;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;

import java.util.Collections;
import java.util.HashMap;

public class DynamoDBSample {

    private AmazonDynamoDBAsyncClient client;
    private DynamoDBMapper mapper;

    private static DynamoDBSample single_instance = null;

    public static DynamoDBSample getInstance()
    {
        if (single_instance == null)
            single_instance = new DynamoDBSample();

        return single_instance;
    }
    public DynamoDBSample() {
        Region region = Region.getRegion(Regions.US_WEST_2);
        ClasspathPropertiesFileCredentialsProvider credentials = new ClasspathPropertiesFileCredentialsProvider("aws.properties");
        client = (AmazonDynamoDBAsyncClient) region.createClient(AmazonDynamoDBAsyncClient.class, credentials, null);
        mapper = new DynamoDBMapper(client);
    }

    public AmazonDynamoDBAsyncClient getClient() {
        return client;
    }

    public void setClient(AmazonDynamoDBAsyncClient client) {
        this.client = client;
    }

    public DynamoDBMapper getMapper() {
        return mapper;
    }

    public void setMapper(DynamoDBMapper mapper) {
        this.mapper = mapper;
    }
}
