# API REST - Gerenciamento de Transações Financeiras

## Integrantes

- Guilherme Abreu
- João Gomes
- Luis Henrique
- Mariana

---

## IPs das Máquinas Virtuais

| Máquina | Função | IP | Porta |
|---------|--------|----|-------|
| Cliente | Notebook do colega (faz as requisições) | 192.168.1.X | — |
| VM 1 | Nginx (Proxy Reverso) | 192.168.1.10 | 80 |
| VM 2 | API REST (Spring Boot) + PostgreSQL local | 192.168.1.100 | 8080 |

**Banco de dados:** PostgreSQL instalado localmente na VM 2.

**Fluxo da requisição:**
```
Cliente (notebook colega) ──► VM 1 (Nginx, 192.168.1.10:80) ──► VM 2 (API + PostgreSQL, 192.168.1.100:8080)
```

---

## Guia de Execução

### VM 2 — Instalar PostgreSQL e Rodar a API

```bash
# 1. Instalar Java 21
sudo apt-get update
sudo apt-get install -y openjdk-21-jdk

# 2. Instalar PostgreSQL local
sudo apt-get install -y postgresql postgresql-contrib

# 3. Criar o banco de dados e a tabela
sudo -u postgres psql -f database/init.sql

# 4. (Opcional) Definir senha do usuário postgres
sudo -u postgres psql -c "ALTER USER postgres PASSWORD 'postgres';"

# 5. Clonar o repositório
git clone <url-do-repositorio>
cd api

# 6. Verificar application.properties — já configurado para banco local:
#    spring.datasource.url=jdbc:postgresql://localhost:5432/transacoes_db
#    spring.datasource.username=postgres
#    spring.datasource.password=postgres

# 7. Iniciar a API
./mvnw spring-boot:run

# Saída esperada:
#   Started ApiApplication in X.XXX seconds
#   Tomcat started on port 8080

# 8. Testar localmente
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

### Cliente (Notebook do Colega)

O colega faz as requisições apontando para o IP da VM 1 (proxy).
O cliente **não** acessa a VM 2 diretamente.

```bash
# Testar via proxy
curl http://192.168.1.10/transacoes

# Criar transação
curl -X POST http://192.168.1.10/transacoes \
  -H "Content-Type: application/json" \
  -d '{"descricao":"Salário","valor":3000.00,"data":"2026-05-29","tipo":"RECEITA"}'
```

---

## Teste do Fluxo Completo

```bash
# 1. Verificar comunicação entre VMs
ping 192.168.1.100   # Da VM 1 para VM 2
ping 192.168.1.10    # Da VM 2 para VM 1

# 2. Testar API direto na VM 2
curl http://192.168.1.100:8080/transacoes

# 3. Testar via proxy (VM 1)
curl http://192.168.1.10/transacoes

# 4. Do notebook do colega (cliente externo) — acessar apenas a VM 1
curl http://192.168.1.10/transacoes

# 5. CRUD completo via proxy
curl -X POST http://192.168.1.10/transacoes \
  -H "Content-Type: application/json" \
  -d '{"descricao":"Salário","valor":3000.00,"data":"2026-05-29","tipo":"RECEITA"}'

curl http://192.168.1.10/transacoes/1

curl -X PUT http://192.168.1.10/transacoes/1 \
  -H "Content-Type: application/json" \
  -d '{"descricao":"Salário Atualizado","valor":3500.00,"data":"2026-05-29","tipo":"RECEITA"}'

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
│   └── init.sql                          # SQL de criação do banco e tabela
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
- **PostgreSQL** (local na VM 2)
- **Hibernate/JPA**
- **Nginx** (Proxy Reverso)
- **Ubuntu Server 22.04 / Debian 12**
