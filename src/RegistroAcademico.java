import java.util.*;
import java.util.stream.Collectors;

public class RegistroAcademico {

    // Chave: id do estudante, Valor: lista de matrículas
    private Map<Integer, List<Matricula>> matriculas;

    // Referência para estudantes e disciplinas (opcional, mas útil)
    private ListaDeEstudantes listaEstudantes;
    private CadastroDeDisciplinas cadastroDisciplinas;

    public RegistroAcademico(ListaDeEstudantes listaEstudantes, CadastroDeDisciplinas cadastroDisciplinas) {
        this.matriculas = new HashMap<>();
        this.listaEstudantes = listaEstudantes;
        this.cadastroDisciplinas = cadastroDisciplinas;
    }

    // 1. Adicionar matrícula
    public void adicionarMatricula(int idEstudante, String codigoDisciplina, double nota) {
        if (!cadastroDisciplinas.verificarDisciplina(codigoDisciplina)) {
            System.out.println(" Disciplina não cadastrada: " + codigoDisciplina);
            return;
        }
        List<Matricula> lista = matriculas.getOrDefault(idEstudante, new ArrayList<>());

        // Atualiza nota se já existir
        boolean atualizada = false;
        for (Matricula m : lista) {
            if (m.getCodigoDisciplina().equals(codigoDisciplina)) {
                m.setNota(nota);
                atualizada = true;
                break;
            }
        }
        if (!atualizada) {
            lista.add(new Matricula(codigoDisciplina, nota));
        }

        matriculas.put(idEstudante, lista);
    }

    // 2. Obter todas as matrículas de um estudante
    public List<Matricula> obterMatriculas(int idEstudante) {
        return matriculas.getOrDefault(idEstudante, new ArrayList<>());
    }

    // 3. Obter nota de uma disciplina específica
    public Optional<Double> obterNota(int idEstudante, String codigoDisciplina) {
        List<Matricula> lista = matriculas.get(idEstudante);
        if (lista == null) return Optional.empty();

        return lista.stream()
                .filter(m -> m.getCodigoDisciplina().equals(codigoDisciplina))
                .map(Matricula::getNota)
                .findFirst();
    }
}