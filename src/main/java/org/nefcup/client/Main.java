package org.nefcup.client;

import org.nefcup.client.repository.EnvironmentRepository;
import org.nefcup.client.service.IgnoreService;
import org.nefcup.client.service.MainService;
import org.nefcup.client.service.NefcupService;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

public class Main {
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
        mainService.process();

    }
}