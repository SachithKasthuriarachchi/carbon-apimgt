package org.wso2.carbon.apimgt.rest.api.publisher.v1.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class DeploymentsDTO   {

    private String name = null;
    private List<DeploymentClusterInfoDTO> clusters = new ArrayList<>();

    /**
     **/
    public DeploymentsDTO name(String name) {
        this.name = name;
        return this;
    }


    @ApiModelProperty(example = "Kubernetes", required = true, value = "")
    @JsonProperty("name")
    @NotNull
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    /**
     **/
    public DeploymentsDTO clusters(List<DeploymentClusterInfoDTO> clusters) {
        this.clusters = clusters;
        return this;
    }


    @ApiModelProperty(required = true, value = "")
    @JsonProperty("clusters")
    @NotNull
    public List<DeploymentClusterInfoDTO> getClusters() {
        return clusters;
    }
    public void setClusters(List<DeploymentClusterInfoDTO> clusters) {
        this.clusters = clusters;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        DeploymentsDTO deployments = (DeploymentsDTO) o;
        return Objects.equals(name, deployments.name) &&
                Objects.equals(clusters, deployments.clusters);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, clusters);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class DeploymentsDTO {\n");

        sb.append("    name: ").append(toIndentedString(name)).append("\n");
        sb.append("    clusters: ").append(toIndentedString(clusters)).append("\n");
        sb.append("}");
        return sb.toString();
    }

    /**
     * Convert the given object to string with each line indented by 4 spaces
     * (except the first line).
     */
    private String toIndentedString(Object o) {
        if (o == null) {
            return "null";
        }
        return o.toString().replace("\n", "\n    ");
    }
}