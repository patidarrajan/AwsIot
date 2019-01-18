package com.rajan.utils;

import java.net.URL;
import java.util.List;
import java.util.stream.Collectors;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.iot.AWSIot;
import com.amazonaws.services.iot.AWSIotClientBuilder;
import com.amazonaws.services.iot.model.CreateJobRequest;
import com.amazonaws.services.iot.model.ListThingsRequest;
import com.amazonaws.services.iot.model.ThingAttribute;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.Bucket;
import com.amazonaws.services.s3.model.ListObjectsV2Result;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.amazonaws.services.s3.model.S3ObjectSummary;

public class AwsBuilderUtils {
	private static AwsBuilderUtils awsBuilderUtils;
	private BasicAWSCredentials awsCredentials;
	private AWSIot iotClient;
	private AmazonS3 s3;

	public static AwsBuilderUtils getInstance() {
		if (awsBuilderUtils == null) {
			awsBuilderUtils = new AwsBuilderUtils();
			awsBuilderUtils.init();
			return awsBuilderUtils;
		}
		return awsBuilderUtils;
	}

	/**
	 * AWS component initializer
	 */
	public void init() {
		awsCredentials = new BasicAWSCredentials(PropertyUtil.getConfig("awsAccessKeyId"),
				PropertyUtil.getConfig("awsSecretAccessKey"));
		iotClient = AWSIotClientBuilder.defaultClient();
		s3 = AmazonS3ClientBuilder.defaultClient();
	}

	/**
	 * list s3 bucket
	 * 
	 * @return 
	 */
	public List<String> listS3Buckets() {
		return s3.listBuckets().stream().map(bucket -> bucket.getName()).collect(Collectors.toList());
	}

	/**
	 * list s3 objects key from bucket
	 * 
	 * @param bucketName
	 * @return
	 */
	public List<String> listS3ObjectsKeyFromBucket(String bucketName) {
		ListObjectsV2Result result = s3.listObjectsV2(bucketName);
		return result.getObjectSummaries().stream().map(s3ObjectSummary -> s3ObjectSummary.getKey()).collect(Collectors.toList());

	}

	/**
	 * list things
	 * 
	 * @param listThingsRequest
	 * @return
	 */
	public List<ThingAttribute> listThing(ListThingsRequest listThingsRequest) {
		return iotClient.listThings(listThingsRequest).getThings();
	}
	
	/**
	 * create job 
	 * @param createJobRequest
	 */
	public void createJob(CreateJobRequest createJobRequest) {
		iotClient.createJob(createJobRequest);
	}
	
	public String getDocumentUrl(String bucketName, String keyName) {
		return s3.getUrl(bucketName, keyName).toExternalForm();
	}
	
}
