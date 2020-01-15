#Introduction 
TODO: Give a short introduction of your project. Let this section explain the objectives or the motivation behind this project. 

#Getting Started
TODO: Guide users through getting your code up and running on their own system. In this section you can talk about:
1.  Installation process
2.  Software dependencies
3.  Latest releases
4.  API references

#Build and Test
TODO: Describe and show how to build your code and run the tests. 

#Changelog
All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](http://keepachangelog.com/en/1.0.0/)
and this project adheres to [Semantic Versioning](http://semver.org/spec/v2.0.0.html).

Guiding Principles:
- There should be an entry for every single version.
- The same types of changes should be grouped.
- Versions and sections should be linkable.
- The latest version comes first.
- The release date of each versions is displayed.
- Keep an [Unreleased] section at the top to track upcoming changes.
- Group changes to describe their impact on the project, as follows:
  - **Compatible versions and migration** for new dependency library versions and migration instructions.  
  Please specify SQL script links, code to change, etc.
  - **Added** for new features.
  - **Changed** for changes in existing functionality.
  - **Deprecated** for soon-to-be removed features.
  - **Removed** for now removed features.
  - **Fixed** for any bug fixes.
  - **Security** in case of vulnerabilities.

## [Unreleased] - yyy.mm.dd

## [2.2.0-SNAPSHOT]
###Added
 - `ExceptionHandler` - Handler of any `Throwable` that might be thrown while a `RequestHandler` works.
 - `ExceptionHandlerFactory` for `ExceptionHandler`.
 You might add new `ExceptionHandler` with almost the same logic as you do with `RequestHandler`s.
 E.g. you have `NoOrderFoundException` and you want to handle that. In your `AbstractController.setupExceptionHandlerFactory()`:
```java
//...
factory.putExceptionHandler(NoOrderFoundException.class, getNoOrderFoundExceptionHandler());
//...
```
 Another e.g.: the same situation but you'd like to have different handling for `actionId=a1`:
```java
//...
factory.putExceptionHandler(NoOrderFoundException.class, getNoOrderFoundExceptionHandler())
                    .putActionExceptionHandler("a1", getNoOrderFoundExceptionHandlerA1())
                .done()
                .putExceptionHandler(IOException.class, getIOExceptionHandler());;
//...
```
 - `AbstractController.handleException()` - it is recursive so if one of your `ExceptionHandler`s will throw an Exception, another one would handle that.
 - `AbstractController.getRequestHandlerNotFoundExceptionHandler()`.
 - `AbstractController.getInvalidSessionExceptionHandlerHandler()`.
 - `abstract AbstractController.setupExceptionHandlerFactory()`.
 - `abstract AbstractController.getDefaultExceptionHandler()`.
###Removed
 - `onInvalidSessionException()` and `onRequestHandlerNotFoundException()` from `AbstractController`.
###Changed
 - Moved `InvalidSessionException`, `RequestHandlerFactorySetupException` and `RequesthandlerNotFoundException` to `com.paxar.qps.common.exception` package.
 - `RequestHandler.handle() throws IOException, ServletException` -> `RequestHandler.handle() throws Exception`.
###Compatible versions and migration
 - If you have overridden `AbstractController.onInvalidSessionException()` or `AbstractController.onRequestHandlerNotFoundException()`, overwrite it to `ExceptionHandler` logic.

## [2.1.5] - 2019.12.24
### Compatible versions and migration:
- `averylib`: 0.0.6
- `dbconnect`: 0.0.3

## [2.1.4-SNAPSHOT]
### Fixed
- `AlphanumericComparator` was limited by `Integer` chunks comparison and threw `NumberFormatException` for the bigger ones.
Switched `Integer` to `BigInteger` which also changed performance (tested with shuffled string `123456789`):
10_000 entries: Integer - 151 millis, BigInteger - 69 millis;
1_000_000 entries: Integer - 5757 millis, BigInteger - 6234 millis.

## [2.1.3-SNAPSHOT] - 2018.12.20
### Added
- `RequestAuthorizer` - now has method `onDenied()`.

## [2.1.2-SNAPSHOT] - 2018.11.26
### Changed
- `AbstractController` will use default `RequestAuthorizer` if `actionId` is blank.

## [2.1.1-SNAPSHOT] - 2018.11.02
### Added
- `RequestValidator` for possible validation before `RequestHandler` will work.
### Changed
- `AbstractController` will call `RequestAuthorizer.authorize()` if `RequestAuthorizer` is present.

## [2.1.0-SNAPSHOT] - 2018.10.02
### Compatible versions and migration:
- Java 1.8

### Added
- Ability to use checked exception in lambda expressions.

## [2.0.2] - 2018.08.17
### Added
- `AlphaNumericComparator` to utils package `utils`

## [2.0.1-SNAPSHOT] - ?
### Added
- MapUtils to utils package: map
- Actually in the version were more changes made before. Just starting tradition to add changelog.

#Contribute
TODO: Explain how other users and developers can contribute to make your code better. 

If you want to learn more about creating good readme files then refer the following 
[guidelines](https://www.visualstudio.com/en-us/docs/git/create-a-readme). 
You can also seek inspiration from the below readme files:
- [ASP.NET Core](https://github.com/aspnet/Home)
- [Visual Studio Code](https://github.com/Microsoft/vscode)
- [Chakra Core](https://github.com/Microsoft/ChakraCore)
