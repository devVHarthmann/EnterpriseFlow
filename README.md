# Programa EnterpriseFlow

## Caras do Codigo corp

### Dev group 1

O programa EnterpriseFlow, da *Caras do Codigo corp*, é um sistema de organização de vendas, estoque, entre outras funçoes basicas que são dificieis de manter conta.
Focando na função de simplificar a organização e distribuição de vendas e estoques e controle das informaçoes relevantes para pequenos comercios.
O programa é focado na simplicidade, com uma interface feita completamente em Java e integrado com Banco de dados pessoal, para individualização do sistema.

### SM: Henrique Rodrigues

### PO: Arthur Vargas

Dev Team: Vitor Harthmann, Bruno Soares, Otavio Martins, Gabriel Brandão

---

## Tecnologias utilizadas

- ![Java](https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=java&logoColor=white)
- ![Swing](https://img.shields.io/badge/Swing-0081CB?style=for-the-badge&logo=java&logoColor=white)
- ![MySQL](https://img.shields.io/badge/MySQL-005C84?style=for-the-badge&logo=mysql&logoColor=white)
- ![JDBC](https://img.shields.io/badge/JDBC-07405E?style=for-the-badge&logo=java&logoColor=white)
- [jBCrypt]([https://mvnrepository.com/artifact/org.mindrot/jbcrypt](https://img.shields.io/badge/jBCrypt-cyan
)) (hash seguro de senhas)


## Estrutura do projeto

```text
src/br/ulbra/
 ├─ dao/         → Classes DAO (AbstractDAO, ClienteDAO, CrudRepository, FornecedorDAO, ItensVendaDAO, ProdutoDAO, UsuarioDAO, VendaDAO)
 ├─ controller/  → Lógica de controle (ClienteController, FornecedorController, ItensVendaController, ProdutoController, UsuarioController, VendaController)
 ├─ model/       → Modelos (Cliente.java, Fornecedor.java, ItensVenda.java, Produto.java, Usuario.java, Venda.java)
 ├─ view/        → Interfaces gráficas (CadastroClientes, CadastroFornecedorView, CadastroUsuarioView, Dashboard, ListaClientesView, ListaFornecedoresView, ListaProdutoView, ListaVendasView, ProdutoView, VendaView, loginUsuarioView)
 └─ img/         → Ícones
```

---

## Banco de Dados

``` sql
--
-- Banco de dados: `dbenterpriseflow`
--

-- --------------------------------------------------------

--
-- Estrutura para tabela `cliente`
--

CREATE TABLE `cliente` (
  `idCliente` int(11) NOT NULL,
  `nome` varchar(255) NOT NULL,
  `email` varchar(255) NOT NULL,
  `telefone` varchar(255) NOT NULL,
  `cpf` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Estrutura para tabela `fornecedor`
--

CREATE TABLE `fornecedor` (
  `idFornecedor` int(11) NOT NULL,
  `razaoSocial` varchar(255) NOT NULL,
  `nomeFantasia` varchar(255) NOT NULL,
  `cnpj` varchar(255) NOT NULL,
  `email` varchar(255) NOT NULL,
  `telefone` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;


-- --------------------------------------------------------

--
-- Estrutura para tabela `itensvenda`
--

CREATE TABLE `itensvenda` (
  `idVenda` int(11) NOT NULL,
  `idProduto` int(11) NOT NULL,
  `quantidadeProd` int(11) NOT NULL,
  `precoUnit` double NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Despejando dados para a tabela `itensvenda`
--

INSERT INTO `itensvenda` (`idVenda`, `idProduto`, `quantidadeProd`, `precoUnit`) VALUES
(1, 6, 3, 0.5),
(2, 6, 1, 0.5);

-- --------------------------------------------------------

--
-- Estrutura para tabela `produto`
--

CREATE TABLE `produto` (
  `idProduto` int(11) NOT NULL,
  `nomeProduto` varchar(255) NOT NULL,
  `categoria` varchar(255) NOT NULL,
  `valorUnitario` double NOT NULL,
  `quantEstoque` int(11) NOT NULL,
-- --------------------------------------------------------

--
-- Estrutura para tabela `usuario`
--

CREATE TABLE `usuario` (
  `idusuario` int(11) NOT NULL,
  `nome` varchar(150) NOT NULL,
  `email` varchar(100) NOT NULL,
  `cargo` varchar(50) NOT NULL,
  `senha` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Despejando dados para a tabela `usuario`
--

INSERT INTO `usuario` (`idusuario`, `nome`, `email`, `cargo`, `senha`) VALUES
(1, 'Otavio', 'o@o.com', 'Dev', '$2a$10$ZawCk5lcNO0iibUvNdtIaOT13trbMt4JFaXXGjHnBGf.vDGgo9y5C');

-- --------------------------------------------------------

--
-- Estrutura para tabela `venda`
--

CREATE TABLE `venda` (
  `idVenda` int(11) NOT NULL,
  `dataVenda` date NOT NULL,
  `valorTotal` double NOT NULL,
  `idUsuario` int(11) NOT NULL,
  `idCliente` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Despejando dados para a tabela `venda`
--

INSERT INTO `venda` (`idVenda`, `dataVenda`, `valorTotal`, `idUsuario`, `idCliente`) VALUES
(1, '2025-11-29', 1.5, 1, 4),
(2, '2025-11-20', 0.5, 1, 4);

--
-- Índices para tabelas despejadas
--

--
-- Índices de tabela `cliente`
--
ALTER TABLE `cliente`
  ADD PRIMARY KEY (`idCliente`),
  ADD UNIQUE KEY `cpf` (`cpf`);

--
-- Índices de tabela `fornecedor`
--
ALTER TABLE `fornecedor`
  ADD PRIMARY KEY (`idFornecedor`);

--
-- Índices de tabela `itensvenda`
--
ALTER TABLE `itensvenda`
  ADD PRIMARY KEY (`idProduto`,`idVenda`),
  ADD KEY `fkVenda` (`idVenda`),
  ADD KEY `fkProduto` (`idProduto`);

--
-- Índices de tabela `produto`
--
ALTER TABLE `produto`
  ADD PRIMARY KEY (`idProduto`),
  ADD KEY `fkProdutoFornecedor` (`idFornecedor`);

--
-- Índices de tabela `usuario`
--
ALTER TABLE `usuario`
  ADD PRIMARY KEY (`idusuario`);

--
-- Índices de tabela `venda`
--
ALTER TABLE `venda`
  ADD PRIMARY KEY (`idVenda`),
  ADD KEY `fkUVenda` (`idUsuario`),
  ADD KEY `fkCVenda` (`idCliente`);

--
-- AUTO_INCREMENT para tabelas despejadas
--

--
-- AUTO_INCREMENT de tabela `cliente`
--
ALTER TABLE `cliente`
  MODIFY `idCliente` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;

--
-- AUTO_INCREMENT de tabela `fornecedor`
--
ALTER TABLE `fornecedor`
  MODIFY `idFornecedor` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- AUTO_INCREMENT de tabela `produto`
--
ALTER TABLE `produto`
  MODIFY `idProduto` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=7;

--
-- AUTO_INCREMENT de tabela `usuario`
--
ALTER TABLE `usuario`
  MODIFY `idusuario` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;

--
-- AUTO_INCREMENT de tabela `venda`
--
ALTER TABLE `venda`
  MODIFY `idVenda` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- Restrições para tabelas despejadas
--

--
-- Restrições para tabelas `itensvenda`
--
ALTER TABLE `itensvenda`
  ADD CONSTRAINT `fkProduto` FOREIGN KEY (`idProduto`) REFERENCES `produto` (`idProduto`),
  ADD CONSTRAINT `fkVenda` FOREIGN KEY (`idVenda`) REFERENCES `venda` (`idVenda`);

--
-- Restrições para tabelas `produto`
--
ALTER TABLE `produto`
  ADD CONSTRAINT `fkProdutoFornecedor` FOREIGN KEY (`idFornecedor`) REFERENCES `fornecedor` (`idFornecedor`);

--
-- Restrições para tabelas `venda`
--
ALTER TABLE `venda`
  ADD CONSTRAINT `fkCVenda` FOREIGN KEY (`idCliente`) REFERENCES `cliente` (`idCliente`),
  ADD CONSTRAINT `fkUVenda` FOREIGN KEY (`idUsuario`) REFERENCES `usuario` (`idusuario`);
COMMIT;

```
## 👤 Criando o primeiro usuário (ADM)

### Opção 1 — Gerar hash manual
Use esta classe para gerar o hash:
```java
import org.mindrot.jbcrypt.BCrypt;
public class HashGenerator {
    public static void main(String[] args) {
        System.out.println(BCrypt.hashpw("admin123", BCrypt.gensalt()));
    }
}
```
Depois insira no banco:
```sql
INSERT INTO usuario (login, senha, nome)
VALUES ('adm', '$2a$10$HASHGERADO...', 'Administrador', 1);
```

### Opção 2 — Criar automaticamente no código
No `CadastroUsuarioView.form`, antes de abrir a tela de login, verifique se há usuários e crie o **adm/admin123** caso não exista.

---

## ▶️ Execução
1. Rode o projeto (classe `CadastroUsuario.view` é a principal).
2. Faça login:
   - Usuário: `adm`
   - Senha: `admin123`
3. Após autenticação, o sistema abre o **Main.view**.

---

## 🔒 Segurança
- Senhas são armazenadas com **jBCrypt**, nunca em texto puro.
- Recomenda-se usar um usuário MySQL dedicado em produção:
```sql
CREATE USER 'appuser'@'localhost' IDENTIFIED BY 'senhaSegura';
GRANT ALL PRIVILEGES ON cruddb1.* TO 'appuser'@'localhost';
```

---
## 👨‍🏫 Sobre
Este projeto foi desenvolvido para fins **educacionais**, como exemplo de CRUD com **Java + MySQL + Swing**, servindo de base para práticas de programação fullstack. Além disso, utiliza da collaboração entre alunos, ensinando-os as dificuldes do mercado de trabalho e como o ambiente de trabalho funciona.

---
##Diagrmas

diagrama de classe produto
![Diagrma de classes de Produto](src/br/ulbra/img/produto.jpeg)

diagrama de classe fonecedor
![Diagrma de classes de Produto](src/br/ulbra/img/fornecedor.jpeg)

diagrama de classe cliente
![Diagrma de classes de Produto](src/br/ulbra/img/cliente.jpeg)

diagrama de classe usuario
![Diagrma de classes de Produto](src/br/ulbra/img/usuario.jpeg)

diagrama de classe venda
![Diagrma de classes de Produto](src/br/ulbra/img/venda.jpeg)

diagrama de entidade relacionamento
![Diagrma de entidade relacionamento](src/br/ulbra/img/diagrama_entidade.jpeg)
