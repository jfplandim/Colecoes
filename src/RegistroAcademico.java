import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Optional;
import java.util.Comparator;

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
    //4. Remover uma matricula de um estudante
    public void removerMatricula(int idEstudante, String codigoDisciplina){
        //pega a lista de matriculas
        List<Matricula> lista = matriculas.get(idEstudante);
        //verifica se a lista existe
        if(lista != null){
            lista.removeIf(m -> m.getCodigoDisciplina().equals(codigoDisciplina));
        }
    }

    //5. Media dos estudantes
    public double mediaDoEstudante(int idEstudante){
        List<Matricula> matriculasDoEstudante = matriculas.get(idEstudante);
        if(matriculasDoEstudante == null || matriculasDoEstudante.isEmpty()){
            return 0.0;
        }
        double somaDasNotas = 0.0;
        for(Matricula matriculaAtual : matriculasDoEstudante){
            somaDasNotas += matriculaAtual.getNota();
        }
        return somaDasNotas / matriculasDoEstudante.size();
    }

    //6. Media da disciplina
    public double mediaDaDisciplina(String codigoDisciplina){
        double somaDasNotas = 0.0;
        int quantidadeDeNotas = 0;

        for (List<Matricula> listaDeUmEstudante : matriculas.values()){
            for(Matricula matriculaAtual : listaDeUmEstudante){
                if (matriculaAtual.getCodigoDisciplina().equals(codigoDisciplina)){
                    somaDasNotas += matriculaAtual.getNota();
                    quantidadeDeNotas++;

                }
            }
        }

        if(quantidadeDeNotas > 0){
            return somaDasNotas / quantidadeDeNotas;
        } else {
            return 0.0;
        }
    }

    //7. Classe auxiliar para ajudar a calcular a proxima
    class EstudanteComMedia {
        private Estudante estudante;
        private double media;

        public EstudanteComMedia(Estudante estudante, double media) {
            this.estudante = estudante;
            this.media = media;
        }

        public Estudante getEstudante() {
            return estudante;
        }

        public double getMedia() {
            return media;
        }
    }

    //8. Estudantes ordenados por média descrescente
    public List<Estudante> topNEstudantesPorMedia(int N) {
        List<EstudanteComMedia> estudantesComMedias = new ArrayList<>();

        for (Integer idEstudante : matriculas.keySet()) {
            Estudante estudante = listaEstudantes.obterEstudantePorIndice(idEstudante);

            if (estudante != null) {
                double media = mediaDoEstudante(idEstudante);
                estudantesComMedias.add(new EstudanteComMedia(estudante, media));
            }
        }

        estudantesComMedias.sort(Comparator.comparingDouble(EstudanteComMedia::getMedia).reversed());

        List<Estudante> topEstudantes = new ArrayList<>();

        int limite = Math.min(N, estudantesComMedias.size());
        for (int i = 0; i < limite; i++) {
            topEstudantes.add(estudantesComMedias.get(i).getEstudante());
        }

        return topEstudantes;
    }
}