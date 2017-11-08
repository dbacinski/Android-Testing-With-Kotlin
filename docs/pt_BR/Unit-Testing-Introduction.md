### Traduzido por:
- Lucas Soares Candalo (@candalo)

# Introdução à Testes Unitários com Kotlin

Você já ouviu boas coisas sobre testes unitários e deseja finalmente aprender sobre eles? Esse é o lugar pra você!

Junte-se à mim e eu irei guiá-lo através da teoria básica dos testes unitários. Eu assumo que você é um desenvolvedor que sabe como criar aplicativos Android mas não tem nenhuma experiência com testes unitários.

## O que é um Teste Unitário

Vamos começar com coisas bem básicas. O que é realmente um Teste Unitário?

Teste Unitário é um pedaço de código que não faz parte da sua aplicação. Ele pode criar e chamar todas as classes e métodos públicos da sua aplicação. Mas por que você iria querer escrever um código que não faça parte da sua aplicação? Apenas porque você deseja verificar se o código da aplicação funciona como o esperado. E você também sempre precisa confirmar isso para ter certeza que você não quebrou nenhuma funcionalidade existente. E você é provavelmente preguiçoso como eu e não deseja fazer isso manualmente! Então você pode escrever códigos de teste que irão verificar o comportamento da aplicação para você. Testes Unitários para o resgate!     

Mas espere um segundo! Você provavelmente já ouviu falar sobre Testes de Integração e Testes de UI que fazem exatamente a mesma coisa. O que diferencia Testes Unitários dos outros testes?

Teste Unitário é focado em testar apenas conjunto de classes (uma ou mais) que atende uma única funcionalidade (domínio) e não depende do código de bibliotecas ou frameworks. Você não deseja testar as bibliotecas que você está usando (pelo menos não no Teste Unitário), eles devem apenas funcionar! Você tem que se focar apenas no seu precioso código e provar para si mesmo que não existem bugs escondidos.

