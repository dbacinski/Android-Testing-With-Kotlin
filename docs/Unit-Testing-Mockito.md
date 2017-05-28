# Unit Testing with Mockito 2 [Work in Progress]
 
#### [Previous: Introduction to Unit Testing with Kotlin](/docs/Unit-Testing-Introduction.md)
 
In the [previous article](/docs/Unit-Testing-Introduction.md) you have learned how to write a simple unit test. Now it is a time to dive deeper and learn about testing state and interactions with help of testing library Mockito 2.
 
At the beginning a bit of theory. There are two ways how you can verify if tested object works correctly. You can test object state or test how object interacts with it’s dependencies.
 
## Testing State
 
Testing State is verifying that the object under test returns the right results after calling given methods. In example when we want to test if [LoginValidator]() works correctly, then we can call method `validatePassword` and check if returned value is equal to what we expect. Assertion is done using [AssertJ](http://joel-costigliola.github.io/assertj/assertj-core-quick-start.html) library which provides fluent api for common `Java` objects.
 
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
While testing State we do not make any assumption on how object is implemented. We treat object as black box and check if it returns correct values for given input. This kind of test is most preferable because we can make changes to class implementation and test won’t fail until our expectations are still met. Thanks to that this, tests are more stable and least painful to maintain.
 
<p align="center">
  <img src="/assets/state.png" height="50%" width="50%" alt="State"/>
</p>
 
## Testing Interactions 
 
Unfortunately oftentimes object do not expose almost any state but it forwards processing to it’s dependencies. This is very common in MVP architecture in which Presenter do not return any values from methods but rather invokes callbacks on View object. Take a look at [LoginPresenter]() which exposes method `public void attemptLogin(LoginCredentials loginCredentials)`. There is no return type but when we will look at its implementation then we will see that this method  invokes method `onLoginSuccessful()` on [LoginView]() when login is successful. So the only way to check if login was successful is by checking if method `onLoginSuccess()` was actually called. 
```java
public class LoginPresenter {
 
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
}
```
 
We can test such case by introducing test double. Test doubles are objects that replaces dependencies of object under test. They help to test object in separation from real dependencies. To be able to test if LoginPresenter is calling `view.onLoginSuccessful()` method we have to introduce test double called Mock object.
 
### Mock 
 
Mock object provides a method to check if object under test has called certain methods. It is possible to implement Mock object by ourself but it does not make much sense because there is a great library called [Mockito](mockito.org) which will do it for us. 
 
<p align="center">
  <img src="/assets/mock.png" alt="Stub and Mock"/>
</p>
 
First we have to create a mock object for our dependency LoginView. It can be done by using `loginViewMock = Mockito.mock(LoginView::class.java)`. We have to pass it to tested object instead of a real dependency. In this case we are passing mock view by calling `createView(loginViewMock)`.
 
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
 
Now we are able to test how `LoginPresenter` interacts with `LoginView`. 
 
`LoginPresenter` is also calling `LoginUseCase which is then calling `LoginRepository`. The problem is that in the real application this would involve network communication and we do not want to depend on that in the Unit Tests. This is where we can use next test double called Stub.
 
### Stub
 
Stub is a test double which returns configured values and it is passed to object under test. When a given method is invoked on stub then it returns values that were previously set in test. This gives us full control over returned values from methods that were invoke on the dependency.
<p align="center">
  <img src="/assets/stub.png" alt="Stub and Mock"/>
</p>
 
Let’s look at LoginUseCase. It invokes `login` method on `LoginRepository` with `login` and `password` parameters. As a result we are getting ` Observable<Boolean>` with an information if login was successful or not. In unit test we would like to have control over returned value from `login` method. To do that we have to replace real `LoginRepository` with it’s stubbed version. This is where Mockito can help us again, with automatically generated stub object.
 
```java
public class LoginUseCase {

    LoginRepository loginRepository;

    public LoginUseCase(LoginRepository loginRepository) {
        this.loginRepository = loginRepository;
    }

    public Observable<Boolean> loginWithCredentialsWithStatus(final LoginCredentials credentials) {
        checkNotNull(credentials);
        return loginRepository.login(credentials.login, credentials.password);
    }
 
[...]
}
```
In order to create stubbed object with Mockito we have to invoke Mockito.mock() method. 
 
What? You said mock()? Yes, the name of the method is a bit misleading, because Mockito is creating objects that can be both Stubs and Mocks using single method called `mock()`. This probably why many people confused Stubs and Mocks, especially the one who didn’t read anything about testing and has started using Mockito. You can read more about diffrence between mocks and stubs in this [article](https://martinfowler.com/articles/mocksArentStubs.html) by Martin Fowler.
 
Getting back to the topic. We would like to force `loginRepositorStub` stub to always return `true` for any input values. To do that we have at least two options. We can use `when` syntax and do it like this `Mockito.`when`(loginRepositoryStub.login(any(), any())).thenReturn(Observable.just(true))`. The problem is that `when` is a keyword in Kotlin and it doesn’t look good. Fortunately Mockito also supports [BDD](https://en.wikipedia.org/wiki/Behavior-driven_development) syntax and we can achive the same result using `given(loginRepositoryStub.login(any(), any())).willReturn(Observable.just(true))`.
 
 
```kotlin
val loginRepositoryStub = mock(LoginRepository::class.java)
val loginViewMock: LoginView = mock(LoginView::class.java)
 
val objectUnderTest = LoginPresenter(LoginUseCase(loginRepositoryStub))
 
@Test
fun `login with correct data`() {
    //given
    objectUnderTest.createView(loginViewMock)
    given(loginRepositoryStub.login(any(), any())).willReturn(Observable.just(true))
    //when    objectUnderTest.attemptLogin(LoginCredentials().withLogin("correct").withPassword("correct"))
    //then
    verify(loginViewMock).onLoginSuccessful()
}
```
*Work in progress*
 
If you want to learn more about test doubles there is a [great article](http://pragmatists.pl/blog/2017/03/test-doubles-fakes-mocks-or-stubs/) by [Pragmatist](https://twitter.com/pragmatists) about it. 
 
 
 
If you like my article, please don’t forget to [give a :star:](https://github.com/dbacinski/Android-Testing-With-Kotlin/).
