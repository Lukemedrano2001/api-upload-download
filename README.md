# API de Upload e Download de Arquivos

Esta API, desenvolvida em Java com o framework Spring Boot, permite a realização de operações de upload e download de arquivos de maneira simples e eficiente.


## Funcionalidades

- **Upload de Arquivos:** Envie arquivos para o servidor, onde serão armazenados na pasta `uploads`.
- **Download de Arquivos:** Baixe arquivos previamente enviados para o servidor.
- **Listagem de Arquivos:** Obtenha uma lista dos arquivos armazenados no servidor.


## Instalação
Antes de iniciar, certifique-se de ter o Java e o Maven instalados em sua máquina.

## Configuração
A configuração do diretório de upload pode ser ajustada no arquivo `application.properties`:

- Pasta para upload de arquivos
file.upload-dir=uploads

- Tamanho máximo permitido para arquivos enviados
spring.servlet.multipart.max-file-size=20MB
spring.servlet.multipart.max-request-size=20MB


## Utilização

### Upload de Arquivos:
Envie arquivos para o servidor utilizando o método HTTP POST:

- POST http://localhost:8080/api/files/upload
- Certifique-se de incluir o arquivo desejado no corpo da requisição, utilizando a chave arquivo e o caminho do arquivo como valor.


### Download de Arquivos:
Baixe arquivos do servidor utilizando o método HTTP GET:

- GET http://localhost:8080/api/files/download/{nome-arquivo.tipo}
- Substitua {nome-arquivo.tipo} pelo nome do arquivo e seu tipo (exemplo: documento.pdf). Utilize o link gerado durante o upload.


### Listagem de Arquivos:
Obtenha uma lista de arquivos armazenados no servidor utilizando o método HTTP GET:

- GET http://localhost:8080/api/files/lista


## Licença
Este projeto está licenciado sob a MIT License.
