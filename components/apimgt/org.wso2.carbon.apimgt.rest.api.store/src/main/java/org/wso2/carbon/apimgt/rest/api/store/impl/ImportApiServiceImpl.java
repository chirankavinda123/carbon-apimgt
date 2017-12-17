package org.wso2.carbon.apimgt.rest.api.store.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.cxf.jaxrs.ext.multipart.Attachment;
import org.wso2.carbon.apimgt.api.APIConsumer;
import org.wso2.carbon.apimgt.api.APIManagementException;
import org.wso2.carbon.apimgt.api.model.APIIdentifier;
import org.wso2.carbon.apimgt.api.model.Application;
import org.wso2.carbon.apimgt.api.model.SubscribedAPI;
import org.wso2.carbon.apimgt.rest.api.store.ApiResponseMessage;
import org.wso2.carbon.apimgt.rest.api.store.ImportApiService;
import org.wso2.carbon.apimgt.rest.api.store.utils.FileBasedApplicationImportExportManager;
import org.wso2.carbon.apimgt.rest.api.util.utils.RestApiUtil;

import java.io.File;
import java.io.InputStream;
import java.util.Set;
import java.util.UUID;
import javax.ws.rs.core.Response;

public class ImportApiServiceImpl extends ImportApiService {
    private static final Log log = LogFactory.getLog(ApplicationsApiServiceImpl.class);

    /**
     * Import an Application which has been exported to a zip file
     *
     * @param fileInputStream content stream of the zip file which contains exported Application
     * @param fileDetail      meta information of the zip file
     * @return imported Application
     */
    @Override
    public Response importApplicationsPost(InputStream fileInputStream, Attachment fileDetail) {
        APIConsumer consumer = null;
        String username = RestApiUtil.getLoggedInUsername();
        String tempDirPath = System.getProperty("java.io.tmpdir") + File.separator + "imported-app-archive-" +
                UUID.randomUUID().toString();
        try {
            consumer = RestApiUtil.getConsumer(username);
            FileBasedApplicationImportExportManager importExportManager =
                    new FileBasedApplicationImportExportManager(consumer, tempDirPath);
            Application applicationDetails = importExportManager.importApplication(fileInputStream);
            Set<SubscribedAPI> subscribedAPIs = applicationDetails.getSubscribedAPIs();
            int appId = consumer.addApplication(applicationDetails, username);
            for (SubscribedAPI subscribedAPI : subscribedAPIs) {
                APIIdentifier apiIdentifier = subscribedAPI.getApiId();
                /*if (importExportManager.isAPIAvailable(apiIdentifier)) {
                    consumer.addSubscription(apiIdentifier, username, appId);
                }*/
            }
            Application importedApplication = consumer.getApplicationById(appId);
            return Response.ok().entity(importedApplication).build();
        } catch (APIManagementException e) {
            RestApiUtil
                    .handleInternalServerError("Error while importing Application" + username, e, log);
        }
        return Response.ok().entity(new ApiResponseMessage(ApiResponseMessage.OK, "magic!")).build();
    }

    @Override
    public Response importApplicationsPut(InputStream fileInputStream, Attachment fileDetail) {
        // do some magic!
        return Response.ok().entity(new ApiResponseMessage(ApiResponseMessage.OK, "magic!")).build();
    }

    @Override
    public String importApplicationsPostGetLastUpdatedTime(InputStream fileInputStream, Attachment fileDetail) {
        return null;
    }

    @Override
    public String importApplicationsPutGetLastUpdatedTime(InputStream fileInputStream, Attachment fileDetail) {
        return null;
    }
}
