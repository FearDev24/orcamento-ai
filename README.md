# Orçamento AI

API inteligente de orçamento financeiro desenvolvida com **Spring Boot** e **Spring AI**, com suporte a comandos em linguagem natural e entrada por áudio.

O projeto permite registrar receitas e despesas, consultar saldo e listar transações. Também possui um fluxo completo de voz: o usuário envia ou grava um áudio, o sistema transcreve a fala, interpreta a intenção com IA, executa a operação financeira e gera uma resposta em áudio.

## Funcionalidades

- Cadastro manual de receitas e despesas
- Consulta de saldo
- Listagem de transações
- Persistência local com H2
- Entrada de comandos por áudio
- Conversão automática de áudio com FFmpeg
- Transcrição local com Whisper.cpp
- Interpretação de linguagem natural com Spring AI
- Tool Calling para executar operações financeiras
- IA local com Ollama + Qwen
- Resposta em áudio com Piper
- Interface web simples para demonstração
- Funcionamento sem depender de APIs pagas

## Fluxo da aplicação

```text
Áudio do usuário
      ↓
FFmpeg
      ↓
Whisper.cpp
      ↓
Texto
      ↓
Spring AI + Ollama/Qwen
      ↓
Tool Calling
      ↓
Serviços da aplicação
      ↓
H2
      ↓
Resposta da IA
      ↓
Piper
      ↓
Áudio de resposta
```

## Tecnologias utilizadas

- Java 21
- Spring Boot
- Spring Web
- Spring Data JPA
- Spring AI
- Ollama
- Qwen 3 4B
- Whisper.cpp
- Piper TTS
- FFmpeg
- H2 Database
- HTML, CSS e JavaScript
- Maven

## Estrutura principal

```text
orcamento-ai
├── src
│   └── main
│       ├── java
│       │   └── com.orcamento.orcamento_ai
│       │       ├── ai
│       │       ├── controller
│       │       ├── exception
│       │       ├── model
│       │       └── repository
│       └── resources
│           ├── application.properties
│           └── static
│               └── index.html
├── data
├── tools
│   ├── ffmpeg
│   ├── piper
│   └── whisper
└── pom.xml
```

> As pastas `tools`, `data` e arquivos gerados localmente não precisam ser versionados no GitHub.

## Pré-requisitos

Antes de executar o projeto, instale:

- Java 21
- Maven ou Maven Wrapper
- Ollama
- FFmpeg
- Whisper.cpp
- Piper TTS

O projeto foi desenvolvido e testado no Windows.

## Configurando o Ollama

Instale o Ollama:

https://ollama.com/

Depois baixe o modelo utilizado pelo projeto:

```bash
ollama pull qwen3:4b
```

Confirme que o Ollama está em execução.

A configuração utilizada no projeto é:

```properties
spring.ai.model.chat=ollama
spring.ai.ollama.base-url=http://localhost:11434
spring.ai.ollama.chat.model=qwen3:4b
```

## Configurando o Whisper.cpp

Baixe uma versão compilada do Whisper.cpp para Windows.

Estrutura esperada:

```text
tools
└── whisper
    ├── whisper-cli.exe
    ├── arquivos DLL
    └── models
        └── ggml-base.bin
```

O modelo utilizado é:

```text
ggml-base.bin
```

O Whisper é responsável por transformar o áudio do usuário em texto.

## Configurando o FFmpeg

Estrutura esperada:

```text
tools
└── ffmpeg
    └── bin
        └── ffmpeg.exe
```

O FFmpeg converte os arquivos recebidos para WAV mono em 16 kHz antes da transcrição.

## Configurando o Piper

Estrutura esperada:

```text
tools
└── piper
    ├── piper.exe
    ├── arquivos DLL
    ├── espeak-ng-data
    └── voices
        ├── pt_BR-faber-medium.onnx
        └── pt_BR-faber-medium.onnx.json
```

A voz utilizada no projeto é:

```text
pt_BR-faber-medium
```

O Piper transforma a resposta textual da IA em áudio.

## Banco de dados

O projeto utiliza H2 com persistência em arquivo.

Configuração:

```properties
spring.datasource.url=jdbc:h2:file:./data/orcamento
spring.datasource.driver-class-name=org.h2.Driver
spring.datasource.username=sa
spring.datasource.password=

spring.jpa.hibernate.ddl-auto=update

spring.h2.console.enabled=true
spring.h2.console.path=/h2-console
```

