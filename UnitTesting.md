# Introduction to Unit Testing [Work in Progress]

You have heard good stuff about unit testing and finnaly want to learn about it? This is a place for you! 

Join me and I will guide you through basic teory about unit testing. I assume that you are a developer that knows how to write application code but do not have any expirience with automatic testing.

## What is a Unit Test

So let’s start with very basic thing. What actually Unit Test is?

Unit Test is a piece of code that is not a part of your application. It is able to create and call all of your application public classes and methods. But why would you like to write code that won't be a part of your application? Simply because you want to verify if application code works as you expect. And you want to verify it over and over again to be sure that you do not break any existing functionalities. And you are probably lazy like me and do not want to do it manualy! So you can write test code that will verify application behavior for you. Unit Test for the rescue!

But wait a second? You probably heard about UI and Integration Tests which does exactly the same thing. What differentiate Unit Tests from other tests? 

Unit Test is focused on testing only set of classes (one or more) that are focused on single functionality (domain) and do not depend on code that you do not own (library or framework code). You do not want to test libraries that you are using (at least not in an Unit Test) they should just work! You want to focus only on your precious code and prove yourself that there are no hidden bugs.

## My first test

## Good name for a Test

## Tested object

## Test structure

#### Given Block

#### When Block

#### Then Block

## Testing state

## Testing interactions



