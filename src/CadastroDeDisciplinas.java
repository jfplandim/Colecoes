import java.util.Set;
import java.util.HashSet;


public class CadastroDeDisciplinas {
    private Set<Disciplina> disciplinas;

    public CadastroDeDisciplinas(){
        this.disciplinas = new HashSet<>();
    }

    public boolean adicionarDisciplina(Disciplina d) {
        if (disciplinas.contains(d)) {
            System.out.println("⚠️ Disciplina duplicada detectada: " + d.getCodigo());
            return false; // Não adiciona
        }
        disciplinas.add(d);
                return true;
    }
    public boolean verificarDisciplina(String codigo){
        Disciplina verificar = new Disciplina (codigo, " ");
        return disciplinas.contains(verificar);
    }

    public void removerDisciplinas(String codigo){
        Disciplina remover = new Disciplina(codigo,"");
        disciplinas.remove(remover);
    }
    public Set<Disciplina> obterTodasDisciplinas(){
        return disciplinas; // retorna em ordem de inserção
    }
}

