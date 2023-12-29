# NEFCUP client
Клиент `nefcup` работает в свзяке с [nefcup server](https://github.com/kuldiegor/nefcup-server). 
Перейдите по ссылке [nefcup server](https://github.com/kuldiegor/nefcup-server) и установите сначала `nefcup сервер`.

## Настройки
Все параметры настраиваются через переменные среды. Ниже приведён список переменных среды, используемые в приложении.

`NEFCUP_SERVICE_ADDRESS` = 
Адрес сервиса. Например http://localhost/nefcup. \
Обязательный параметр

`NEFCUP_TOKEN` =
Токен для аутентификации и авторизации в сервисе nefcup server. \
Обязательный параметр

`NEFCUP_PROJECT_NAME` =
Название проекта для web пути. \
Обязательный параметр

`NEFCUP_PROJECT_DIRECTORY` =
Корень проекта. Директория для поиска файлов для загрузки, если не указана то поиск файлов будет производиться в рабочей директории

## Игнорирование файлов при загрузке
Если необходимо добавить игнорирование файлов при загрузке, то можно создать файл в корне проекта `ignore.nefcup`.

Любые строки начинающиеся с решётки `#` игнорируются (комментарии).

Пути указываются относительно корня проекта.

Пример файла `ignore.nefcup`

```gitignore
#This is comment
#In second line the sharp is part of name
\#sometext
temp/
temp
.gitignore
temp/123
```

## Игнорирование файлов при удалении каталога
Если необходимо добавить игнорирование файлов при удалении `delete` `clean`, то можно создать файл в корне проекта `clean_ignore.nefcup`.
Формат тот же что и у `ignore.nefcup`

## Запуск
Для запуска необходимо заполнить обязательные переменные среды. \
Запустить клиент командой
```shell
java -jar nefcup-client-1.4.0-jar-with-dependencies.jar
```

или запустить скрипт

```shell
./nefcup.sh
```

## Методы
Доступны следующие методы
+ `clean` - Удаления каталога на сервере с названием из переменной `NEFCUP_PROJECT_NAME`
+ `upload(:replace)` - Загрузка файлов на сервер
+ `deploy` - Выполняет последовательно команды `clean` `upload` 
+ `delete` - Удаляет определённый файл или каталог на сервере, учитывая файл `clean_ignore.nefcup` в корне проекта.

По умолчанию, клиент запущенный без метода, запустит метод `deploy`

Для метода `upload` доступна настройка `replace`, она позволяет указать возможность замены файла в случае его присутствия на сервере. 
Также можно в качестве параметра указать файл. Метод `upload` запущенный без указания файла будет отправлять все файлы в корне проекта, учитывая `ignore.nefcup`\
Например:
```shell
java -jar nefcup-client-1.4.0-jar-with-dependencies.jar upload
```
```shell
java -jar nefcup-client-1.4.0-jar-with-dependencies.jar upload:replace
```
```shell
java -jar nefcup-client-1.4.0-jar-with-dependencies.jar upload test.txt
```
```shell
java -jar nefcup-client-1.4.0-jar-with-dependencies.jar upload:replace test.txt
```