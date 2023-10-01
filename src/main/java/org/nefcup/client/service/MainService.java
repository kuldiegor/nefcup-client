package org.nefcup.client.service;

import lombok.RequiredArgsConstructor;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RequiredArgsConstructor
public class MainService {
    private final String projectDirectoryStr;
    private final String projectNameStr;
    private final NefcupService nefcupService;
    private final IgnoreService ignoreService;

    public void process() throws IOException {
        nefcupService.cleanProject(projectNameStr);
        Path projectDirectory = Path.of(projectDirectoryStr);
        try (Stream<Path> pathStream = Files.walk(projectDirectory)) {
            List<Path> pathList = pathStream.collect(Collectors.toList());
            for (Path path:pathList){
                Path relativize = projectDirectory.relativize(path);
                String relativizeStr = relativize.toString();

                if (ignoreService.isIgnore(relativize)){
                    continue;
                }
                if (Files.isDirectory(path)){
                    nefcupService.createProjectDirectory(projectNameStr,relativizeStr);
                } else if (Files.isRegularFile(path)){
                    try (InputStream inputStream = Files.newInputStream(path)) {
                        nefcupService.uploadProjectFile(projectNameStr,relativizeStr,inputStream);
                    }
                }
            }
        }
    }
}
