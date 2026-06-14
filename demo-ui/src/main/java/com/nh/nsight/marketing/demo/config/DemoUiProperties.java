package com.nh.nsight.marketing.demo.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "nsight.demo")
public class DemoUiProperties {
    private DeploymentMode deploymentMode = DeploymentMode.bootrun;
    private String tomcatGatewayUrl = "http://localhost:8080";
    private String bootrunHost = "http://localhost";

    public DeploymentMode getDeploymentMode() {
        return deploymentMode;
    }

    public void setDeploymentMode(DeploymentMode deploymentMode) {
        this.deploymentMode = deploymentMode;
    }

    public String getTomcatGatewayUrl() {
        return tomcatGatewayUrl;
    }

    public void setTomcatGatewayUrl(String tomcatGatewayUrl) {
        this.tomcatGatewayUrl = tomcatGatewayUrl;
    }

    public String getBootrunHost() {
        return bootrunHost;
    }

    public void setBootrunHost(String bootrunHost) {
        this.bootrunHost = bootrunHost;
    }

    public enum DeploymentMode {
        bootrun, tomcat
    }
}
