# Configuração do Nginx - Proxy Reverso

## Instruções de Instalação

### 1. Instalar o Nginx (VM 1 - Linux)

```bash
# Ubuntu/Debian
sudo apt-get update
sudo apt-get install nginx

# CentOS/RHEL
sudo yum install nginx
```

### 2. Copiar arquivo de configuração

```bash
# Copiar o arquivo api.conf para o diretório sites-available do Nginx
sudo cp api.conf /etc/nginx/sites-available/api

# Se em CentOS/RHEL, copiar para conf.d
sudo cp api.conf /etc/nginx/conf.d/api.conf
```

### 3. Criar link simbólico (Ubuntu/Debian)

```bash
# Criar link simbólico em sites-enabled
sudo ln -s /etc/nginx/sites-available/api /etc/nginx/sites-enabled/api

# Desabilitar o site padrão (conforme enunciado)
sudo rm /etc/nginx/sites-enabled/default
```

### 4. Testar configuração

```bash
# Validar sintaxe do arquivo de configuração
sudo nginx -t

# Saída esperada:
# nginx: the configuration file /etc/nginx/nginx.conf syntax is ok
# nginx: configuration file /etc/nginx/nginx.conf test is successful
```

### 5. Reiniciar/Recarregar Nginx

```bash
# Reiniciar o Nginx
sudo systemctl restart nginx

# Ou apenas recarregar (sem derrubar conexões)
sudo systemctl reload nginx

# Verificar status
sudo systemctl status nginx
```

### 6. Verificar se está funcionando

```bash
# Ver se o Nginx está escutando na porta 80
sudo netstat -tulpn | grep :80

# Ou com ss (mais novo)
sudo ss -tulpn | grep :80
```

---

## Configuração da Rede

### VM 1 (Nginx - Proxy)
- **IP interno**: 192.168.1.10 (exemplo)
- **Porta**: 80 (HTTP)
- **Função**: Recebe requisições do cliente e repassa para VM 2

### VM 2 (API - Backend)
- **IP interno**: 192.168.1.100 (exemplo)
- **Porta**: 8080 (API Spring Boot)
- **Função**: Processa as requisições da API

### Fluxo de Requisição
```
Cliente (192.168.1.X)
    ↓
    ↓ (GET http://192.168.1.10/transacoes)
    ↓
VM 1 - Nginx (192.168.1.10:80)
    ↓
    ↓ (proxy_pass http://192.168.1.100:8080/transacoes)
    ↓
VM 2 - API (192.168.1.100:8080)
    ↓
    ↓ (Resposta JSON)
    ↓
VM 1 - Nginx
    ↓
    ↓ (Retorna resposta)
    ↓
Cliente
```

---

## Teste de Conectividade

### Testar ping entre VMs

```bash
# Da VM 1 para VM 2
ping 192.168.1.100

# Da VM 2 para VM 1
ping 192.168.1.10

# Se o ping não funcionar, verifique:
# 1. Se as VMs estão na mesma rede (modo Bridge ou Host-Only)
# 2. Se o firewall não está bloqueando ICMP
# 3. Se os IPs estão corretos
```

### Testar conexão na porta 8080 (VM 2)

```bash
# Verificar se a API está respondendo na VM 2
curl http://192.168.1.100:8080/transacoes

# Se funcionar, deve retornar a lista de transações em JSON
```

### Testar proxy reverso (VM 1)

```bash
# Fazer requisição através do Nginx
curl http://192.168.1.10/transacoes

# Deve retornar a mesma resposta que a requisição direta na VM 2
```

---

## Configuração Customizada

### Alterar IP/Porta da API Backend

Editar o arquivo `api.conf`:

```nginx
upstream api_backend {
    server 192.168.1.100:8080;  # Mudar aqui
}
```

Depois recarregar o Nginx:

```bash
sudo systemctl reload nginx
```

### Habilitar HTTPS/SSL

Descomentar a seção de HTTPS no arquivo `api.conf` e configurar:
- Caminho do certificado SSL
- Caminho da chave privada

---

## Troubleshooting

### Erro: "Permission denied" ao copiar arquivo

```bash
# Use sudo
sudo cp api.conf /etc/nginx/sites-available/api
```

### Erro: "connect() failed" ao testar

```bash
# Verificar:
# 1. Se a API está rodando na VM 2
# 2. Se o IP e porta estão corretos em api.conf
# 3. Se há firewall bloqueando a comunicação
# 4. Ver logs: sudo tail -f /var/log/nginx/api_error.log
```

### Nginx não reinicia

```bash
# Testar configuração
sudo nginx -t

# Ver logs de erro
sudo journalctl -xe
sudo tail -f /var/log/nginx/error.log
```

---

## Logs

Monitorar logs em tempo real:

```bash
# Access log
sudo tail -f /var/log/nginx/api_access.log

# Error log
sudo tail -f /var/log/nginx/api_error.log
```

---

## Comandos Úteis

```bash
# Iniciar Nginx
sudo systemctl start nginx

# Parar Nginx
sudo systemctl stop nginx

# Reiniciar Nginx
sudo systemctl restart nginx

# Recarregar configuração (sem derrubar)
sudo systemctl reload nginx

# Habilitar Nginx ao iniciar o sistema
sudo systemctl enable nginx

# Ver status
sudo systemctl status nginx

# Ver versão
nginx -v

# Ver compilação (módulos)
nginx -V
```
