package org.wso2.carbon.apimgt.internal.service.impl;

import org.apache.cxf.jaxrs.ext.MessageContext;
import org.wso2.carbon.apimgt.api.APIManagementException;
import org.wso2.carbon.apimgt.api.ExceptionCodes;
import org.wso2.carbon.apimgt.impl.APIConstants;
import org.wso2.carbon.apimgt.impl.dto.APIRuntimeArtifactDto;
import org.wso2.carbon.apimgt.impl.dto.RuntimeArtifactDto;
import org.wso2.carbon.apimgt.impl.gatewayartifactsynchronizer.APIArtifactGeneratorUtil;
import org.wso2.carbon.apimgt.impl.gatewayartifactsynchronizer.RuntimeArtifactGeneratorUtil;
import org.wso2.carbon.apimgt.internal.service.RetrieveApiArtifactsApiService;
import org.wso2.carbon.apimgt.internal.service.dto.SynapseArtifactListDTO;
import org.wso2.carbon.apimgt.internal.service.dto.UUIDListDTO;
import org.wso2.carbon.apimgt.internal.service.utils.SubscriptionValidationDataUtil;
import org.wso2.carbon.apimgt.rest.api.common.RestApiConstants;
import org.wso2.carbon.apimgt.rest.api.util.utils.RestApiUtil;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.StreamingOutput;
import java.io.File;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;


public class RetrieveApiArtifactsApiServiceImpl implements RetrieveApiArtifactsApiService {

    public Response retrieveApiArtifactsPost(String xWSO2Tenant, String gatewayLabel, String type, UUIDListDTO uuidList,
                                             MessageContext messageContext)
            throws APIManagementException {
        xWSO2Tenant = SubscriptionValidationDataUtil.validateTenantDomain(xWSO2Tenant, messageContext);
        RuntimeArtifactDto runtimeArtifactDto =
                APIArtifactGeneratorUtil.generateAPIArtifact(uuidList.getUuids(), "", "", gatewayLabel, type,
                        xWSO2Tenant);
        if (runtimeArtifactDto != null) {
            if (runtimeArtifactDto.isFile()) {
                File artifact = (File) runtimeArtifactDto.getArtifact();
                StreamingOutput streamingOutput = (outputStream) -> {
                    try {
                        Files.copy(artifact.toPath(), outputStream);
                    } finally {
                        Files.delete(artifact.toPath());
                    }
                };
                return Response.ok(streamingOutput).header(RestApiConstants.HEADER_CONTENT_DISPOSITION,
                        "attachment; filename=apis.zip").header(RestApiConstants.HEADER_CONTENT_TYPE,
                        APIConstants.APPLICATION_ZIP).build();
            } else {
                SynapseArtifactListDTO synapseArtifactListDTO = new SynapseArtifactListDTO();
                if (runtimeArtifactDto.getArtifact() instanceof List) {
                    synapseArtifactListDTO.setList((List<String>) runtimeArtifactDto.getArtifact());
                    synapseArtifactListDTO.setCount(((List<String>) runtimeArtifactDto.getArtifact()).size());
                }
                return Response.ok().entity(synapseArtifactListDTO)
                        .header(RestApiConstants.HEADER_CONTENT_TYPE, RestApiConstants.APPLICATION_JSON).build();
            }
        } else {
            return Response
                    .status(Response.Status.NOT_FOUND)
                    .entity(RestApiUtil.getErrorDTO(ExceptionCodes.NO_API_ARTIFACT_FOUND))
                    .build();
        }
    }
}
