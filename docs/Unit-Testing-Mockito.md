# Unit Testing with Mockito 2 [Work in Progress]

#### [Previous: Introduction to Unit Testing with Kotlin](/docs/Unit-Testing-Introduction.md)

In the [previous article](/docs/Unit-Testing-Introduction.md) you have learned how to write a simple unit test. Now it is a time to dive deeper and learn about testing state and interactions with help of testing library Mockito 2.

## State vs Interactions

- State - verifying that the object under test returns the right result
- Interactions - verifying that object under test calls certain methods

## Test Doubles

Test doubles are objects that replaces dependencies of object under test. They help to test object in separation from real dependencies.  Most notable test doubles are:

- Stub - returns values configured in test to object under test
- Mock - informs test that object under test has called certain methods

<p align="center">
  <img src="/assets/stub_mock.png" alt="Stub and Mock"/>
</p>

If you like my article, please don’t forget to [give a :star:](https://github.com/dbacinski/Android-Testing-With-Kotlin/).