Se você deseja ler mais sobre os diferentes tipos de teste, você pode ler [esse artigo (em inglês)](https://testing.googleblog.com/2015/04/just-say-no-to-more-end-to-end-tests.html), mas agora vamos prosseguir.

### Uma aplicação Android simples
Antes de criar qualquer teste eu quero introduzir você à um aplicativo Android simples que fornece uma tela de login. Essa tela tem duas entradas para login e senha, e ela valida essas entradas. Quando as entradas são válidas nós podemos logar com os dados corretos ou receber um error indicando que as credenciais estão incorretas. Eu escolhi a arquitetura MVP, que irá nos ajudar a escrever testes que não dependam do framework do Android. Se você não é familiar com essa arquitetura, por favor leia [esse artigo (em inglês)](http://macoscope.com/blog/model-view-presenter-architecture-in-android-applications/).

<p align="center">
  <img src="/assets/login_validation.png" alt="Sample application with validation error" width="33%" height="33%"/>
</p>

## Setup do projeto

Agora que nós temos uma aplicação a ser testada nós podemos criar nosso primeiro teste. Nós iremos usar o [JUnit4](https://github.com/junit-team/junit4/wiki/getting-started) para executar os testes e a linguagem de programação Kotlin. O executor de testes é uma biblioteca que executa nosso código de teste e agrega os resultados de uma maneira amigável. Eu não irei entrar em detalhes sobre como realizar o setup de um projeto Android com testes escritos em Kotlin, porque já existe um excelente [artigo (em inglês)](http://fernandocejas.com/2017/02/03/android-testing-with-kotlin/) que descreve isso em detalhes. Você também pode ver o setup do projeto [Android-Testing-In-Kotlin](/app/build.gradle).

## Seu primeiro teste

Para criar nosso primeiro teste nós temos que criar uma classe com métodos públicos anotados com `@org.junit.Test` na pasta `/src/test/kotlin`. Desse modo, nós falamos para o JUnit4 onde os testes estão localizados. Nós podemos iniciar com a checagem se o nosso app nos permite logar com os dados corretos. Nós queremos instrumentar [LoginRepository](/app/src/main/kotlin/com/example/unittesting/login/presenter/LoginPresenter.kt), para fazer isso eu preciso criar a classe [LoginRepositoryTest](/app/src/test/kotlin/com/example/unittesting/login/presenter/LoginPresenterTest.kt) com um método de teste. No começo nós queremos testar se é posssível logar com as credenciais corretas, então eu criei um método de teste com o nome `login with correct login and password`.
``` kotlin
class LoginRepositoryTest {

    @Test
    fun `login with correct login and password`() {

    }
}
```
Em Kotlin nós podemos nomear os testes com nomes naturais como `login with correct login and password` mas isso somente se aplica para códigos que rodam na JVM. Testes unitários rodam apenas na JVM e nós podemos usar nomes descritivos.  

> Dica: Para suprimir o erro `Identifier not allowed for Android project...` exibido pelo Android Studio você deve ir até `Preferences... -> Editor -> Inspections -> Kotlin` e encontrar `Illegal Android Identifier` e então selecionar `Tests` em `In All Scopes` para desativar a checagem para os testes.

## Estrutura dos testes

Cada teste deve ser criado a partir dos blocos seguintes:

- Arrange/Given - onde nós preparamos todos os dados necessários para realizar o teste
- Act/When - onde nós iremos chamar um único método no objeto testado
- Assert/Then - onde nós iremos verificar o resultado do teste, se passou ou falhou

JUnit4 não separa os blocos de teste de nenhum modo, então é recomendada a adição de comentários no código de teste. Principalmente, se você está iniciando sua jornada com testes.

``` kotlin
@Test
fun `login with correct login and password`() {
    //given

    //when

    //then

}
```
## Bloco Given

Nosso teste começa com o bloco `given` no qual nós poderemos preparar os dados do nosso teste e criar o objeto testado.

Eu estou [criando a instância](https://kotlinlang.org/docs/reference/classes.html#creating-instances-of-classes) do objeto testado `LoginRepository` e atribuindo-o à uma [read-only property (propriedade somente de leitura)](https://kotlinlang.org/docs/reference/properties.html#declaring-properties). É muito conveniente distinguir objetos testados dos parâmetros do teste, assim eu irei chamá-lo de `objectUnderTest`. Você também pode nomeá-los: `sut`, `subject` ou `target`. Escolha o nome que se encaixa melhor na sua situação, apenas seja consistente através do projeto.

Quando nós temos uma instância do objeto testado, então nós podemos seguir em frente para os parâmetros do teste. Desse modo, será `correctLogin` com o valor `'dbacinski'` e `correctPassword` com o valor `'correct'`. É muito importante escolher nomes significativos para cada parâmetro de teste, deve ser claro o tipo de valores que cada um deles contém.

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

Agora o bloco `given` está concluído e nós podemos prosseguir.

## Block When

No bloco `when` nós devemos chamar o método que nós queremos testar com os parâmetros preparados no bloco `given`. Então eu chamo o método `objectUnderTest.login(correctLogin, correctPassword)`. No bloco `when` nós devemos ter apenas uma linha de código para tornar claro o que realmente está sendo testado.


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

## Bloco Then

É hora de verificar se o objeto testado retorna o valor esperado. Mas primeiro nós temos que armazenar o resultado do método testado na propriedade `val result` e então examiná-lo no bloco `then`. Agora nós temos que fazer uma asserção que verifica se o valor do resultado é o valor que estamos esperando. Isso irá lançar um erro quando a asserção não for satisfeita e o teste irá falhar.
Nesse caso o objeto retornado é um Observable de RxJava 2 mas nós podemos convertê-lo facilmente para um `TestObserver` que é uma classe que fornece métodos de asserção. Eu estou checando se o valor do resultado é `true` de outro modo o resultado irá falhar.

Testes de Observables de RxJava é um tópico para um artigo separado e eu não irei entrar em mais detalhes aqui.
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
## Executando os testes

Nós podemos executar um teste pressionando `Ctrl + Shift + F10` no Android Studio/IntelliJ ou através de um Terminal usando o comando `./gradlew test`.

Após executar o teste que nós escrevemos, você deve receber uma barra verde na IDE ou a saída `BUILD SUCCESSFUL` no Terminal.
<p align="center">
  <img src="/assets/ide_success.png" alt="Passed test in IDE"/>
</p>

## Falha no teste

Quando a asserção do bloco `then` não for satisfeita então o teste irá falhar com a seguinte saída:

<p align="center">
  <img src="/assets/ide_failure.png" alt="Passed test in IDE"/>
</p>

Nós temos uma informação que o valor esperado deve ser `true` mas o valor atual retornado pelo objeto testado é `false`.

``` java
java.lang.AssertionError:
Values at position 0 differ; Expected: true (class: Boolean),
Actual: false (class: Boolean) (latch = 0, values = 1, errors = 0, completions = 1)
```

Nós também podemos ver que o teste que falhou tem o nome `login with correct login and password` e está na classe `LoginRepositoryTest`. A asserção falhou na linha `20` do arquivo `LoginRepositoryTest.kt`. Graças a uma mensagem de erro tão informativa, nós podemos ver exatamente qual asserção não foi satisfeita e corrigir o objeto testado.

``` java
at com.example.unittesting.entity.login.LoginRepositoryTest
.login with correct login and password(LoginRepositoryTest.kt:20)
```

## Conclusão

Nessa altura você já está pronto para escrever seu primeiro teste unitário bem básico, executá-lo e examinar o que ocorreu de errado quando ele falhou. Fique ligado para o próximos tópicos mais avançados. Se você encontrou alguns erros se sinta livre para criar um Pull Request. Você também pode propor o próximo tópico relacionado à testes criando uma [Issue](https://github.com/dbacinski/Android-Testing-With-Kotlin/issues/new).

Se você gostou do meu artigo, por favor não se esqueça de [dar uma :star:](https://github.com/dbacinski/Android-Testing-With-Kotlin/).

#### Próximo: [Testes Unitários com Mockito 2](/docs/Unit-Testing-Mockito.md)

