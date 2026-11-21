# brasil-transparente-processor

Módulo responsável por processar os arquivos CSV de despesas da União (Poderes Executivo, Legislativo, Judiciário e Órgãos Autônomos) e persistir os dados agregados e simplificados no banco de dados.

## 🚀 Começando

### Pré-requisitos
- Java 21+
- Docker e Docker Compose
- Git

---

### Configuração Inicial

1. **Clonar o repositório:**
   ```bash
   git clone https://github.com/brasil-transparente/brasil-transparente-processor.git
   cd brasil-transparente-processor
   ```

2. **Baixar os arquivos de gastos:**
   - Baixe os arquivos CSV disponíveis no [Google Drive](https://drive.google.com/drive/folders/1EvbRIqP9Eg8dZJP6RKSpf7KoippdhC3c?usp=drive_link).
   - Extraia-os em um diretório local da sua preferência.

3. **Configurar as variáveis de ambiente:**
   - Crie um arquivo `.env` na raiz do projeto (opcional para o docker compose):
     ```env
     DB_USER_LOCAL=root
     DB_PASSWORD_LOCAL=password
     ```

4. **Subir o banco de dados via Docker:**
   ```bash
   docker compose up -d
   ```
   > O container disponibiliza o MySQL na porta configurada no `docker-compose.yml` (padrão: `3307`).

5. **Ajustar as propriedades da aplicação:**
   - No arquivo `src/main/resources/application.properties`, ajuste:
     - `spring.datasource.url`: certifique-se de apontar para a porta correta do MySQL local (ex: `jdbc:mysql://localhost:3307/gastos` se usar a porta exposta pelo docker compose).
     - `spring.datasource.username` e `spring.datasource.password`: confirme com suas credenciais do banco.
     - `CSV_PATH`: caminho absoluto da pasta onde estão os arquivos CSV baixados.

---

### Executando a Aplicação

- **Via Gradle:**
  ```bash
  ./gradlew bootRun
  ```
- **Via IDE:** execute a classe principal `BrasilTransparenteProcessorApplication`.

- **Para rodar os testes:**
  ```bash
  ./gradlew test
  ```

---

### Processando os Dados

Com a aplicação em execução, envie uma requisição POST informando o ano desejado:

```bash
curl -X POST "http://localhost:8080/processYear?year=2024"
```

A aplicação fará a leitura de todos os arquivos do ano, agregação hierárquica e cálculo da estrutura simplificada, persistindo os dados no banco configurado.

---

## 🤝 Como Contribuir
- 📌 **Discord**: Participe da comunidade e tire dúvidas no nosso Discord: https://discord.gg/sQbf3bSzt4
- 🐛 **Issues**: Acompanhe e escolha tarefas no GitHub Issues.
- 🛠️ **Pull Requests**: Abra um PR com sua proposta de alteração detalhada.

---

## ⚖️ Licença
[![AGPL-3.0](https://img.shields.io/badge/License-AGPL_v3-blue.svg)](https://www.gnu.org/licenses/agpl-3.0)

Este projeto está licenciado sob os termos da **GNU Affero General Public License v3.0** (AGPL-3.0). Consulte o arquivo [LICENSE.md](LICENSE.md) para mais detalhes.
