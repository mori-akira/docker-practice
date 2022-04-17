package jp.co.molygray;

import java.time.Duration;
import java.util.List;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.model.Image;
import com.github.dockerjava.core.DefaultDockerClientConfig;
import com.github.dockerjava.core.DockerClientConfig;
import com.github.dockerjava.core.DockerClientImpl;
import com.github.dockerjava.httpclient5.ApacheDockerHttpClient;
import com.github.dockerjava.transport.DockerHttpClient;

public class DockerExecutorSample {

    public static void main(String[] args) throws Exception {

	DockerClientConfig config = DefaultDockerClientConfig.createDefaultConfigBuilder()
		.withDockerHost("tcp://localhost:2376")
		.withDockerTlsVerify(false)
		.withDockerCertPath("C:\\Users\\kgrmr\\.docker")
		.build();

	DockerHttpClient httpClient = new ApacheDockerHttpClient.Builder()
		.dockerHost(config.getDockerHost())
		.sslConfig(config.getSSLConfig())
		.maxConnections(100)
		.connectionTimeout(Duration.ofSeconds(30))
		.responseTimeout(Duration.ofSeconds(45))
		.build();

	DockerClient dockerClient = DockerClientImpl.getInstance(config, httpClient);
	List<Image> imageList = dockerClient.listImagesCmd().exec();
	imageList.forEach(System.out::println);
	}
}
