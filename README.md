# Finneo Mobile App

Finneo é um agregador financeiro de investimentos abrangente para Android, projetado para ajudar os usuários a rastrear seus portfólios tradicionais (fiduciários) e de criptomoedas em um só lugar. O aplicativo oferece uma interface limpa e intuitiva para visualizar a alocação de ativos e o desempenho.

Construído com Kotlin e Jetpack Compose, o Finneo utiliza uma stack de desenvolvimento Android moderna para proporcionar uma experiência de usuário responsiva e envolvente.

## Features

*   **Visão Unificada do Portfólio**: Alterne perfeitamente entre os painéis de investimento Fiduciário e Criptomoedas.
*   **Autenticação de Usuário**: Sistema seguro de login e registro usando Firebase Authentication, com suporte para email/senha e Login com Google.
*   **Integração de Carteira de Criptomoedas**:
    *   Adicione e gerencie endereços de carteira compatíveis com EVM (Ex: Ethereum, BNB Chain, Polygon).
    *   Busca automaticamente saldos e valores de tokens de múltiplas redes blockchain usando a Covalent API.
    *   Recupera dados de preço em tempo real e variações de preço em 24 horas da CoinGecko API.
*   **Visualização de Dados**:
    *   Gráficos de pizza para exibir a composição do portfólio e a alocação de ativos.
    *   Uma grade de componentes `AssetCard` fornece uma visão geral rápida dos ativos individuais.
    *   Componentes `DetailCard` oferecem uma visão detalhada e expansível dos ativos agrupados por tipo.
*   **Compartilhamento Seguro**:
    *   Gere um resumo compartilhável da composição do seu portfólio.
    *   Para privacidade, apenas as proporções dos ativos são compartilhadas, e não os valores monetários absolutos.
    *   Um código QR exclusivo é gerado para facilitar o compartilhamento.
*   **Visibilidade de Valor**: Alterne a visibilidade dos valores monetários em todo o aplicativo para maior privacidade em locais públicos.

## Stack de Tecnologia e Arquitetura

*   **Linguagem**: [Kotlin](https://kotlinlang.org/)
*   **UI**: [Jetpack Compose](https://developer.android.com/jetpack/compose) para uma UI totalmente declarativa.
*   **Arquitetura**: Model-View-ViewModel (MVVM) para separar a lógica da UI da lógica de negócios.
*   **Assincronicidade**: [Kotlin Coroutines](https://kotlinlang.org/docs/coroutines-guide.html) e `ViewModelScope` para gerenciar tarefas em segundo plano.
*   **Navegação**: [Jetpack Navigation for Compose](https://developer.android.com/jetpack/compose/navigation) para lidar com as transições de tela no aplicativo.
*   **Backend as a Service (BaaS)**: [Firebase](https://firebase.google.com/)
    *   **Firebase Authentication** para gerenciamento de usuários.
    *   **Cloud Firestore** para armazenar dados do usuário, incluindo endereços de carteira salvos.
*   **Rede (Networking)**:
    *   [Retrofit](https://square.github.io/retrofit/) para requisições HTTP type-safe a APIs de terceiros.
    *   [Covalent API](https://www.covalenthq.com/docs/api/) para buscar saldos de carteiras multi-chain.
    *   [CoinGecko API](https://www.coingecko.com/en/api) para buscar dados de preço de criptomoedas.
*   **Gerenciamento de Dependências**: [Gradle](https://gradle.org/) com Kotlin DSL (`build.gradle.kts`) e catálogos de versão (`libs.versions.toml`).

## Principais Componentes

O aplicativo é estruturado em torno de várias telas e view models composable chave:

*   **Autenticação (`LoginSectionEmail.kt`, `LoginSectionPassword.kt`, `RegisterScreen.kt`)**: Gerencia todo o fluxo de integração do usuário, do registro ao login.
*   **Tela Inicial (`HomeScreen.kt`)**: O painel principal que consolida os dados do portfólio. Apresenta a visualização por abas (Fiduciário vs. Cripto), o resumo do valor total, cartões de ativos e o gráfico de pizza de alocação do portfólio.
*   **Gerenciamento de Carteiras (`AddWalletScreen.kt`, `WalletViewModel.kt`)**: Fornece a interface para os usuários adicionarem novos endereços de carteiras EVM. O `WalletViewModel` lida com a lógica de salvar/buscar dados da carteira no/do Firestore.
*   **Dados Cripto (`CryptoRepository.kt`, `CryptoApiService.kt`)**: Um repositório dedicado que orquestra as chamadas para as APIs Covalent e CoinGecko para buscar e processar todos os dados relacionados a criptomoedas.
*   **Fluxo de Compartilhamento (`ShareScreen.kt`, `ShareGenerationScreen.kt`)**: Um processo de múltiplas etapas que permite aos usuários selecionar quais partes de seu portfólio compartilhar e, em seguida, gera um código QR contendo os dados anonimizados.

## Configuração e Instalação

Para construir e rodar o projeto localmente, siga estes passos:

1.  **Clone o repositório**:
    ```sh
    git clone https://github.com/cristhianbertan/finneo-mobile-app.git
    ```

2.  **Abra no Android Studio**:
    *   Abra o Android Studio (a versão Hedgehog ou mais recente é recomendada).
    *   Selecione `File > Open (Arquivo > Abrir)` e navegue até o diretório do repositório clonado.

3.  **Firebase Setup**:
    *   O projeto usa um arquivo `google-services.json`. Para a sua própria build, você precisará criar um novo projeto no [Firebase Console](https://console.firebase.google.com/).
    *   Adicione um aplicativo Android ao seu projeto Firebase com o nome do pacote `com.finneo`.
    *   Baixe o novo arquivo `google-services.json` e coloque-o no diretório `app/`
    *   No console do Firebase, habilite a **Authentication (Autenticação)** (com Email/Senha e provedores Google) e o **Cloud Firestore**.

4.  **API Keys**:
    *   O projeto usa uma chave de API da Covalent que está hardcoded (embutida) em `app/src/main/java/com/finneo/data/CryptoRepository.kt`. Você precisará substituí-la pela sua própria chave.

5.  **Build and Run**:
    *   Aguarde o Gradle sincronizar as dependências do projeto.
    *   Selecione um dispositivo de destino (target device) (emulador ou dispositivo físico) e clique no botão "Run" (Executar).

5.  **Testes**:
*   **Para realizar os testes, utilize os dados abaixo:**.
    
    *   **E-mail: `teste@gmail.com`**.
    *   **Senha: `123456`**.
    *   **Endereço de carteira de criptomoedas: `0x3e8734ec146c981e3ed1f6b582d447dde701d90c`**
    *   **API Key**: `cqt_rQbQrMC9qDCHhDvY9TQkdbVh8wMH` (Substituir no arquivo `CryptoRepository.kt` a variável privada `API_KEY`.
