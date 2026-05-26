# API REST - Gerenciamento de Transações Financeiras

## Integrantes

- Guilherme Abreu
- João Gomes
- Luis Henrique
- Mariana

---

## IPs das Máquinas Virtuais

| VM | Função | IP | Porta |
|----|--------|----|-------|
| VM 1 | Nginx (Proxy Reverso) | 192.168.1.10 | 80 |
| VM 2 | API REST (Spring Boot) | 192.168.1.100 | 8080 |

**Banco de dados:** PostgreSQL hospedado no Neon Cloud.

**Fluxo da requisição:**
```
Cliente (PC) ──► VM 1 (Nginx, 192.168.1.10:80) ──► VM 2 (API, 192.168.1.100:8080) ──► PostgreSQL (Neon Cloud)
```

---

## Guia de Execução

### VM 2 — Rodar a API

```bash
# 1. Instalar Java 21
sudo apt-get update
sudo apt-get install -y openjdk-21-jdk

# 2. Clonar o repositório
git clone <url-do-repositorio>
cd api

# 3. Configurar conexão com o banco de dados
#    Editar src/main/resources/application.properties com a URL, usuário e senha do PostgreSQL.
#    O arquivo já vem configurado para o Neon Cloud.

# 4. Iniciar a API
./mvnw spring-boot:run

# Saída esperada:
#   Started ApiApplication in X.XXX seconds
#   Tomcat started on port 8080

# 5. Testar se está funcionando
curl http://localhost:8080/transacoes
```

### VM 1 — Configurar o Nginx (Proxy Reverso)

O arquivo de configuração do Nginx está em `nginx/api.conf`.

```bash
# 1. Instalar Nginx
sudo apt-get update
sudo apt-get install -y nginx

# 2. Copiar o arquivo de configuração para o Nginx
sudo cp nginx/api.conf /etc/nginx/sites-available/api

# 3. Remover o site padrão do Nginx (conforme exigido pelo enunciado)
sudo rm /etc/nginx/sites-enabled/default

# 4. Ativar o novo virtual host
sudo ln -s /etc/nginx/sites-available/api /etc/nginx/sites-enabled/api

# 5. Se necessário, editar o IP da VM 2 no arquivo copiado:
#    sudo nano /etc/nginx/sites-available/api
#    Alterar a linha: server 192.168.1.100:8080;

# 6. Testar configuração e reiniciar
sudo nginx -t
sudo systemctl restart nginx

# 7. Testar o proxy
curl http://192.168.1.10/transacoes
```

---

## Teste do Fluxo Completo

```bash
# Verificar comunicação entre VMs
ping 192.168.1.100   # Da VM 1 para VM 2
ping 192.168.1.10    # Da VM 2 para VM 1

# Testar API direto na VM 2
curl http://192.168.1.100:8080/transacoes

# Testar via proxy (VM 1)
curl http://192.168.1.10/transacoes

# Criar transação via proxy
curl -X POST http://192.168.1.10/transacoes \
  -H "Content-Type: application/json" \
  -d '{"descricao":"Salário","valor":3000.00,"data":"2026-05-24","tipo":"RECEITA"}'

# Buscar por ID
curl http://192.168.1.10/transacoes/1

# Atualizar
curl -X PUT http://192.168.1.10/transacoes/1 \
  -H "Content-Type: application/json" \
  -d '{"descricao":"Salário Atualizado","valor":3500.00,"data":"2026-05-24","tipo":"RECEITA"}'

# Deletar
curl -X DELETE http://192.168.1.10/transacoes/1
```

---

## Estrutura do Repositório

```
├── api/                                  # Código fonte da API REST
│   └── src/main/java/br/com/redes2/api/
│       ├── ApiApplication.java           # Ponto de entrada
│       ├── controller/
│       │   └── TransacaoController.java  # Endpoints CRUD (/transacoes)
│       ├── model/
│       │   └── Transacao.java            # Entidade da tabela "transacoes"
│       └── repository/
│           └── TransacaoRepository.java  # Acesso ao banco
├── database/
│   └── init.sql                          # SQL de criação do banco
├── nginx/
│   └── api.conf                          # Configuração do proxy reverso
└── docs/
    ├── ROTAS.md                          # Documentação dos endpoints
    └── POSTMAN_COLLECTION.json           # Coleção para testes
```

---

## Endpoints da API

| Método | Rota | Ação | Status |
|--------|------|------|--------|
| GET | `/transacoes` | Lista todas | 200 |
| POST | `/transacoes` | Cadastra nova | 201 |
| GET | `/transacoes/{id}` | Busca por ID | 200 / 404 |
| PUT | `/transacoes/{id}` | Atualiza | 200 / 404 |
| DELETE | `/transacoes/{id}` | Remove | 204 / 404 |

Documentação completa: [docs/ROTAS.md](docs/ROTAS.md)

---

## Tecnologias

- **Java 21** + **Spring Boot 4.0.6** + **Maven**
- **PostgreSQL** (Neon Cloud)
- **Hibernate/JPA**
- **Nginx** (Proxy Reverso)
- **Ubuntu Server 22.04 / Debian 12**
>>>>>>> master
