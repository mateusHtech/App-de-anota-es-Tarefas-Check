# Tarefas Check 📋

## Informações do aluno

**Nome:** Mateus Henrique  
**Matrícula:** 8370
**Data de entrega:** 02/11/2026

## Nome do aplicativo

**Tarefas Check**

## Justificativa da escolha do tema

O tema foi escolhido com o objetivo de desenvolver um aplicativo simples e útil para organização de tarefas e anotações do dia a dia.

A proposta surgiu da necessidade de reunir, em um único aplicativo, tarefas que precisam ser realizadas, permitindo que o usuário registre informações, acompanhe o que já foi concluído e organize suas atividades de maneira visual.

Além da utilidade prática, o tema também possibilitou aplicar diferentes conceitos estudados durante a disciplina de Desenvolvimento Android, como criação de interfaces, navegação entre telas, gerenciamento de estados e armazenamento local de dados.

## Descrição do funcionamento

O **Tarefas Check** é um aplicativo Android desenvolvido em Kotlin que permite ao usuário criar e organizar tarefas.

Na tela principal, o usuário pode visualizar suas tarefas cadastradas e identificar quais já foram concluídas. As tarefas podem possuir título, detalhes e uma cor, facilitando sua identificação visual.

O aplicativo permite:

- Criar novas tarefas;
- Adicionar título e detalhes;
- Escolher uma cor para cada tarefa;
- Visualizar os detalhes de uma tarefa;
- Editar tarefas existentes;
- Marcar tarefas como concluídas;
- Filtrar tarefas;
- Excluir tarefas individualmente;
- Excluir todas as tarefas;
- Navegar entre diferentes telas do aplicativo;
- Alterar o modo de aparência entre claro, escuro e automático.

As tarefas são armazenadas localmente no dispositivo utilizando **Room**, permitindo que os dados sejam mantidos mesmo após o fechamento do aplicativo.

O aplicativo também possui telas de configurações, ajuda e informações sobre o projeto.

## Tecnologias utilizadas

- Kotlin
- Jetpack Compose
- Material 3
- Navigation Compose
- Room
- ViewModel
- Repository
- Git e GitHub

## Observações

O projeto foi desenvolvido como atividade da disciplina de **Desenvolvimento Android Intermediário**, com o objetivo de aplicar na prática os conceitos estudados durante o curso.

O código-fonte completo do aplicativo está disponível neste repositório.


## 🏗️ Arquitetura

O projeto utiliza uma organização baseada em **UI + ViewModel + Repository + DAO + Room**.

```text
Interface
   ↓
ViewModel
   ↓
Repository
   ↓
DAO
   ↓
Room
   ↓
Banco de dados local
```

### Principais partes

**UI**

Responsável pela interface e interação com o usuário.

**ViewModel**

Mantém e controla os dados utilizados pela interface.

**Repository**

Intermediário entre o ViewModel e o acesso aos dados.

**DAO**

Define as operações realizadas no banco de dados.

**Room**

Responsável pela persistência local das tarefas.

## 📂 Estrutura do projeto

```text
app/
├── data/
│   ├── Tarefa.kt
│   ├── TarefaDao.kt
│   ├── TarefaDatabase.kt
│   ├── TarefaRepository.kt
│   └── CorTarefa.kt
│
├── viewmodel/
│   ├── TarefaViewModel.kt
│   └── TarefaViewModeFactory.kt
│
├── ui/
│   └── theme/
│       └── Theme.kt
│
├── MainActivity.kt
└── res/
```

### Principais arquivos

| Arquivo | Função |
|---|---|
| `MainActivity.kt` | Inicialização do aplicativo e telas principais |
| `Tarefa.kt` | Entidade que representa uma tarefa |
| `TarefaDao.kt` | Operações do banco de dados |
| `TarefaDatabase.kt` | Configuração do Room |
| `TarefaRepository.kt` | Intermediação entre ViewModel e DAO |
| `TarefaViewModel.kt` | Gerenciamento dos dados da interface |
| `TarefaViewModeFactory.kt` | Criação do ViewModel |
| `CorTarefa.kt` | Definição das cores disponíveis |
| `Theme.kt` | Tema e esquema de cores do aplicativo |

## 🎨 Aparência

O aplicativo possui três opções de aparência:

- ☀️ **Claro**
- 🌙 **Escuro**
- 📱 **Automático**

A preferência escolhida pelo usuário é armazenada localmente.

O projeto utiliza o sistema de cores do **Material 3**, permitindo que diferentes componentes da interface utilizem as cores definidas pelo tema.

A identidade visual utiliza principalmente tons de:

- laranja
- creme
- dourado
- marrom
- tons pastel para os cartões de tarefas

## 🧭 Navegação

O aplicativo utiliza **Navigation Compose** para controlar a navegação entre as telas.

Principais destinos:

```text
principal
nova_tarefa
editar_tarefa
detalhes
menu
configuracoes
ajuda
sobre
```

Fluxo principal:

```text
Tela Principal
     │
     ├── Nova tarefa
     │
     ├── Editar tarefa
     │
     ├── Detalhes
     │
     └── Menu
           ├── Configurações
           ├── Ajuda
           └── Sobre
```

## 💾 Persistência de dados

As tarefas são armazenadas localmente utilizando **Room**.

O fluxo de dados segue:

```text
Tela
 ↓
ViewModel
 ↓
Repository
 ↓
DAO
 ↓
Room
 ↓
Banco local
```

Dessa forma, os dados não dependem apenas da memória temporária da tela.

## 📚 Conceitos praticados

Durante o desenvolvimento foram praticados conceitos de:

- Kotlin
- Classes e objetos
- Funções
- Lambdas
- Estados
- `remember`
- `mutableStateOf`
- `collectAsState`
- Jetpack Compose
- Composables
- Material 3
- Navegação
- ViewModel
- Repository
- DAO
- Room
- Flow
- Persistência local
- SharedPreferences
- Git
- GitHub

## 🔀 Controle de versão

O projeto utiliza **Git** para controle de versão e **GitHub** para armazenamento do repositório remoto.

Repositório:

`App-de-Anota-es-Tarefas-Check`

Branch principal:

```text
main
```


## 📌 Status do projeto

Em desenvolvimento.

Novas funcionalidades, melhorias visuais, organização do código e documentação poderão ser adicionadas conforme o projeto evolui.
