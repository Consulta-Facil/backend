# 🩺 API de Agendamento de Consultas

## 📌 Objetivo e Público-Alvo

A API de Agendamento de Consultas tem como objetivo centralizar e facilitar o agendamento de atendimentos entre **pacientes** e **profissionais da saúde** (médicos, psicólogos, fisioterapeutas, etc.), promovendo organização, transparência e controle de agenda para todos os envolvidos.

**Público-alvo:**
- Pacientes que desejam agendar ou consultar atendimentos.
- Profissionais de saúde que desejam organizar sua agenda.
- Administradores responsáveis por supervisionar e manter o sistema.

---

## ⚙️ Funcionalidades Implementadas

### 🔐 Autenticação e Autorização
- Cadastro de usuários com papel: paciente, profissional ou administrador.
- Login com autenticação via JWT.
- Controle de acesso por tipo de usuário.
- Atribuição de papéis por administradores.

### 👩‍⚕️ Profissionais e Horários
- Cadastro e edição de horários disponíveis pelos profissionais.
- Visualização de profissionais e seus horários livres por pacientes.
- Gerenciamento de cadastro de profissionais pelos administradores.

### 📅 Agendamentos
- Agendamento de consultas com base em horários livres.
- Cancelamento com antecedência mínima (24h).
- Prevenção de sobreposição de horários.
- Visualização de agenda por profissionais.
- Monitoramento de todos os agendamentos por administradores.

### 📊 Relatórios e Histórico
- Histórico de consultas para pacientes.
- Filtro de agenda por data ou paciente para profissionais.
- Relatórios operacionais por data ou profissional para administradores.

---

## 🧪 Instruções de Execução Local

### ✅ Pré-requisitos
- Java 17+
- Maven
- MySQL

### 📦 Build e Execução

1. Clone o repositório:
   ```bash
   git clone https://github.com/seu-usuario/api-agendamento-consultas.git
   cd api-agendamento-consultas
   ```

2. Configure o banco de dados MySQL e crie o schema:
   ```sql
   CREATE DATABASE clinica_db;
   ```

3. Edite `src/main/resources/application.properties`:
   ```properties
   spring.datasource.url=jdbc:mysql://localhost:3306/clinica_db
   spring.datasource.username=seu_usuario
   spring.datasource.password=sua_senha
   spring.jpa.hibernate.ddl-auto=update
   ```

4. Execute o projeto:
   ```bash
   ./mvnw spring-boot:run
   ```

---

## 🔐 Obter o Token JWT e Testar Endpoints

### 1. Cadastro
- **POST** `/auth/register`
```json
{
  "nome": "João da Silva",
  "email": "joao@example.com",
  "senha": "123456",
  "papel": "PACIENTE"
}
```

### 2. Login
- **POST** `/auth/login`
```json
{
  "email": "joao@example.com",
  "senha": "123456"
}
```

### 3. Resposta
```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
}
```

Use o token nos headers:
```
Authorization: Bearer <token>
```

Você pode testar com ferramentas como Postman ou acessar o Swagger:
```
http://localhost:8080/swagger-ui.html
```

---

## 🗃️ Resumo do Modelo de Dados

### Entidades principais:
- **Usuário**: id, nome, email, senha, papel (PACIENTE, PROFISSIONAL, ADMIN)
- **HorárioDisponivel**: id, profissional_id, dataHora
- **Consulta**: id, paciente_id, profissional_id, dataHora, status (AGENDADA, CANCELADA)

### Regras de Validação:
- E-mails únicos e válidos.
- Agendamentos não podem ser feitos com menos de 24h de antecedência para cancelamento.
- Não é possível sobrepor horários para o mesmo profissional.
- Senha com no mínimo 6 caracteres.

---

## 🔒 Autenticação e Autorização

- Implementação baseada em **JWT** (JSON Web Token).
- Ao fazer login, o usuário recebe um token com validade e informações do papel.
- O token deve ser enviado nos headers das requisições protegidas.
- A API utiliza `Spring Security` com filtros para:
  - Validar token.
  - Autorizar o acesso baseado no papel (roles).

### Perfis de Acesso
| Papel         | Acesso                                                                 |
|---------------|------------------------------------------------------------------------|
| PACIENTE      | Visualizar profissionais, agendar, consultar histórico, cancelar       |
| PROFISSIONAL  | Gerenciar seus horários, visualizar sua agenda                         |
| ADMINISTRADOR | Acesso total a dados e gerenciamento de usuários e consultas           |

---

## 🧪 Testes Implementados

### ✅ Testes Unitários
- Cobertura de serviços com validação de regras de negócio:
  - Validação de horário disponível.
  - Regras de cancelamento.
  - Lógica de autenticação.
- Uso de **JUnit 5** e **Mockito** para simulações.

### ✅ Testes Funcionais
- Testes de endpoints REST com **Spring Boot Test**:
  - Cadastro e login de usuários.
  - Agendamento e cancelamento de consultas.
  - Filtro e consulta de agendas.

---

© 2025 - Sistema de Agendamento de Consultas Médicas
