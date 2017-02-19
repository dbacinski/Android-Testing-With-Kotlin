# Introduction to Unit Testing with Kotlin

Have you ever heard good staff about unit testing and finally wanted to learn about it? This is a place for you! 

Join me and I will guide you through basic theory about unit testing. I assume that you are a developer that knows how to  create Android applications but do not have any experience with automatic testing.

## What is a Unit Test

So let’s start with very basic things. What is a Unit Test actually?

Unit Test is a piece of code that is not a part of your application. It can create and call all of your application’s public classes and methods. But why would you like to write a code that won't be a part of your application? Simply because you want to verify if application code works as you expect. And you want to verify it over and over again to be sure that you do not break any existing functionalities. And you are probably lazy like me and do not want to do it manualy! So you can write test code that will verify application behavior for you. Unit Tests for the rescue!

But wait a second! You probably heard about UI and Integration Tests which do exactly the same thing. What differentiates Unit Tests from the other tests? 

Unit Test is focused on testing only set of classes (one or more) that fulfils single functionality (domain) and do not depend on libraries or framework code. You do not want to test libraries that you are using (at least not in an Unit Test), they should just work! You want to focus only on your precious code and prove yourself that there are no hidden bugs.

### Simple Android application

Before creating any test I want to introduce you to an simple Android app that provides a login screen. It has two inputs for login and password, and it validates those inputs. When the inputs are valid we can sign in with correct data or get an error that credentials are incorrect. I have chosen MVP architecture, which will help us to write very fast test that deos not depend on Android framework. If you are not familiar with this architecture then please read[this article](http://macoscope.com/blog/model-view-presenter-architecture-in-android-applications/).

[Screen - without input]
[Screen - with invalid input]

## Project setup

Now when we have an application to be tested we can create our first test. We will use [JUnit4](https://github.com/junit-team/junit4/wiki/getting-started) test runner and Kotlin programming language. Test runner is a library that runs our test code and aggregates results in a friendly way. I won't go into details how to setup Android project with tests written in Kotlin, because there is a great [article](http://fernandocejas.com/2017/02/03/android-testing-with-kotlin/) that describes it in details. You can also check [Android-Testing-In-Kotlin project](/app/build.gradle) setup.


Now when we have an application to be tested then we can create our first test. We will use [Junit4](https://github.com/junit-team/junit4/wiki/getting-started) test runner and Kotlin programming language. Test runner is a library that runs our test code and aggregate results in a friendly way. I won't go into deatils how setup Android project with tests written in Kotlin, because there is a great [article](http://fernandocejas.com/2017/02/03/android-testing-with-kotlin/) that describe it in details. You can also check [Android-Testing-In-Kotlin project](/app/build.gradle) setup.

## My first test

To create our first test we have to create class with public method annotated with `@org.junit.Test` and in folder `/src/test/kotlin`. In this way we tell JUnit4 where the test code is located. We can start with checking if our app allows to login with correct data. We will instrument [LoginRepository](app/src/main/java/com/example/unittesting/entity/login/LoginRepository.java), to do that I have created [LoginRepositoryTest](/app/src/test/kotlin/com/example/unittesting/entity/login/LoginRepositoryTest.kt) class with test method. At the beggining we want to test if it is possible to sign in with correct credentials, so I have created test method with name `login with correct login and password`.

```
class LoginRepositoryTest {

    @Test
    fun `login with correct login and password`() {
    
    }
}
```
In Kotlin we can name test with natural names like `login with correct login and password` but it only applies to code which is run on JVM. Thankfully unit tests are run on JVM and we can use such descriptive names. 

## Test structure

Each test should be created from the following blocks:

- Arrange/Given - in which we will prepare all needed data that are required to perform test
- Act/When - in which we will call single method on tested object
- Assert/Then - in which we will check result of the test, either pass or fail

JUnit4 does not separate test blocks in any way so it is convenient to add comments to a test code. Especially if you are just beginning your journey with tests.

```
    @Test
    fun `login with correct login and password`() {
      //given
      
      //when
      
      //then
    
    }
```
## Given Block

Out test begins with `given` block in which we will prepare our test data and create tested object. 

I am [creating instance](https://kotlinlang.org/docs/reference/classes.html#creating-instances-of-classes) of tested object `LoginRepository` and assign it to [read-only property](https://kotlinlang.org/docs/reference/properties.html#declaring-properties). It is very convenient to distinguish tested object from test parameters so I am calling it `objectUnderTest`. You can also name it: `sut`, `subject` or `target`. Choose the name which fits you best just be consistent across your project.

When we have instance of tested object then we can move on to test parameters. That will be `correctLogin` with value `'dbacinski'` and `correctPassword` with value `'correct'`. It is very important to choose meaningful names for each test parameter, it must be clear what kind of values each of them contains.

```
    @Test
    fun `login with correct login and password`() {
        //given
        val objectUnderTest = LoginRepository()
        val correctLogin = 'dbacinski'
        val correctPassword = `correct`
        //when

        //then
        
    }
```

Now `given` block is finished and we can move on.

## When Block

In `when` block we have to call method that we want to test with parameters that were prepared in the `given` block. So I call method `objectUnderTest.login(correctLogin, correctPassword)`. In `when` block we should have only one line of code to make it clear what is actually being tested. 


```
    @Test
    fun `login with correct login and password`() {
        //given
        val objectUnderTest = LoginRepository()
        val correctLogin = 'dbacinski'
        val correctPassword = `correct`
        //when
        objectUnderTest.login(correctLogin, correctPassword)
        //then
        
    }
```

## Then Block

Now it is time to verify if tested objects return value that we expect. But first we have to store result of tested method in property `val result` and then examine it in the `then` block. To check expected value we have to do an assertion. In this case returned object is RxJava 2 Observable but we can convert it easily to `TestObserver` which is a class that provides assertion methods. I am checking if returned value is `true` otherwise test will fail.

```
    @Test
    fun `login with correct login and password`() {
        //given
        val objectUnderTest = LoginRepository()
        val correctLogin = 'dbacinski'
        val correctPassword = `correct`
        //when
        val result = objectUnderTest.login(login, password)
        //then
        result.test().assertResult(true)
    }
```
## Running test

We can run a test by pressing `Ctrl + Shift + F10` in Android Studio/IntelliJ or from Terminal using command `./gradlew test`.

## Test failure

When the assertion from `then` block won't be satisfied then test will fail with the following output:

```
java.lang.AssertionError: Values at position 0 differ; Expected: true (class: Boolean), Actual: false (class: Boolean) (latch = 0, values = 1, errors = 0, completions = 1)

	at io.reactivex.observers.BaseTestConsumer.fail(BaseTestConsumer.java:133)
	at io.reactivex.observers.BaseTestConsumer.assertValues(BaseTestConsumer.java:411)
	at io.reactivex.observers.BaseTestConsumer.assertResult(BaseTestConsumer.java:613)
	at com.example.unittesting.entity.login.LoginRepositoryTest.login with correct login and password(LoginRepositoryTest.kt:20)
	...

Process finished with exit code 255
```

We have an information that expected value should be `true (class: Boolean)` but actual value returned by tested object was `false (class: Boolean)`. We also can see that failed test has name `login with correct login and password` and is in the class `LoginRepositoryTest`. Assertion has failed at line `20` in file `LoginRepositoryTest.kt`. Thanks to such informative error message we can figure out exactly which assertion was not satisfied and fix tested object.

## Next: [Unit Testing with Mockito 2](/Unit-Testing-Mockito.md) 
