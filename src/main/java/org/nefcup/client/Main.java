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
package org.nefcup.client;

import org.nefcup.client.repository.EnvironmentRepository;
import org.nefcup.client.service.IgnoreService;
import org.nefcup.client.service.MainService;
import org.nefcup.client.service.NefcupService;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class Main {
    private static final String CLEAN_METHOD = "clean";
    private static final String UPLOAD_METHOD = "upload";
    private static final String DEPLOY_METHOD = "deploy";
    private static final String REPLACE_PARAMETER = "replace";

    public static void main(String[] args) throws IOException {
        if (EnvironmentRepository.getToken()==null){
            System.out.println("The \""+EnvironmentRepository.TOKEN_ENVIRONMENT_NAME+"\" environment variable is not specified");
            System.exit(1);
        }
        if (EnvironmentRepository.getServiceAddress()==null){
            System.out.println("The \""+EnvironmentRepository.SERVICE_ADDRESS_ENVIRONMENT_NAME+"\" environment variable is not specified");
            System.exit(1);
        }
        if (EnvironmentRepository.getProjectName()==null){
            System.out.println("The \""+EnvironmentRepository.PROJECT_NAME_ENVIRONMENT_NAME+"\" environment variable is not specified");
            System.exit(1);
        }
        String projectDirectory = EnvironmentRepository.getProjectDirectory();
        if (projectDirectory==null){
            projectDirectory = System.getProperty("user.dir");
        }
        Path ignoreNefcup = Path.of(projectDirectory, "ignore.nefcup");
        String patternsText = null;
        if (Files.exists(ignoreNefcup)){
            patternsText = Files.readString(ignoreNefcup);
        }
        IgnoreService ignoreService = new IgnoreService(patternsText);
        NefcupService nefcupService = new NefcupService(
                EnvironmentRepository.getServiceAddress(),
                EnvironmentRepository.getToken(),
                EnvironmentRepository.getProjectName()
        );
        MainService mainService = new MainService(
                projectDirectory,
                EnvironmentRepository.getProjectName(),
                nefcupService,
                ignoreService
        );
        String method = DEPLOY_METHOD;
        boolean isReplace = false;
        if (args.length!=0){
            String[] methodAndParameters = args[0].toLowerCase().split(":");
            if (methodAndParameters.length==0){
                System.out.println("The method and parameters cannot be parsed");
                System.exit(1);
            }
            method = methodAndParameters[0];
            if (methodAndParameters.length>1){
                isReplace = REPLACE_PARAMETER.equalsIgnoreCase(methodAndParameters[1]);
            }
        }
        switch (method){
            case DEPLOY_METHOD -> mainService.deploy();
            case CLEAN_METHOD -> mainService.clean();
            case UPLOAD_METHOD -> mainService.upload(isReplace);
            default -> {
                System.out.println("The method and parameters cannot be parsed");
                System.exit(1);
            }
        }
    }
}