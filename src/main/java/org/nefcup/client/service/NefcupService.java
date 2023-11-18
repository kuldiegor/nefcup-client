/*
    Copyright 2023 Dmitrij Kulabuhov

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
*/
package org.nefcup.client.service;

import com.google.gson.Gson;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.config.RequestConfig;
import org.apache.hc.client5.http.impl.DefaultRedirectStrategy;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClientBuilder;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.ContentType;
import org.apache.hc.core5.http.HttpStatus;
import org.apache.hc.core5.http.io.entity.InputStreamEntity;
import org.apache.hc.core5.http.io.entity.StringEntity;
import org.nefcup.client.entity.ProjectCleanRequest;
import org.nefcup.client.entity.ProjectCreateDirectoryRequest;

import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeUnit;

public class NefcupService {
    private static final int TIMEOUT = 10 * 1000;
    private static final String PROJECT_FILE_UPLOAD_PATH = "/project/file/upload";
    private static final String PROJECT_CLEAN_PATH = "/project/clean";
    private static final String PROJECT_DIRECTORY_CREATE_PATH = "/project/directory/create";

    private final String serviceAddress;
    private final String token;
    private final String projectName;

    private final CloseableHttpClient httpClient;
    private final Gson gson;


    public NefcupService(String serviceAddress, String token, String projectName) {
        this.serviceAddress = serviceAddress;
        this.token = token;
        this.projectName = projectName;
        HttpClientBuilder httpClientBuilder = HttpClients.custom()
                .setDefaultRequestConfig(
                        RequestConfig.custom()
                                .setCookieSpec("standard")
                                .setConnectionRequestTimeout(TIMEOUT, TimeUnit.MILLISECONDS)
                                .build())
                .setRedirectStrategy(new DefaultRedirectStrategy());
        httpClient = httpClientBuilder.build();
        gson = new Gson();
    }

    public void uploadProjectFile(
            String projectName,
            String fileName,
            InputStream inputStream,
            boolean isReplace) throws IOException {
        HttpPost httpPost = new HttpPost(serviceAddress +PROJECT_FILE_UPLOAD_PATH+
                "?project-name="+URLEncoder.encode(projectName,StandardCharsets.UTF_8)+
                "&file-name="+URLEncoder.encode(fileName,StandardCharsets.UTF_8)+
                "&is-replace="+URLEncoder.encode(Boolean.toString(isReplace),StandardCharsets.UTF_8)
                );

        httpPost.setEntity(new InputStreamEntity(inputStream,ContentType.APPLICATION_OCTET_STREAM));
        httpPost.addHeader("token",token);
        httpClient.execute(httpPost,response -> {
            if (response.getCode()!= HttpStatus.SC_OK){
                response.close();
                throw new IOException("error when upload file code = "+response.getCode());
            }
            return null;
        });
    }

    public void cleanProject(String projectName, String cleanIgnoreText) throws IOException {
        HttpPost httpPost = new HttpPost(serviceAddress +PROJECT_CLEAN_PATH);
        String json = gson.toJson(
                new ProjectCleanRequest(
                        projectName,
                        cleanIgnoreText
                )
        );
        httpPost.setEntity(new StringEntity(json, ContentType.APPLICATION_JSON));
        httpPost.addHeader("token",token);
        httpClient.execute(httpPost,response -> {
            if (response.getCode()!= HttpStatus.SC_OK){
                response.close();
                throw new IOException("error when clean project code = "+response.getCode());
            }
            return null;
        });
    }

    public void createProjectDirectory(String projectName,
                                String directoryName) throws IOException {
        HttpPost httpPost = new HttpPost(serviceAddress +PROJECT_DIRECTORY_CREATE_PATH);
        String json = gson.toJson(new ProjectCreateDirectoryRequest(projectName,directoryName));
        httpPost.setEntity(new StringEntity(json, ContentType.APPLICATION_JSON));
        httpPost.addHeader("token",token);
        httpClient.execute(httpPost,response -> {
            if (response.getCode()!= HttpStatus.SC_OK){
                response.close();
                throw new IOException("error when create directory code = "+response.getCode());
            }
            return null;
        });
    }
}
