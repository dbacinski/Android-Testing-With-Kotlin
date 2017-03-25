# Unit Testing with Mockito 2 [Work in Progress]

#### [Previous: Introduction to Unit Testing with Kotlin](/docs/Unit-Testing-Introduction.md)

In the [previous article](/docs/Unit-Testing-Introduction.md) you have learned how to write a simple unit test. Now it is a time to dive deeper and learn about testing state and interactions with help of testing library Mockito 2.

At the beginning a bit of theory. There are two ways how you can verify if tested object works correctly. You can test object state or test how object interacts with its dependencies.

## Testing State

Testing State is verifying that the object under test returns the right results when calling certain methods. In example when we want to test if [LoginValidator]() works correctly, then we can call method `validatePassword` and check if returned value is equal to what we have expected.

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
//TODO AssertJ

While testing State we do not make any assumption on how object is implemented. We treat object as black box and check if it returns correct values for given input. This kind of test is most preferable because we can change class implementation and test won’t fail until our expectations are still met. Thanks to that this kind of test are least painful to maintain.

## Testing Interactions 

Unfortunately oftentimes object do not expose almost any state but it forward processing to its dependencies. This is very common in MVP architecture in which Presenter do not return values from methods but invokes callbacks on View object. Take a look at [LoginPresenter]() which exposes method `public void attemptLogin(LoginCredentials loginCredentials)`. There is no return type but when we will look at its implementation ten we will see that this method  invokes method `onLoginSuccessful()` on [LoginView]() when login is successful.

```java
public void attemptLogin(LoginCredentials loginCredentials) {
    loginUseCase.loginWithCredentialsWithStatus(loginCredentials)
                .subscribe(success -> {
                        if (success) {
                            getView().onLoginSuccessful();
                        } 
[...]
                    });
}
```


verifying that object under test calls certain methods

Test doubles are objects that replaces dependencies of object under test. They help to test object in separation from real dependencies.  Most notable test doubles are:

- Stub - returns values configured in test to object under test
- Mock - informs test that object under test has called certain methods

<p align="center">
  <img src="/assets/stub_mock.png" alt="Stub and Mock"/>
</p>

If you like my article, please don’t forget to [give a :star:](https://github.com/dbacinski/Android-Testing-With-Kotlin/).
