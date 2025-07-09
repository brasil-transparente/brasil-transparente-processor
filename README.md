,# brasil-transparente-processor

Módulo responsável por ler arquivos de gastos de todas as partes da União previamente organizados no formato csv e salvar no banco os dados processados.

## 📦 Instalação e Configuração

### Pré-requisitos
- Java 21+
- MySQL
- Git
- Docker

### Passos
1. Clone o repositório:
   ```bash
   git clone https://github.com/brasil-transparente/brasil-transparente-processor.git
   ```
2. Copie os arquivos de gastos da União do Drive (link abaixo) para a pasta do projeto.
3. Certifique-se de ter o Docker instalado na sua máquina.
4. Adicione um arquivo .env na raiz do projeto com os seguintes campos.
```
DB_USER_LOCAL=seu_usuário_aqui
DB_PASSWORD_LOCAL=sua_senha_aqui
```
5. No terminal, rode o comando abaixo para iniciar criar o container e iniciar o banco de dados.
```bash
   docker compose up
```
6. Certifique-se de que o CSV_PATH no application.properties do projeto está apontando para o local correto dos arquivos.
7. Certifique-se de que os dados do banco de dados local estão corretos no application.properties.
8. Suba a aplicação utilizando o SpringBoot, executando a classe BrasilTransparenteProcessorApplication.
9. Chame o Controller utilizando o POST ( /processYear={ano} ) e passando o ano de 2024.
10. Se tudo estiver correto, a aplicação irá ler todos os arquivos e salvar no banco local todos os dados.

📁 Link para o Drive: https://drive.google.com/drive/folders/1EvbRIqP9Eg8dZJP6RKSpf7KoippdhC3c?usp=drive_link

## 🤝 Como Contribuir
- 📌 **Participe no Discord**: A melhor forma de ajudar na contribuição do projeto é estar alinhado com o que está sendo discutido no nosso Discord:
https://discord.gg/sQbf3bSzt4
- 🐛 **Issues existentes**: Dentro do repositório no GitHub mantemos uma lista de Issues que devem trabalhadas, geralmente alocadas dentro de projetos. É possível acompanhar o andamento das entregas por lá.
- 🛠️ **Reportar problemas/sugestões**: Para reportar bugs e sugerir novas melhorias, por favor, entre em contato com a gente no nosso Discord.

## ⚖️ Licença
[![AGPL-3.0](https://img.shields.io/badge/License-AGPL_v3-blue.svg)](https://www.gnu.org/licenses/agpl-3.0)

Este projeto está licenciado sob os termos da **GNU Affero General Public License v3.0** (AGPL-3.0).
- ✅ Liberdade para usar e modificar.
- 🔁 Exige compartilhamento das modificações.
- 🌍 Código-fonte deve ser disponibilizado para usuários.

Consulte o arquivo LICENSE.md para o texto completo da licença.
