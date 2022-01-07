# redes-ep2-typerace
Repositório para o EP2 de Redes de Computadores, EACH-USP - 2021/2

# Integrantes
* Maria Eduarda Rodrigues Garcia - 1111111
* Gabriel Medeiros Jospin - 11796020

## Pré-requisitos
* JDK 11 ou maior (testado com a JDK11 OpenJDK)
* Gradle (incluso no repositório, não é necessário instalá-lo)
* ~~Baidu Antivirus~~

### Rodando
Antes de rodar o server pela primeira vez é necessário gerar o banco de palvras com:

Para rodar o servidor
```sh
./gradlew server:buildDB
```


Para rodar o servidor
```sh
./gradlew server:run
```

Para rodar um cliente
```sh
./gradlew client:run
```
