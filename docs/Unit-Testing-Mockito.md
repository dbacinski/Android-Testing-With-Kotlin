# Unit Testing with Mockito 2 [Work in Progress]

#### [Previous: Introduction to Unit Testing with Kotlin](/docs/Unit-Testing-Introduction.md)

In the [previous article](/docs/Unit-Testing-Introduction.md) you have learned how to write a simple unit test. Now it is a time to dive deeper and learn about testing state and interactions with help of testing library Mockito 2.

At the beginning a bit of theory. There are two ways how you can verify if tested object works correctly. You can test object state or test how object interacts with its dependencies.

## Testing State

Testing State is verifying that the object under test returns the right results after calling certain methods. In example when we want to test if [LoginValidator]() works correctly, then we can call method `validatePassword` and check if returned value is equal to what we have expected. Assertion is done using [AssertJ](http://joel-costigliola.github.io/assertj/assertj-core-quick-start.html) library which provides fluent api for common objects.

```kotlin
@Test
fun `empty password is invalid`() {
    //given
    val objectUnderTest = LoginValidator()
    //when
    val result = objectUnderTest.validatePassword("")
    //then
    assertThat(result).isFalse()
}
```
While testing State we do not make any assumption on how object is implemented. We treat object as black box and check if it returns correct values for given input. This kind of test is most preferable because we can change class implementation and test won’t fail until our expectations are still met. Thanks to that this kind of test are least painful to maintain.

<p align="center">
  <img src="/assets/state.png" height="50%" width="50%" alt="State"/>
</p>

## Testing Interactions 

Unfortunately oftentimes object do not expose almost any state but it forward processing to its dependencies. This is very common in MVP architecture in which Presenter do not return values from methods but invokes callbacks on View object. Take a look at [LoginPresenter]() which exposes method `public void attemptLogin(LoginCredentials loginCredentials)`. There is no return type but when we will look at its implementation then we will see that this method  invokes method `onLoginSuccessful()` on [LoginView]() when login is successful. So the only way to check if login was successful is by checking if method `onLoginSuccess()` was actually called. 
```java
private LoginView view

public void createView(T view) {
    this.view = view;
}

public void attemptLogin(LoginCredentials loginCredentials) {
    loginUseCase.loginWithCredentialsWithStatus(loginCredentials)
                .subscribe(success -> {
                        if (success) {
                            view.onLoginSuccessful();
                        } 
[...]
                    });
}
```

We can test such case by introducing test double. Test doubles are objects that replaces dependencies of object under test. They help to test object in separation from real dependencies. To be able to test if LoginPresenter is calling `onLoginSuccessful()` method we have to introduce test double called Mock object.

### Mock 

Mock object provides a method to check if object under test has called certain methods. It is possible to implement Mock object by ourself but it do not make much sense because there is a great library called [Mockito](mockito.org) which will do it for us. 

<p align="center">
  <img src="/assets/mock.png" alt="Stub and Mock"/>
</p>

First we have to create a mock object for our dependency LoginView. It can be done by using `Mockito.mock(LoginView::class.java)`. Such instance we have to pass to tested object instead of a real dependency. In this case we are passing mock view by calling `createView(loginViewMock)`.

```kotlin
val loginViewMock: LoginView = mock(LoginView::class.java)
val objectUnderTest = LoginPresenter()


@Test
fun `login with correct data`() {
    //given
    objectUnderTest.createView(loginViewMock)
}
```
Next we have to call tested method which is `attemptLogin` and we can finally verify if `onLoginSuccessful()` was called. To do that we have to use Mockito method called `verify`. To verify if `onLoginSuccessful()` was called on mock object we have to use such syntax `Mockito.verify(loginViewMock).onLoginSuccessful()`

```kotlin
val loginViewMock: LoginView = Mockito.mock(LoginView::class.java)
val objectUnderTest = LoginPresenter()


@Test
fun `login with correct data`() {
    //given
    val correctLogin = "dbacinski"
    val correctPassword = "correct"
    objectUnderTest.createView(loginViewMock)
    //when
    objectUnderTest.attemptLogin(LoginCredentials()
        .withLogin(correctLogin)
        .withPassword(correctPassword))
    //then
    Mockito.verify(loginViewMock).onLoginSuccessful()
}
```
### Stub 
Stub returns values configured in test to object under test

If you want to learn more about test doubles there is a [great article](http://pragmatists.pl/blog/2017/03/test-doubles-fakes-mocks-or-stubs/) by [Pragmatist](https://twitter.com/pragmatists) about it. 

#### Test Double - Mock
Let's get back to the tested class  [LoginPresenter]().



If you like my article, please don’t forget to [give a :star:](https://github.com/dbacinski/Android-Testing-With-Kotlin/).
