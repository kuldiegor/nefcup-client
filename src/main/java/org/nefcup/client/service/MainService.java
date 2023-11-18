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

import lombok.RequiredArgsConstructor;

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

    public void clean() throws IOException {

        nefcupService.cleanProject(projectNameStr,readIfExistsCleanIgnore());
    }

    public void upload(boolean isReplace) throws IOException {
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
                        nefcupService.uploadProjectFile(projectNameStr,relativizeStr,inputStream,isReplace);
                    }
                }
            }
        }
    }

    public void deploy() throws IOException {
        clean();
        upload(false);
    }

    private String readIfExistsCleanIgnore(){
        try {
            Path cleanIgnorePath = Path.of(projectDirectoryStr, "clean_ignore.nefcup");
            if (Files.exists(cleanIgnorePath)) {
                return Files.readString(cleanIgnorePath);
            } else {
                return null;
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
