# Tarefas Check 📋

Aplicativo Android desenvolvido em Kotlin para criação, visualização e organização de tarefas e anotações.

## 📱 Sobre o projeto

O **Tarefas Check** é um aplicativo de gerenciamento de tarefas desenvolvido como projeto final da disciplina de Desenvolvimento Android Intermediário.

A proposta é permitir que o usuário crie, visualize, edite, conclua e exclua tarefas de forma simples, utilizando uma interface baseada em cartões.

O projeto também foi desenvolvido como forma de praticar conceitos de desenvolvimento Android com Kotlin, Jetpack Compose, persistência local de dados e arquitetura organizada.

## 🚀 Funcionalidades

Atualmente, o aplicativo possui:

- Criar novas tarefas
- Definir título e detalhes
- Escolher uma cor para a tarefa
- Visualizar tarefas na tela principal
- Marcar tarefas como concluídas
- Filtrar tarefas
- Editar tarefas
- Excluir tarefas individualmente
- Excluir todas as tarefas
- Visualizar detalhes de uma tarefa
- Menu de opções do aplicativo
- Tela de configurações
- Tela de ajuda
- Tela sobre o aplicativo
- Seleção de tema claro
- Seleção de tema escuro
- Modo automático baseado no sistema
- Persistência da preferência de tema
- Persistência local das tarefas

## 🛠️ Tecnologias utilizadas

- **Kotlin**
- **Jetpack Compose**
- **Material 3**
- **Navigation Compose**
- **Room**
- **ViewModel**
- **Repository**
- **Flow**
- **SharedPreferences**
- **Gradle Kotlin DSL**
- **Git**
- **GitHub**

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

## 🎯 Objetivo acadêmico

Além de entregar o aplicativo proposto, o projeto tem como objetivo consolidar conhecimentos de desenvolvimento Android utilizando Kotlin e tecnologias modernas do ecossistema Android.

O código também está sendo utilizado como material de estudo para compreender, na prática, como diferentes componentes de um aplicativo Android se relacionam.

## 📌 Status do projeto

Em desenvolvimento.

Novas funcionalidades, melhorias visuais, organização do código e documentação poderão ser adicionadas conforme o projeto evolui.
