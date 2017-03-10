# Introduction to Unit Testing with Kotlin

Have you ever heard good stuff about unit testing and finally you want to learn about it? This is a place for you! 

Join me and I will guide you through basic theory of unit testing. I assume that you are a developer who knows how to  create Android applications but does not have any experience with automatic testing.

## What is a Unit Test

So let’s start with very basic things. What is a Unit Test actually?

Unit Test is a piece of code that is not a part of your application. It can create and call all of your application’s public classes and methods. But why would you like to write a code that won't be a part of your application? Simply because you want to verify whether application code works as you expect. And you want to verify it over and over again to be sure that you do not break any existing functionalities. And you are probably lazy like me and do not want to do it manually! So you can write test code that will verify application behavior for you. Unit Tests for the rescue!

But wait a second! You probably heard about UI and Integration Tests which do exactly the same thing. What differentiates Unit Tests from the other tests? 

Unit Test is focused on testing only set of classes (one or more) that fulfils single functionality (domain) and do not depend on libraries or framework code. You do not want to test libraries that you are using (at least not in an Unit Test), they should just work! You want to focus only on your precious code and prove yourself that there are no hidden bugs.

If you want to read more about test types you can read [this article] (https://testing.googleblog.com/2015/04/just-say-no-to-more-end-to-end-tests.html), but now let’s move on.

### Simple Android application

Before creating any test I want to introduce you to a simple Android app that provides a login screen. It has two inputs for login and password, and it validates those inputs. When the inputs are valid we can sign in with correct data or get an error that credentials are incorrect. I have chosen MVP architecture, which will help us to write tests that do not depend on Android framework. If you are not familiar with this architecture then please read [this article](http://macoscope.com/blog/model-view-presenter-architecture-in-android-applications/).

<p align="center">
  <img src="/assets/login_validation.png" alt="Sample application with validation error" width="33%" height="33%"/>
</p>

## Project setup

Now when we have an application to be tested we can create our first test. We will use [JUnit4](https://github.com/junit-team/junit4/wiki/getting-started) test runner and Kotlin programming language. Test runner is a library that runs our test code and aggregates results in a friendly way. I won't go into details how to setup Android project with tests written in Kotlin, because there is a great [article](http://fernandocejas.com/2017/02/03/android-testing-with-kotlin/) that describes it in details. You can also check [Android-Testing-In-Kotlin project](/app/build.gradle) setup.

## Your first test

To create our first test we have to create a class with public method annotated with `@org.junit.Test` in `/src/test/kotlin` folder. This way we tell JUnit4 where the test code is located. We can start with checking whether our app allows us to login with correct data. We want to instrument [LoginRepository](/app/src/main/java/com/example/unittesting/entity/login/LoginRepository.java), to do that I have to create [LoginRepositoryTest](/app/src/test/kotlin/com/example/unittesting/entity/login/LoginRepositoryTest.kt) class with test method. At the beginning we want to test if it is possible to sign in with correct credentials, so I have created test method with name `login with correct login and password`.

``` kotlin
class LoginRepositoryTest {

    @Test
    fun `login with correct login and password`() {
    
    }
}
```
In Kotlin we can name test with natural names like `login with correct login and password` but it only applies to code which is ran on JVM. Thankfully unit tests are ran on JVM and we can use such descriptive names.

> Tip: To suppress error `Identifier not allowed for Android project...` displayed by Android Studio you have to got to `Preferences... -> Editor -> Inspections -> Kotlin` and disable `Illegal Android Identifier` inspection.

## Test structure

Each test should be created from the following blocks:

- Arrange/Given - in which we will prepare all needed data required to perform test
- Act/When - in which we will call single method on tested object
- Assert/Then - in which we will check result of the test, either pass or fail

JUnit4 does not separate test blocks in any way, so it is convenient to add comments to a test code. Especially, if you are just beginning your journey with tests.

``` kotlin
@Test
fun `login with correct login and password`() {
    //given

    //when

    //then
    
}
```
## Given Block

Our test begins with `given` block in which we will prepare our test data and create tested object. 

I am [creating instance](https://kotlinlang.org/docs/reference/classes.html#creating-instances-of-classes) of tested object `LoginRepository` and assign it to [read-only property](https://kotlinlang.org/docs/reference/properties.html#declaring-properties). It is very convenient to distinguish tested object from test parameters, so I am calling it `objectUnderTest`. You can also name it: `sut`, `subject` or `target`. Choose the name which fits you best just be consistent across your project.

When we have instance of tested object, then we can move on to test parameters. That will be `correctLogin` with value `'dbacinski'` and `correctPassword` with value `'correct'`. It is very important to choose meaningful names for each test parameter, it must be clear what kind of values each of them contain.

``` kotlin
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


``` kotlin
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

It is time to verify if tested object return value that we expect. But first we have to store result of tested method in a property `val result` and then examine it in the `then` block. Now we can do an assertion which checks if result value is the value that we expect. It will throw an error when assertion won’t be satisfied and test will fail.

In this case returned object is RxJava 2 Observable but we can convert it easily to `TestObserver` which is a class that provides assertion methods. I am checking if result value is `true` otherwise test will fail. 

Testing RxJava Observables is a topic for a separate article and I won’t go into more details here.

``` kotlin
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

We can run a test by pressing `Ctrl + Shift + F10` in Android Studio/IntelliJ or from a Terminal using command `./gradlew test`.

After running test that we have just written, you should get a green bar in IDE or `BUILD SUCCESSFUL` output in the Terminal.

<p align="center">
  <img src="/assets/ide_success.png" alt="Passed test in IDE"/>
</p>

## Test failure

When the assertion from `then` block won't be satisfied then test will fail with the following output:

<p align="center">
  <img src="/assets/ide_failure.png" alt="Passed test in IDE"/>
</p>

We have an information that expected value should be `true` but actual value returned by tested object was `false`.

``` java
java.lang.AssertionError: 
Values at position 0 differ; Expected: true (class: Boolean),
Actual: false (class: Boolean) (latch = 0, values = 1, errors = 0, completions = 1)
```

We also can see that failed test has the name `login with correct login and password` and is in the class `LoginRepositoryTest`. Assertion has failed at line `20` in file `LoginRepositoryTest.kt`. Thanks to such an informative error message, we can figure out exactly which assertion was not satisfied and fix the tested object.

``` java
at com.example.unittesting.entity.login.LoginRepositoryTest
.login with correct login and password(LoginRepositoryTest.kt:20)
```

## Conclusion

At this point you are ready to write your first very basic unit test, run it and examine what went wrong when it failed. Stay tuned for next more advanced topics. If you have found some errors feel free to create a Pull Request. You can also propose next testing related topic by creating an [Issue](https://github.com/dbacinski/Android-Testing-With-Kotlin/issues/new).

If you like my article, please don’t forget to [give a :star:](https://github.com/dbacinski/Android-Testing-With-Kotlin/).


#### Next: Unit Testing with Mockito 2 [Coming Soon]
