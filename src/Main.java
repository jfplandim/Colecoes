import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

        // 1. Instanciando objetos
        ListaDeEstudantes listaEstudantes = new ListaDeEstudantes();
        CadastroDeDisciplinas cadastroDisciplinas = new CadastroDeDisciplinas();
        RegistroAcademico registro = new RegistroAcademico(listaEstudantes, cadastroDisciplinas);

        //2.Carregar os dados dos arquivos
        carregarEstudantes("estudantes.csv", listaEstudantes);
        carregarDisciplinas("disciplinas.csv", cadastroDisciplinas);
        carregarMatriculas("matriculas.csv", registro);

        System.out.println("\n--- RELATÓRIOS ACADÊMICOS ---");

        // 3. Lista de estudantes (ordem de cadastro)
        System.out.println("\n== Lista de Estudantes (ordem de cadastro) ==");
        for(Estudante e : listaEstudantes.obterTodosEstudantes()){
            System.out.println(e);
        }

        // 4. Lista de estudantes (ordenada)
        System.out.println("\n== Lista de Estudantes (ordenada) ==");
        listaEstudantes.ordenarEstudantesPorNome();
        for(Estudante e : listaEstudantes.obterTodosEstudantes()){
            System.out.println(e);
        }

        // 5. Disciplinas (ordem de inserção)
        System.out.println("\n== Disciplinas (inserção) ==");
        for(Disciplina d : cadastroDisciplinas.obterTodasDisciplinas()){
            System.out.print(d);

        }

        // 6. Duplicatas detectadas (já exibidas ao tentar adicionar duplicadas)
        System.out.println("\n\n== Duplicatas detectadas na importação ==");
        for(Disciplina d : cadastroDisciplinas.obterDisciplinasDuplicadas()){
            System.out.print(d);
        }

       // 7. Matrículas e médias individuais
        System.out.println("\n\n== Matrículas ==");
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
            System.out.printf("%s: %s Média: %.2f\n", e.getNome(), matriculasFormatadas, media);
        }

        //8. Médias por disciplina
        System.out.println("\n== Médias por Disciplina ==");
        for (Disciplina d : cadastroDisciplinas.obterTodasDisciplinas()) {
            double media = registro.mediaDaDisciplina(d.getCodigo());
            System.out.printf("%s: %.2f\n", d.getCodigo(), media);
        }

        //9. Top 3 alunos por média
        System.out.println("\n== Top 3 alunos por média ==");
        List<Estudante> topAlunos = registro.topNEstudantesPorMedia(3);
        int posicao = 1;
        for(int i=1; i < 4; i++){
            Estudante e = topAlunos.get(i-1);
            System.out.printf("%d) %s - %.2f\n", i, e.getNome(), registro.mediaDoEstudante(e.getId()));
        }


        //10. Alunos com média >= 8.0
        System.out.println("\n== Alunos com média >= 8.0 ==");
        List<String> alunosMedia8 = new ArrayList<>();
        for(Estudante e : listaEstudantes.obterTodosEstudantes()){
            if(registro.mediaDoEstudante(e.getId()) >= 8.0){
                alunosMedia8.add(e.getNome());
            }
        }
        //juntar os nomes em uma string separada por virgula
        String resultadoFinal1 = String.join(", ", alunosMedia8);
        System.out.println(resultadoFinal1);

        //11. Disciplinas com média < 6.0
        System.out.println("\n== Disciplinas com média < 6.0 ==");
        List<String> disciplinasMedia6 = new ArrayList<>();
        for(Disciplina d : cadastroDisciplinas.obterTodasDisciplinas()){
            if(registro.mediaDaDisciplina(d.getCodigo()) < 6.0){
                disciplinasMedia6.add(d.getNome());
            }
        }
        String resultadoFinal2 = String.join(", ", disciplinasMedia6);
        System.out.println(resultadoFinal2);

        //12.Exibir disciplinas com nota acima da média da disciplina.
        System.out.println("\n== Extra: Notas Acima da Média da Disciplina (TODOS OS ALUNOS) ==");
        for(Estudante e : listaEstudantes.obterTodosEstudantes()) {
            if (e != null) {
                List<Matricula> matriculasDoFoco = registro.obterMatriculas(e.getId());
                for (Matricula matriculaAtual : matriculasDoFoco) {
                    double notaAluno = matriculaAtual.getNota();
                    double mediaDisciplina = registro.mediaDaDisciplina(matriculaAtual.getCodigoDisciplina());
                    if (notaAluno > mediaDisciplina) {
                        System.out.printf("%s na %s: Nota %.2f (Acima da Média %.2f)\n", e.getNome(), matriculaAtual.getCodigoDisciplina(), notaAluno, mediaDisciplina);
                    }
                }
            }
        }

        //13. Buscar Aluno Por Substring
        System.out.println("\n== Extra: Buscando Aluno Por Substring ==");
        Scanner scanner = new Scanner(System.in);
        System.out.print("Digite a substring para buscar estudantes (ex: 'ru', 'ana', 'a'): ");
        String termoBusca = scanner.nextLine();
        List<Estudante> encontrados = listaEstudantes.buscarEstudantesPorNome(termoBusca);
        System.out.printf("\n--- Resultados da busca por '%s' (%d encontrados) ---\n", termoBusca, encontrados.size());
        if (encontrados.isEmpty()) {
            System.out.println("Nenhum estudante encontrado.");
        } else {
            for(Estudante estudanteEncontrado : encontrados){
                System.out.printf("%s\n", estudanteEncontrado);
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
            System.err.println("🚨 Erro: Arquivo de estudantes não encontrado: " + nomeArquivo);
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
            System.err.println("🚨 Erro: Arquivo de disciplinas não encontrado: " + nomeArquivo);
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
            System.err.println("🚨 Erro: Arquivo de matrículas não encontrado: " + nomeArquivo);
        }
    }
}