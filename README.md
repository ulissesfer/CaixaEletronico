# CaixaEletronico

## Problema proposto:
 Criar um sistema que simule um caixa eletr�nico que dispense notas conforme o valor solicitado.
 Deve disponibilizar o menor n�mero de notas poss�veis.

## Solu��o
 - Foi criada um sistema utilizando Java 8 + Maven + SpringBoot 2.1.6.RELEASE;
 - O sistema � baseado em microsservi�o REST, e utiliza um banco de dados em mem�ria(h2) para simular um caixa eletr�nico com um n�mero finito de notas;
 - A documenta��o swagger para simular chamadas poder� ser acessada em "{ip-host}:8082/api/caixa-eletronico/swagger-ui.html", quando a api estiver rodando;
 - Para testes unit�rios foi utilizado Mockito.

### Rodar a aplica��o
 - Para build deve ser utilizado o Maven:
   - Com o maven devidamente configurado, executar:  'mvn clean package' na raiz do projeto;
 - Para rodar a aplica��o, poder� ser utilizado o comando do java ou do maven:
   - java -jar '.jar da aplica��o'
   - mvn spring-boot:run
 - Foi adicionado um script para inserir um caixa eletr�nico com todas as notas (script em /src/main/resources/data.sql):
   - Para testes deve ser utilizado o id do caixa eletr�nico que est� neste script, ou, conforme o valor indicado no mesmo.