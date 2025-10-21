# Projeto Coleções Java

## 👥 Integrantes do Grupo
- João Guilherme Aragão Malta
- José Francisco Paes Landim Sobrinho

## 📋 Descrição do Projeto

Este projeto foi desenvolvido com o objetivo de **automatizar o controle de estudantes, disciplinas e notas** em uma escola de ensino médio.  
A aplicação foi criada em **Java**, utilizando as coleções do **Java Collections Framework (List, Set e Map)** para garantir um gerenciamento eficiente e organizado das informações.

O sistema permite:

- **Gerenciar a lista de estudantes**, possibilitando incluir, remover, buscar e ordenar registros.
- **Controlar disciplinas únicas**, evitando duplicatas e mantendo a integridade dos dados.
- **Associar cada estudante às suas matrículas e notas**, permitindo consultas rápidas e estruturadas.
- **Consultar médias de alunos e disciplinas**, além de identificar os estudantes aprovados.

Além de atender aos requisitos funcionais, o projeto também busca consolidar o entendimento sobre o uso e a escolha adequada das diferentes **implementações de coleções** — como `ArrayList`, `LinkedList`, `HashSet`, `TreeSet` e `HashMap` — aplicadas de acordo com a necessidade de cada operação.

---

## 🎯 Objetivos de Aprendizagem

1. Compreender o funcionamento e a aplicação prática das coleções **List**, **Set** e **Map** em Java.
2. Escolher a implementação mais adequada para cada cenário de uso.
3. Implementar métodos que manipulem dados de forma eficiente e organizada.
4. Gerar relatórios simples a partir de dados mantidos em memória, reforçando o uso prático das coleções.


# 🎯 Escolhas de Estruturas de Coleção

## 🧩 List – ArrayList
```java
List<Estudante> estudantes = new ArrayList<>();
```

Usada para gerenciar a **lista de estudantes**.
- **Preserva a ordem de inserção**, essencial para exibir registros na sequência em que foram adicionados.
- **Permite duplicatas**, útil antes de aplicar filtros ou validações.
- **Acesso rápido por índice**, ideal para buscas e manipulação direta.
- **Uso no projeto:** armazenar e ordenar estudantes, facilitando a geração de relatórios.

*Alternativas consideradas:*  
`LinkedList` (melhor para inserções/remoções frequentes no meio da lista, mas menos eficiente para acesso aleatório).

---

## 🔹 Set – HashSet
```java
Set<Disciplina> disciplinas = new HashSet<>();
```

Utilizado para controlar **disciplinas únicas**.
- **Garante unicidade**, impedindo elementos duplicados.
- **Operações rápidas** (inserção, busca e remoção em O(1) na média).
- **Uso no projeto:** manter o conjunto de disciplinas sem repetições.

*Alternativas consideradas:*  
`TreeSet` (mantém ordenação natural, mas com custo maior — O(log n)).

---

## 🗂 Map – HashMap
```java
Map<Estudante, List<Matricula>> registroAcademico = new HashMap<>();
```

Adotado para associar **estudantes às suas notas e matrículas**.
- **Associação chave-valor eficiente**, ideal para buscas diretas.
- **Alta performance** nas operações principais.
- **Uso no projeto:** mapear o aluno (chave) às suas informações acadêmicas (valor).

*Alternativas consideradas:*  
`TreeMap` (mantém chaves ordenadas, mas com custo maior) e `LinkedHashMap` (preserva a ordem de inserção).

---

Essas escolhas garantem **eficiência, clareza e coerência** com os requisitos do sistema — aproveitando as características ideais de cada tipo de coleção conforme a necessidade de ordenação, unicidade e associação de dados.

## 🚀 Como Executar o Programa

### Pré-requisitos
- Java JDK 8 ou superior instalado
- Terminal/Prompt de Comando

### Passo a Passo

*1. Clone o repositório:*
bash
git clone https://github.com/jfplandim/Colecoes.git
cd Colecoes


*2. Compile o programa:*
bash
javac Main.java

Ou se houver múltiplas classes:
bash
javac *.java


*3. Execute o programa:*
bash
java Main


*4. Verifique o arquivo de saída:*
O programa gerará automaticamente o arquivo output.txt no diretório atual com os resultados do processamento.

bash
cat output.txt  # Linux/Mac
type output.txt # Windows


### Estrutura de Arquivos

Colecoes/
├── Main.java
├── output.txt (gerado após execução)
└── README.md


## 💡 Desafios Encontrados

### 1. *Escolha entre ArrayList e LinkedList*
Inicialmente, houve dúvida sobre qual implementação de List usar. Após análise do padrão de acesso aos dados (mais leituras por índice do que inserções no meio), optamos por ArrayList pela melhor performance em get().

### 2. *Tratamento de valores nulos em HashMap*
HashMap permite chave e valor nulos (apenas uma chave null), o que exigiu validações extras para evitar NullPointerException em operações críticas.

### 3. *Métodos do Registro Acadêmico*
Durante a construção da classe Registro Acadêmico houve uma dificuldade na parte de escrita de alguns métodos após a lógica ser definida.

### 4. *Geração formatada do output.txt*
A escrita estruturada do arquivo de saída exigiu atenção especial para garantir legibilidade, com uso de PrintWriter e formatação adequada dos dados de cada coleção.

## 📊 Exemplo de Saída (output.txt)


========== RELATÓRIO DE COLEÇÕES ==========

=== LIST (ArrayList) ===
Total de elementos: 5
Elementos: [Item1, Item2, Item3, Item2, Item4]

=== SET (HashSet) ===
Total de elementos únicos: 4
Elementos: [Item1, Item2, Item3, Item4]

=== MAP (HashMap) ===
Total de pares chave-valor: 3
Mapeamento:
Item1 -> 2
Item2 -> 3
Item3 -> 1

===========================================


## 📚 Recursos Utilizados
- Java Collections Framework
- Java I/O (FileWriter, PrintWriter)
- Documentação oficial Oracle Java SE

---

*Desenvolvido como projeto acadêmico para demonstração de Collections em Java*
