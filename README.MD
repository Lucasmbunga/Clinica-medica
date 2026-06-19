#  de Gestão Clínica (Medica-Clinic)

[![Java Version](https://img.shields.io/badge/Java-21-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)](https://openjdk.org/)
[![JavaFX](https://img.shields.io/badge/JavaFX-21-02599C?style=for-the-badge&logo=javafx&logoColor=white)](https://openjfx.io/)
[![MySQL](https://img.shields.io/badge/MySQL-8.0-4479A1?style=for-the-badge&logo=mysql&logoColor=white)](https://www.mysql.com/)
[![Maven](https://img.shields.io/badge/Maven-3.9-C71A36?style=for-the-badge&logo=apache-maven&logoColor=white)](https://maven.apache.org/)

O **Medica-Clinic** é um sistema desktop corporativo voltado para a automatização operacional de clínicas médicas. Desenvolvido como projeto prático do IIº Semestre do 2º Ano de Engenharia, o software foca na aplicação rigorosa de padrões de arquitetura de software, alta coesão, baixo acoplamento e persistência de dados relacional robusta.

##  Módulos do Sistema

O software foi concebido sob o padrão **SPA (Single Page Application)** utilizando painéis trocáveis dinamicamente (`BorderPane`), eliminando sobreposição amadora de janelas pop-up.

* **Autenticação com RBAC:** Sistema de login seguro com controle de acesso baseado em funções de utilizador (`ADMINISTRADOR`, `RECEPCIONISTA`, `MEDICO`).
* **Gestão de Pacientes:** Ficha de cadastro unificado contendo dados pessoais, múltiplos contactos (atributo multivalorado) e o endereço de residencia.
* **Corpo Clínico (Médicos):** Vinculação estrita entre o cadastro de especialistas (CRM) e as suas credenciais atómicas de acesso ao sistema.
* **Agenda & Consultas:** Central de triagem de turnos e marcação de consultas com validação de chaves estrangeiras (`RESTRICT` para proteção contra deleções órfãs).
* **Prontuário Eletrónico :** Registo detalhado de diagnósticos, prescrições (receituários) e histórico clínico evolutivo com amarrações `UNIQUE`.

## Tecnologias Utilizadas
* Linguagem: Java 21 (Long Term Support)

* Interface Gráfica: JavaFX 21.0.2

* Gerenciador de Dependências: Apache Maven

* Driver do Banco de Dados: MySQL Connector/J

* Estilização: CSS Customizado (Modern Design)

##  Arquitetura e Padrões de Projeto

O projeto segue estritamente os padrões de engenharia de software corporativa:

* **Camada de Apresentação (View):** Desenvolvida em **FXML** com estilização via **CSS moderno** (Baseado em Tailwind UI Colors), garantindo responsividade interna através de layouts dinâmicos (`VBox`, `HBox`, `ScrollPane`, `Region`).
* **Camada de Controlo (Controller):** Gerenciamento do ciclo de vida dos componentes através da interface `Initializable` do JavaFX, garantindo proteção contra estados nulos.
* **Camada de Modelo (Model/Domain):** Representação do mundo real encapsulada com uso de Composição e propriedades nativas do JavaFX (`SimpleStringProperty`) para binding em tempo real.
* **Camada de Persistência (DAO):** Padrão *Data Access Object* com JDBC puro, garantindo isolamento completo do SQL da regra de negócio.
* **Modularidade (JPMS):** Arquitetura forte utilizando o sistema de módulos do Java (`module-info.java`) com restrição de encapsulamento e abertura controlada de pacotes para o reflexo do `javafx.fxml`.

##  Modelo Relacional do Banco de Dados

O banco de dados foi construído sob o mecanismo **InnoDB** para garantir suporte total a **Transações ACID** e integridade referencial estrita.

```plaintext
   +-----------------+         1:1         +-----------------+
   |    paciente     |-------------------|    endereco     |
   +-----------------+                   +-----------------+
            |
            | 1:N
            |
   +-----------------+         N:1         +-----------------+
   |    consulta     |-------------------|     medico      |
   +-----------------+                   +-----------------+
            |                                     | 1:1
            | 1:1                                 |
   +-----------------+                     +---------------+
   |   prontuario    |                     |    usuario    |
   +-----------------+                     +---------------+
```
## Como Executar o Projeto
### Pré-requisitos
Antes de executar o projeto, é necessário certificar-se de que tem os seguintes requisitos:
* Java JDK 21 ou superior instalado e configurado.

* Apache Maven 3.9+.

* MySQL  8.0+.

### Configuração do Banco de Dados

Para a criação do banco de dados e tabelas, basta copiar o script abaixo e colar num ambiente MySQL como MySQL Workbenck. As credenciais do administrador do sistema são: usuário: admin e senha:admin.  
```sql 
-- =============================================================================
-- SCRIPT DE CRIAÇÃO DA BASE DE DADOS: CLINICA MEDICA
-- ARQUITETURA CORPORATIVA INTEGRADA (PACIENTES, MÉDICOS E PRONTUÁRIOS)
-- AUTOR: Lucas Pascoal
-- =============================================================================

-- 1. Criação e Seleção do Esquema (Database)
CREATE DATABASE IF NOT EXISTS clinica_medica
CHARACTER SET utf8mb4
COLLATE utf8mb4_unicode_ci;
USE clinica_medica;

-- =============================================================================
-- 1. TABELAS INDEPENDENTES E ESTRUTURAIS DE ACESSO
-- =============================================================================

-- Tabela: usuario (Base essencial para o controle de acessos RBAC do sistema)

CREATE TABLE IF NOT EXISTS usuarios (
    id INT AUTO_INCREMENT,
    nome VARCHAR(150) NOT NULL,
    login VARCHAR(50) NOT NULL UNIQUE,
    senha VARCHAR(255) NOT NULL, -- Suporta hashes BCrypt de segurança
    perfil ENUM('ADMINISTRADOR', 'RECEPCIONISTA', 'MEDICO') NOT NULL,
    CONSTRAINT pk_usuario PRIMARY KEY (id)
) ENGINE=InnoDB;

-- Tabela: paciente
CREATE TABLE IF NOT EXISTS pacientes (
    id INT AUTO_INCREMENT,
    nome VARCHAR(150) NOT NULL,
    cpf VARCHAR(14) NOT NULL UNIQUE,
    data_nascimento DATE NOT NULL,
    CONSTRAINT pk_paciente PRIMARY KEY (id)
) ENGINE=InnoDB;

ALTER TABLE pacientes add column telefone varchar(20);
-- Tabela: medico
CREATE TABLE IF NOT EXISTS medicos (
    id INT AUTO_INCREMENT,
    numero_crm VARCHAR(20) NOT NULL UNIQUE,
    especialidade VARCHAR(100) NOT NULL,
    id_usuario INT NOT NULL, -- Ligação direta 1:1 com a conta de utilizador
    CONSTRAINT pk_medico PRIMARY KEY (id),
    CONSTRAINT fk_medico_usuario FOREIGN KEY (id_usuario) 
        REFERENCES usuarios(id) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB;
ALTER TABLE medicos add column telefone varchar(20);
select*from usuarios;


-- =============================================================================
-- 2. TABELAS DEPENDENTES (ENTIDADES FRACAS E ASSOCIAÇÕES)
-- =============================================================================

-- Tabela: endereco (Relacionamento 1:1 Forte com Paciente conforme o MER)
CREATE TABLE IF NOT EXISTS enderecos (
    id INT AUTO_INCREMENT,
    id_paciente INT NOT NULL UNIQUE, -- UNIQUE garante a regra de negócio 1:1
    rua VARCHAR(150) NOT NULL,
    bairro VARCHAR(100) NOT NULL,
    zona VARCHAR(50),
    numero_casa VARCHAR(20),
    CONSTRAINT pk_endereco PRIMARY KEY (id),
    CONSTRAINT fk_endereco_paciente FOREIGN KEY (id_paciente) 
        REFERENCES pacientes(id) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB;

-- Tabela: telefone (Atributo multivalorado associado ao Paciente)
CREATE TABLE IF NOT EXISTS telefones (
    id INT AUTO_INCREMENT,
    id_paciente INT NOT NULL,
    id_medico INT,
    contato VARCHAR(20) NOT NULL,
    CONSTRAINT pk_telefone PRIMARY KEY (id),
    CONSTRAINT fk_telefone_paciente FOREIGN KEY (id_paciente) 
        REFERENCES pacientes(id) ON DELETE CASCADE ON UPDATE CASCADE,
        CONSTRAINT fk_telefone_medico FOREIGN KEY (id_medico) 
        REFERENCES medicos(id) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB;

-- Tabela: consulta (Ponto central de amarração do MER entre Paciente e Médico)
CREATE TABLE IF NOT EXISTS consultas (
    id INT AUTO_INCREMENT,
    id_paciente INT NOT NULL,
    id_medico INT NOT NULL,
    data_consulta DATE NOT NULL,
    hora_nascimento TIME NOT NULL, -- Campo mapeado do MER para o horário agendado
    status VARCHAR(50) NOT NULL DEFAULT 'Agendado',
    CONSTRAINT pk_consulta PRIMARY KEY (id),
    CONSTRAINT fk_consulta_paciente FOREIGN KEY (id_paciente) 
        REFERENCES pacientes(id) ON DELETE RESTRICT ON UPDATE CASCADE,
    CONSTRAINT fk_consulta_medico FOREIGN KEY (id_medico) 
        REFERENCES medicos(id) ON DELETE RESTRICT ON UPDATE CASCADE
) ENGINE=InnoDB;

-- Tabela: prontuario (Histórico clínico gerado a partir de uma consulta concluída)
CREATE TABLE IF NOT EXISTS prontuarios (
    id INT AUTO_INCREMENT,
    id_consulta INT NOT NULL UNIQUE, -- UNIQUE assegura que cada consulta gera apenas um prontuário
    diagnostico TEXT NOT NULL,
    prescricao TEXT NOT NULL,
    observacoes TEXT,
    data_registro DATETIME NOT NULL,
    CONSTRAINT pk_prontuario PRIMARY KEY (id),
    CONSTRAINT fk_prontuario_consulta FOREIGN KEY (id_consulta) 
        REFERENCES consultas(id) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB;

-- =============================================================================
-- 3. CARGA INICIAL DE SEGURANÇA (SEED)
-- Criação do Administrador raiz para permitir o primeiro login na aplicação.
-- =============================================================================
INSERT INTO usuarios (nome, login, senha, perfil) 
VALUES ('Administrador ', 'admin', 'admin', 'ADMINISTRADOR')
ON DUPLICATE KEY UPDATE login=login;

```