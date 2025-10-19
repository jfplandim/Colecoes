import java.util.*;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) {

        // 1. Estudantes
        ListaDeEstudantes listaEstudantes = new ListaDeEstudantes();
        listaEstudantes.adicionarEstudante(new Estudante(1, "Ana"));
        listaEstudantes.adicionarEstudante(new Estudante(2, "Bruno"));
        listaEstudantes.adicionarEstudante(new Estudante(3, "Carla"));
        listaEstudantes.adicionarEstudante(new Estudante(4, "Diego"));
        listaEstudantes.adicionarEstudante(new Estudante(5, "Elisa"));

        // 2. Disciplinas
        CadastroDeDisciplinas cadastroDisciplinas = new CadastroDeDisciplinas();
        cadastroDisciplinas.adicionarDisciplina(new Disciplina("MAT101", "Matemática"));
        cadastroDisciplinas.adicionarDisciplina(new Disciplina("PRG201", "Programação"));
        cadastroDisciplinas.adicionarDisciplina(new Disciplina("BD301", "Banco de Dados"));
        cadastroDisciplinas.adicionarDisciplina(new Disciplina("EDF110", "Educação Física"));


        // 3. Registro Acadêmico
        RegistroAcademico registro = new RegistroAcademico(listaEstudantes, cadastroDisciplinas);
        registro.adicionarMatricula(1, "MAT101", 8.5);
        registro.adicionarMatricula(1, "PRG201", 9.0);
        registro.adicionarMatricula(2, "PRG201", 7.0);
        registro.adicionarMatricula(2, "MAT101", 5.0);
        registro.adicionarMatricula(3, "BD301", 6.5);
        registro.adicionarMatricula(3, "MAT101", 7.5);
        registro.adicionarMatricula(4, "PRG201", 8.0);
        registro.adicionarMatricula(5, "EDF110", 10.0);

        // 4. Saídas
        // Lista de estudantes (ordem de cadastro)
        System.out.println("== Lista de Estudantes (ordem de cadastro) ==");
        for (int i = 0; i < 5; i++) {
            System.out.println(listaEstudantes.obterEstudantePorIndice(i));
        }

        // Lista de estudantes (ordenada)
        listaEstudantes.ordenarEstudantesPorNome();
        System.out.println("== Lista de Estudantes (ordenada) ==");
        System.out.println(listaEstudantes.buscarEstudantesPorNome("").stream()
                .map(Estudante::getNome).collect(Collectors.joining(", ")));

        // Disciplinas (ordem de inserção)
        System.out.println("== Disciplinas (inserção) ==");
        System.out.println(cadastroDisciplinas.obterTodasDisciplinas().stream()
                .map(Disciplina::getCodigo).collect(Collectors.joining(", ")));

        // Duplicatas detectadas (já exibidas ao tentar adicionar duplicadas)

        // Matrículas e médias individuais
        System.out.println("== Matrículas ==");
        for (int i = 0; i < 5; i++) {
            Estudante e = listaEstudantes.obterEstudantePorIndice(i);
            List<Matricula> matriculas = registro.obterMatriculas(e.getId());
            double media = matriculas.stream().mapToDouble(Matricula::getNota).average().orElse(0);
            String notas = matriculas.stream()
                    .map(m -> m.getCodigoDisciplina() + "(" + m.getNota() + ")")
                    .collect(Collectors.joining(", "));
            System.out.println(e.getNome() + ": " + notas + " Média: " + media);
        }

        // Médias por disciplina
        System.out.println("== Médias por Disciplina ==");
        Set<Disciplina> disciplinas = cadastroDisciplinas.obterTodasDisciplinas();
        for (Disciplina d : disciplinas) {
            double soma = 0;
            int count = 0;
            for (int i = 0; i < 5; i++) {
                Optional<Double> nota = registro.obterNota(listaEstudantes.obterEstudantePorIndice(i).getId(), d.getCodigo());
                if (nota.isPresent()) {
                    soma += nota.get();
                    count++;
                }
            }
            double mediaDisciplina = (count > 0) ? soma / count : 0;
            System.out.println(d.getCodigo() + ": " + mediaDisciplina);
        }

        // Top 3 alunos por média
        System.out.println("== Top 3 alunos por média ==");
        List<Estudante> topAlunos = listaEstudantes.buscarEstudantesPorNome("").stream()
                .sorted((a, b) -> {
                    double mediaA = registro.obterMatriculas(a.getId()).stream().mapToDouble(Matricula::getNota).average().orElse(0);
                    double mediaB = registro.obterMatriculas(b.getId()).stream().mapToDouble(Matricula::getNota).average().orElse(0);
                    return Double.compare(mediaB, mediaA);
                })
                .limit(3)
                .collect(Collectors.toList());

        for (int i = 0; i < topAlunos.size(); i++) {
            Estudante e = topAlunos.get(i);
            double media = registro.obterMatriculas(e.getId()).stream().mapToDouble(Matricula::getNota).average().orElse(0);
            System.out.println((i + 1) + ") " + e.getNome() + " - " + media);
        }

        // Alunos com média >= 8.0
        System.out.println("== Alunos com média >= 8.0 ==");
        String acima8 = listaEstudantes.buscarEstudantesPorNome("").stream()
                .filter(e -> registro.obterMatriculas(e.getId()).stream().mapToDouble(Matricula::getNota).average().orElse(0) >= 8.0)
                .map(Estudante::getNome)
                .collect(Collectors.joining(", "));
        System.out.println(acima8.isEmpty() ? "(nenhuma)" : acima8);

        // Disciplinas com média < 6.0
        System.out.println("== Disciplinas com média < 6.0 ==");
        String discMenor6 = disciplinas.stream()
                .filter(d -> {
                    double soma = 0;
                    int count = 0;
                    for (int i = 0; i < 5; i++) {
                        Optional<Double> nota = registro.obterNota(listaEstudantes.obterEstudantePorIndice(i).getId(), d.getCodigo());
                        if (nota.isPresent()) {
                            soma += nota.get();
                            count++;
                        }
                    }
                    double media = (count > 0) ? soma / count : 0;
                    return media < 6.0;
                })
                .map(Disciplina::getCodigo)
                .collect(Collectors.joining(", "));
        System.out.println(discMenor6.isEmpty() ? "(nenhuma)" : discMenor6);
    }
}

