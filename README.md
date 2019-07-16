# CaixaEletronico

## Problema proposto:
 Criar um sistema que simule um caixa eletrônico que dispense notas conforme o valor solicitado.
 Deve disponibilizar o menor número de notas possíveis.

## Solução
 - Foi criada um sistema utilizando Java 8 + Maven + SpringBoot 2.1.6.RELEASE;
 - O sistema é baseado em microsserviço REST, e utiliza um banco de dados em memória(h2) para simular um caixa eletrônico com um número finito de notas;
 - A documentação swagger para simular chamadas poderá ser acessada em "{ip-host}:8082/api/caixa-eletronico/swagger-ui.html", quando a api estiver rodando;
 - Para testes unitários foi utilizado Mockito.

### Rodar a aplicação
 - Para build deve ser utilizado o Maven:
   - Com o maven devidamente configurado, executar:  'mvn clean package' na raiz do projeto;
 - Para rodar a aplicação, poderá ser utilizado o comando do java ou do maven:
   - java -jar '.jar da aplicação'
   - mvn spring-boot:run
 - Foi adicionado um script para inserir um caixa eletrônico com todas as notas (script em /src/main/resources/data.sql):
   - Para testes deve ser utilizado o id do caixa eletrônico que está neste script, ou, conforme o valor indicado no mesmo.