Console H2:

```text
http://localhost:8080/h2-console
```

JDBC URL:

```text
jdbc:h2:file:./data/orcamento
```

Usuário:

```text
sa
```

Senha:

```text
deixe em branco
```

## Executando o projeto

Na pasta do projeto:

```bash
./mvnw spring-boot:run
```

No Windows PowerShell também é possível executar:

```powershell
.\mvnw spring-boot:run
```

Ou executar diretamente pelo IntelliJ IDEA.

Quando a aplicação iniciar, acesse:

```text
http://localhost:8080
```

## Interface web

A aplicação possui uma interface simples que permite:

- visualizar o saldo atual
- cadastrar uma transação manualmente
- visualizar as transações registradas
- gravar áudio pelo navegador
- enviar o áudio para processamento
- receber uma resposta em áudio

Exemplos de comandos de voz:

```text
Gastei 50 reais no mercado hoje.
```

```text
Gastei 120 reais de gasolina hoje.
```

```text
Recebi 3200 reais de salário hoje.
```

## Endpoints

### Criar transação

```http
POST /transacoes
```

Exemplo:

```json
{
  "descricao": "Almoco",
  "valor": 45.90,
  "tipo": "DESPESA",
  "categoria": "ALIMENTACAO",
  "data": "2026-10-05"
}
```

### Listar transações

```http
GET /transacoes
```

### Consultar saldo

```http
GET /transacoes/saldo
```

### Assistente de IA

```http
GET /assistente?mensagem=...
```

Exemplo:

```text
http://localhost:8080/assistente?mensagem=qual%20meu%20saldo
```

### Processar áudio

```http
POST /audio
```

O endpoint recebe um arquivo utilizando `multipart/form-data` no campo:

```text
arquivo
```

Exemplo com `curl`:

```powershell
curl.exe -X POST "http://localhost:8080/audio" `
-F "arquivo=@teste.ogg" `
--output "resposta-api.wav"
```

## Tool Calling

A IA não altera diretamente o banco de dados.

O Spring AI disponibiliza métodos da aplicação como ferramentas para o modelo. A classe `OrcamentoTools` contém operações como:

- consultar saldo
- registrar transação

Exemplo conceitual:

```java
@Tool(description = "Consulta o saldo atual do orçamento")
public BigDecimal consultarSaldo() {
    return transacaoService.calcularSaldo();
}
```

Com isso, o modelo identifica a intenção do usuário e chama uma função real da aplicação.

## Exemplo de funcionamento

Usuário fala:

```text
Gastei 50 reais no mercado hoje.
```

O sistema:

1. recebe o áudio
2. converte com FFmpeg
3. transcreve com Whisper.cpp
4. envia o texto ao modelo Qwen através do Spring AI
5. o modelo escolhe a ferramenta de registro
6. a aplicação grava a despesa no H2
7. a IA gera uma confirmação
8. o Piper transforma a confirmação em áudio
9. o endpoint devolve o arquivo WAV ao usuário

Resultado aproximado:

```json
{
  "descricao": "Mercado",
  "valor": 50.00,
  "tipo": "DESPESA",
  "categoria": "ALIMENTACAO",
  "data": "2026-10-05"
}
```

## Categorias disponíveis

```text
ALIMENTACAO
TRANSPORTE
MORADIA
SAUDE
LAZER
RENDA
OUTROS
```

## Tipos de transação

```text
RECEITA
DESPESA
```

## Diferencial do projeto

A implementação foi feita utilizando ferramentas locais e gratuitas.

Em vez de depender de serviços pagos para IA, transcrição e geração de voz, foram utilizados:

```text
Ollama + Qwen      → IA generativa
Whisper.cpp        → Speech-to-Text
Piper              → Text-to-Speech
FFmpeg             → conversão de áudio
H2                 → persistência
```

Dessa forma, o fluxo principal pode funcionar localmente sem consumo de créditos de APIs comerciais.

## Observação

Os executáveis, modelos de IA, modelos de voz e arquivos de banco de dados não são incluídos no repositório por causa do tamanho e por serem dependências locais.

Após clonar o projeto, configure a pasta `tools` conforme as instruções deste README.

## Autor

Projeto desenvolvido como parte de um desafio de **Spring Boot + Spring AI**, com foco em IA generativa, Tool Calling, processamento de áudio e integração com serviços de domínio.
