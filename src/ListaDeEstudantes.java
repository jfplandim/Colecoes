import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class ListaDeEstudantes {
    private List <Estudante> estudantes;

    public ListaDeEstudantes(){
        this.estudantes = new ArrayList<>();
    }

    public void adicionarEstudante(Estudante e){
        estudantes.add(e);
    }

    public void removerEstudantePorId(int id){
        estudantes.removeIf(estudante -> estudante.getId() == id);
    }

    public Estudante obterEstudantePorIndice(int indice){
        return estudantes.get(indice);
    }

    public List<Estudante> buscarEstudantesPorNome(String substring){
        List <Estudante> encontrados = new ArrayList<>();
        for (Estudante estudante : estudantes){
            if(estudante.getNome().toLowerCase().contains(substring.toLowerCase())){
                encontrados.add(estudante);
            }
        }
        return encontrados;
    }

    public void ordenarEstudantesPorNome(){
        Collections.sort(estudantes, Comparator.comparing(Estudante::getNome));
    }

    public Estudante obterEstudantePorId(int id) {
        for (Estudante estudante : estudantes) {
            if (estudante.getId() == id) {
                return estudante;
            }
        }
        return null; // Retorna nulo se não encontrar
    }

    public List<Estudante> obterTodosEstudantes() {
        return this.estudantes;
    }
}