import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class Main {
    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);

        // 1. Instanciando objetos
        ListaDeEstudantes listaEstudantes = new ListaDeEstudantes();
        CadastroDeDisciplinas cadastroDisciplinas = new CadastroDeDisciplinas();
        RegistroAcademico registro = new RegistroAcademico(listaEstudantes, cadastroDisciplinas);

        //2.Carregar os dados dos arquivos
        carregarEstudantes("estudantes.csv", listaEstudantes);
        carregarDisciplinas("disciplinas.csv", cadastroDisciplinas);
        carregarMatriculas("matriculas.csv", registro);

        try {
            // Cria um FileWriter para o arquivo "output.txt"
            FileWriter arquivo =  new FileWriter("output.txt");

            // O PrintWriter permite usar métodos como println() e printf() no arquivo
            PrintWriter escritor = new PrintWriter(arquivo);


            //Chama o método que contém todo o código
            gerarRelatorios(escritor, scanner, listaEstudantes, cadastroDisciplinas, registro);

            // Fecha o PrintWriter para garantir que tudo foi salvo no arquivo
            escritor.close();

            System.out.println(" Relatórios acadêmicos gerados com sucesso em 'output.txt'");

        } catch (IOException e) {
            System.err.println(" Erro ao gerar arquivo de saída: " + e.getMessage());
        } finally {

            scanner.close();
        }
    }

    public static void gerarRelatorios(PrintWriter escritor, Scanner scanner,
                                       ListaDeEstudantes listaEstudantes,
                                       CadastroDeDisciplinas cadastroDisciplinas,
                                       RegistroAcademico registro) {

        escritor.println("\n--- RELATÓRIOS ACADÊMICOS ---");

        // 3. Lista de estudantes (ordem de cadastro)
        escritor.println("\n== Lista de Estudantes (ordem de cadastro) ==");
        for(Estudante e : listaEstudantes.obterTodosEstudantes()){
            escritor.println(e);
        }

        // 4. Lista de estudantes (ordenada)
        escritor.println("\n== Lista de Estudantes (ordenada) ==");
        listaEstudantes.ordenarEstudantesPorNome();
        for(Estudante e : listaEstudantes.obterTodosEstudantes()){
            escritor.println(e);
        }

        // 5. Disciplinas (ordem de inserção)
        escritor.println("\n== Disciplinas (inserção) ==");
        for(Disciplina d : cadastroDisciplinas.obterTodasDisciplinas()){
            escritor.print(d);
        }
        escritor.println();

        // 6. Duplicatas detectadas (já exibidas ao tentar adicionar duplicadas)
        escritor.println("\n\n== Duplicatas detectadas na importação ==");
        for(Disciplina d : cadastroDisciplinas.obterDisciplinasDuplicadas()){
            System.out.print(d);
        }

       // 7. Matrículas e médias individuais
        escritor.println("\n\n== Matrículas ==");
        for(Estudante e : listaEstudantes.obterTodosEstudantes()){
            List<Matricula> matriculasDoEstudante = registro.obterMatriculas(e.getId());
            double media = registro.mediaDoEstudante(e.getId());

            //lista temporaria para formatar
            List<String> notasFormatadas = new ArrayList<>();

            //formatar matriculas
            for (Matricula m : matriculasDoEstudante) {
                String formato = m.getCodigoDisciplina() + "(" + m.getNota() + ")";
                notasFormatadas.add(formato);
            }
            String matriculasFormatadas = String.join(", ", notasFormatadas);
            escritor.printf("%s: %s Média: %.2f\n", e.getNome(), matriculasFormatadas, media);
        }

        //8. Médias por disciplina
        escritor.println("\n== Médias por Disciplina ==");
        for (Disciplina d : cadastroDisciplinas.obterTodasDisciplinas()) {
            double media = registro.mediaDaDisciplina(d.getCodigo());
            escritor.printf("%s: %.2f\n", d.getCodigo(), media);
        }

        //9. Top 3 alunos por média
        escritor.println("\n== Top 3 alunos por média ==");
        List<Estudante> topAlunos = registro.topNEstudantesPorMedia(3);
        int posicao = 1;
        for(int i=1; i < 4; i++){
            Estudante e = topAlunos.get(i-1);
            escritor.printf("%d) %s - %.2f\n", i, e.getNome(), registro.mediaDoEstudante(e.getId()));
        }


        //10. Alunos com média >= 8.0
        escritor.println("\n== Alunos com média >= 8.0 ==");
        List<String> alunosMedia8 = new ArrayList<>();
        for(Estudante e : listaEstudantes.obterTodosEstudantes()){
            if(registro.mediaDoEstudante(e.getId()) >= 8.0){
                alunosMedia8.add(e.getNome());
            }
        }
        //juntar os nomes em uma string separada por virgula
        String resultadoFinal1 = String.join(", ", alunosMedia8);
        escritor.println(resultadoFinal1);

        //11. Disciplinas com média < 6.0
        escritor.println("\n== Disciplinas com média < 6.0 ==");
        List<String> disciplinasMedia6 = new ArrayList<>();
        for(Disciplina d : cadastroDisciplinas.obterTodasDisciplinas()){
            if(registro.mediaDaDisciplina(d.getCodigo()) < 6.0){
                disciplinasMedia6.add(d.getNome());
            }
        }
        String resultadoFinal2 = String.join(", ", disciplinasMedia6);
        escritor.println(resultadoFinal2);

        //12.Exibir disciplinas com nota acima da média da disciplina.
        escritor.println("\n== Extra: Notas Acima da Média da Disciplina (TODOS OS ALUNOS) ==");
        for(Estudante e : listaEstudantes.obterTodosEstudantes()) {
            if (e != null) {
                List<Matricula> matriculasDoFoco = registro.obterMatriculas(e.getId());
                for (Matricula matriculaAtual : matriculasDoFoco) {
                    double notaAluno = matriculaAtual.getNota();
                    double mediaDisciplina = registro.mediaDaDisciplina(matriculaAtual.getCodigoDisciplina());
                    if (notaAluno > mediaDisciplina) {
                        escritor.printf("%s na %s: Nota %.2f (Acima da Média %.2f)\n", e.getNome(), matriculaAtual.getCodigoDisciplina(), notaAluno, mediaDisciplina);
                    }
                }
            }
        }

        //13. Buscar Aluno Por Substring
        escritor.println("\n== Extra: Buscando Aluno Por Substring ==");

        escritor.print("Digite a substring para buscar estudantes (ex: 'ru', 'ana', 'a'): ");
        System.out.print("Digite a substring para buscar estudantes (ex: 'ru', 'ana', 'a'): ");
        String termoBusca = scanner.nextLine();
        List<Estudante> encontrados = listaEstudantes.buscarEstudantesPorNome(termoBusca);
        escritor.printf("\n--- Resultados da busca por '%s' (%d encontrados) ---\n", termoBusca, encontrados.size());
        if (encontrados.isEmpty()) {
            escritor.println("Nenhum estudante encontrado.");
        } else {
            for(Estudante estudanteEncontrado : encontrados){
                escritor.printf("%s\n", estudanteEncontrado);
            }
        }

    }

    //Metodos para carregar os arquivos
    public static void carregarEstudantes(String nomeArquivo, ListaDeEstudantes lista) {
        try (Scanner scanner = new Scanner(new File(nomeArquivo))) {
            if (scanner.hasNextLine()) scanner.nextLine(); // Pula cabeçalho
            while (scanner.hasNextLine()) {
                String[] dados = scanner.nextLine().split(",");
                if (dados.length == 2) {
                    lista.adicionarEstudante(new Estudante(Integer.parseInt(dados[0]), dados[1]));
                }
            }
        } catch (FileNotFoundException e) {
            System.err.println(" Erro: Arquivo de estudantes não encontrado: " + nomeArquivo);
        }
    }

    public static void carregarDisciplinas(String nomeArquivo, CadastroDeDisciplinas cadastro) {
        try (Scanner scanner = new Scanner(new File(nomeArquivo))) {
            if (scanner.hasNextLine()) scanner.nextLine(); // Pula cabeçalho
            while (scanner.hasNextLine()) {
                String[] dados = scanner.nextLine().split(",");
                if (dados.length == 2) {
                    cadastro.adicionarDisciplina(new Disciplina(dados[0], dados[1]));
                }
            }
        } catch (FileNotFoundException e) {
            System.err.println(" Erro: Arquivo de disciplinas não encontrado: " + nomeArquivo);
        }
    }

    public static void carregarMatriculas(String nomeArquivo, RegistroAcademico registro) {
        try (Scanner scanner = new Scanner(new File(nomeArquivo))) {
            if (scanner.hasNextLine()) scanner.nextLine(); // Pula cabeçalho
            while (scanner.hasNextLine()) {
                String[] dados = scanner.nextLine().split(",");
                if (dados.length == 3) {
                    registro.adicionarMatricula(Integer.parseInt(dados[0]), dados[1], Double.parseDouble(dados[2]));
                }
            }
        } catch (FileNotFoundException e) {
            System.err.println(" Erro: Arquivo de matrículas não encontrado: " + nomeArquivo);
        }
    }
}