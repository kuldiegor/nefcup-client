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
package org.nefcup.client.repository;

public class EnvironmentRepository {
    /*
    * Адрес сервиса например http://localhost/nefcup
    * Обязательный параметр */
    public static final String SERVICE_ADDRESS_ENVIRONMENT_NAME = "NEFCUP_SERVICE_ADDRESS";

    /*
    * Токен для аутентификации и авторизации
    * Обязательный параметр */
    public static final String TOKEN_ENVIRONMENT_NAME = "NEFCUP_TOKEN";

    /*
    * Название проекта для web пути
    * Обязательный параметр */
    public static final String PROJECT_NAME_ENVIRONMENT_NAME = "NEFCUP_PROJECT_NAME";

    /*
    * Директория для поиска файлов для загрузки,
    * если не указана то поиск файлов будет производиться в рабочей директории*/
    public static final String PROJECT_DIRECTORY_ENVIRONMENT_NAME = "NEFCUP_PROJECT_DIRECTORY";


    public static String getServiceAddress(){
        return System.getenv(SERVICE_ADDRESS_ENVIRONMENT_NAME);
    }

    public static String getToken(){
        return System.getenv(TOKEN_ENVIRONMENT_NAME);
    }

    public static String getProjectName(){
        return System.getenv(PROJECT_NAME_ENVIRONMENT_NAME);
    }

    public static String getProjectDirectory(){
        return System.getenv(PROJECT_DIRECTORY_ENVIRONMENT_NAME);
    }


}
