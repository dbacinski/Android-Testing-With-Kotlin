# Introduction to Unit Testing [Work in Progress]

You have heard good stuff about unit testing and finally want to learn about it? This is a place for you! 

Join me and I will guide you through basic theory about unit testing. I assume that you are a developer that knows how to  create Android applications but do not have any experience with automatic testing.

## What is a Unit Test

So let’s start with very basic thing. What actually Unit Test is?

Unit Test is a piece of code that is not a part of your application. It can create and call all of your application public classes and methods. But why would you like to write code that won't be a part of your application? Simply because you want to verify if application code works as you expect. And you want to verify it over and over again to be sure that you do not break any existing functionalities. And you are probably lazy like me and do not want to do it manualy! So you can write test code that will verify application behavior for you. Unit Tests for the rescue!

But wait a second? You probably heard about UI and Integration Tests which does exactly the same thing. What differentiate Unit Tests from the other tests? 

Unit Test is focused on testing only set of classes (one or more) that are focused on single functionality (domain) and do not depend on libraries or framework code. You do not want to test libraries that you are using (at least not in an Unit Test) they should just work! You want to focus only on your precious code and prove yourself that there are no hidden bugs.

### Simple Android application

Before creating any test I want to introduce you to an simple Android app that provides a login screen. It has two inputs for login and password, and it validates those inputs. When the inputs are valid we can sign in with correct data or get an error that credentials are incorrect. I have chosen MVP architecture, which will help us to write very fast test that do not depends on Android framework. If you are not familiar with this architecture then please read [this] article.

[Screen - without input]
[Screen - with invalid input]

## Project setup

Now when we have an application to be tested we can create our first test. We will use Junit4 test runner and Kotlin programming language. Test runner is a library that runs our test code and aggregate results in a friendly way. I won't go into deatils how setup Android project with tests written in Kotlin, because there is a great [article] that describe it in details.

## My first test

To create our first test we have to create class with public method annotated with `@org.junit.Test` and in folder `/src/test/kotlin`. This way we tell JUnit4 where is test code located. We can start with checking if our app allows to login with correct data. We will instrument LoginPresenter, to do that I have created LoginPresenterTest class with test method. At the beggining we want to test if it is possible to sign in with correct credentials, so I have created test method with name `login with correct data`.

```
class LoginPresenterTest {

    @Test
    fun `login with correct data`() {
    
    }
}
```

In Kotlin we can name test with natural names like `login with correct data` but it only applies to code which is run on JVM. Thankfully unit tests are run on JVM and we can use such a descriptive names. 

## Test structure

Each test should be created from following blocks:

- Arrange/Given - in which we will prepare all needed data that are required to perform test
- Act/When - in which we will call single method on tested object
- Assert/Then - in which we will check result of the test, either pass or fail

JUnit4 do not separate test blocks in any way so it is convenient to add comments to a test code. Especially if you are just beginning your journey with tests.

```
    @Test
    fun `login with correct data`() {
      //given
      
      //when
      
      //then
    
    }
```





#### Given Block

#### When Block

#### Then Block

## Testing state

## Testing interactions



