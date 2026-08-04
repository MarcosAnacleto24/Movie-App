# 🎬 MovieApp - Android Nativo (Kotlin & XML)

O **MovieApp** é um aplicativo Android nativo desenvolvido para consulta de catálogo de filmes, gerenciamento de perfis de usuário e sincronização de favoritos em tempo real. O projeto consome dados da API do **TMDB (The Movie Database)** e integra serviços do **Firebase** e persistência local.

---

## 🚀 Funcionalidades

- **Catálogo de Filmes:** Exibição de lançamentos, populares e detalhes (sinopse, elenco, nota e gênero).
- **Pesquisa em Tempo Real:** Filtragem dinâmica de filmes via `SimpleSearchView`.
- **Favoritos Sincronizados:** Salvamento, remoção e consulta de filmes favoritos integrados com o **Firebase Realtime Database**.
- **Gerenciamento de Perfil:** Atualização de dados cadastrais e foto de perfil com suporte a câmera/galeria via **Firebase Storage**.
- **Tratamento de Estados de UI:** Feedback visual com `ProgressBar` e telas de estado (Loading, Success, Error).

---

## 🛠️ Tecnologias e Arquitetura

O projeto segue as recomendações oficiais do Google para desenvolvimento Android moderno:

- **Linguagem:** [Kotlin](https://kotlinlang.org/)
- **Arquitetura:** Clean Architecture (Data, Domain, Presenter) + MVVM
- **Injeção de Dependência:** Hilt / Dagger
- **Asincronismo & Reatividade:** Kotlin Coroutines, Flow & LiveData
- **Interface e Layouts:** XML, ViewBinding, Custom Views, ListAdapter + DiffUtil
- **Comunicação Web:** Retrofit2 & OkHttp3
- **Persistência e Backend:** 
  - Room Database (Persistência Local)
  - Firebase Realtime Database
  - Firebase Storage
  - Firebase Authentication
- **Carregamento de Imagens:** Glide
- **Gerenciamento de Estado:** Sealed Classes (`StateView`)

---

## 📐 Estrutura do Projeto

```text
com.example.movieapp/
├── data/           # Repositórios, Mappers e Data Sources (Retrofit/Room/Firebase)
├── domain/         # Models e Use Cases
├── presenter/      # UI (Fragments, ViewModels, Adapters)
└── util/           # Extensões, Helpers e StateView
```

---

## 📱 Demonstração da Interface

| Detalhes do Filme | Lista de Favoritos | Editar Perfil |
| :---: | :---: | :---: |
| <img width="1080" height="2400" alt="MovieDetails Movie App" src="https://github.com/user-attachments/assets/d9b281a6-519b-42c2-a1cf-e240b91a808d" /> | <img width="1080" height="2400" alt="MovieFavorite Movie App" src="https://github.com/user-attachments/assets/b5fbda91-3778-420c-a2e6-7939651250a8" /> | <img width="1080" height="2400" alt="ProfileEdit Movie App" src="https://github.com/user-attachments/assets/6e39e54e-1705-47d9-86b7-a4b6b9eee1de" />

---

## Desenvolvido por:

**Marcos Anacleto**

Formado em **Análise e Desenvolvimento de Sistemas**
Cursando **Tecnologia em Desenvolvimento de Aplicativos Móveis - Unicesumar**
Foco em **Desenvolvimento Android Nativo com Kotlin**

[LinkedIn](https://www.linkedin.com/in/marcos-anacleto-5660a7208/) | [GitHub](https://github.com/MarcosAnacleto24)
