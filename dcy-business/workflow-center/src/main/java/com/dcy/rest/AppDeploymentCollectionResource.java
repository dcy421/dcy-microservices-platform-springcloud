package com.dcy.rest;

import org.apache.commons.lang3.StringUtils;
import org.flowable.app.api.AppRepositoryService;
import org.flowable.app.api.repository.AppDeployment;
import org.flowable.app.api.repository.AppDeploymentBuilder;
import org.flowable.app.api.repository.AppDeploymentQuery;
import org.flowable.app.engine.impl.repository.AppDeploymentQueryProperty;
import org.flowable.app.rest.AppRestApiInterceptor;
import org.flowable.app.rest.AppRestResponseFactory;
import org.flowable.app.rest.service.api.repository.AppDeploymentResponse;
import org.flowable.common.engine.api.FlowableException;
import org.flowable.common.engine.api.FlowableIllegalArgumentException;
import org.flowable.common.engine.api.query.QueryProperty;
import org.flowable.common.rest.api.DataResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.ZipInputStream;

import static org.flowable.common.rest.api.PaginateListUtil.paginateList;

/**
 * @Author：dcy
 * @Description:
 * @Date: 2020-02-21 08:54
 */
@RestController
public class AppDeploymentCollectionResource {

    private static Map<String, QueryProperty> allowedSortProperties = new HashMap<>();

    static {
        allowedSortProperties.put("id", AppDeploymentQueryProperty.DEPLOYMENT_ID);
        allowedSortProperties.put("name", AppDeploymentQueryProperty.DEPLOYMENT_NAME);
        allowedSortProperties.put("deployTime", AppDeploymentQueryProperty.DEPLOY_TIME);
        allowedSortProperties.put("tenantId", AppDeploymentQueryProperty.DEPLOYMENT_TENANT_ID);
    }

    @Autowired
    protected AppRestResponseFactory appRestResponseFactory;

    @Autowired
    protected AppRepositoryService appRepositoryService;

    @Autowired(required = false)
    protected AppRestApiInterceptor restApiInterceptor;


    @GetMapping(value = "/app-repository/deployments", produces = "application/json")
    public DataResponse<AppDeploymentResponse> getDeployments(@RequestParam Map<String, String> allRequestParams) {
        AppDeploymentQuery deploymentQuery = appRepositoryService.createDeploymentQuery();

        // Apply filters
        if (allRequestParams.containsKey("name")) {
            deploymentQuery.deploymentName(allRequestParams.get("name"));
        }
        if (allRequestParams.containsKey("nameLike")) {
            deploymentQuery.deploymentNameLike(allRequestParams.get("nameLike"));
        }
        if (allRequestParams.containsKey("category")) {
            deploymentQuery.deploymentCategory(allRequestParams.get("category"));
        }
        if (allRequestParams.containsKey("categoryNotEquals")) {
            deploymentQuery.deploymentCategoryNotEquals(allRequestParams.get("categoryNotEquals"));
        }
        if (allRequestParams.containsKey("tenantId")) {
            deploymentQuery.deploymentTenantId(allRequestParams.get("tenantId"));
        }
        if (allRequestParams.containsKey("tenantIdLike")) {
            deploymentQuery.deploymentTenantIdLike(allRequestParams.get("tenantIdLike"));
        }
        if (allRequestParams.containsKey("withoutTenantId")) {
            Boolean withoutTenantId = Boolean.valueOf(allRequestParams.get("withoutTenantId"));
            if (withoutTenantId) {
                deploymentQuery.deploymentWithoutTenantId();
            }
        }

        if (restApiInterceptor != null) {
            restApiInterceptor.accessDeploymentsWithQuery(deploymentQuery);
        }

        return paginateList(allRequestParams, deploymentQuery, "id", allowedSortProperties, appRestResponseFactory::createAppDeploymentResponseList);
    }

    @PostMapping(value = "/app-repository/deployments", produces = "application/json", consumes = "multipart/form-data")
    public AppDeploymentResponse uploadDeployment(@RequestParam(value = "tenantId", required = false) String tenantId, HttpServletRequest request, HttpServletResponse response) {

        if (restApiInterceptor != null) {
            restApiInterceptor.executeNewDeploymentForTenantId(tenantId);
        }

        if (!(request instanceof MultipartHttpServletRequest)) {
            throw new FlowableIllegalArgumentException("Multipart request is required");
        }

        String queryString = request.getQueryString();
        Map<String, String> decodedQueryStrings = splitQueryString(queryString);

        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;

        if (multipartRequest.getFileMap().size() == 0) {
            throw new FlowableIllegalArgumentException("Multipart request with file content is required");
        }

        MultipartFile file = multipartRequest.getFileMap().values().iterator().next();

        try {
            AppDeploymentBuilder deploymentBuilder = appRepositoryService.createDeployment();
            String fileName = file.getOriginalFilename();
            if (StringUtils.isEmpty(fileName) || !(fileName.endsWith(".app") || fileName.toLowerCase().endsWith(".bar") || fileName.toLowerCase().endsWith(".zip"))) {
                fileName = file.getName();
            }

            if (fileName.endsWith(".app")) {
                deploymentBuilder.addInputStream(fileName, file.getInputStream());
            } else if (fileName.toLowerCase().endsWith(".bar") || fileName.toLowerCase().endsWith(".zip")) {
                deploymentBuilder.addZipInputStream(new ZipInputStream(file.getInputStream()));
            } else {
                throw new FlowableIllegalArgumentException("File must be of type .app");
            }

            if (!decodedQueryStrings.containsKey("deploymentName") || StringUtils.isEmpty(decodedQueryStrings.get("deploymentName"))) {
                String fileNameWithoutExtension = fileName.split("\\.")[0];

                if (StringUtils.isNotEmpty(fileNameWithoutExtension)) {
                    fileName = fileNameWithoutExtension;
                }

                deploymentBuilder.name(fileName);

            } else {
                deploymentBuilder.name(decodedQueryStrings.get("deploymentName"));
            }

            if (decodedQueryStrings.containsKey("deploymentKey") && StringUtils.isNotEmpty(decodedQueryStrings.get("deploymentKey"))) {
                deploymentBuilder.key(decodedQueryStrings.get("deploymentKey"));
            }

            if (tenantId != null) {
                deploymentBuilder.tenantId(tenantId);
            }
            deploymentBuilder.name(fileName);

            if (tenantId != null) {
                deploymentBuilder.tenantId(tenantId);
            }

            if (restApiInterceptor != null) {
                restApiInterceptor.enhanceDeployment(deploymentBuilder);
            }

            AppDeployment deployment = deploymentBuilder.deploy();
            response.setStatus(HttpStatus.CREATED.value());

            return appRestResponseFactory.createAppDeploymentResponse(deployment);

        } catch (Exception e) {
            if (e instanceof FlowableException) {
                throw (FlowableException) e;
            }
            throw new FlowableException(e.getMessage(), e);
        }
    }

    public Map<String, String> splitQueryString(String queryString) {
        if (StringUtils.isEmpty(queryString)) {
            return Collections.emptyMap();
        }
        Map<String, String> queryMap = new HashMap<>();
        for (String param : queryString.split("&")) {
            queryMap.put(StringUtils.substringBefore(param, "="), decode(StringUtils.substringAfter(param, "=")));
        }
        return queryMap;
    }

    protected String decode(String string) {
        if (string != null) {
            try {
                return URLDecoder.decode(string, "UTF-8");
            } catch (UnsupportedEncodingException uee) {
                throw new IllegalStateException("JVM does not support UTF-8 encoding.", uee);
            }
        }
        return null;
    }
}
