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
