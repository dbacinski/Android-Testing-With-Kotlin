# Unit Testing with Mockito 2

#### Previous: [Introduction to Unit Testing with Kotlin](/docs/Unit-Testing-Introduction.md)

In the [previous article](/docs/Unit-Testing-Introduction.md) you have learned how to write a simple unit test. Now it is a time to dive deeper and learn about testing state and interactions with help of testing library Mockito 2.

At the beginning a bit of theory. There are two ways how you can verify if tested object is working correctly. You can test object state or test how object interacts with it’s dependencies.

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
While testing State we do not make any assumption on how object is implemented. We treat object as black box and check if it returns correct values for a given input. This kind of test is most preferable because we can make changes to class implementation and test won’t fail until our expectations are still met. Thanks to that this, tests are more stable and less painful to maintain.

<p align="center">
  <img src="/assets/state.png" height="50%" width="50%" alt="State"/>
</p>

## Testing Interactions 

Unfortunately oftentimes object do not expose almost any state but it forwards processing to it’s dependencies. This is very common case in MVP architecture in which Presenter do not return any values from its methods but rather invokes callbacks on View interface. Take a look at [LoginPresenter]() which exposes method `public void attemptLogin(LoginCredentials loginCredentials)`. There is no return type but when we will look at its implementation then we will see that method `onLoginSuccessful()` from [LoginView]() is invoked when login is successful. So the only way to check if login was successful is to check if method `onLoginSuccess()` was actually called. 
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

We can test such case by introducing test double. Test doubles are objects that replaces dependencies of object under test. They help to test object in separation from real dependencies. If you want to learn more about test doubles there is a [great article](http://pragmatists.pl/blog/2017/03/test-doubles-fakes-mocks-or-stubs/) by [Pragmatist](https://twitter.com/pragmatists) about it. 

To test if LoginPresenter calls `view.onLoginSuccessful()` method we have to introduce test double called Mock object.

### Mock 

Mock object provides a way to check if object under test has called certain methods. It is possible to implement Mock object by ourself but it does not make much sense because there is a great library called [Mockito](mockito.org) which will do it for us. 

<p align="center">
  <img src="/assets/mock.png" alt="Mock"/>
</p>

First we have to create a mock object from our dependency LoginView. It can be done by using `loginViewMock = Mockito.mock(LoginView::class.java)`. Now we have to pass it to tested object instead of a real dependency. In this case we are passing mock view by calling `createView(loginViewMock)` method.

```kotlin
val loginViewMock: LoginView = mock(LoginView::class.java)
val objectUnderTest = LoginPresenter()


@Test
fun `login with correct data`() {
    //given
    objectUnderTest.createView(loginViewMock)
}
```
Next we have to call tested method `attemptLogin` and we can finally verify if `onLoginSuccessful()` was called. To do that we have to use Mockito utility called `verify`. We have to use syntax `Mockito.verify(loginViewMock).onLoginSuccessful()` to verify if `onLoginSuccessful()` was called on mock object exactly once.

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

`LoginPresenter` is also calling `LoginUseCase which is then calling `LoginRepository`. The problem is that in the real application this would cause network communication and we do not want to do it in the Unit Tests. This is where we can use next test double called Stub.

### Stub

Stub is a test double which returns configured values. When a given method is invoked on stub then it returns values that were predefined in test. This gives us full control over returned values from methods that were invoke on the dependency.
<p align="center">
  <img src="/assets/stub_mock.png" alt="Stub and Mock"/>
</p>

Let’s look at LoginUseCase. It invokes `login` method on `LoginRepository` with two parameters. As a result we are getting ` Observable<Boolean>` that contains an information if login was successful or not. In unit test we would like to have control over returned value from `login` method. To do that we have to replace real `LoginRepository` with it’s stubbed version. This is where Mockito can help us again and generate object stub.

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

What? You said mock()? Yes, the name of the method is a bit misleading, because Mockito is creating objects that can be both Stubs and Mocks using single method called `mock()`. This probably why many people confused Stubs and Mocks. You can read more about diffrences between mocks and stubs in this [article](https://martinfowler.com/articles/mocksArentStubs.html) by Martin Fowler.

Getting back to the topic. We would like to force `loginRepositorStub` stub to always return `true` for any input values. To do that we have at least two options. We can use `when` syntax and do it like this `Mockito.`when`(loginRepositoryStub.login(any(), any())).thenReturn(Observable.just(true))`. The problem is that `when` is a keyword in Kotlin and it doesn’t look good. Fortunately Mockito also supports [BDD](https://en.wikipedia.org/wiki/Behavior-driven_development) syntax and we can achive the same result using `given(loginRepositoryStub.login(any(), any())).willReturn(Observable.just(true))`. Now for any credentials passed to login method we always get Observable with true value.

```kotlin
val loginRepositoryStub = mock(LoginRepository::class.java)
val loginViewMock: LoginView = mock(LoginView::class.java)

val objectUnderTest = LoginPresenter(LoginUseCase(loginRepositoryStub))

@Test
fun `login with correct data`() {
    //given
    objectUnderTest.createView(loginViewMock)
    given(loginRepositoryStub.login(any(), any())).willReturn(Observable.just(true))
    //when
    objectUnderTest.attemptLogin(LoginCredentials().withLogin("correct").withPassword("correct"))
    //then
    verify(loginViewMock).onLoginSuccessful()
}
```
Thanks to `loginRepositoryStub` we have full control over external dependency and we can verify how our object works when login is successful (or not). We also do not have to rely on any unpredictable and slow source like backend API. This makes our test rock solid and lightning fast. Stubs are also very useful while testing state so do not be afraid to mix and match testing state and interactions.

## Conclusion

In this article you have learned how to use Mockito and AssertJ to test your objects. Always try to favor testing state over interactions, because it makes your tests much more stable and less sensitive to refactorings. Make use of Stubs to control your dependencies  and speed up your tests. Try to avoid Mocks where it is possible to test the same thing by checking object state and use AssertJ to make your assertions more readable. Stay tuned for next more advanced topics. If you have found some errors feel free to create a Pull Request. You can also propose next testing related topic by creating an [Issue](https://github.com/dbacinski/Android-Testing-With-Kotlin/issues/new).


If you like my article, please don’t forget to [give a :star:](https://github.com/dbacinski/Android-Testing-With-Kotlin/).
